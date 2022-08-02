package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.Inscricao;
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
public class InscricaoDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	
	@Transactional
	public void adicionar(Inscricao inscricao) throws NegocioException {
		try {
			manager.merge(inscricao);
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
	public void alterar(Inscricao inscricao) throws NegocioException {	
		try {
			manager.merge(inscricao);
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
	public void cancelar(Inscricao inscricao) throws NegocioException {	
		try {
			manager.merge(inscricao);
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
	public void excluir(Inscricao inscricao) throws NegocioException {		
	
		log.info("obj inscricao na pagina: " + inscricao);
		inscricao = buscarPeloCodigo(inscricao.getCodigo());
		log.info("obj inscricao na banco: " + inscricao);
		
		try {
			manager.remove(inscricao);
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
	
	public Inscricao buscarPeloCodigo(Long codigo) {
		return manager.find(Inscricao.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public Set<Inscricao> buscarTodos(Long tenantId) { 
		return new HashSet<Inscricao>( manager.createNamedQuery("Inscricao.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList() );
	}	

	public Set<Inscricao> buscarInscricoes(Servico servico, Long tenantId) {		
		return new HashSet<Inscricao>( manager.createNamedQuery("Inscricao.buscarInscricoes", Inscricao.class)
				.setParameter("servico", servico)
				.setParameter("tenantId", tenantId)
				.getResultList() );	
	}

	public Set<Inscricao> buscarInscricoesAtivas(Servico servico, Long tenantId) {		
		return new HashSet<Inscricao>( manager.createNamedQuery("Inscricao.buscarInscricoesAtivas", Inscricao.class)
				.setParameter("servico", servico)
				.setParameter("tenantId", tenantId)
				.getResultList() );	
	}
	
	public Long qdeInscricoesAtivas(Servico servico, Long tenantId) {		
		return manager.createNamedQuery("Inscricao.qdeInscricoesAtivas", Long.class)
				.setParameter("servico", servico)
				.setParameter("tenantId", tenantId)
				.getSingleResult();	
	}
	
	// busca quantidade de pessoas participando/sem repetiçao/ativos
	public Long buscarQdePessoas(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("SELECT count(distinct i.pessoa.codigo) "
				+ "	FROM Inscricao i "
				+ "		INNER JOIN Servico s ON i.servico.codigo = s.codigo "
				+ "	Where s.unidade = :unidade "
				+ " and i.desistente = :desistente "
				+ " and s.tenant_id = :tenantId "
				+ " and s.status = :status "
				+ " and s.dataIni between :ini and :fim", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("desistente", false)
				.setParameter("status", Status.ATIVO)
				.getSingleResult();	
	}
	public Long buscarQdePessoasExec(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("SELECT count(distinct i.pessoa.codigo) "
				+ "	FROM Inscricao i "
				+ "	INNER JOIN Servico s ON i.servico.codigo = s.codigo "
				+ "	Where s.unidadeExecutora = :unidade "
				+ " and i.desistente = :desistente "
				+ " and s.tenant_id = :tenantId "
				+ " and s.status = :status "
				+ " and s.dataIni between :ini and :fim", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("desistente", false)
				.setParameter("status", Status.ATIVO)
				.getSingleResult();	
	}
	public Long buscarQdePessoasTotal(Unidade unidade, Long tenantId) {		
		return manager.createQuery("SELECT count(distinct i.pessoa.codigo) "
				+ "	FROM Inscricao i "
				+ "	INNER JOIN Servico s ON i.servico.codigo = s.codigo "
				+ "	Where s.unidade = :unidade "
				+ " and i.desistente = :desistente "
				+ " and s.tenant_id = :tenantId "
				+ " and s.status = :status ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("desistente", false)
				.setParameter("status", Status.ATIVO)
				.getSingleResult();	
	}
	public Long buscarQdePessoasExecTotal(Unidade unidade, Long tenantId) {		
		return manager.createQuery("SELECT count(distinct i.pessoa.codigo) "
				+ "	FROM Inscricao i "
				+ "	INNER JOIN Servico s ON i.servico.codigo = s.codigo "
				+ "	Where s.unidadeExecutora = :unidade "
				+ " and i.desistente = :desistente "
				+ " and s.tenant_id = :tenantId "
				+ " and s.status = :status ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("desistente", false)
				.setParameter("status", Status.ATIVO)
				.getSingleResult();	
	}
	
	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
}