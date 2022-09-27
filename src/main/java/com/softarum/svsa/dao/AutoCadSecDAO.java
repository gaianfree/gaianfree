package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.AutoCadSecTO;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
public class AutoCadSecDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;	
	
	@Transactional
	public void salvar(AutoCadSecTO to) throws NegocioException {
		log.info("DAO : tenant = " + to.getSecretaria().getCodigo());
		log.info("DAO : unidade = " + to.getUnidade().getCodigo());
		
		try {
			manager.merge(to.getSecretaria());
			manager.merge(to.getUnidade());
		//	manager.merge(to.getUsuario());
			
			
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
	
	/*@Transactional
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
	}*/
	
	
	
	/*
	 * Buscas
	 */
	
	
	public Tenant buscarPeloCodigo(Long codigo) {
		return manager.find(Tenant.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tenant> buscarTodos(Long codigo) {
		return manager.createNamedQuery("Tenant.buscarTodos")
				.setParameter("codigo", codigo)
				.getResultList();
	}
	
	public List<Tenant> buscarTodosCRAS(Long codigo) {
		return manager.createNamedQuery("Tenant.buscarTodosCRAS", Tenant.class)
				.setParameter("codigo", codigo)
				.setParameter("secretaria", TipoUnidade.CRAS)
				.getResultList();
	}	
	
	public List<Tenant> buscarTodosCREAS(Long codigo) {
		return manager.createNamedQuery("Tenant.buscarTodosCREAS", Tenant.class)
				.setParameter("codigo", codigo)
				.setParameter("secretaria", TipoUnidade.CREAS)
				.getResultList();
	}
	// para o job de geracao de RMA
	public List<Tenant> buscarTodosCRAS() {
		return manager.createNamedQuery("Tenant.buscarTodosCRAS2", Tenant.class)
				.setParameter("secretaria", TipoUnidade.CRAS)
				.getResultList();
	}	
	// para o job de geracao de RMA
	public List<Tenant> buscarTodosCREAS() {
		return manager.createNamedQuery("Tenant.buscarTodosCREAS2", Tenant.class)
				.setParameter("tenant", TipoUnidade.CREAS)
				.getResultList();
	}
		
/*	@SuppressWarnings("unchecked")
	public List<Unidade> buscarComPaginacao(int first, int pageSize, Long tenantId) {
		return manager.createNamedQuery("Unidade.buscarTodos")
				.setParameter("tenantId", tenantId)
				.setFirstResult(first)
				.setMaxResults(pageSize)
				.getResultList();
	}*/

/*	public Long encontrarQuantidadeDeUnidades(Long tenantId) {
		return manager.createQuery("select count(u) from Unidade u where u.tenant_id = :tenantId", Long.class)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}*/
	
	// criado para realização de testes unitários com JIntegrity
	/*public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}*/
}