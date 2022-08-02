package com.softarum.svsa.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.softarum.svsa.dao.CondicaoSaudeDAO;
import com.softarum.svsa.modelo.CondicaoSaude;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.util.NegocioException;

/**
 * @author gabrielrodrigues  - refactored by Murakami
 *
 */
public class CondicaoSaudeService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CondicaoSaudeDAO saudeDAO;


	

	public CondicaoSaude salvar(CondicaoSaude condicaoSaude) throws NegocioException {			
		return saudeDAO.salvar(condicaoSaude);
	}

	public CondicaoSaude obterCondicaoSaude(Pessoa pessoa, Long tenantId) {
		
		return saudeDAO.obterCondicaoSaude(pessoa, tenantId);
	}
	
	public void setSaudeDAO(CondicaoSaudeDAO saudeDAO) {
		this.saudeDAO = saudeDAO;
	}
	
}