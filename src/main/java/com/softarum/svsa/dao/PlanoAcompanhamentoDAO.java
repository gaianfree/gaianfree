package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.MetaPlanoFamiliar;
import com.softarum.svsa.modelo.PlanoAcompanhamento;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.AcompanhamentoDTO;
import com.softarum.svsa.modelo.to.DatasIniFimTO;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class PlanoAcompanhamentoDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	
	@Transactional
	public void salvar(PlanoAcompanhamento plano) throws NegocioException {	
		
		try {
			manager.merge(plano);	
			
			// altera o status para PAIF ou PAEFI em acompanhamento
			manager.merge(plano.getProntuario());
		
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
	public void excluir(PlanoAcompanhamento plano) throws NegocioException {
		try {
			plano = manager.merge(plano);
			manager.remove(plano);
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
	
	public PlanoAcompanhamento buscarPeloCodigo(Long codigo) {
		return manager.find(PlanoAcompanhamento.class, codigo);
	}
	
	public Long verificarPlanoAtivo(Long prontuario, Long tenantId) {	
		// SELECT * FROM svsa_salto.planoacompanhamento where dataDesligamento is null and codigo_prontuario = 23013;
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento p "
					+ "WHERE p.dataDesligamento is null "
					+ "and p.tenant_id = :tenantId "
					+ "and prontuario.codigo = :prontuario", Long.class)
				.setParameter("prontuario", prontuario)	
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	
	}

	public List<PlanoAcompanhamento> buscarPlanosAno(Integer ano, Long prontuario, Long tenantId) {		
		return manager.createNamedQuery("PlanoAcompanhamento.buscarPlanosAno", PlanoAcompanhamento.class)
				.setParameter("prontuario", prontuario)
				.setParameter("tenantId", tenantId)
				.setParameter("ano", ano)
				.getResultList();	
	}
	
	public List<PlanoAcompanhamento> buscarPlanos(Prontuario prontuario, Long tenantId) {
		return manager.createNamedQuery("PlanoAcompanhamento.buscarPlanos", PlanoAcompanhamento.class)
				.setParameter("prontuario", prontuario)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}

		
	
	/* ########################################
	 * Consulta usando DTO Projection  JPQL
	 * #########################################
	 */
	/* Avisos */
	public List<AcompanhamentoDTO> buscarFamiliasAcompanhamentoDTO(Unidade unidade, Long tenantId) {		
		
		List<AcompanhamentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AcompanhamentoDTO( "
				+ "plano.codigo, "
				+ "plano.dataIngresso, "
				+ "plano.prazoMeses, "
				+ "usuario.nome, "
				+ "prontuario.codigo, "
				+ "prontuario.prontuario, "
				+ "pessoa.nome, "
				+ "pessoa.codigo, "
				+ "endereco.bairro)"
			+ "FROM PlanoAcompanhamento plano "
				+ "INNER JOIN Prontuario prontuario ON plano.prontuario.codigo = prontuario.codigo "
				+ "INNER JOIN Usuario usuario ON plano.tecnico.codigo = usuario.codigo "
				+ "INNER JOIN Familia familia ON plano.prontuario.codigo = familia.prontuario.codigo "
				+ "INNER JOIN Endereco endereco ON familia.endereco.codigo = endereco.codigo "
				+ "INNER JOIN Pessoa pessoa ON familia.pessoaReferencia.codigo = pessoa.codigo "			
			+ "WHERE prontuario.unidade.codigo = :codigo_unidade "
				+ "and plano.dataDesligamento is null "
				+ "and plano.tenant_id = :tenantId "
				+ "and prontuario.excluido = :exc", AcompanhamentoDTO.class)
			.setParameter("codigo_unidade", unidade.getCodigo())
			.setParameter("tenantId", tenantId)
			.setParameter("exc", false)
			.getResultList();
		
		return lista;
	}
	
	/* Relatorio Planos Acompanhamento */
	public List<AcompanhamentoDTO> buscarFamiliasAcompanhamentoDTO(Unidade unidade, DatasIniFimTO datasTO, Long tenantId) {			
		/*
		 SELECT plano.codigo,
				plano.dataIngresso, 
				plano.prazoMeses, 
				plano.servico, 
		        usuario.nome AS tecnicoResponsavel, 
				prontuario.codigo AS codigoProntuario, 
		        prontuario.prontuario AS prontuarioFisico,
		        pessoa.nome AS pessoaReferencia
			FROM svsa_salto.planoAcompanhamento plano
				INNER JOIN svsa_salto.prontuario prontuario ON plano.codigo_prontuario = prontuario.codigo
				INNER JOIN svsa_salto.usuario usuario ON plano.codigo_tecnico = usuario.codigo
			    INNER JOIN svsa_salto.familia familia ON plano.codigo_prontuario = familia.codigo_prontuario
				INNER JOIN svsa_salto.pessoa pessoa ON familia.codigo_pessoa_referencia = pessoa.codigo
			WHERE prontuario.codigo_unidade = 1;
		 */	
		List<AcompanhamentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AcompanhamentoDTO( "
				+ "plano.codigo, "
				+ "plano.dataIngresso, "
				+ "plano.prazoMeses, "
				+ "usuario.nome, "
				+ "prontuario.codigo, "
				+ "prontuario.prontuario, "
				+ "pessoa.nome, "
				+ "pessoa.codigo, "
				+ "endereco.bairro)"
			+ "FROM PlanoAcompanhamento plano "
				+ "INNER JOIN Prontuario prontuario ON plano.prontuario.codigo = prontuario.codigo "
				+ "INNER JOIN Usuario usuario ON plano.tecnico.codigo = usuario.codigo "
				+ "INNER JOIN Familia familia ON plano.prontuario.codigo = familia.prontuario.codigo "
				+ "INNER JOIN Endereco endereco ON familia.endereco.codigo = endereco.codigo "
				+ "INNER JOIN Pessoa pessoa ON familia.pessoaReferencia.codigo = pessoa.codigo "			
			+ "WHERE prontuario.unidade = :unidade "
				+ "and plano.tenant_id = :tenantId "
				+ "and plano.dataIngresso between :ini and :fim "
				+ "and plano.dataDesligamento is null "
				+ "and prontuario.excluido = :exc", AcompanhamentoDTO.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", datasTO.getIni(), TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(datasTO.getFim()), TemporalType.TIMESTAMP)
			.setParameter("exc", false)
			.getResultList();
		
		return lista;
	}
	
	public List<AcompanhamentoDTO> buscarAdolAcompanhamentoDTO(Unidade unidade, Long tenantId) {			
		/*			
		 	SELECT pessoa.codigo,
				pessoa.nome AS pessoa,
				plano.dataEncaminhamento,
		        usuario.nome AS tecnicoResponsavel		        
			FROM svsa_salto.Pia plano
				INNER JOIN svsa_salto.Pessoa pessoa ON plano.codigo_pessoa = pessoa.codigo
                INNER JOIN svsa_salto.Familia familia ON pessoa.codigo_familia = familia.codigo
                INNER JOIN svsa_salto.Prontuario prontuario ON familia.codigo_prontuario = prontuario.codigo
				INNER JOIN svsa_salto.usuario usuario ON plano.codigo_responsavel = usuario.codigo                
			WHERE prontuario.codigo_unidade = 7;
		 */	
		List<AcompanhamentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AcompanhamentoDTO( "
				+ "pessoa.codigo, "
				+ "pessoa.nome AS pessoa, "
				+ "plano.dataEncaminhamento, "
				+ "unidade.nome AS unidade)"
			+ "FROM Pia plano "
				+ "INNER JOIN Pessoa pessoa ON plano.adolescente.codigo = pessoa.codigo "
				+ "INNER JOIN Familia familia ON pessoa.familia.codigo = familia.codigo "
				+ "INNER JOIN Prontuario prontuario ON familia.prontuario.codigo = prontuario.codigo "
				+ "INNER JOIN Unidade unidade ON prontuario.unidade.codigo = unidade.codigo "						
			+ "WHERE plano.dataDesligamento is null "
				+ "and prontuario.unidade.codigo = :codigo_unidade "
				+ "and plano.tenant_id = :tenantId "
				+ "and pessoa.excluida = :exc", AcompanhamentoDTO.class)	
			.setParameter("codigo_unidade", unidade.getCodigo())
			.setParameter("tenantId", tenantId)
			.setParameter("exc", false)
			.getResultList();
		
		return lista;
	}
	
	
	
	/*
	 * DashBoard
	 */
	
	public Long buscarQdeAcompanhamento(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento p "
					+ "INNER JOIN Prontuario prontuario ON p.prontuario.codigo = prontuario.codigo "
					+ "WHERE p.dataIngresso between :ini and :fim "
					+ "and prontuario.unidade = :unidade "
					+ "and p.tenant_id = :tenantId "
					+ "and prontuario.excluido = :exc", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("exc", false)
				.getSingleResult();
	
	}
	
	
	
	
	
	/*
	 * MetaPlanoFamiliar
	 * 
	 */
	
	@Transactional
	public MetaPlanoFamiliar salvarMeta(MetaPlanoFamiliar meta) throws PersistenceException {		
		return manager.merge(meta);		
	}	
		
	@Transactional
	public void excluirMeta(MetaPlanoFamiliar meta) throws NegocioException {		
	
		meta = buscarMetaCodigo(meta.getCodigo());
		try {
			manager.remove(meta);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Meta não pode ser excluída.");
		}
	}
	
	public MetaPlanoFamiliar buscarMetaCodigo(Long codigo) {
		return manager.find(MetaPlanoFamiliar.class, codigo);
	}	

	public List<MetaPlanoFamiliar> buscarMetas(PlanoAcompanhamento plano, Long tenantId) {		
		return manager.createNamedQuery("MetaPlanoFamiliar.buscarMetas", MetaPlanoFamiliar.class)
				.setParameter("plano", plano)
				.setParameter("tenantId", tenantId)
				.getResultList();	
	}
	
	
	
	
	
	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}

	
}