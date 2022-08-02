package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.HistPessoaUV;
import com.softarum.svsa.modelo.HistProntuarioUV;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class HistoricoUVDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	
	/*
	 *  historico Prontuário UV
	 */	
	
	
	@Transactional
	public void salvar(HistProntuarioUV historico) throws NegocioException {	
		try {
			manager.merge(historico);
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
	
	
	@SuppressWarnings("unchecked")
	public List<HistProntuarioUV> buscarTodos(Long tenantId) {
		return manager.createNamedQuery("HistProntuarioUV.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<HistProntuarioUV> buscarTodos(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("HistProntuarioUV.buscarTodosUnidade")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<HistProntuarioUV> buscarTodos(Unidade unidade, Date dataInicio, Date dataFim, Long tenantId) {
		return manager.createNamedQuery("HistProntuarioUV.buscarTodosPeriodo")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", dataInicio, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(dataFim), TemporalType.TIMESTAMP)			
				.getResultList();
	}
	
	
	
	/*
	 *  historico Pessoa UV
	 */	
	
	@Transactional
	public void salvar(HistPessoaUV historico) throws NegocioException {	
		try {
			manager.merge(historico);
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
	
	
	@SuppressWarnings("unchecked")
	public List<HistPessoaUV> buscarTodosPUV(Long tenantId) {
		return manager.createNamedQuery("HistPessoaUV.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<HistPessoaUV> buscarTodosPUV(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("HistPessoaUV.buscarTodosUnidade")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<HistPessoaUV> buscarTodosPUV(Unidade unidade, Date dataInicio, Date dataFim, Long tenantId) {
		return manager.createNamedQuery("HistPessoaUV.buscarTodosPeriodo")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", dataInicio, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(dataFim), TemporalType.TIMESTAMP)			
				.getResultList();
	}
	
		
}