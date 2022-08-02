package com.softarum.svsa.controller.enc;

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
import com.softarum.svsa.modelo.HistoricoEncaminhamento;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.AgendamentoIndividualService;
import com.softarum.svsa.service.CapaProntuarioService;
import com.softarum.svsa.service.HistoricoEncaminhamentoService;
import com.softarum.svsa.service.MPComposicaoService;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * Encaminhamento de famílias CRAS para o CREAS
 * @author murakamiadmin
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class EncaminharProntuarioBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private PessoaReferencia pessoaReferencia;
	private List<PessoaReferencia> listaPessoasReferencia = new ArrayList<>();
	private HistoricoEncaminhamento historicoEncaminhamento;
	private List<HistoricoEncaminhamento>  historicosEncaminhamento = new ArrayList<>();
	private List<Unidade>  todosCREAS = new ArrayList<>();
	private Usuario usuarioLogado;
	
	@Inject
	private MPComposicaoService composicaoService;
	@Inject
	private PessoaService pessoaService;
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private CapaProntuarioService capaProntuarioService;
	@Inject
	private HistoricoEncaminhamentoService histEncaminhamentoService;
	@Inject
	private AgendamentoIndividualService listaAtendimentoService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {
		
		// recuperar todos os CREAS
		todosCREAS = unidadeService.buscarCREAS(loginBean.getTenantId());
		
		this.usuarioLogado = loginBean.getUsuario();		
		log.info("encaminharTransferenciaBean criado.");
	}

	public void salvar() {
		try {			
			/*
			 * Se existe atendimento pendente arremessa uma exceção.
			 */
			//listaAtendimentoService.verificarAgendamento(pessoaReferencia.getFamilia().getProntuario(), loginBean.getTenantId());
			/*
			 * Se existe historico nao recebido arremessa uma exceção.
			 */
			histEncaminhamentoService.verificarEncaminhamento(pessoaReferencia.getFamilia().getProntuario(), loginBean.getTenantId());	


			this.historicoEncaminhamento.setUnidadeEncaminhou(usuarioLogado.getUnidade());						
			Prontuario p = this.capaProntuarioService.buscarPeloCodigo(pessoaReferencia.getFamilia().getProntuario().getCodigo());						
			this.historicoEncaminhamento.setProntuario(p);
			this.historicoEncaminhamento.setUsuarioEncaminhou(usuarioLogado);		
			this.historicoEncaminhamento.setDespacho("Ainda não recebido.");
			this.historicoEncaminhamento.setTenant_id(loginBean.getTenantId());
			this.histEncaminhamentoService.salvarHE(historicoEncaminhamento);

			MessageUtil.sucesso("Encaminhamento realizado com sucesso e histórico gravado!");			
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
		historicoEncaminhamento = new HistoricoEncaminhamento();
		historicosEncaminhamento = histEncaminhamentoService.buscarTodosHE(pessoaReferencia.getFamilia().getProntuario(), loginBean.getTenantId());
		historicoEncaminhamento.setTenant_id(loginBean.getTenantId());
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
        PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPessoaReferencia", options, null);
        	
    }	
	
	public void selecionarPessoaReferencia(SelectEvent<?> event) {
		
		PessoaDTO dto = (PessoaDTO) event.getObject();		
		PessoaReferencia p = pessoaService.buscarPFPeloCodigo(dto.getCodigo());
		
		this.pessoaReferencia = p;		
		MessageUtil.sucesso("Pessoa Referencia Selecionada: " + this.pessoaReferencia.getNome());	
		
		historicosEncaminhamento = histEncaminhamentoService.buscarTodosHE(pessoaReferencia.getFamilia().getProntuario(), loginBean.getTenantId());
	}	
	
	public boolean isPessoaReferenciaSelecionada() {
        return pessoaReferencia != null && pessoaReferencia.getCodigo() != null;
    }
	
	public boolean isCreas() {
		if(usuarioLogado.getUnidade().getTipo() == TipoUnidade.CREAS)
			return true;
		return false;
	}

}