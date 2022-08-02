package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.softarum.svsa.modelo.SituacaoViolencia;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author gabrielrodrigues  - refactored by Murakami
 *
 */
public class SituacaoViolenciaDAO implements Serializable {

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
	public void salvar(SituacaoViolencia situacaoCreas) throws NegocioException {
		try {
			manager.merge(situacaoCreas);
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
	public void excluir(SituacaoViolencia situacao) throws NegocioException {
		situacao = buscarPeloCodigo(situacao.getCodigo());
		try {
			manager.remove(situacao);
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
	
	private SituacaoViolencia buscarPeloCodigo(Long codigo) {
		return manager.find(SituacaoViolencia.class, codigo);
	}

	
	public List<SituacaoViolencia> buscarSituacoesViolencia(Pessoa pessoa, Long tenantId) {
		return manager.createNamedQuery("SituacaoViolencia.buscarSituacoesViolencia", SituacaoViolencia.class)
				.setParameter("pessoa", pessoa)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}


}