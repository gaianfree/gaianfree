package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.HistoricoEncaminhamento;
import com.softarum.svsa.modelo.PlanoAcompanhamento;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class HistoricoEncaminhamentoDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	@Inject
	private CapaProntuarioDAO capaDAO;
	
	
	
	
	/*
	 *  historico encaminhamento CREAS
	 */	
	
	@Transactional
	public void salvarHE(HistoricoEncaminhamento historicoEncaminhamento) throws NegocioException {
		try {
			manager.merge(historicoEncaminhamento);	
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
	 * Cria prontuario, plano do prontuario e atualiza historico.
	 */
	@Transactional
	public void salvarRecebimento(HistoricoEncaminhamento historico, Prontuario prontuarioVinculado, PlanoAcompanhamento plano) throws NegocioException {
		
		try {
			
			Prontuario p = manager.merge(prontuarioVinculado);		
		
			if(plano != null) {
				plano.setProntuario(p);
				manager.merge(plano);
			}
			manager.merge(historico);		
			Prontuario pp = capaDAO.buscarPeloCodigo(historico.getProntuario().getCodigo());
			pp.setProntuarioVinculado(p);		
			manager.merge(pp);
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
	public void excluir(HistoricoEncaminhamento historico) throws NegocioException {
		historico = buscarPeloCodigoHE(historico.getCodigo());
		try {
			manager.remove(historico);
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
	
	public HistoricoEncaminhamento buscarHEPeloCodigo(Long codigo) {
		return manager.find(HistoricoEncaminhamento.class, codigo);
	}
	
			
	public HistoricoEncaminhamento buscarPeloCodigoHE(Long codigo) {
		return manager.find(HistoricoEncaminhamento.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<HistoricoEncaminhamento> buscarTodosHE(Prontuario prontuario, Long tenantId) {
		return manager.createNamedQuery("HistoricoEncaminhamento.buscarTodosProntuario")
				.setParameter("prontuario", prontuario)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}

	public HistoricoEncaminhamento buscarEncaminhamentoAberto(Prontuario prontuario, Long tenantId) {
		return manager.createNamedQuery("HistoricoEncaminhamento.buscarEncaminhamentoAberto", HistoricoEncaminhamento.class)
				.setParameter("prontuario", prontuario)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
		
	}

	@SuppressWarnings("unchecked")
	public List<HistoricoEncaminhamento> buscarEncaminhamentosPendentes(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("HistoricoEncaminhamento.buscarEncaminhamentosPendentes")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<HistoricoEncaminhamento> buscarEncaminhamentosRecebidos(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("HistoricoEncaminhamento.buscarEncaminhamentosRecebidos")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	
	/* Encaminhamentos realizados */

	public List<HistoricoEncaminhamento> buscarEncaminhamentos(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("HistoricoEncaminhamento.buscarEncaminhamentos", HistoricoEncaminhamento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	public List<HistoricoEncaminhamento> buscarEncaminhamentos(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("HistoricoEncaminhamento.buscarEncaminhamentosPeriodo", HistoricoEncaminhamento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getResultList();
	}
	
	/* Encaminhamentos recebidos */

	public List<HistoricoEncaminhamento> buscarRecebidos(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("HistoricoEncaminhamento.buscarRecebidos", HistoricoEncaminhamento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	public List<HistoricoEncaminhamento> buscarRecebidos(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("HistoricoEncaminhamento.buscarRecebidosPeriodo", HistoricoEncaminhamento.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getResultList();
	}
	
	
}