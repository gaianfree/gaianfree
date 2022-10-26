package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.softarum.svsa.modelo.Tenant;

/**
 * @author murakamiadmin
 *
 */
public class TenantDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;	
	
	
	/*
	 * Buscas
	 */
	
	
	public Tenant buscarPeloCodigo(Long codigo) {
		return manager.find(Tenant.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tenant> buscarTodos() {
		return manager.createNamedQuery("Tenant.buscarTodos")
				.getResultList();
	}
		
	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
}