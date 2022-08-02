package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.MetaPia;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Pia;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.AcompMseDTO;
import com.softarum.svsa.modelo.to.DatasIniFimTO;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class PiaDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	
	
	/* 
	 * Plano PIA
	 * 
	 */
	
	@Transactional
	public void salvar(Pia plano) throws PersistenceException, NegocioException {	
		try {
			manager.merge(plano);
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
	public void excluir(Pia plano) throws NegocioException {		
	
		try {
			//manager.remove(plano);
			manager.remove(manager.contains(plano) ? plano : manager.merge(plano));
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
	 * Buscas PIA
	 */ 
	
	
	public Pia buscarPeloCodigo(Long codigo) {
		return manager.find(Pia.class, codigo);
	}
	
	/* usado pelo MPComposição familiar */
	public Long verificarPlanoAtivo(Long adolescente, Long tenantId) {	
		// SELECT * FROM svsa_salto.planoacompanhamento where dataDesligamento is null and codigo_prontuario = 23013;
		return manager.createQuery("SELECT count(p) FROM Pia p "
					+ "WHERE p.dataDesligamento is null "
					+ "and p.tenant_id = :tenantId "
					+ "and p.adolescente.codigo = :adolescente", Long.class)
				.setParameter("adolescente", adolescente)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	
	}
	
	//SELECT * FROM svsa_salto.pia where dataDesligamento is null and codigo_pessoa = 41970;
	public List<Pia> buscarPlanoAtivo(Long adolescente, Long tenantId) {	
		// SELECT * FROM svsa_salto.planoacompanhamento where dataDesligamento is null and codigo_prontuario = 23013;
		return manager.createQuery("SELECT p FROM Pia p "
					+ "WHERE p.dataDesligamento is null "
					+ "and p.tenant_id = :tenantId "
					+ "and p.adolescente.codigo = :adolescente", Pia.class)
				.setParameter("adolescente", adolescente)
				.setParameter("tenantId", tenantId)
				.getResultList();
	
	}
	public List<Pia> buscarPlanosAno(Integer ano, Long pessoa, Long tenantId) {		
		return manager.createNamedQuery("Pia.buscarPlanosAno", Pia.class)
				.setParameter("pessoa", pessoa)
				.setParameter("tenantId", tenantId)
				.setParameter("ano", ano)
				.getResultList();	
	}
	
	public List<Pia> buscarPlanos(Pessoa pessoa, Long tenantId) {
		return manager.createNamedQuery("Pia.buscarPlanos", Pia.class)
				.setParameter("pessoa", pessoa)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	
	
	
	
	
	
	/*
	 * MetaPia
	 * 
	 */
	
	@Transactional
	public MetaPia salvarMeta(MetaPia meta) throws PersistenceException {		
		return manager.merge(meta);		
	}	
		
	@Transactional
	public void excluirMeta(MetaPia meta) throws NegocioException {		
	
		meta = buscarMetaCodigo(meta.getCodigo());
		try {
			manager.remove(meta);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Meta não pode ser excluída.");
		}
	}
	
	public MetaPia buscarMetaCodigo(Long codigo) {
		return manager.find(MetaPia.class, codigo);
	}	

	public List<MetaPia> buscarMetas(Pia plano, Long tenantId) {		
		return manager.createNamedQuery("MetaPia.buscarMetas", MetaPia.class)
				.setParameter("plano", plano)
				.setParameter("tenantId", tenantId)
				.getResultList();	
	}
	
	
	
	
		

		
	
	/* ########################################
	 * Consultas usando DTO Projection  JPQL
	 * #########################################
	 */
	
	
	public List<AcompMseDTO> buscarAdolAcompanhamentoDTO(Unidade unidade, Long tenantId) {			
		/*
		SELECT plano.codigo,
			plano.nrProcesso, 
			plano.dataEncaminhamento, 
			plano.dataHomologacao,
			plano.tipoMse, 
			usuario.nome AS responsavel, 				
			pessoa.nome AS adolescente
		FROM svsa_salto.pia plano
			INNER JOIN svsa_salto.pessoa pessoa ON plano.codigo_pessoa = pessoa.codigo
			INNER JOIN svsa_salto.familia familia ON pessoa.codigo_familia = familia.codigo
			INNER JOIN svsa_salto.prontuario prontuario ON prontuario.codigo = familia.codigo_prontuario
			INNER JOIN svsa_salto.usuario usuario ON usuario.codigo = plano.codigo_responsavel
		WHERE prontuario.codigo_unidade = 1 and plano.dataDesligamento is null;

		 */	
		List<AcompMseDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AcompMseDTO( "
				+ "plano.codigo, "
				+ "plano.nrProcesso, "
				+ "plano.dataEncaminhamento, "
				+ "plano.dataHomologacao, "
				+ "plano.tipoMse, "
				+ "usuario.nome, "
				+ "pessoa.nome)"
			+ "FROM Pia plano "
				+ "INNER JOIN Pessoa pessoa 		ON plano.adolescente.codigo = pessoa.codigo "
				+ "INNER JOIN Familia familia 		ON pessoa.familia.codigo = familia.codigo "
				+ "INNER JOIN Prontuario prontuario ON prontuario.codigo = familia.prontuario.codigo "
				+ "INNER JOIN Usuario usuario 		ON usuario.codigo = plano.responsavel.codigo "			
			+ "WHERE prontuario.unidade.codigo = :codigo_unidade "
			+ "and plano.tenant_id = :tenantId "
			+ "and pessoa.excluida = :exc ", AcompMseDTO.class)
		.setParameter("codigo_unidade", unidade.getCodigo())
		.setParameter("tenantId", tenantId)
		.setParameter("exc", false)
		.getResultList();
		return lista;
	}
	public List<AcompMseDTO> buscarAdolAcompanhamentoDTO(Unidade unidade, DatasIniFimTO datasTO, Long tenantId) {			
		
		List<AcompMseDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AcompMseDTO( "
				+ "plano.codigo, "
				+ "plano.nrProcesso, "
				+ "plano.dataEncaminhamento, "
				+ "plano.dataHomologacao, "
				+ "plano.tipoMse, "
				+ "usuario.nome, "
				+ "pessoa.nome)"
			+ "FROM Pia plano "
				+ "INNER JOIN Pessoa pessoa 		ON plano.adolescente.codigo = pessoa.codigo "
				+ "INNER JOIN Familia familia 		ON pessoa.familia.codigo = familia.codigo "
				+ "INNER JOIN Prontuario prontuario ON prontuario.codigo = familia.prontuario.codigo "
				+ "INNER JOIN Usuario usuario 		ON usuario.codigo = plano.responsavel.codigo "			
			+ "WHERE prontuario.unidade = :unidade "
			+ "and plano.tenant_id = :tenantId "
			+ "and plano.dataEncaminhamento between :ini and :fim "
			+ "and pessoa.excluida = :exc ", AcompMseDTO.class)				
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", datasTO.getIni(), TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(datasTO.getFim()), TemporalType.TIMESTAMP)
			.setParameter("exc", false)
			.getResultList();
			return lista;
	}
	
	
	

	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}

	
}