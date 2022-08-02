package com.softarum.svsa.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.softarum.svsa.dao.CondicaoEducacionalDAO;
import com.softarum.svsa.modelo.CondicaoEducacional;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.util.NegocioException;

/**
 * @author gabrielrodrigues
 *
 */
public class CondicaoEducacionalService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CondicaoEducacionalDAO educacionalDAO;


	

	public CondicaoEducacional salvar(CondicaoEducacional condicaoEducacional) throws NegocioException {			
		return educacionalDAO.salvar(condicaoEducacional);
	}

	public CondicaoEducacional obterCondicaoEducacional(Pessoa pessoa, Long tenantId) {
		
		return educacionalDAO.obterCondicaoEducacional(pessoa, tenantId);
	}
	
	public void setEducacionalDAO(CondicaoEducacionalDAO educacionalDAO) {
		this.educacionalDAO = educacionalDAO;
	}
	
}