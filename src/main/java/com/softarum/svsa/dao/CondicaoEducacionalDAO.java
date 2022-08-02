package com.softarum.svsa.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.softarum.svsa.modelo.CondicaoEducacional;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author gabrielrodrigues
 *
 */
public class CondicaoEducacionalDAO implements Serializable {

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
	public CondicaoEducacional salvar(CondicaoEducacional condicaoEducacional) throws NegocioException {
		try {
			return manager.merge(condicaoEducacional);
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
	
	public CondicaoEducacional obterCondicaoEducacional(Pessoa pessoa, Long tenantId) {
		
		return manager.createNamedQuery("CondicaoEducacional.obterCondicaoEducacional", CondicaoEducacional.class)
				.setParameter("pessoa", pessoa)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}
	
	
}