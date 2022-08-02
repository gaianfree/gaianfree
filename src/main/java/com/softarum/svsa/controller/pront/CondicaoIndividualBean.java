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
import com.softarum.svsa.controller.pront.ind.CondicaoEducacionalBean;
import com.softarum.svsa.controller.pront.ind.CondicaoSaudeBean;
import com.softarum.svsa.controller.pront.ind.CondicaoTrabalhoBean;
import com.softarum.svsa.controller.pront.ind.SituacaoViolenciaBean;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.to.PessoaDTO;
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
@Named(value="ciBean")
@ViewScoped
public class CondicaoIndividualBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	
	private Pessoa pessoa;
	private List<Pessoa> listaPessoas = new ArrayList<>();
	private Usuario usuarioLogado;	
	
	@Inject
	private PessoaService pessoaService;
	@Inject
	private LoginBean sessaoBean;
	@Inject
	private CondicaoEducacionalBean educacionalBean;
	@Inject
	private CondicaoSaudeBean saudeBean;
	@Inject
	private CondicaoTrabalhoBean trabalhoBean;
	@Inject
	private SituacaoViolenciaBean violenciaBean;
	
	@PostConstruct
	public void inicializar() {		
		
		setUsuarioLogado(sessaoBean.getUsuario());		
	}	
		
	public void selecionarPessoa(SelectEvent<?> event) {
		
		PessoaDTO dto = (PessoaDTO) event.getObject();		
		Pessoa p = pessoaService.buscarPeloCodigo(dto.getCodigo());
		this.pessoa = p;		

		/* dados do individuo */
		educacionalBean.setPessoa(this.pessoa);
		saudeBean.setPessoa(this.pessoa);
		trabalhoBean.setPessoa(this.pessoa);
		violenciaBean.setPessoa(this.pessoa);
		
		MessageUtil.sucesso("Pessoa Selecionada: " + this.pessoa.getNome());		
	}

	public boolean isPessoaSelecionada() {
        return pessoa != null && pessoa.getCodigo() != null;
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

}