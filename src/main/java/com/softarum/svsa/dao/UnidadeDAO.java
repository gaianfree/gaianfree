package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
public class UnidadeDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;	
	
	@Transactional
	public void salvar(Unidade unidade) throws NegocioException {
		log.info("DAO : tenant = " + unidade.getTenant_id());
		try {
			manager.merge(unidade);
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
	public void excluir(Unidade unidade) throws NegocioException {
		unidade = buscarPeloCodigo(unidade.getCodigo());
		try {
			manager.remove(unidade);
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
	
	
	public Unidade buscarPeloCodigo(Long codigo) {
		return manager.find(Unidade.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Unidade> buscarTodos(Long tenantId) {
		return manager.createNamedQuery("Unidade.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	public List<Unidade> buscarTodosCRAS(Long tenantId) {
		return manager.createNamedQuery("Unidade.buscarTodosCRAS", Unidade.class)
				.setParameter("tenantId", tenantId)
				.setParameter("tipo", TipoUnidade.CRAS)
				.getResultList();
	}	
	
	public List<Unidade> buscarTodosCREAS(Long tenantId) {
		return manager.createNamedQuery("Unidade.buscarTodosCREAS", Unidade.class)
				.setParameter("tenantId", tenantId)
				.setParameter("tipo", TipoUnidade.CREAS)
				.getResultList();
	}
	// para o job de geracao de RMA
	public List<Unidade> buscarTodosCRAS() {
		return manager.createNamedQuery("Unidade.buscarTodosCRAS2", Unidade.class)
				.setParameter("tipo", TipoUnidade.CRAS)
				.getResultList();
	}	
	// para o job de geracao de RMA
	public List<Unidade> buscarTodosCREAS() {
		return manager.createNamedQuery("Unidade.buscarTodosCREAS2", Unidade.class)
				.setParameter("tipo", TipoUnidade.CREAS)
				.getResultList();
	}	
		
	@SuppressWarnings("unchecked")
	public List<Unidade> buscarComPaginacao(int first, int pageSize, Long tenantId) {
		return manager.createNamedQuery("Unidade.buscarTodos")
				.setParameter("tenantId", tenantId)
				.setFirstResult(first)
				.setMaxResults(pageSize)
				.getResultList();
	}

	public Long encontrarQuantidadeDeUnidades(Long tenantId) {
		return manager.createQuery("select count(u) from Unidade u where u.tenant_id = :tenantId", Long.class)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}
	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
}