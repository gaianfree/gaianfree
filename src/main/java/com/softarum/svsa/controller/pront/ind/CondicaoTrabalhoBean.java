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
import com.softarum.svsa.modelo.CondicaoTrabalho;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.enums.CondicaoOcupacao;
import com.softarum.svsa.service.CondicaoTrabalhoService;
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
@Named(value="trabalhoBean")
@ViewScoped
public class CondicaoTrabalhoBean implements Serializable {

	
	private static final long serialVersionUID = 1769116747361287180L;
	
	private CondicaoTrabalho condicaoTrabalho;
	private List<CondicaoOcupacao> condicoesOcupacao;	
	private Pessoa pessoa;
	
	@Inject
	private LoginBean loginBean;
	@Inject 
	private CondicaoTrabalhoService condicaoTrabalhoService;
	
	
	
	@PostConstruct
	public void inicializar() {		
		log.info("[LOG] " + loginBean.getUserName() + " -> " + this.getClass().getSimpleName());
		
		setCondicoesOcupacao(Arrays.asList(CondicaoOcupacao.values()));

	}

	public void salvar() {

		try {

			condicaoTrabalho.setTecnico(loginBean.getUsuario());
			condicaoTrabalho.setTenant_id(loginBean.getTenantId());

			condicaoTrabalho = condicaoTrabalhoService.salvar(condicaoTrabalho);
			MessageUtil.sucesso("Condição Trabalho gravada com sucesso.");

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}

	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;

		try {
			condicaoTrabalho = condicaoTrabalhoService.obterCondicaoTrabalho(pessoa, loginBean.getTenantId());

		} catch (NoResultException e) {
			condicaoTrabalho = new CondicaoTrabalho();
			condicaoTrabalho.setPessoa(pessoa);
			condicaoTrabalho.setTenant_id(loginBean.getTenantId());
		}
	}

}