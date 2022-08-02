package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.HistoricoEncaminhamento;
import com.softarum.svsa.modelo.HistoricoTransferencia;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.PlanoAcompanhamento;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class HistoricoTransferenciaDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	@Inject
	private CapaProntuarioDAO capaDAO;
	
	
	/*
	 *  historico transferencia
	 */	
	
	
	@Transactional
	public void salvar(HistoricoTransferencia historicoTransferencia) throws NegocioException {	
		try {
			manager.merge(historicoTransferencia);
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
	public void excluir(HistoricoTransferencia historico) throws NegocioException {
		historico = buscarPeloCodigo(historico.getCodigo());
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
	
	public HistoricoTransferencia buscarPeloCodigo(Long codigo) {
		return manager.find(HistoricoTransferencia.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<HistoricoTransferencia> buscarTodos(Long tenantId) {
		return manager.createNamedQuery("HistoricoTransferencia.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	/* Usado pelo helper para lançar na evolução da pessoa */
	public List<AtendimentoDTO> buscarTransferenciasDTO(Pessoa pessoa, Long tenantId) {
		
		/*
		SELECT  h.dataTransferencia,
			h.motivo,
	        t.nome as tecnico,
			u.nome as unidadeDestino,
			pessoa.nome as nomePessoa,
			pessoa.codigo as codigoPessoa              
		FROM svsa_salto.Pessoa pessoa
			INNER JOIN svsa_salto.Familia familia ON (pessoa.codigo_familia = familia.codigo)
			INNER JOIN svsa_salto.Prontuario p ON (p.codigo = familia.codigo_prontuario)    
			INNER JOIN svsa_salto.HistoricoTransferencia h ON (p.codigo = h.codigo_prontuario)
			INNER JOIN svsa_salto.Unidade u ON (h.codigo_destino = u.codigo)
			INNER JOIN svsa_salto.usuario t ON (h.codigo_usuario = t.codigo)
	    WHERE pessoa.codigo = 25738;
		 */	
		List<AtendimentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoDTO( "
				+ "h.dataTransferencia, "
				+ "h.motivo, "
				+ "t.nome, "
				+ "u.nome, "
				+ "pessoa.nome) "
			+ "FROM Pessoa pessoa "
				+ "INNER JOIN Familia f ON (pessoa.familia = f.codigo) "
				+ "INNER JOIN Prontuario p ON (p.codigo = f.prontuario) "
			    + "INNER JOIN HistoricoTransferencia h ON (p.codigo = h.prontuario) "
			    + "INNER JOIN Unidade u ON (h.unidade = u.codigo) "
			    + "INNER JOIN Usuario t ON (h.usuario = t.codigo) "
			+ "WHERE pessoa.codigo = :codigo_pessoa "
			+ "and pessoa.tenant_id = :tenantId ", AtendimentoDTO.class)
		.setParameter("codigo_pessoa", pessoa.getCodigo())
		.setParameter("tenantId", tenantId)
		.getResultList();
		
		return lista;

	}

	@SuppressWarnings("unchecked")
	public List<HistoricoTransferencia> buscarTodos(Prontuario prontuario, Long tenantId) {
		return manager.createNamedQuery("HistoricoTransferencia.buscarTodosProntuario")
				.setParameter("prontuario", prontuario)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	public List<HistoricoTransferencia> buscarTodosUnidade(Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("HistoricoTransferencia.buscarTodosUnidade")
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	
	
	
	public List<HistoricoTransferencia> buscarTodosPeriodo(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createNamedQuery("HistoricoTransferencia.buscarTodosPeriodo", HistoricoTransferencia.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getResultList();
	}
	
	
	
	/*
	 *  historico encaminhamento CREAS
	 */	
	
	@Transactional
	public void salvarHE(HistoricoEncaminhamento historicoEncaminhamento) {
		manager.merge(historicoEncaminhamento);			
	}
	
	/*
	 * Cria prontuario, plano do prontuario e atualiza historico.
	 */
	@Transactional
	public void salvarRecebimento(HistoricoEncaminhamento historico, Prontuario prontuarioVinculado, PlanoAcompanhamento plano) {		
		Prontuario p = manager.merge(prontuarioVinculado);
		plano.setProntuario(p);
		manager.merge(plano);
		manager.merge(historico);		
		Prontuario pp = capaDAO.buscarPeloCodigo(historico.getProntuario().getCodigo());
		pp.setProntuarioVinculado(p);
		manager.merge(pp);
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
	
	
}