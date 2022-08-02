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
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.MPComposicaoService;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.util.MessageUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * @author murakamiadmin
 *
 */
@Getter
@Setter
@Named(value="manterProntuarioBean")
@ViewScoped
public class ManterProntuarioBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	//private LogUtil logUtil = new LogUtil(ManterProntuarioBean.class);
	
	private PessoaReferencia pessoaReferencia;
	private List<PessoaReferencia> listaPessoasReferencia = new ArrayList<>();
	private boolean administrativo;
	private Usuario usuarioLogado;
	
	
	@Inject
	private LoginBean loginBean;
	@Inject
	private PessoaService pessoaService;
	@Inject
	private MPComposicaoService composicaoService;	
	@Inject
	private MPComposicaoFamiliarBean mpComposicaoBean;
	@Inject
	private MPAcompanhamentoFamiliarBean mpAcompanhamentoBean;
	@Inject
	private MPCondicaoHabitacionalBean mpHabitacionalBean;
	@Inject
	private MPBeneficioEventualBean mpBeneficioBean;
	@Inject
	private MPConvivenciaFamiliarBean mpConvivenciaBean;
	/* Membros */
	@Inject
	private MPSituacaoViolenciaBean mpViolenciaBean;
	@Inject
	private MPCondicaoSaudeBean mpSaudeBean;
	@Inject
	private MPCondicaoTrabalhoBean mpTrabalhoBean;
	@Inject
	private MPCondicaoEducacionalBean mpEducacionalBean;
	@Inject
	private MPAcompanhamentoIndividualBean mpIndividualBean;
	
	
	@PostConstruct
	public void inicializar() {		
		
		usuarioLogado = loginBean.getUsuario();

		if(usuarioLogado.getRole() == Role.ADMINISTRATIVO 
				|| usuarioLogado.getRole() == Role.CADASTRADOR
				|| usuarioLogado.getRole() == Role.AGENTE_SOCIAL)
			setAdministrativo(true);
		else
			setAdministrativo(false);
	
	}
	
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
	
	public void abrirDialogoGeral() {
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("draggable", true);
        options.put("responsive", true);
        options.put("closeOnEscape", true);
        PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPReferenciaGeral", options, null);
        	
    }	
	
	public void selecionarPessoaReferencia(SelectEvent<?> event) {
		
		PessoaDTO dto = (PessoaDTO) event.getObject();		
		this.pessoaReferencia = pessoaService.buscarPFPeloCodigo(dto.getCodigo());
		
		mpComposicaoBean.setPessoaReferencia(this.pessoaReferencia);
		mpAcompanhamentoBean.setPessoaReferencia(this.pessoaReferencia);
		mpHabitacionalBean.setPessoaReferencia(this.pessoaReferencia);
		mpBeneficioBean.setPessoaReferencia(this.pessoaReferencia);
		mpConvivenciaBean.setPessoaReferencia(this.pessoaReferencia);
		
		/* dados de membros */
		mpViolenciaBean.setMembros(mpComposicaoBean.getPessoas());
		mpSaudeBean.setMembros(mpComposicaoBean.getPessoas());
		mpTrabalhoBean.setMembros(mpComposicaoBean.getPessoas());
		mpEducacionalBean.setMembros(mpComposicaoBean.getPessoas());
		mpIndividualBean.setMembros(mpComposicaoBean.getPessoas());
		
		MessageUtil.sucesso("Pessoa Referencia Selecionada: " + this.pessoaReferencia.getNome());			
	}	
	
	public boolean isPessoaReferenciaSelecionada() {
        return pessoaReferencia != null && pessoaReferencia.getCodigo() != null;
    }
	
}