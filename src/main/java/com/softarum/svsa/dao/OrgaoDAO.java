package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.softarum.svsa.modelo.Orgao;
import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author Talita
 *
 */
public class OrgaoDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	
	@Transactional
	public void salvar(Orgao orgao) throws NegocioException {
		try {
			manager.merge(orgao);
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
	public void excluir(Orgao orgao) throws NegocioException {
		orgao = buscarPeloCodigo(orgao.getCodigo());
		try {
			manager.remove(orgao);
			manager.flush();
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
	
	
	
	/*
	 * Buscas
	 */
	
	public List<Orgao> buscarCodigosEncaminhamento(CodigoEncaminhamento codigosEncaminhamento, Long tenantId) {
		return manager.createNamedQuery("Orgao.buscarCodigosEncaminhamento", Orgao.class)
				.setParameter("codigosEncaminhamento", codigosEncaminhamento)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	public Orgao buscarPeloCodigo(Long codigo) {
		return manager.find(Orgao.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Orgao> buscarTodos(Long tenantId) {
		return manager.createNamedQuery("Orgao.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
		
	@SuppressWarnings("unchecked")
	public List<Orgao> buscarComPaginacao(int first, int pageSize, Long tenantId) {
		return manager.createNamedQuery("Orgao.buscarTodos")
				.setParameter("tenantId", tenantId)
				.setFirstResult(first)
				.setMaxResults(pageSize)
				.getResultList();
	}

	public Long encontrarQuantidadeDeOrgaos(Long tenantId) {
		return manager.createQuery("select count(o) from Unidade o where o.tenant_id = :tenantId", Long.class)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}
	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
	
}
