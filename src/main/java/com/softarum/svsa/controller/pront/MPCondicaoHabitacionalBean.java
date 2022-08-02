package com.softarum.svsa.controller.pront;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.CondicaoHabitacional;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.service.CondicaoHabitacionalService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin  - refactored by Murakami
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class MPCondicaoHabitacionalBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287181L;
	
	private CondicaoHabitacional condicaoHabitacional = null;
	private PessoaReferencia pessoaReferencia;	
	
	@Inject
	LoginBean loginBean;
	@Inject
	private CondicaoHabitacionalService condicaoService;
		
	
	public void buscarCondicaoHabitacional(){
		try {
			condicaoHabitacional = condicaoService.obterCondicaohabitacional(getPessoaReferencia().getFamilia().getProntuario(), loginBean.getTenantId());
			log.info(condicaoHabitacional.toString());
		}
		catch(NoResultException e) {
			condicaoHabitacional = null;
		}
	}
	
	public boolean isPessoaReferenciaSelecionada() {
        return pessoaReferencia != null && pessoaReferencia.getCodigo() != null;
    }
	
	public void setPessoaReferencia(PessoaReferencia pessoaReferencia) {
		this.pessoaReferencia = pessoaReferencia;
		buscarCondicaoHabitacional();
	}

}