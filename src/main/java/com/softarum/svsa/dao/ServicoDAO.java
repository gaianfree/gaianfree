package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.Servico;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
public class ServicoDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	
	@Transactional
	public Servico salvar(Servico servico) throws NegocioException {	
		try {
			return manager.merge(servico);	
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
	public Servico salvarCopia(Servico servico) throws NegocioException {
		try {
			return manager.merge(servico);
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
	public void excluir(Servico servico) throws NegocioException {		
	
		servico = buscarPeloCodigo(servico.getCodigo());
		log.info(servico.getCodigo());
		try {
			manager.remove(servico);
			manager.flush();
			log.info("excluido!");
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
	
	public Servico buscarPeloCodigo(Long codigo) {
		return manager.find(Servico.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Servico> buscarTodos(Long tenantId) { 
		return manager.createNamedQuery("Servico.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	

	public List<Servico> buscarServicosAno(Integer ano, Unidade unidade, Long tenantId) {		
		return manager.createNamedQuery("Servico.buscarServicosAno", Servico.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ano", ano)
				.getResultList();	
	}
	public List<Servico> buscarServicosAnoExecutora(Integer ano, Unidade unidade, Long tenantId) {		
		return manager.createNamedQuery("Servico.buscarServicosAnoExecutora", Servico.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ano", ano)
				.getResultList();	
	}
	
	public Long qdeServicosAtivos(Unidade unidade, Long tenantId) {		
		return manager.createNamedQuery("Servico.buscarServicosUnidadeAtivos", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", Status.ATIVO)
				.getSingleResult();	
	}
		
	
	public List<Servico> buscarServicos(Unidade unidade, Long tenantId) {		
		return manager.createNamedQuery("Servico.buscarServicosUnidade", Servico.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();	
	}
	
	public List<Servico> buscarServicosPeriodo(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Servico.buscarServicosAtivos", Servico.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", Status.ATIVO)
				.getResultList();
	}	
	public List<Servico> buscarServicosPeriodo(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("Servico.buscarServicosPeriodo", Servico.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", Status.ATIVO)
				.getResultList();
	}
	//executora dos serviços
	public List<Servico> buscarServicosPeriodoExec(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Servico.buscarServicosAtivosExecutora", Servico.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", Status.ATIVO)
				.getResultList();
	}		
	// buscar serviços da unidade excutora
	public List<Servico> buscarServicosPeriodoExec(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("Servico.buscarServicosPeriodoExecutora", Servico.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", Status.ATIVO)
				.getResultList();
	}
	
	
	
	/*
	 * DashBoard
	 */
	
	public Long buscarQdeSCFV(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("SELECT count(s) FROM Servico s "
				+ "WHERE s.dataIni between :ini and :fim "
				+ "and s.unidade = :unidade "
				+ "and s.tenant_id = :tenantId "
				+ "and s.status = :status", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", Status.ATIVO)
				.getSingleResult();	
	}
	
	
	
	// criado para realização de testes unitários com JIntegrity
		public void setEntityManager(EntityManager manager) {
			this.manager = manager;
		}

	
}