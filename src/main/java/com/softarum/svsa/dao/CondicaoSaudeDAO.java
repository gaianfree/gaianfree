package com.softarum.svsa.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.softarum.svsa.modelo.CondicaoSaude;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author gabrielrodrigues  - refactored by Murakami
 *
 */
public class CondicaoSaudeDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	public EntityManager getManager() {
		return manager;
	}
	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	@Transactional
	public CondicaoSaude salvar(CondicaoSaude condicaoSaude) throws NegocioException {
		try {
			return manager.merge(condicaoSaude);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Não foi possível executar a operação.");
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new NegocioException("Não foi possível executar a operação.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException("Não foi possível executar a operação.");
		} catch (Error e) {
			e.printStackTrace();
			throw new NegocioException("Não foi possível executar a operação.");
		}
	}
	
	public CondicaoSaude obterCondicaoSaude(Pessoa pessoa, Long tenantId) {
		
		return manager.createNamedQuery("CondicaoSaude.obterCondicaoSaude", CondicaoSaude.class)
				.setParameter("pessoa", pessoa)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}
	
	
}