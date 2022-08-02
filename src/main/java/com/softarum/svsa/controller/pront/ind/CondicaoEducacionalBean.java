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
import com.softarum.svsa.modelo.CondicaoEducacional;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.enums.Escolaridade;
import com.softarum.svsa.service.CondicaoEducacionalService;
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
@Named(value="educacionalBean")
@ViewScoped
public class CondicaoEducacionalBean implements Serializable {

	
	private static final long serialVersionUID = 1769116747361287180L;
	
	private CondicaoEducacional condicaoEducacional;
	private List<Escolaridade> escolaridades;	
	private Pessoa pessoa;
	
	@Inject
	private LoginBean loginBean;
	@Inject 
	private CondicaoEducacionalService condicaoEducacionalService;
	
	
	
	@PostConstruct
	public void inicializar() {		
		log.info("[LOG] " + loginBean.getUserName() + " -> " + this.getClass().getSimpleName());
		
		setEscolaridades(Arrays.asList(Escolaridade.values()));

	}

	public void salvar() {

		try {
			log.info("[LOG] " + "salvar");
			condicaoEducacional.setTecnico(loginBean.getUsuario());
			condicaoEducacional.setTenant_id(loginBean.getTenantId());

			condicaoEducacional = condicaoEducacionalService.salvar(condicaoEducacional);
			MessageUtil.sucesso("Condição Educacional gravada com sucesso.");

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}

	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
		
		log.info("Pessoa selecionada: " + getPessoa().getNome());
		

		try {
			condicaoEducacional = condicaoEducacionalService.obterCondicaoEducacional(pessoa, loginBean.getTenantId());

		} catch (NoResultException e) {
			condicaoEducacional = new CondicaoEducacional();
			condicaoEducacional.setPessoa(pessoa);
			condicaoEducacional.setTenant_id(loginBean.getTenantId());
		}
	}	

}