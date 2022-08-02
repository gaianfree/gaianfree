package com.softarum.svsa.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.softarum.svsa.dao.CondicaoTrabalhoDAO;
import com.softarum.svsa.modelo.CondicaoTrabalho;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.util.NegocioException;

/**
 * @author gabrielrodrigues  - refactored by Murakami
 *
 */
public class CondicaoTrabalhoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CondicaoTrabalhoDAO trabalhoDAO;


	

	public CondicaoTrabalho salvar(CondicaoTrabalho condicaoTrabalho) throws NegocioException {			
		return trabalhoDAO.salvar(condicaoTrabalho);
	}

	public CondicaoTrabalho obterCondicaoTrabalho(Pessoa pessoa, Long tenantId) {
		
		return trabalhoDAO.obterCondicaoTrabalho(pessoa, tenantId);
	}
	
	public void setTrabalhoDAO(CondicaoTrabalhoDAO trabalhoDAO) {
		this.trabalhoDAO = trabalhoDAO;
	}
	
}