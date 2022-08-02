package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.Acao;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class AcaoDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;	
	
	@Transactional
	public Acao salvar(Acao acao) throws NegocioException {
		try {
			return manager.merge(acao);	
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw e;
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
	public void salvar(List<Acao> acoes) throws NegocioException {	
		
		try {
			for(Acao a : acoes) {
				manager.merge(a);
			}
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw e;
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
	public void excluir(Acao acao) throws NegocioException {
		acao = buscarPeloCodigo(acao.getCodigo());
		try {
			manager.remove(acao);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw e;
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
	
	public Acao buscarPeloCodigo(Long codigo) {
		return manager.find(Acao.class, codigo);
	}
	
	
	public List<Acao> buscarAcoesPendentes(Unidade unidade, Long tenantId) {			
		return manager.createNamedQuery("Acao.buscarAcoesPendentes", Acao.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.AGENDADO)
				.getResultList();	
	}
	
	@SuppressWarnings("unchecked")
	public List<Acao> buscarTodos(Long tenantId) {
		return manager.createNamedQuery("Acao.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	
	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}

	public List<Acao> buscarAcoes(Pessoa pessoa, Long tenantId) {
		return manager.createNamedQuery("Acao.buscarAcoes", Acao.class)
			.setParameter("pessoa", pessoa)
			.setParameter("tenantId", tenantId)
			.getResultList();	
	}
	/* ########################################
	 * Consulta ações
	 * #########################################
	 */
	public List<Acao> buscarPessoaAcao(Pessoa pessoa, Long tenantId) {		
	
			/*
			 SELECT * FROM svsa.Acao a
				INNER JOIN svsa.pessoaacao pa ON (a.codigo = pa.codigo_acao)
				INNER JOIN svsa.pessoa p ON (p.codigo = pa.codigo_pessoa_acao)				
				INNER JOIN svsa.unidade u ON (u.codigo = a.codigo_unidade)
			WHERE pa.codigo_pessoa_acao = 23021;
			 */	
			List<Acao> lista = manager.createQuery("select a from Acao a "
							+ "INNER JOIN a.pessoas pa "
							+ "INNER JOIN Pessoa p ON (p.codigo = pa.codigo) "
							+ "INNER JOIN Unidade u ON (u.codigo = a.unidade) "				
							+ "where pa.codigo = :codigo_pessoa "
							+ "and a.tenant_id = :tenantId", Acao.class)
				.setParameter("codigo_pessoa", pessoa.getCodigo())
				.setParameter("tenantId", tenantId)
				.getResultList();
			
			return lista;
		}
	

	/* Relatorio de Acoes */
	
	public List<Acao> buscarAcoesPeriodo(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		//select a from Acao a where a.unidade = :unidade and a.data between :ini and :fim 
		/* tem que trazer pela pessoa por conta da migração, pois a unidade é da unidade em que a ação foi feito. */
		/*List<Acao> lista = manager.createQuery("select la from Acao la "
				+ " INNER JOIN Pessoa pes ON la.pessoa = pes "
				+ " INNER JOIN Familia fam ON pes.familia = fam "
				+ " INNER JOIN Prontuario pro ON fam.prontuario = pro "
				+ " INNER JOIN Unidade uni ON pro.unidade = uni "
				+ "where uni = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.data between :ini and :fim "
				+ "order by la.data", Acao.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)			
			.getResultList();*/
			
		List<Acao> lista = manager.createQuery("select la from Acao la "
				+ "where la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.data between :ini and :fim "
				+ "and la.statusAtendimento not in ('AGENDADO')"
				+ "order by la.data", Acao.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)			
			.getResultList();
		
			return lista;
		
	}
	
	public List<Acao> buscarAcoesUnidade(Unidade unidade, Long tenantId) {		
		
		// select a from Acao a where a.unidade = :unidade"
		/* tem que trazer pela pessoa por conta da migração, pois a unidade é da unidade em que a ação foi feito. */
		/*List<Acao> lista = manager.createQuery("select la from Acao la "
				+ " INNER JOIN Pessoa pes ON la.pessoa = pes "
				+ " INNER JOIN Familia fam ON pes.familia = fam "
				+ " INNER JOIN Prontuario pro ON fam.prontuario = pro "
				+ " INNER JOIN Unidade uni ON pro.unidade = uni "
				+ "where uni = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "order by la.data", Acao.class)			
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.getResultList();*/
			
		
		List<Acao> lista = manager.createQuery("select la from Acao la "
				+ "where la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.statusAtendimento not in ('AGENDADO')"
				+ "order by la.data", Acao.class)			
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.getResultList();
		
			return lista;
		
	}
	
	public List<Acao> buscarAcaoAndamento(Unidade unidade, Long tenantId) {			
		return manager.createNamedQuery("Acao.buscarAcoesPendentes", Acao.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.AGENDADO)
				.getResultList();	
	}
	
}