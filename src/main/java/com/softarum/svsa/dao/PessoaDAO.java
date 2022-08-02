package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.softarum.svsa.modelo.Pais;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.ProgramaSocial;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class PessoaDAO implements Serializable {

	private static final long serialVersionUID = 2L;
	private Logger log = Logger.getLogger(PessoaDAO.class);

	@Inject
	private EntityManager manager;

	@Transactional
	public void excluir(Pessoa pessoa) throws NegocioException {
		try {
			manager.remove(pessoa);
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

	// buscas

	public Pessoa buscarPeloCodigo(Long codigo) {
		return manager.find(Pessoa.class, codigo);
	}
	public PessoaReferencia buscarPFPeloCodigo(Long codigo) {
		return manager.find(PessoaReferencia.class, codigo);
	}

	public Pessoa buscarPessoa(Long codigo, Unidade unidade, Long tenantId) {

		String jpql = "from Pessoa p where p.codigo = :codigo " + "and p.familia.prontuario.unidade = :unidade "
				+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc";

		TypedQuery<Pessoa> query = manager.createQuery(jpql, Pessoa.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("codigo", codigo);
		query.setParameter("unidade", unidade);
		query.setParameter("exc", false);

		return query.getSingleResult();

	}
	
	

	/* SelecionaPessoa */

	
	public List<PessoaDTO> pesquisarPessoaDTO(String termo, Unidade unidade, Long tenantId) {
		log.info("TermoPesquisa por nome na DAO = " + termo);

		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " + "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status ) "
			+ "from Pessoa p "
			+ "where p.nome LIKE :termo " 
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc "
				+ "and p.familia.prontuario.unidade = :unidade ";

		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);
		query.setParameter("unidade", unidade);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("tenantId", tenantId);
		query.setParameter("exc", false);

		return query.getResultList();
	}

	public List<PessoaDTO> pesquisarPessoaPorEnderecoDTO(String termo, Unidade unidade, Long tenantId) {
		log.info("TermoPesquisa por endereco na DAO = " + termo);

		/*
		String jpql = "from Pessoa p 
		where p.familia.endereco.endereco LIKE :termo "
				+ "and p.familia.prontuario.unidade = :unidade " + "and p.tenant_id = :tenantId "
				+ "and p.excluida = :exc";

		TypedQuery<Pessoa> query = manager.createQuery(jpql, Pessoa.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("unidade", unidade);
		query.setParameter("exc", false);

		return query.getResultList();
	}
	*/
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " + "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status ) "
			+ "from Pessoa p "
			+ "where p.familia.endereco.endereco LIKE :termo "
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc "
				+ "and p.familia.prontuario.unidade = :unidade ";
		
		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);
		query.setParameter("unidade", unidade);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("tenantId", tenantId);
		query.setParameter("exc", false);

		return query.getResultList();
}
	public List<PessoaDTO> pesquisarPessoaPorNomeSocialDTO(String termo, Unidade unidade, Long tenantId) {
		log.info("TermoPesquisa por nome social na DAO = " + termo);

		/*
		String jpql = "from Pessoa p where p.nomeSocial LIKE :termo " + "and p.familia.prontuario.unidade = :unidade "
				+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc";

		TypedQuery<Pessoa> query = manager.createQuery(jpql, Pessoa.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("unidade", unidade);
		query.setParameter("exc", false);

		return query.getResultList();
		 */
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " + "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status ) "
			+ "from Pessoa p "
			+ "where p.nomeSocial LIKE :termo "
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc "
				+ "and p.familia.prontuario.unidade = :unidade ";
		
		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);
		query.setParameter("unidade", unidade);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("tenantId", tenantId);
		query.setParameter("exc", false);
		
		return query.getResultList();
	}

	public List<PessoaDTO> pesquisarPessoaPorProntuarioDTO(String termo, Unidade unidade, Long tenantId) {
		log.info("TermoPesquisa por prontuario na DAO = " + termo);

		/*
		String jpql = "from Pessoa p where p.familia.prontuario.codigo = :termo "
				+ "and p.familia.prontuario.unidade = :unidade " + "and p.tenant_id = :tenantId "
				+ "and p.excluida = :exc";

		TypedQuery<Pessoa> query = manager.createQuery(jpql, Pessoa.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", Long.valueOf(termo));
		query.setParameter("unidade", unidade);
		query.setParameter("exc", false);

		return query.getResultList();
		*/
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " + "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status ) "
			+ "from Pessoa p "
			+ "where p.familia.prontuario.codigo = :termo "
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc "
				+ "and p.familia.prontuario.unidade = :unidade ";
		
		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);
		query.setParameter("unidade", unidade);
		query.setParameter("termo", Long.valueOf(termo));
		query.setParameter("tenantId", tenantId);
		query.setParameter("exc", false);
		
		return query.getResultList();
	}

	/* SelecionaPessoaGeral */

	public List<PessoaDTO> pesquisarPessoaDTO(String termo, Long tenantId) {

		log.info("TermoPesquisa pessoa na DAO = " + termo);
		/*
		String jpql = "from Pessoa p where p.nome LIKE :termo " + "and p.tenant_id = :tenantId "
				+ "and p.excluida = :exc";
		*/
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " + "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status ) "
			+ "from Pessoa p "
			+ "where p.nome LIKE :termo " 
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";

		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("exc", false);

		return query.getResultList();

	}

	public List<PessoaDTO> pesquisarPessoaPorEnderecoDTO(String termo, Long tenantId) {
		log.info("TermoPesquisa pessoa por endereco na DAO = " + termo);

		/*
		String jpql = "from Pessoa p where p.familia.endereco.endereco LIKE :termo " + "and p.tenant_id = :tenantId "
				+ "and p.excluida = :exc";
		*/
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " + "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status ) "
			+ "from Pessoa p "
			+ "where p.familia.endereco.endereco LIKE :termo " 
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";
		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("exc", false);

		return query.getResultList();
	}

	public List<PessoaDTO> pesquisarPessoaPorNomeSocialDTO(String termo, Long tenantId) {
		log.info("TermoPesquisa pessoa por nome social na DAO = " + termo);
		/*
		String jpql = "from Pessoa p where p.nomeSocial LIKE :termo " + "and p.tenant_id = :tenantId "
				+ "and p.excluida = :exc";
		*/
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " + "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status ) "
			+ "from Pessoa p "
			+ "where p.nomeSocial LIKE :termo " 
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";
		
		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("exc", false);		
		
		return query.getResultList();
	}

	public List<PessoaDTO> pesquisarPessoaPorProntuarioDTO(String termo, Long tenantId) {
		log.info("TermoPesquisa pessoa por prontuario na DAO = " + termo);
		/*
		String jpql = "from Pessoa p where p.familia.prontuario.codigo = :termo " + "and p.tenant_id = :tenantId "
				+ "and p.excluida = :exc";
		*/
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " + "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status ) "
			+ "from Pessoa p "
			+ "where p.familia.prontuario.codigo = :termo " 
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";

		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", Long.valueOf(termo));
		query.setParameter("exc", false);

		return query.getResultList();
	}
	
	
	
	

	/*
	 * SelecionaPessoaReferencia
	 */
	
	
	public List<PessoaDTO> pesquisarPorNome(String termo, Unidade unidade, Long tenantId) {
		log.info("TermoPesquisa PR por nome na DAO = " + termo);		
		
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " 
				+ "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status, "
				+ "p.nomeMae) "
			+ "from PessoaReferencia p "
			+ "where p.nome LIKE :termo " 
				+ "and p.familia.prontuario.unidade = :unidade "
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";

		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("unidade", unidade);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("exc", false);
		
		return query.getResultList();		
	}	
	
	public List<PessoaDTO> pesquisarPorNome(String termo, Long tenantId) {
		log.info("TermoPesquisa por pessoaReferencia/geral na DAO = " + termo);

		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " 
				+ "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status, "
				+ "p.nomeMae) "
			+ "from PessoaReferencia p "
			+ "where p.nome LIKE :termo " 
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";
		
		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("exc", false);
		
		return query.getResultList();
	}	
	
	public List<PessoaDTO> pesquisarPorEndereco(String termo, Unidade unidade, Long tenantId) {
		log.info("TermoPesquisa por endereco/unidade na DAO = " + termo);
		
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " 
				+ "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status, "
				+ "p.nomeMae) "
			+ "from PessoaReferencia p "
			+ "where p.familia.endereco.endereco LIKE :termo " 
				+ "and p.familia.prontuario.unidade = :unidade "
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";

		
		TypedQuery<PessoaDTO> query = manager
				.createQuery(jpql, PessoaDTO.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("unidade", unidade);
		query.setParameter("exc", false);
		
		return query.getResultList();
	}	
	public List<PessoaDTO> pesquisarPorEndereco(String termo, Long tenantId) {
		log.info("TermoPesquisa por endereco/geral na DAO = " + termo);
	
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " 
				+ "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status, "
				+ "p.nomeMae) "
			+ "from PessoaReferencia p "
			+ "where p.familia.endereco.endereco LIKE :termo " 
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";
		
		TypedQuery<PessoaDTO> query = manager
				.createQuery(jpql, PessoaDTO.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("exc", false);
		
		return query.getResultList();
	}	
	public List<PessoaDTO> pesquisarPorNomeSocial(String termo, Unidade unidade, Long tenantId) {
		log.info("TermoPesquisa por nome social/unidade na DAO = " + termo);
		
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " 
				+ "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status, "
				+ "p.nomeMae) "
			+ "from PessoaReferencia p "
			+ "where p.nomeSocial LIKE :termo " 
				+ "and p.familia.prontuario.unidade = :unidade "
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";
		
		TypedQuery<PessoaDTO> query = manager
				.createQuery(jpql, PessoaDTO.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("unidade", unidade);
		query.setParameter("exc", false);
		
		return query.getResultList();
	}	
	public List<PessoaDTO> pesquisarPorNomeSocial(String termo, Long tenantId) {
		log.info("TermoPesquisa por nome social/geral na DAO = " + termo);
		
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " 
				+ "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status, "
				+ "p.nomeMae) "
			+ "from PessoaReferencia p "
			+ "where p.nomeSocial LIKE :termo " 
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";
		
		TypedQuery<PessoaDTO> query = manager
				.createQuery(jpql, PessoaDTO.class);	
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", "%" + termo + "%");
		query.setParameter("exc", false);
		
		return query.getResultList();
	}
	public List<PessoaDTO> pesquisarPorProntuario(String termo, Unidade unidade, Long tenantId) {
		log.info("TermoPesquisa por prontuario/unidade na DAO = " + termo + " unidade = " + unidade.getCodigo());
				
		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " 
				+ "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status, "
				+ "p.nomeMae) "
			+ "from PessoaReferencia p "
			+ "where p.familia.prontuario.codigo = :termo " 
				+ "and p.familia.prontuario.unidade = :unidade "
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";
		
		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", Long.valueOf(termo));
		query.setParameter("unidade", unidade);
		query.setParameter("exc", false);
		
		return query.getResultList();
	}
	public List<PessoaDTO> pesquisarPorProntuario(Long termo, Long tenantId) {
		log.info("TermoPesquisa por prontuario/geral na DAO = " + termo);	

		String jpql = "SELECT new com.softarum.svsa.modelo.to.PessoaDTO( "
				+ "p.codigo, "
				+ "p.familia.prontuario.codigo, "
				+ "p.familia.prontuario.prontuario, " 
				+ "p.nome, " 
				+ "p.nomeSocial, " 
				+ "p.dataNascimento, "
				+ "p.familia.prontuario.unidade.nome, " 
				+ "p.status, " 
				+ "p.familia.prontuario.status, "
				+ "p.nomeMae) "
			+ "from PessoaReferencia p "
			+ "where p.familia.prontuario.codigo = :termo " 
				+ "and p.tenant_id = :tenantId " 
				+ "and p.excluida = :exc ";
		
		TypedQuery<PessoaDTO> query = manager.createQuery(jpql, PessoaDTO.class);	
		query.setParameter("tenantId", tenantId);
		query.setParameter("termo", termo);
		query.setParameter("exc", false);
		
		return query.getResultList();
	}
	
	
	
	
	
	
	
	
	
	
	
	

	/* Filtros PesquisaPessoa paginação */

	@SuppressWarnings("unchecked")
	public List<Pessoa> buscarComPaginacao(int first, int pageSize, String termo, int codigo, Long tenantId) {

		if (codigo == 1) { // nome
			log.debug("filtro = " + codigo);
			return manager
					.createQuery("Select p From Pessoa p where p.nome LIKE :termo " + "and p.tenant_id = :tenantId "
							+ "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%").setParameter("tenantId", tenantId)
					.setParameter("exc", false).setFirstResult(first).setMaxResults(pageSize).getResultList();
		} else if (codigo == 2) { // filtroNomeSocial
			log.debug("filtro = " + codigo);
			return manager
					.createQuery("Select p From Pessoa p where p.nomeSocial LIKE :termo "
							+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%").setParameter("tenantId", tenantId)
					.setParameter("exc", false).setFirstResult(first).setMaxResults(pageSize).getResultList();
		} else if (codigo == 3) { // filtroMae
			log.debug("filtro = " + codigo);
			return manager
					.createQuery("Select p From Pessoa p where p.nomeMae LIKE :termo " + "and p.tenant_id = :tenantId "
							+ "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%").setParameter("tenantId", tenantId)
					.setParameter("exc", false).setFirstResult(first).setMaxResults(pageSize).getResultList();
		} else if (codigo == 4) { // filtroEndereco
			log.debug("filtro = " + codigo);
			return manager
					.createQuery("Select p From Pessoa p where p.familia.endereco.endereco LIKE :termo "
							+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%").setParameter("tenantId", tenantId)
					.setParameter("exc", false).setFirstResult(first).setMaxResults(pageSize).getResultList();
		} else if (codigo == 5) { // filtroCodigoProntuario
			log.debug("filtro = " + codigo);
			return manager
					.createQuery("Select p From Pessoa p where p.familia.prontuario.codigo = :termo "
							+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc")
					.setParameter("termo", Long.valueOf(termo)).setParameter("tenantId", tenantId)
					.setParameter("exc", false).setFirstResult(first).setMaxResults(pageSize).getResultList();
		} else if (codigo == 6) { // filtroFisico
			log.debug("filtro = " + codigo);
			return manager
					.createQuery("Select p From Pessoa p where p.familia.prontuario.prontuario LIKE :termo "
							+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%").setParameter("tenantId", tenantId)
					.setParameter("exc", false).setFirstResult(first).setMaxResults(pageSize).getResultList();
		} else if (codigo == 7) { // filtroCodigoPessoa
			log.debug("filtro = " + codigo);
			return manager
					.createQuery("Select p From Pessoa p where p.codigo = :termo " + "and p.tenant_id = :tenantId "
							+ "and p.excluida = :exc")
					.setParameter("termo", Long.valueOf(termo)).setParameter("tenantId", tenantId)
					.setParameter("exc", false).setFirstResult(first).setMaxResults(pageSize).getResultList();
		} else {
			return manager.createNamedQuery("Pessoa.buscarTodos").setParameter("exc", false)
					.setParameter("tenantId", tenantId).setFirstResult(first).setMaxResults(pageSize).getResultList();
		}	
	}
	
	/* Filtros RelatorioPaisPessoa paginação*/
	
	@SuppressWarnings("unchecked")
	public List<Pessoa> buscarComPaginacao(int first, int pageSize, String termo, int codigo, Unidade unidade, Pais pais, Long tenantId) {	
		
		if(codigo == 1) {  // nome
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.nome LIKE :termo "
					+ "and p.familia.prontuario.unidade = :unidade "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")			
				.setParameter("termo", "%" + termo.toUpperCase() + "%")
				.setParameter("unidade", unidade)
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else if(codigo == 3) {  // filtroMae
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.nomeMae LIKE :termo "
					+ "and p.familia.prontuario.unidade = :unidade "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")			
				.setParameter("termo", "%" + termo.toUpperCase() + "%")
				.setParameter("unidade", unidade)
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else if(codigo == 5) {  // filtroCodigoProntuario
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.familia.prontuario.codigo = :termo "
					+ "and p.familia.prontuario.unidade = :unidade "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")			
				.setParameter("termo", Long.valueOf(termo))
				.setParameter("unidade", unidade)
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else if(codigo == 6) {  // filtroFisico
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.familia.prontuario.prontuario LIKE :termo "
					+ "and p.familia.prontuario.unidade = :unidade "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")			
				.setParameter("termo", "%" + termo.toUpperCase() + "%")
				.setParameter("unidade", unidade)
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else if(codigo == 7) {  // filtroCodigoPessoa
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.codigo = :termo "
					+ "and p.familia.prontuario.unidade = :unidade "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")			
				.setParameter("termo", Long.valueOf(termo))
				.setParameter("unidade", unidade)
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else {
			return manager.createQuery("Select p from Pessoa p where p.excluida = :exc "
				+ "and p.familia.prontuario.unidade = :unidade "
				+ "and p.paisOrigem = :pais "
				+ "and p.tenant_id = :tenantId")
				.setParameter("exc", false)
				.setParameter("unidade", unidade)
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}		
	}
	
	@SuppressWarnings("unchecked")
	public List<Pessoa> buscarComPaginacao(int first, int pageSize, String termo, int codigo, Pais pais, Long tenantId) {	
		
		if(codigo == 1) {  // nome
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.nome LIKE :termo "
					+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")			
				.setParameter("termo", "%" + termo.toUpperCase() + "%")
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else if(codigo == 2) {  // filtroNomeSocial
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.nomeSocial LIKE :termo "
					+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")
				.setParameter("termo", "%" + termo.toUpperCase() + "%")
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else if(codigo == 3) {  // filtroMae
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.nomeMae LIKE :termo "
					+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")			
				.setParameter("termo", "%" + termo.toUpperCase() + "%")
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else if(codigo == 4) {  // filtroEndereco
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.familia.endereco.endereco LIKE :termo "
					+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")			
				.setParameter("termo", "%" + termo.toUpperCase() + "%")
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else if(codigo == 5) {  // filtroCodigoProntuario
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.familia.prontuario.codigo = :termo "
					+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")			
				.setParameter("termo", Long.valueOf(termo))
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else if(codigo == 6) {  // filtroFisico
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.familia.prontuario.prontuario LIKE :termo "
					+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")			
				.setParameter("termo", "%" + termo.toUpperCase() + "%")
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else if(codigo == 7) {  // filtroCodigoPessoa
			log.debug("filtro = " + codigo);
			return manager.createQuery("Select p From Pessoa p where p.codigo = :termo "
					+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId "
					+ "and p.excluida = :exc")			
				.setParameter("termo", Long.valueOf(termo))
				.setParameter("pais", pais)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}
		else {
			return manager.createQuery("Select p from Pessoa p where p.excluida = :exc "
					+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
					+ "and p.paisOrigem = :pais "
					+ "and p.tenant_id = :tenantId")
					.setParameter("exc", false)
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}		
	}
	
	//Quantidade de pessoas
	

	public Long encontrarQdePessoas(String termo, int codigo, Long tenantId) {

		if (codigo == 1) { // nome
			log.debug("filtro = " + codigo);
			return (Long) manager
					.createQuery("Select count(p) From Pessoa p where p.nome LIKE :termo "
							+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%").setParameter("tenantId", tenantId)
					.setParameter("exc", false).getSingleResult();
		} else if (codigo == 2) { // filtroNomeSocial
			log.debug("filtro = " + codigo);
			return (Long) manager
					.createQuery("Select count(p) From Pessoa p where p.nomeSocial LIKE :termo "
							+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%").setParameter("tenantId", tenantId)
					.setParameter("exc", false).getSingleResult();
		} else if (codigo == 3) { // filtroMae
			log.debug("filtro = " + codigo);
			return (Long) manager
					.createQuery("Select count(p) From Pessoa p where p.nomeMae LIKE :termo "
							+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%").setParameter("tenantId", tenantId)
					.setParameter("exc", false).getSingleResult();
		} else if (codigo == 4) { // filtroEndereco
			log.debug("filtro = " + codigo);
			return (Long) manager
					.createQuery("Select count(p) From Pessoa p where p.familia.endereco.endereco LIKE :termo "
							+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%").setParameter("tenantId", tenantId)
					.setParameter("exc", false).getSingleResult();
		} else if (codigo == 5) { // filtroCodigoProntuario
			log.debug("filtro = " + codigo);
			return (Long) manager
					.createQuery("Select count(p) From Pessoa p where p.familia.prontuario.codigo = :termo "
							+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc")
					.setParameter("termo", Long.valueOf(termo)).setParameter("tenantId", tenantId)
					.setParameter("exc", false).getSingleResult();
		} else if (codigo == 6) { // filtroFisico
			log.debug("filtro = " + codigo);
			return (Long) manager
					.createQuery("Select count(p) From Pessoa p where p.familia.prontuario.prontuario LIKE :termo "
							+ "and p.tenant_id = :tenantId " + "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%").setParameter("tenantId", tenantId)
					.setParameter("exc", false).getSingleResult();
		} else {
			return (Long) manager
					.createQuery(
							"Select count(p) From Pessoa p where p.excluida = :exc " + "and p.tenant_id = :tenantId ")
					.setParameter("exc", false).setParameter("tenantId", tenantId).getSingleResult();
		}
	}

	
	//Quantidade de pessoas RelatorioPessoaPais
	
		public Long encontrarQdePessoas(String termo, int codigo, Unidade unidade, Pais pais, Long tenantId) {	
			
			if(codigo == 1) {  // nome
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.nome LIKE :termo "
						+ "and p.familia.prontuario.unidade = :unidade "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")			
					.setParameter("termo", "%" + termo.toUpperCase() + "%")
					.setParameter("unidade", unidade)
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else if(codigo == 2) {  // filtroNomeSocial
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.nomeSocial LIKE :termo "
						+ "and p.familia.prontuario.unidade = :unidade "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%")
					.setParameter("unidade", unidade)
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else if(codigo == 3) {  // filtroMae
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.nomeMae LIKE :termo "
						+ "and p.familia.prontuario.unidade = :unidade "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")			
					.setParameter("termo", "%" + termo.toUpperCase() + "%")
					.setParameter("unidade", unidade)
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else if(codigo == 4) {  // filtroEndereco
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.familia.endereco.endereco LIKE :termo "
						+ "and p.familia.prontuario.unidade = :unidade "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")			
					.setParameter("termo", "%" + termo.toUpperCase() + "%")
					.setParameter("unidade", unidade)
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else if(codigo == 5) {  // filtroCodigoProntuario
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.familia.prontuario.codigo = :termo "
						+ "and p.familia.prontuario.unidade = :unidade "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")			
					.setParameter("termo", Long.valueOf(termo))
					.setParameter("unidade", unidade)
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else if(codigo == 6) {  // filtroFisico
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.familia.prontuario.prontuario LIKE :termo "
						+ "and p.familia.prontuario.unidade = :unidade "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")			
					.setParameter("termo", "%" + termo.toUpperCase() + "%")
					.setParameter("unidade", unidade)
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else {
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.excluida = :exc "
						+ "and p.familia.prontuario.unidade = :unidade "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId ")					
					.setParameter("exc", false)
					.setParameter("unidade", unidade)
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.getSingleResult();
			}		
		}
	
		public Long encontrarQdePessoas(String termo, int codigo, Pais pais, Long tenantId) {	
			
			if(codigo == 1) {  // nome
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.nome LIKE :termo "
						+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")			
					.setParameter("termo", "%" + termo.toUpperCase() + "%")
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else if(codigo == 2) {  // filtroNomeSocial
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.nomeSocial LIKE :termo "
						+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")
					.setParameter("termo", "%" + termo.toUpperCase() + "%")
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else if(codigo == 3) {  // filtroMae
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.nomeMae LIKE :termo "
						+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")			
					.setParameter("termo", "%" + termo.toUpperCase() + "%")
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else if(codigo == 4) {  // filtroEndereco
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.familia.endereco.endereco LIKE :termo "
						+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")			
					.setParameter("termo", "%" + termo.toUpperCase() + "%")
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else if(codigo == 5) {  // filtroCodigoProntuario
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.familia.prontuario.codigo = :termo "
						+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")			
					.setParameter("termo", Long.valueOf(termo))
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else if(codigo == 6) {  // filtroFisico
				log.debug("filtro = " + codigo);
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.familia.prontuario.prontuario LIKE :termo "
						+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc")			
					.setParameter("termo", "%" + termo.toUpperCase() + "%")
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.setParameter("exc", false)
					.getSingleResult();
			}
			else {
				return (Long) manager.createQuery("Select count(p) From Pessoa p where p.excluida = :exc "
						+ "and p.familia.prontuario.unidade.tipo not in ('SASC') "
						+ "and p.paisOrigem = :pais "
						+ "and p.tenant_id = :tenantId ")					
					.setParameter("exc", false)
					.setParameter("pais", pais)
					.setParameter("tenantId", tenantId)
					.getSingleResult();
			}		
		}
	
	

	/* Filtros PesquisaPessoa paginação */

	/*
	 * RelatorioProgSocial
	 */
	public List<Pessoa> pesquisarPessoaPorProgSocial(ProgramaSocial programa, Unidade unidade, Long tenantId) {
		return manager
				.createQuery("Select p From Pessoa p " + "where p.formaIngresso.programaSocial = :programa "
						+ "and p.familia.prontuario.unidade = :unidade " + "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc", Pessoa.class)
				.setParameter("unidade", unidade).setParameter("tenantId", tenantId).setParameter("programa", programa)
				.setParameter("exc", false).getResultList();
	}

	public List<Pessoa> pesquisarPessoaPorProgSocial(ProgramaSocial programa, Long tenantId) {
		return manager
				.createQuery(
						"Select p From Pessoa p " + "where p.formaIngresso.programaSocial = :programa "
								+ "and p.tenant_id = :tenantId "
								+ "and p.familia.prontuario.unidade.tipo not in ('SASC') " + "and p.excluida = :exc",
						Pessoa.class)
				.setParameter("programa", programa).setParameter("tenantId", tenantId).setParameter("exc", false)
				.getResultList();
	}

	/*
	 * RelatorioPessoasPais
	 */
	public List<Pessoa> pesquisarPessoasPais(Pais pais, Unidade unidade, Long tenantId) {
		return manager.createQuery("Select p From Pessoa p "
				+ "where p.paisOrigem = :pais "
				+ "and p.familia.prontuario.unidade = :unidade "
				+ "and p.tenant_id = :tenantId "
				+ "and p.excluida = :exc", Pessoa.class)		
				.setParameter("pais", pais)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("exc", false)
				.getResultList();
	}

	public List<Pessoa> pesquisarPessoasPais(Pais pais, Long tenantId) {
		return manager
				.createQuery("Select p From Pessoa p " + "where p.paisOrigem = :pais "
						+ "and p.familia.prontuario.unidade.tipo not in ('SASC') " + "and p.tenant_id = :tenantId "
						+ "and p.excluida = :exc", Pessoa.class)
				.setParameter("pais", pais).setParameter("tenantId", tenantId).setParameter("exc", false)
				.getResultList();
	}

	/*
	 * Buscar todos os Países
	 * 
	 */
	public Pais buscarPais(Long codigo) {
		return manager.find(Pais.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Pais> buscarTodosPaises() {
		return manager.createNamedQuery("Pais.buscarTodosPaises").getResultList();
	}

	// para fins de testes unitários
	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

}
