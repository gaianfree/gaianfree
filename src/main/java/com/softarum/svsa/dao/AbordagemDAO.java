package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.Abordagem;
import com.softarum.svsa.modelo.PessoaAbordagem;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class AbordagemDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;	
	
	@Transactional
	public Abordagem salvar(Abordagem abordagem) throws NegocioException {
		try {
			// se pessoa nova
			if(abordagem.getPessoa().getCodigo() == null) {
				abordagem.getPessoa().setTenant_id(abordagem.getTenant_id());
				abordagem.getPessoa().setUnidade(abordagem.getUnidade());
				PessoaAbordagem p = manager.merge(abordagem.getPessoa());
				abordagem.setPessoa(p);
			}
			else {
				manager.merge(abordagem.getPessoa());
			}
			return manager.merge(abordagem);	
			
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
	public void excluir(Abordagem abordagem) throws NegocioException {
		abordagem = buscarPeloCodigo(abordagem.getCodigo());
		try {
			manager.remove(abordagem);
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
	
	public Abordagem buscarPeloCodigo(Long codigo) {
		return manager.find(Abordagem.class, codigo);
	}
	
	public PessoaAbordagem buscarPeloNome(String nome) {
		return manager.createNamedQuery("PessoaAbordagem.buscarPeloNome", PessoaAbordagem.class)
				.setParameter("nome", nome)
				.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> buscarNomes(String query, Unidade unidade, Long tenantId) {
		return manager.createNamedQuery("PessoaAbordagem.buscarNomes")
				.setParameter("tenantId", tenantId)
				.setParameter("unidade", unidade)
				.setParameter("nome", "%" + query + "%")
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Abordagem> buscarTodos(Long tenantId) {
		return manager.createNamedQuery("Abordagem.buscarTodos")
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Abordagem> buscarTodosDia(Long tenantId, Unidade unidade) {
		
		Date data = new Date();
		return manager.createNamedQuery("Abordagem.buscarTodosDia")
				.setParameter("tenantId", tenantId)
				.setParameter("unidade", unidade)
				.setParameter("ini", DateUtils.minusDay(data), TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(data), TemporalType.TIMESTAMP)
				.getResultList();
	}
	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}

	public List<Abordagem> buscarAbordagensPeriodo(Unidade unidade, Date ini, Date fim, Long tenantId) {
			
		List<Abordagem> lista = manager.createQuery("select a from Abordagem a "
				+ "where a.unidade = :unidade "
				+ "and a.tenant_id = :tenantId "
				+ "and a.data between :ini and :fim "
				+ "order by a.data", Abordagem.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)			
			.getResultList();
		
			return lista;
		
	}
	
	public List<Abordagem> buscarAbordagensUnidade(Unidade unidade, Long tenantId) {		
		
		
		List<Abordagem> lista = manager.createQuery("select a from Abordagem a "
				+ "where a.unidade = :unidade "
				+ "and a.tenant_id = :tenantId "
				+ "order by a.data", Abordagem.class)			
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.getResultList();
		
			return lista;
		
	}
	
}