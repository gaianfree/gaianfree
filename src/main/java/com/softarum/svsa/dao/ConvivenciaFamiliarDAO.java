package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.softarum.svsa.modelo.ConvivenciaFamiliar;
import com.softarum.svsa.modelo.ObsConvivenciaFamiliar;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author gabrielrodrigues
 *
 */
public class ConvivenciaFamiliarDAO implements Serializable {

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
	public ConvivenciaFamiliar salvar(ConvivenciaFamiliar convivenciaFamiliar) throws NegocioException {
		try {
			return manager.merge(convivenciaFamiliar);
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
	public List<ObsConvivenciaFamiliar> salvarObservacao
	(ConvivenciaFamiliar convivenciaFamiliar) throws NegocioException {
		
		try {
			ConvivenciaFamiliar c = manager.merge(convivenciaFamiliar);
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
	
	public ConvivenciaFamiliar obterConvivenciaFamiliar(Prontuario prontuario, Long tenantId) {
		
		return manager.createNamedQuery("ConvivenciaFamiliar.obterConvivenciaFamiliar", ConvivenciaFamiliar.class)
				.setParameter("prontuario", prontuario)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}
}