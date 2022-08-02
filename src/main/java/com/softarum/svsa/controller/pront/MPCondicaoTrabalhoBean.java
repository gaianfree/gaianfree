package com.softarum.svsa.controller.pront;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.primefaces.PrimeFaces;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.CondicaoTrabalho;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.service.CondicaoTrabalhoService;

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
@Named
@ViewScoped
public class MPCondicaoTrabalhoBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287181L;
	
	private CondicaoTrabalho condicaoTrabalho;
	private List<Pessoa> membros;
	private Pessoa pessoa;

	@Inject
	LoginBean loginBean;
	@Inject
	private CondicaoTrabalhoService trabalhoService;
		

	public void buscarCondicaoTrabalho(){
		try {
			condicaoTrabalho = trabalhoService.obterCondicaoTrabalho(getPessoa(), loginBean.getTenantId());
			log.info("condição trabalho = " + condicaoTrabalho.getRelato());
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('trabWidget').show();");
		}
		catch(NoResultException e) {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('trabalhoWidget2').show();");
		}
	}
	
	public boolean isPessoaSelecionada() {
        return pessoa != null && pessoa.getCodigo() != null;
    }
	
	public boolean isCondicaoTra() {
        return condicaoTrabalho != null && condicaoTrabalho.getCodigo() != null;
    }
	
	public void setPessoa(Pessoa pessoa) {
		log.info("Pessoa selecionada: " + pessoa.getNome());
		this.pessoa = pessoa;
		buscarCondicaoTrabalho();
	}

	public void setMembros(List<Pessoa> membros) {
		this.membros = membros;
		if(this.membros != null && this.membros.size() > 0 )
			setPessoa(membros.get(0));
	}
	
}