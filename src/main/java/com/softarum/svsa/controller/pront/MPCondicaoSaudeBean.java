package com.softarum.svsa.controller.pront;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.primefaces.PrimeFaces;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.CondicaoSaude;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.service.CondicaoSaudeService;

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
public class MPCondicaoSaudeBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287181L;
	
	private CondicaoSaude condicaoSaude;
	private List<Pessoa> membros;
	private Pessoa pessoa;

	@Inject
	LoginBean loginBean;
	@Inject
	private CondicaoSaudeService saudeService;
		

	private void buscarCondicaoSaude(){
		try {
			
			condicaoSaude = saudeService.obterCondicaoSaude(getPessoa(), loginBean.getTenantId());
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('saudeWidget').show();");
			
		}
		catch(NoResultException e) {			
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('saudeWidget2').show();");			
		}
	}
	
	public boolean isPessoaSelecionada() {
        return pessoa != null && pessoa.getCodigo() != null;
    }
	
	public boolean isCondicaoSau() {
        return condicaoSaude != null && condicaoSaude.getCodigo() != null;
    }
	
	public void setPessoa(Pessoa pessoa) {
		log.info("Pessoa selecionada: " + pessoa.getNome());
		this.pessoa = pessoa;
		buscarCondicaoSaude();
	}
	
	public void setMembros(List<Pessoa> membros) {
		this.membros = membros;
		if(this.membros != null && this.membros.size() > 0 )
			setPessoa(membros.get(0));
	}
	
}