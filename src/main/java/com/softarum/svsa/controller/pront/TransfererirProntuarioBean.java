package com.softarum.svsa.controller.pront;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.HistoricoTransferencia;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.AgendamentoIndividualService;
import com.softarum.svsa.service.CapaProntuarioService;
import com.softarum.svsa.service.HistoricoTransferenciaService;
import com.softarum.svsa.service.MPComposicaoService;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.UnidadeService;
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
public class TransfererirProntuarioBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private PessoaReferencia pessoaReferencia;
	private List<PessoaReferencia> listaPessoasReferencia = new ArrayList<>();
	private HistoricoTransferencia historicoTransferencia;
	private List<HistoricoTransferencia> historicosTransferencia = new ArrayList<>();
	private List<Unidade> unidades = new ArrayList<>();
	private Unidade unidade;
	
	@Inject
	private PessoaService pessoaService;
	@Inject
	MPComposicaoService composicaoService;
	@Inject
	UnidadeService unidadeService;
	@Inject
	CapaProntuarioService capaProntuarioService;
	@Inject
	HistoricoTransferenciaService historicoTransferenciaService;
	@Inject
	AgendamentoIndividualService listaAtendimentoService;
	@Inject
	LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {
		if(loginBean.getUsuario().getUnidade().getTipo() == TipoUnidade.CRAS) {
			unidades = unidadeService.buscarCRAS(loginBean.getTenantId());
		} else if(loginBean.getUsuario().getUnidade().getTipo() == TipoUnidade.CREAS) {
			unidades = unidadeService.buscarCREAS(loginBean.getTenantId());
		} else {
			unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		}
		
		log.info("realizarTransferenciaBean criado.");
	}

	public void salvar() {
		try {			
			
			
			/*
			 * Se existe atendimento pendente arremessa uma exceção.
			 */
			listaAtendimentoService.verificarAgendamento(pessoaReferencia.getFamilia().getProntuario(), loginBean.getTenantId());
			
			
			log.info(pessoaReferencia.getNome());
			this.historicoTransferencia.setUnidade(pessoaReferencia.getFamilia().getProntuario().getUnidade());
			this.historicoTransferencia.setUnidadeDestino(unidade);
			
			Prontuario p = this.capaProntuarioService.buscarPeloCodigo(pessoaReferencia.getFamilia().getProntuario().getCodigo());
			p.setUnidade(unidade);
			this.capaProntuarioService.salvar(p);
			
			this.historicoTransferencia.setProntuario(p);
			this.historicoTransferencia.setUsuario(loginBean.getUsuario());		
			this.historicoTransferencia.setTenant_id(loginBean.getTenantId());
			
			this.historicoTransferenciaService.salvar(historicoTransferencia);

			MessageUtil.sucesso("Transferência realizada com sucesso e histórico gravado!");			
			
			this.limpar();
			pessoaReferencia = new PessoaReferencia();
			
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.erro("Erro desconhecido. Contatar o administrador");
		}
	}
	
	public void limpar() {	
		historicoTransferencia = new HistoricoTransferencia();
		historicoTransferencia.setTenant_id(loginBean.getTenantId());
		historicosTransferencia = new ArrayList<>();
	}
		
	/*
	 * Pessoa de Referencia
	 */
	public void todasPessoasReferencia() {
        listaPessoasReferencia = composicaoService.todasPessoasReferencia(loginBean.getTenantId());
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
        
        if(loginBean.getUsuario().getUnidade().getTipo() == TipoUnidade.SASC) {
        	PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPReferenciaGeral", options, null);
        }
        else {
        	PrimeFaces.current().dialog().openDynamic("SelecionaPessoaReferencia", options, null);
        }
        	
    }	
	
	public void selecionarPessoaReferencia(SelectEvent<?> event) {
		
		PessoaDTO dto = (PessoaDTO) event.getObject();		
		this.pessoaReferencia = pessoaService.buscarPFPeloCodigo(dto.getCodigo());
		
		MessageUtil.sucesso("Pessoa Referencia Selecionada: " + this.pessoaReferencia.getNome());	
		
		historicosTransferencia = historicoTransferenciaService.buscarTodos(pessoaReferencia.getFamilia().getProntuario(), loginBean.getTenantId());
	}	
	
	public boolean isPessoaReferenciaSelecionada() {
        return pessoaReferencia != null && pessoaReferencia.getCodigo() != null;
    }

	public boolean isCreas() {
		if(loginBean.getUsuario().getUnidade().getTipo().equals(TipoUnidade.CREAS)) {
			return true;			
		}
		return false;
	}
	
}