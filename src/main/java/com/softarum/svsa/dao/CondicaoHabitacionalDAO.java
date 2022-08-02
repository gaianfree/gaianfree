package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.softarum.svsa.modelo.CondicaoHabitacional;
import com.softarum.svsa.modelo.ObsCondicaoHabitacional;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author gabrielrodrigues  - refactored by Murakami
 *
 */
public class CondicaoHabitacionalDAO implements Serializable {

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
	public CondicaoHabitacional salvar(CondicaoHabitacional condicaoHabitacional) throws NegocioException {
		try {
			return manager.merge(condicaoHabitacional);
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
	
	@Transactional
	public List<ObsCondicaoHabitacional> salvarObservacao
	(CondicaoHabitacional condicaoHabitacional) throws NegocioException {
		
		try {
			CondicaoHabitacional c = manager.merge(condicaoHabitacional);
			return c.getObservacoes();
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
	
	
	public CondicaoHabitacional obterCondicaohabitacional(Prontuario prontuario, Long tenantId) {
		
		return manager.createNamedQuery("CondicaoHabitacional.obterCondicaoHabitacional", CondicaoHabitacional.class)
				.setParameter("prontuario", prontuario)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}
	
	
}