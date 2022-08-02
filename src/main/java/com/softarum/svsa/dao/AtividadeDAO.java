package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.softarum.svsa.modelo.Atividade;
import com.softarum.svsa.modelo.Servico;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class AtividadeDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	
	@Transactional
	public void salvar(Atividade atividade) throws NegocioException {
		try {
			manager.merge(atividade);
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
	public void excluir(Atividade atividade) throws NegocioException {		
	
		atividade = buscarPeloCodigo(atividade.getCodigo());
		try {
			manager.remove(atividade);
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
	
	public Atividade buscarPeloCodigo(Long codigo) {
		return manager.find(Atividade.class, codigo);
	}
	

	@SuppressWarnings("unchecked")
	public Set<Atividade> buscarTodos(Long tenantId) {

		return new HashSet<Atividade>( manager.createNamedQuery("Atividade.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList() );
	}	

	public Set<Atividade> buscarAtividades(Servico servico, Long tenantId) {		
		return new HashSet<Atividade>( manager.createNamedQuery("Atividade.buscarAtividades", Atividade.class)
				.setParameter("servico", servico)
				.setParameter("tenantId", tenantId)
				.getResultList() );	
	}
	
	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
}