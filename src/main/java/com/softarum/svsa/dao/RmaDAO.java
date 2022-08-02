package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.softarum.svsa.modelo.RmaCras;
import com.softarum.svsa.modelo.RmaCreas;
import com.softarum.svsa.modelo.RmaPop;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.jpa.Transactional;

import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
public class RmaDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	
	/*
	 * CRAS
	 */
	
	
	@Transactional
	public RmaCras salvarCras(RmaCras rmaCras) throws PersistenceException {
		log.info("Salvando rmaCras ...");
		return manager.merge(rmaCras);
	}
	
	public RmaCras buscarCras(Long codigo) {
		return manager.find(RmaCras.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<RmaCras> buscarTodosCras(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("RmaCras.buscarTodos")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	public RmaCras buscarRmaCrasFechado(Unidade unidade, String mesAnoRef, Long tenantId) {
		return manager.createNamedQuery("RmaCras.buscarRmaFechado", RmaCras.class)
				.setParameter("unidade", unidade)
				.setParameter("mesAnoRef", mesAnoRef)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}
	@SuppressWarnings("unchecked")
	public List<String> buscarRmasCrasFechados(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("RmaCras.buscarRmasFechados")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	
	
	
	/*
	 * CREAS
	 */
	
	
	@Transactional
	public RmaCreas salvarCreas(RmaCreas rmaCreas) throws PersistenceException {
		log.info("Salvando rmaCreas ...");
		return manager.merge(rmaCreas);
	}		
	
	public RmaCreas buscarCreas(Long codigo) {
		return manager.find(RmaCreas.class, codigo);
	}	
	
	@SuppressWarnings("unchecked")
	public List<RmaCreas> buscarTodosCreas(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("RmaCreas.buscarTodos")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	
	public RmaCreas buscarRmaCreasFechado(Unidade unidade, String mesAnoRef, Long tenantId) {
		return manager.createNamedQuery("RmaCreas.buscarRmaFechado", RmaCreas.class)
				.setParameter("unidade", unidade)
				.setParameter("mesAnoRef", mesAnoRef)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<String> buscarRmasCreasFechados(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("RmaCreas.buscarRmasFechados")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	
	
	
	/*
	 * POP
	 */
	
	
	@Transactional
	public RmaPop salvarPop(RmaPop rmaPop) throws PersistenceException {
		log.info("Salvando rmaPop ...");
		return manager.merge(rmaPop);
	}
	
	public RmaPop buscarPop(Long codigo) {
		return manager.find(RmaPop.class, codigo);
	}
	@SuppressWarnings("unchecked")
	public List<RmaPop> buscarTodosPop(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("RmaPop.buscarTodos")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	
	public RmaPop buscarRmaPopFechado(Unidade unidade, String mesAnoRef, Long tenantId) {
		return manager.createNamedQuery("RmaPop.buscarRmaFechado", RmaPop.class)
				.setParameter("unidade", unidade)
				.setParameter("mesAnoRef", mesAnoRef)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<String> buscarRmasPopFechados(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("RmaPop.buscarRmasFechados")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	
	
	
	
	
	
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
}