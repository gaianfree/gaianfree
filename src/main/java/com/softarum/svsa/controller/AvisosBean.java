package com.softarum.svsa.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import com.softarum.svsa.modelo.HistoricoEncaminhamento;
import com.softarum.svsa.modelo.Oficio;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.AcompanhamentoDTO;
import com.softarum.svsa.modelo.to.PlanoTO;
import com.softarum.svsa.service.AvisosService;
import com.softarum.svsa.service.HistoricoEncaminhamentoService;
import com.softarum.svsa.service.PlanoAcompanhamentoService;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;

/**
 * Essa classe cria o dashBoard com avisos importantes
 * @author murakamiadmin
 *
 */
@Getter
@Setter
@Named
@ViewScoped
public class AvisosBean implements Serializable {

	private static final long serialVersionUID = 7353776636052365608L;
	
	private Usuario usuarioLogado;
	private DashboardModel model;
	
	private boolean tecnico = false ;
	private boolean tecnicoCreas = false ;
	private boolean coordenador = false;
	private HistoricoEncaminhamento historico;
	
	private List<AcompanhamentoDTO> listaDTO = new ArrayList<>();
	private List<AcompanhamentoDTO> listaMseDTO = new ArrayList<>();
	private List<PlanoTO> planosTO = new ArrayList<>();
	private List<PlanoTO> planosMseTO = new ArrayList<>();
	private List<HistoricoEncaminhamento>  pendentes = new ArrayList<>();
	private List<HistoricoEncaminhamento>  recebidos = new ArrayList<>();
	private List<Oficio> oficios = new ArrayList<>();	
	
	
	@Inject
	private AvisosService avisosService;
	@Inject
	private HistoricoEncaminhamentoService histEncaminhamentoService;
	@Inject
	private PlanoAcompanhamentoService planoService;
	@Inject
	private LoginBean loginBean;
	
	
	@PostConstruct
	public void inicializar() {	
		
		this.usuarioLogado = loginBean.getUsuario();
		
		model = new DefaultDashboardModel();
		
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        
        column1.addWidget("encaminhamentos");
        column2.addWidget("prazosPlanos");
        column1.addWidget("statusEncaminhamentos");
        column1.addWidget("oficios");
        column1.addWidget("prazosPlanosMse");
        
       
        model.addColumn(column1);
        model.addColumn(column2);  
      
        paraTecnico();
        
        paraTecnicoCreas();
        
        paraCoordenador();
	} 

	private void paraTecnico() {
		if(usuarioLogado.getGrupo() == Grupo.TECNICOS || usuarioLogado.getGrupo() == Grupo.COORDENADORES) {
			avisosPlanos();
			avisosOficios();
			if(!listaDTO.isEmpty() || !listaMseDTO.isEmpty())
				tecnico = true;
		}
	}
	private void paraTecnicoCreas() {
		if(usuarioLogado.getUnidade().getTipo() == TipoUnidade.CREAS) {
			if(usuarioLogado.getGrupo() == Grupo.TECNICOS || usuarioLogado.getGrupo() == Grupo.COORDENADORES) {
				avisosPlanosMse();
				avisosOficios();
				if(!listaDTO.isEmpty() || !listaMseDTO.isEmpty())
					tecnicoCreas = true;
			}
		}
	}
	
	private void paraCoordenador() {
		if(usuarioLogado.getGrupo() == Grupo.COORDENADORES) {
			avisosEncaminhamentos();
			if( !pendentes.isEmpty() || !recebidos.isEmpty() )
				coordenador = true;
		}
	}
	
	
	/*
	 * Avisos Planos Familiares
	 */
	
	private void avisosPlanos() {		
		
		listaDTO = planoService.buscarFamiliasAcompanhamentoDTO(usuarioLogado.getUnidade(), loginBean.getTenantId());
		
		for(AcompanhamentoDTO dto : listaDTO) {				
			
			PlanoTO to = new PlanoTO();
			to.setDataIngresso(dto.getDataIngresso());
			to.setPessoaReferencia(dto.getPessoaReferencia());
			to.setProntuario(dto.getCodigoProntuario());
			to.setTerminoDias(calculaDiasFaltantes(dto.getDataIngresso(), dto.getPrazoMeses()));
			to.setTecnicoResponsavel(dto.getTecnicoResponsavel());
			
			if(to.getTerminoDias() < 30) {
				planosTO.add(to);
			}
		}			
	}
	
	/*
	 * Avisos Planos Individuais
	 */
	
	private void avisosPlanosMse() {		
		
		listaMseDTO = planoService.buscarAdolAcompanhamentoDTO(usuarioLogado.getUnidade(), loginBean.getTenantId());
		
		for(AcompanhamentoDTO dto : listaMseDTO) {				
			
			PlanoTO to = new PlanoTO();
			to.setProntuario(dto.getCodigo());
			to.setDataIngresso(dto.getDataIngresso());
			to.setPessoaReferencia(dto.getPessoaReferencia());			
			to.setTecnicoResponsavel(dto.getTecnicoResponsavel());
			
			planosMseTO.add(to);
		}			
	}
	
	
	/*
	 * Avisos Encaminhamentos
	 */
	
	private void avisosEncaminhamentos() {
		
		// só para coordenadores
		if(usuarioLogado.getGrupo() == Grupo.COORDENADORES)
			pendentes = avisosService.buscarEncaminhamentosPendentes(usuarioLogado.getUnidade(), loginBean.getTenantId());
			recebidos = avisosService.buscarEncaminhamentosRecebidos(usuarioLogado.getUnidade(), loginBean.getTenantId());
	}
	
	
	/*
	 * Avisos Oficios
	 */
	
	private void avisosOficios() {		

		oficios = avisosService.buscarOficiosPendentes(usuarioLogado.getUnidade(), loginBean.getTenantId());
			
	}
	
	
	public void ok(HistoricoEncaminhamento historico) {
		
		historico.setDataCiencia(new Date());
		
		try {
			this.histEncaminhamentoService.salvarHE(historico);
			recebidos.remove(historico);
		} catch (NegocioException e) {
			MessageUtil.erro(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private long calculaDiasFaltantes(Date data, int prazo) {		
		
		LocalDate dataInicio = DateUtils.asLocalDate(data);		
		LocalDate dataFim = dataInicio.plusMonths(prazo);		
		LocalDate hoje = LocalDate.now();
		
		long diferencaEmDias = ChronoUnit.DAYS.between(hoje, dataFim);				
				
		return diferencaEmDias;
	}
		
	/*
	 * Todos - geral
	 */
    
    public void handleReorder(DashboardReorderEvent event) {
        MessageUtil.alerta("Reordenado.");
    }
     
    public void handleClose(CloseEvent event) {
    	 MessageUtil.alerta("Painel fechado.");
    }
     
    public void handleToggle(ToggleEvent event) {
    	MessageUtil.alerta("Toggle.");
    }
	
	public void onClose(CloseEvent event) {
		MessageUtil.info("Painel fechado! Para ver novamente atualize a página.");
	}
	
}