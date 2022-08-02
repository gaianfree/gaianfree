package com.softarum.svsa.controller;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import com.softarum.svsa.controller.pront.ind.SituacaoViolenciaBean;
import com.softarum.svsa.modelo.Acao;
import com.softarum.svsa.modelo.BeneficioEventual;
import com.softarum.svsa.modelo.IndProtecao;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.SituacaoProtecao;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.EnumUtil;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.service.AgendamentoIndividualService;
import com.softarum.svsa.service.UsuarioService;
import com.softarum.svsa.util.DateUtils;
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
public class RealizarAtendimentoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<ListaAtendimento> listaAtendimentos = new ArrayList<>();
	//private List<AtendimentoDTO> resumoAtendimentos = new ArrayList<>();	
	private List<CodigoAuxiliarAtendimento> codigosAuxiliares;	
	private List<Acao> acoes = new ArrayList<>();
	private List<ListaAtendimento> faltas = new ArrayList<>();
	
	

	private ListaAtendimento item;
	private BeneficioEventual beneficio;
	private boolean auxilioFuneral;
	private boolean auxilioNatalidade;
	
	private DualListModel<Usuario> tecnicos;	
	private Usuario usuarioLogado;		
	private boolean cadastrador;
	private boolean statusPoll = true;
	
	@Inject
	private IndicadoresHelper helper;
	
	@Inject 
	UsuarioService usuarioService;
	@Inject
	AgendamentoIndividualService listaAtendimentoService;
	@Inject 
	SituacaoViolenciaBean violenciaBean;
	@Inject
	LoginBean loginBean;

	@PostConstruct
	private void inicializar() {	
		
		try {		
		
			usuarioLogado = loginBean.getUsuario();		
			log.debug("1. Usuario logado: " + loginBean.getUsuario().getNome());		
			if(usuarioLogado == null) {
				usuarioLogado = loginBean.getUsuario();
				log.info("2. Usuario logado: " + loginBean.getUsuario().getNome());
			}
			
			carregarCodAux();
			
			carregarTecnicos();
						
			buscarListaAtendimento();	
			
			limpar();

		}
		catch(Exception e){
			log.error("Erro inicializar() do RealizarAtendimento: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void carregarCodAux() throws NegocioException {		
		
		try {
			if(usuarioLogado.getGrupo() == Grupo.COORDENADORES
					|| usuarioLogado.getGrupo() == Grupo.TECNICOS) {
				setCadastrador(false);
				this.codigosAuxiliares = EnumUtil.getCodigosAtendIndividualizado();
				// se for CREAS acrescentar codigos de abordagem
				if(usuarioLogado.getUnidade().getTipo() == TipoUnidade.CREAS) {
					this.codigosAuxiliares = EnumUtil.getCodigosAtendAbordagem();
				}
			}			
			else if (usuarioLogado.getGrupo() == Grupo.ADMINISTRATIVOS) {	
				setCadastrador(true);
				this.codigosAuxiliares = EnumUtil.getCodigosAtendCadunico();
			}
		}
		catch(Exception e){
			log.error("Erro carregarCodAux() do RealizarAtendimento");
			throw e;
		}
	}
	
	private void carregarTecnicos() throws NegocioException {		
		try {
			List<Usuario>tecsSource = new ArrayList<Usuario>();
			tecsSource = usuarioService.buscarTecnicos(usuarioLogado.getUnidade(), loginBean.getTenantId());
			tecsSource.remove(usuarioLogado);
			List<Usuario> tecsTarget = new ArrayList<Usuario>();
	       
	        tecnicos = new DualListModel<Usuario>(tecsSource, tecsTarget);
		}
        catch(Exception e){
        	log.error("Erro carregarTecnicos() do RealizarAtendimento");
			throw e;
        }
	}
	
	public void onTransfer(TransferEvent event) {

        for(Object tecnico : event.getItems()) {
        	log.info("Tecnico selecionado: " + ((Usuario) tecnico).getNome());
        	MessageUtil.sucesso("Técnico " + ((Usuario) tecnico).getNome() + " selecionado.");
        }         
    }
	
	
	public void encerrar() {
		try {
			
			item.setTecnico(usuarioLogado);	
			item.setTecnicos(new HashSet<Usuario>(tecnicos.getTarget()));		
			item.setTenant_id(loginBean.getTenantId());
	
			this.listaAtendimentoService.encerrarAtendimento(item, beneficio);

			buscarListaAtendimento();
			
			MessageUtil.sucesso("Atendimento individual gravado com sucesso!");
			limpar();
			stopPoll();
								
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void autoSave() {
		try {
			Instant time = Instant.now();			
			log.info("auto save atendimento individual... : " + time);
			
			item.setTecnico(usuarioLogado);			
			item.setTecnicos(new HashSet<Usuario>(tecnicos.getTarget()));
			item.setTenant_id(loginBean.getTenantId());
			
			log.info("ANTES...auto save atendimento INDIV codigo = " + item.getCodigo());
			this.listaAtendimentoService.autoSave(item);
			log.info("DEPOIS...auto save atendimento INDIV codigo = " + item.getCodigo());
			
			MessageUtil.sucesso("Auto save executado.");
								
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void stopPoll() {
		log.debug("true");
		statusPoll = true;
	}
	public void startPoll() {
		log.debug("false");
		statusPoll = false;
	}
	
	public String agendamento() {
		return "/restricted/agenda/AgendamentoIndividual.xhtml";
	}
	
	
	/*
	 * Verifica se é MSE e atualiza a lista recuperada
	 */
	public void buscarListaAtendimento() throws NegocioException {
		
		try {
			this.listaAtendimentos =  listaAtendimentoService.buscarAtendimentosRole(loginBean.getUsuario(), loginBean.getTenantId());
			for(ListaAtendimento l : listaAtendimentos) {
				if(listaAtendimentoService.verificarMSE(l.getPessoa(), loginBean.getTenantId())) {
					l.getPessoa().setMse("MSE");
				}
			}	
		}
		catch(Exception e){
			log.error("Erro buscarListaAtendimento() do RealizarAtendimento");
			throw e;
		}
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
	
	/* Busca o histórico da pessoa 
	public void consultarResumoAtendimentos() {		
		log.info("Buscando historico da pessoa : " + item.getPessoa().getCodigo() + "-" + item.getPessoa().getNome());
		this.resumoAtendimentos = listaAtendimentoService.buscarResumoAtendimentosDTO(item.getPessoa(), loginBean.getTenantId());		
	}
	*/	
	public void consultaFaltas() {
		
		this.faltas = listaAtendimentoService.consultaFaltas(item.getPessoa(), loginBean.getTenantId());
	}	

	public void limpar() {
		item = new ListaAtendimento();
		item.setTenant_id(loginBean.getTenantId());
		beneficio = new BeneficioEventual();
		beneficio.setTenant_id(loginBean.getTenantId());
	}
	
	public boolean isItemSelecionado() {
		
        return item != null && item.getCodigo() != null;
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
    
    
    /*
     * Situação de Violencia
     */
    public void salvarSituacaoViolencia() {    	
    	violenciaBean.getSituacaoViolencia().setData(DateUtils.asDate(LocalDateTime.now()));
    	log.debug(item.getPessoa());
    	violenciaBean.setPessoa(item.getPessoa());
    	violenciaBean.salvar();    	
    }
    public void carregarSituacoesViolencia() {    	
    	log.debug(item.getPessoa());
    	violenciaBean.setPessoa(item.getPessoa());
    	violenciaBean.carregarSituacoesViolencia();    
    	violenciaBean.limpar();
    }
    
    
    public void atualizarDataTable() {
    	try {
    		stopPoll();
			buscarListaAtendimento();
		} catch (NegocioException e) {
			MessageUtil.erro(e.getMessage());
			e.printStackTrace();
		}
	}
}