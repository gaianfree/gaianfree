package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.softarum.svsa.modelo.Atividade;
import com.softarum.svsa.modelo.Frequencia;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Servico;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class FrequenciaDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(FrequenciaDAO.class);
	
	@Inject
	private EntityManager manager;
	
	
	@Transactional
	public void salvar(Frequencia frequencia) throws NegocioException {	
		log.debug("salvando uma frequencia" );
		try {
			manager.merge(frequencia);	
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
	public void excluir(Frequencia frequencia) throws NegocioException {		
	
		frequencia = buscarPeloCodigo(frequencia.getCodigo());
		try {
			manager.remove(frequencia);
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
	
	public Frequencia buscarPeloCodigo(Long codigo) {
		return manager.find(Frequencia.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Frequencia> buscarTodos(Long tenantId) { 
		return manager.createNamedQuery("Frequencia.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}	

	public List<Frequencia> buscarFrequencias(Atividade atividade, Servico servico, Long tenantId) {
		/*
		 * SELECT * FROM svsa_salto.frequencia f			
				INNER JOIN svsa_salto.inscricao i ON i.codigo_pessoa = f.codigo_pessoa
				INNER JOIN svsa_salto.servico s ON s.codigo = i.codigo_servico	
				WHERE f.codigo_atividade = 4 and i.desistente = true and s.codigo = 3;
		 */		
		List<Frequencia> lista = manager.createQuery("select f FROM Frequencia f "
				+ "INNER JOIN Inscricao i ON i.pessoa.codigo = f.pessoa.codigo "
				+ "INNER JOIN Servico s ON s.codigo = i.servico.codigo "
				+ "where f.atividade = :atividade "
				+ "and f.tenant_id = :tenantId "
				+ "and s.codigo = :servico "
				+ "and i.desistente = false", Frequencia.class)
				.setParameter("atividade", atividade)
				.setParameter("tenantId", tenantId)
				.setParameter("servico", servico.getCodigo())	
				.getResultList();

		return lista;
	}
	
	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}

	public Frequencia buscarFrequencia(Atividade atividade, Pessoa pessoa, Long tenantId) {
		return manager.createNamedQuery("Frequencia.buscarFrequenciaPessoa", Frequencia.class)
				.setParameter("atividade", atividade)
				.setParameter("tenantId", tenantId)
				.setParameter("pessoa", pessoa)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}
	
	public boolean verificarFrequencia(Atividade atividade, Long tenantId) {
		Query q = manager.createQuery("Select count(f.codigo) from Frequencia f  "
				+ "where f.atividade = :atividade "
				+ "and f.tenant_id = :tenantId ");
		q.setParameter("atividade", atividade);	
		q.setParameter("tenantId", tenantId);
		Long qde = (Long) q.getSingleResult();
		
		if(qde > 0) {
			return true;
		}
		return false;
	}
	public boolean verificarFrequencia(Atividade atividade, Pessoa pessoa, Long tenantId) {
		Query q = manager.createQuery("Select count(f.codigo) from Frequencia f  "
				+ "where f.atividade = :atividade "
				+ "and f.pessoa = :pessoa "
				+ "and f.tenant_id = :tenantId ");
		q.setParameter("atividade", atividade);
		q.setParameter("pessoa", pessoa);	
		q.setParameter("tenantId", tenantId);
		Long qde = (Long) q.getSingleResult();
		
		if(qde > 0) {
			return true;
		}
		return false;
	}

	
}