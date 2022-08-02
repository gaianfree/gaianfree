package com.softarum.svsa.controller;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import com.softarum.svsa.modelo.BeneficioEventual;
import com.softarum.svsa.modelo.IndProtecao;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.SituacaoProtecao;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.EnumUtil;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.AgendamentoIndividualService;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.UsuarioService;
import com.softarum.svsa.service.VisitaService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class RealizarAtendimentoSAgBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<AtendimentoDTO> resumoAtendimentos = new ArrayList<>();
	private ListaAtendimento item;
	private List<ListaAtendimento> listaAtendimentos = new ArrayList<>();
	private List<ListaAtendimento> faltas = new ArrayList<>();
	private DualListModel<Usuario> tecnicos;
	private List<CodigoAuxiliarAtendimento> codigosAuxiliares;	
	private boolean statusPoll = true;
		
	private boolean administrativo;
	private boolean auxilioFuneral;
	private boolean auxilioNatalidade;
	
	private BeneficioEventual beneficio;	
	
	private Usuario usuarioLogado;	
	
	@Inject
	private PessoaService pessoaService;
	@Inject
	private IndicadoresHelper helper;	
	@Inject 
	UsuarioService usuarioService;
	@Inject 
	VisitaService visitaService;
	@Inject
	AgendamentoIndividualService listaAtendimentoService;	
	@Inject
	LoginBean loginBean;

	@PostConstruct
	public void inicializar()  {	
		
		try {
			usuarioLogado = loginBean.getUsuario();

			if(usuarioLogado.getRole() == Role.ADMINISTRATIVO) {
				setAdministrativo(true);				
			} else if(usuarioLogado.getRole() == Role.CADASTRADOR) {
				setAdministrativo(true);
				this.codigosAuxiliares = EnumUtil.getCodigosAtendCadunico();
			} else if(usuarioLogado.getRole() == Role.AGENTE_SOCIAL) {
				setAdministrativo(true);
				this.codigosAuxiliares = Arrays.asList(CodigoAuxiliarAtendimento.VISITA_DOMICILIAR);
			} else {			
				setAdministrativo(false);
				this.codigosAuxiliares = EnumUtil.getCodigosAtendIndividualizado();
				// se for CREAS acrescentar codigos de abordagem
				if(usuarioLogado.getUnidade().getTipo() == TipoUnidade.CREAS) {
					this.codigosAuxiliares = EnumUtil.getCodigosAtendAbordagem();
				}
			}
			
			this.listaAtendimentos = visitaService.buscarAtendimentosPendentes(loginBean.getUsuario().getUnidade(), loginBean.getTenantId());
			
			limpar();
		}		
		catch(Exception e){
			log.error("Erro inicializar() Realizar atendimento sem agendamento: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	private void carregarTecnicos() {		
		
		List<Usuario>tecsSource = new ArrayList<Usuario>();
		tecsSource = usuarioService.buscarTecnicos(usuarioLogado.getUnidade(), loginBean.getTenantId());
		tecsSource.remove(usuarioLogado);
		List<Usuario> tecsTarget = new ArrayList<Usuario>();
       
        tecnicos = new DualListModel<Usuario>(tecsSource, tecsTarget);
	}
	
	public void onTransfer(TransferEvent event) {

        for(Object tecnico : event.getItems()) {
        	log.info("Tecnico selecionado: " + ((Usuario) tecnico).getNome());
        	MessageUtil.sucesso("Técnico " + ((Usuario) tecnico).getNome() + " selecionado.");
        }         
    }
	
	
	public void encerrar() {
		try {			
			
			if(item.getPessoa() != null) {
				
				item.setTecnicos(new HashSet<Usuario>(tecnicos.getTarget()));
				item.setTenant_id(loginBean.getTenantId());
				
				this.visitaService.encerrar(item, beneficio);
				
				this.listaAtendimentos = visitaService.buscarAtendimentosPendentes(loginBean.getUsuario().getUnidade(), loginBean.getTenantId());
												
				MessageUtil.sucesso("Atendimento SEM agendamento gravado com sucesso!");
				limpar();
			}else {
				MessageUtil.erro("Selecione uma pessoa é obrigatório!");
			}
								
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void excluir() {
		try {
			this.visitaService.excluir(item);
			this.listaAtendimentos.remove(item);
			MessageUtil.sucesso("Item" + item.getCodigo() + " excluído com sucesso.");
			
			limpar();
				
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void autoSave() {
		try {
			
			log.info("auto save atendimento individual sem agendamento... : " + Instant.now());
			
			if(item.getPessoa() != null) {

				item.setTecnicos(new HashSet<Usuario>(tecnicos.getTarget()));
				item.setTenant_id(loginBean.getTenantId());
				
				log.info("ANTES...auto save atendimento INDIVID SEM AG codigo = " + item.getCodigo());
				item = this.visitaService.autoSave(item);
				log.info("DEPOIS...auto save atendimento INDIVID SEM AG codigo = " + item.getCodigo());
				
				MessageUtil.sucesso("Auto save executado.");
			}else {
				MessageUtil.erro("Selecione a pessoa atendida!");
			}
								
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void abrirDialogo() {
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("draggable", true);
        options.put("responsive", true);
        options.put("closeOnEscape", true);
        PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPessoa", options, null);        	
    }	
	
	public void selecionarPessoa(SelectEvent<?> event) {			
		PessoaDTO dto = (PessoaDTO) event.getObject();		
		Pessoa p = pessoaService.buscarPeloCodigo(dto.getCodigo());
		item.setPessoa(p);
	}
	
	public boolean isPessoaSelecionada() {
        return item.getPessoa() != null && item.getPessoa().getCodigo() != null;
    }
	
	public void atualizarDataTable() {
		this.listaAtendimentos = visitaService.buscarAtendimentosPendentes(loginBean.getUsuario().getUnidade(), loginBean.getTenantId());
	}
	
	public void consultarResumoAtendimentos() {		
		log.info("Buscando historico da pessoa : " + item.getPessoa().getCodigo() + "-" + item.getPessoa().getNome());
		this.resumoAtendimentos = listaAtendimentoService.buscarResumoAtendimentosDTO(item.getPessoa(), loginBean.getTenantId());		
	}
		
	public void consultaFaltas() {		
		
		this.faltas = listaAtendimentoService.consultaFaltas(item.getPessoa(), loginBean.getTenantId());
	}	

	public void limpar() {
		
		carregarTecnicos();
		setBeneficio(new BeneficioEventual());
		getBeneficio().setTenant_id(loginBean.getTenantId());
		
		//this.pessoa= null;
		this.item = new ListaAtendimento();
		this.item.setDataAgendamento(new Date());
		this.item.setRole(usuarioLogado.getRole());
		this.item.setTecnico(usuarioLogado);
		this.item.setUnidade(usuarioLogado.getUnidade());
		this.item.setTenant_id(loginBean.getTenantId());
		//this.item.setCodigoAuxiliar(CodigoAuxiliarAtendimento.VISITA_DOMICILIAR);
	}
	
	public void stopPoll() {
		log.debug("true");
		statusPoll = true;
	}
	public void startPoll() {
		log.debug("false");
		statusPoll = false;
	}
	
	public void verificaAuxilio() {
		log.info("Verifica Auxilios" + item.getCodigoAuxiliar());
		if (item.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.AUXILIO_FUNERAL) {
			auxilioFuneral = true;
			auxilioNatalidade = false;
		}
		else if (item.getCodigoAuxiliar() == CodigoAuxiliarAtendimento.AUXILIO_NATALIDADE) {
			auxilioNatalidade = true;
			auxilioFuneral = false;
		}
		else {
			auxilioFuneral = false;
			auxilioNatalidade = false;
		}
	}
	
	/* 
	 * Indicadores
	 */
	public void carregarIndicadores() {
		
		helper.carregarIndicadores(item, loginBean.getTenantId());
	}
	
	public void carregarSelectedIndicadores() {
		
		helper.carregarSelectedIndicadores(item, loginBean.getTenantId());		
	}
	
	public void gravarIndicadores() {	
		
		try {
			helper.gravarIndicadores(item);
		}
		catch(NegocioException ne) {			
			MessageUtil.info(ne.getMessage());
		}
		catch(Exception e) {			
			MessageUtil.info(e.getMessage());
		}
	}
	
	public List<SelectItem> getProtecoes() {
		return helper.getProtecoes();
	}
	public List<SituacaoProtecao> getSituacoes() {
		return helper.getSituacoes();
	}	
	public List<IndProtecao> getSelectedIndicadores() {
        return helper.getSelectedIndicadores();
    }
    public void setSelectedIndicadores(List<IndProtecao> selectedIndicadores) {
        helper.setSelectedIndicadores(selectedIndicadores);
    }
    /* 
	 * Indicadores fim
	 */
}