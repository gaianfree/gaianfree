package com.softarum.svsa.controller.pront.ind;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.CondicaoSaude;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.enums.TipoDeficiencia;
import com.softarum.svsa.service.CondicaoSaudeService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;


/**
 * @author gabrielrodrigues
 *
 */
@Log4j
@Getter
@Setter
@Named(value="saudeBean")
@ViewScoped
public class CondicaoSaudeBean implements Serializable {

	
	private static final long serialVersionUID = 1769116747361287180L;
	
	private CondicaoSaude condicaoSaude;
	private List<TipoDeficiencia> tiposDeficiencias;	
	private Pessoa pessoa;
	
	private boolean deficienteCampo;
	private boolean gestanteCampo;
	
	@Inject
	private LoginBean loginBean;
	@Inject 
	private CondicaoSaudeService condicaoSaudeService;
	
	
	
	@PostConstruct
	public void inicializar() {		
		log.info("[LOG] " + loginBean.getUserName() + " -> " + this.getClass().getSimpleName());
		
		setTiposDeficiencias(Arrays.asList(TipoDeficiencia.values()));

	}

	public void salvar() {

		try {

			condicaoSaude.setTecnico(loginBean.getUsuario());
			condicaoSaude.setTenant_id(loginBean.getTenantId());

			condicaoSaude = condicaoSaudeService.salvar(condicaoSaude);
			MessageUtil.sucesso("Condição Saúde gravada com sucesso.");

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}

	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;

		try {
			condicaoSaude = condicaoSaudeService.obterCondicaoSaude(pessoa, loginBean.getTenantId());

		} catch (NoResultException e) {
			condicaoSaude = new CondicaoSaude();
			condicaoSaude.setPessoa(pessoa);
			condicaoSaude.setTenant_id(loginBean.getTenantId());
		}
	}	

}