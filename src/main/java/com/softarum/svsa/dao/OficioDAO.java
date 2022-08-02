package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.Oficio;
import com.softarum.svsa.modelo.OficioEmitido;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

import lombok.extern.log4j.Log4j;

/**
 * @author Murakami
 *
 */
@Log4j
public class OficioDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	
	@Transactional
	public Oficio salvar(Oficio oficio) throws NegocioException {
		try {
			return manager.merge(oficio);
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
	public OficioEmitido salvarResposta(Oficio oficio, OficioEmitido oficioEmitido) throws NegocioException {
		
		try {
			oficioEmitido = manager.merge(oficioEmitido);			
			log.debug(oficioEmitido.toString());
			
			oficio.setNrOficioResp(oficioEmitido.getNrOficioEmitido());
			oficio.setOficioEmitido(oficioEmitido);			
			oficio = manager.merge(oficio);
			log.debug(oficio.toString());
			
			return oficioEmitido;
			
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
	public void excluir(Oficio oficio) throws NegocioException {
		oficio = buscarPeloCodigo(oficio.getCodigo());
		try {
			manager.remove(oficio);
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
	 * Caso de uso ListaAtendimento Helper - destinado a Histórico dapessoa
	 */
	public List<Oficio> buscarOficiosHist(Pessoa pessoa, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarOficiosHist", Oficio.class)
				.setParameter("pessoa", pessoa)
				.setParameter("tenantId", tenantId)
				.getResultList();	
	}
	
	
	/*
	 * Caso de Uso: Receber Oficio
	 */	
	public List<Oficio> buscarTodosOficiosRecebidos(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarTodosOficios", Oficio.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	public List<Oficio> buscarOficiosRecebidos(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarOficiosRecebidos", Oficio.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();	
	}
	public List<Oficio> buscarTodosOficiosSasc(Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarTodosOficiosSasc", Oficio.class)	
				.setParameter("tenantId", tenantId)
				.getResultList();	
	}
	
	
	/*
	 * RelatorioOficio
	 */
	public List<Oficio> buscarOficiosUnidade(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarOficiosUnidade", Oficio.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	public List<Oficio> buscarOficiosUnidade(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarOficiosUnidadePeriodo", Oficio.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getResultList();
	}
	
	/* Consultas para verificar ofícios enviados pela sasc */
	public List<Oficio> buscarOficiosSasc(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarOficiosSasc", Oficio.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	public List<Oficio> buscarOficiosSasc(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarOficiosSascPeriodo", Oficio.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getResultList();
	}
	
	/*
	 * RelatorioOficios Gráfico
	 */
	public List<Oficio> buscarOficiosGrafico(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarOfGraficoUnidade", Oficio.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	public List<Oficio> buscarOficiosGrafico(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarOfGraficoPeriodo", Oficio.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getResultList();
	}
	
	/* Gráfico dos ofícios enviados pela sasc */
	public List<Oficio> buscarOficiosGraficoSasc(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarOfGraficoSasc", Oficio.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	public List<Oficio> buscarOficiosGraficoSasc(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarOfGraficoSascPeriodo", Oficio.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getResultList();
	}

	
	/*
	 * Avisos /SvsaHome.xhtml
	 */
	public List<Oficio> buscarOficiosPendentes(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("Oficio.buscarOficiosPendentes", Oficio.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.getResultList();
	}
	
	/*
	 * Outros métodos
	 */
	
	private Oficio buscarPeloCodigo(Long codigo) {
		return manager.find(Oficio.class, codigo);
	}	
	
	public EntityManager getManager() {
		return manager;
	}
	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	
	
}