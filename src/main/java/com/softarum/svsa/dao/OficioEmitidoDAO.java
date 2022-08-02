package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.OficioEmitido;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author Lauro
 *
 */
public class OficioEmitidoDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	
	@Transactional
	public OficioEmitido salvar(OficioEmitido oficioEmitido) throws NegocioException {
		try {
			return manager.merge(oficioEmitido);
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
	public void excluir(OficioEmitido oficioEmitido) throws NegocioException {
		//oficioEmitido = buscarPeloCodigo(oficioEmitido.getCodigo());
		try {
			manager.merge(oficioEmitido);
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
	 * Caso de uso ListaAtendimento Helper - destinado a Histórico da pessoa
	 */
	public List<OficioEmitido> buscarOficiosEmitidosHist(Pessoa pessoa, Long tenantId) {
		return manager.createNamedQuery("OficioEmitido.buscarOficiosEmitidosHist", OficioEmitido.class)
				.setParameter("pessoa", pessoa)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.getResultList();	
	}
	
	/*
	 * Caso de Uso: Emitir Oficio
	 */
	public List<OficioEmitido> buscarOficiosEmitidos(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("OficioEmitido.buscarOficiosEmitidos", OficioEmitido.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.getResultList();	
	}

	/*
	 * Caso de uso: Relatorio Oficio Emitido
	 */
	public List<OficioEmitido> buscarOficiosEmitidosUnidade(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("OficioEmitido.buscarOficiosEmitidosUnidade", OficioEmitido.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.getResultList();
	}
	
	public List<OficioEmitido> buscarOficiosEmitidosUnidade(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("OficioEmitido.buscarOficiosEmitidosUnidadePeriodo", OficioEmitido.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("exc", false)
				.getResultList();
	}
	
	/*
	 * Relatorio Oficios Emitidos Gráfico
	 */
	
	public List<OficioEmitido> buscarOficiosEmitidosGrafico(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("OficioEmitido.buscarOfGraficoEmitidosUnidade", OficioEmitido.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.getResultList();
	}	
	public List<OficioEmitido> buscarOficiosEmitidosGrafico(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("OficioEmitido.buscarOfGraficoEmitidosPeriodo", OficioEmitido.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("exc", false)
				.getResultList();
	}

	/*
	 * Outros métodos
	 */
	
	public Long obterUltimoOficio(Unidade unidade, Integer ano, Long tenantId) {
		return (Long) manager.createNamedQuery("OficioEmitido.obterNrOficioEmitido")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ano", ano)
				.setParameter("exc", false)
				.getSingleResult();
	}
	
	public OficioEmitido buscarPeloCodigo(Long codigo) {
		return manager.find(OficioEmitido.class, codigo);
	}	
	
	public EntityManager getManager() {
		return manager;
	}
	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	
}