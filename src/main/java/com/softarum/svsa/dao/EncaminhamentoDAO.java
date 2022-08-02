package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.Encaminhamento;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author gabrielrodrigues/Murakami
 *
 */
public class EncaminhamentoDAO implements Serializable {

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
	public Encaminhamento salvar(Encaminhamento encaminhamento) throws PersistenceException, NegocioException {
		try {
			return manager.merge(encaminhamento);
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
	public void excluir(Encaminhamento encaminhamento) throws NegocioException {
		encaminhamento = buscarPeloCodigo(encaminhamento.getCodigo());
		try {
			manager.remove(encaminhamento);
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
	
	private Encaminhamento buscarPeloCodigo(Long codigo) {
		return manager.find(Encaminhamento.class, codigo);
	}

	
	public List<Encaminhamento> buscarEncaminhamentos(Pessoa pessoa, Long tenantId) {
		return manager.createNamedQuery("Encaminhamento.buscarEncaminhamentos", Encaminhamento.class)
				.setParameter("pessoa", pessoa)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	/*
	 * RelatorioEncExterno
	 */
	public List<Encaminhamento> buscarEncaminhamentos(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Encaminhamento.buscarEncaminhamentosUnidade", Encaminhamento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	public List<Encaminhamento> buscarEncaminhamentos(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("Encaminhamento.buscarEncaminhamentosPeriodo", Encaminhamento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getResultList();
	}
	/*
	 * RelatorioEncExterno Gráfico
	 */
	public List<Encaminhamento> buscarEncaminhamentosGrafico(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Encaminhamento.buscarEncGraficoUnidade", Encaminhamento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	public List<Encaminhamento> buscarEncaminhamentosGrafico(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("Encaminhamento.buscarEncGraficoPeriodo", Encaminhamento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getResultList();
	}


}