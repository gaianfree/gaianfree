package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;


import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.util.DateUtils;

/**
 * @author murakamiadmin
 *
 */
public class ProdutividadeDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	
	
	/* Produtividade atendimentos cadunico */

	public List<AtendimentoDTO> buscarCadUnicoProdDTO(Date ini, Date fim, Long tenantId) {	
		/*
		 	SELECT 
			    u.nome, unid.nome
			FROM svsa_salto.ListaAtendimento l
		        INNER JOIN svsa_salto.Usuario u ON (u.codigo = l.codigo_tecnico)
		        INNER JOIN svsa_salto.Unidade unid ON (unid.codigo = u.codigo_unidade)
			WHERE
			    l.codigoAuxiliar IN ('CADASTRAMENTO_CADUNICO' , 'CADASTRAMENTO_CADUNICO_BPC', 'ATUALIZACAO_CADUNICO', 'OUTROS_CADUNICO')
			ORDER BY u.nome;
		 */
		List<AtendimentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoDTO( "
				+ "u.nome, unid.nome, u.role) " + "FROM ListaAtendimento l "
				+ " INNER JOIN Usuario u ON u.codigo = l.tecnico.codigo "				
				+ " INNER JOIN Unidade unid ON unid.codigo = u.unidade.codigo "
				+ "WHERE "
				+ " l.codigoAuxiliar in ('CADASTRAMENTO_CADUNICO' , 'CADASTRAMENTO_CADUNICO_BPC', 'ATUALIZACAO_CADUNICO', 'OUTROS_CADUNICO') "
				+ " and l.dataAtendimento between :ini and :fim "
				+ " and l.statusAtendimento = :status "
				+ " and l.tenant_id = :tenantId "
				+ " and unid.tipo not in ('SASC') "
				+ " ORDER BY u.nome"
				, AtendimentoDTO.class)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();

		return lista;
	}
	
	public List<AtendimentoDTO> buscarCadUnicoProdDTO(Long tenantId) {	
		/*
		 	SELECT 
			    u.nome, unid.nome
			FROM svsa_salto.ListaAtendimento l
		        INNER JOIN svsa_salto.Usuario u ON (u.codigo = l.codigo_tecnico)
		        INNER JOIN svsa_salto.Unidade unid ON (unid.codigo = u.codigo_unidade)
			WHERE
			    l.codigoAuxiliar IN ('CADASTRAMENTO_CADUNICO' , 'CADASTRAMENTO_CADUNICO_BPC', 'ATUALIZACAO_CADUNICO', 'OUTROS_CADUNICO')
			ORDER BY u.nome;
		 */
		List<AtendimentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoDTO( "
				+ "u.nome, unid.nome, u.role) " + "FROM ListaAtendimento l "
				+ " INNER JOIN Usuario u ON u.codigo = l.tecnico.codigo "				
				+ " INNER JOIN Unidade unid ON unid.codigo = u.unidade.codigo "
				+ "WHERE "
				+ " l.codigoAuxiliar in ('CADASTRAMENTO_CADUNICO' , 'CADASTRAMENTO_CADUNICO_BPC', 'ATUALIZACAO_CADUNICO', 'OUTROS_CADUNICO') "
				+ " and l.statusAtendimento = :status "
				+ " and l.tenant_id = :tenantId "
				+ " and unid.tipo not in ('SASC') "
				+ "ORDER BY u.nome"
				, AtendimentoDTO.class)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();

		return lista;
	}
	
	
	
	/* Produtividade atendimentos por tecnico */
	
	
	
	public List<AtendimentoDTO> buscarTecnicoProdDTO(Unidade unidade, Long tenantId) {
		
		List<AtendimentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoDTO( "
				+ "u.nome, unid.nome, u.role) " + "FROM ListaAtendimento l "
				+ " INNER JOIN Usuario u ON u.codigo = l.tecnico.codigo "				
				+ " INNER JOIN Unidade unid ON unid.codigo = u.unidade.codigo "
				+ "WHERE l.statusAtendimento = :status "
				+ " and l.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO') "
				+ " and unid = :unidade "
				+ " and l.tenant_id = :tenantId "
				+ " and unid.tipo not in ('SASC') "
				+ "ORDER BY u.nome"
				, AtendimentoDTO.class)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.setParameter("unidade", unidade)
				.getResultList();
	
		return lista;
	}

	public List<AtendimentoDTO> buscarTecnicoProdDTO(Date ini, Date fim, Unidade unidade, Long tenantId) {
		
		List<AtendimentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoDTO( "
				+ "u.nome, unid.nome, u.role) " + "FROM ListaAtendimento l "
				+ " INNER JOIN Usuario u ON u.codigo = l.tecnico.codigo "				
				+ " INNER JOIN Unidade unid ON unid.codigo = u.unidade.codigo "
				+ "WHERE l.dataAtendimento between :ini and :fim "
				+ " and l.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO') "
				+ " and l.statusAtendimento = :status "
				+ " and unid = :unidade "
				+ " and l.tenant_id = :tenantId "
				+ " and unid.tipo not in ('SASC') "
				+ " ORDER BY u.nome"
				, AtendimentoDTO.class)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)				
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.setParameter("unidade", unidade)
				.getResultList();

		return lista;
	}
	
	public Long buscarTotalGeral(Date ini, Date fim, Long tenantId) {	
		return manager.createQuery("select count(l) from ListaAtendimento l "
				+ "INNER JOIN Usuario u ON u.codigo = l.tecnico.codigo "				
				+ "INNER JOIN Unidade unid ON unid.codigo = u.unidade.codigo "
				+ "where l.statusAtendimento = :status "
				+ "and l.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO') "
				+ "and l.dataAtendimento between :ini and :fim "
				+ "and l.tenant_id = :tenantId "
				+ "and unid.tipo not in ('SASC') ", Long.class)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();
	}
	
	public Long buscarTotalGeral(Long tenantId) {	
		return manager.createQuery("select count(l) from ListaAtendimento l "
				+ "INNER JOIN Usuario u ON u.codigo = l.tecnico.codigo "				
				+ "INNER JOIN Unidade unid ON unid.codigo = u.unidade.codigo "
				+ "where l.statusAtendimento = :status "
				+ "and l.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO') "
				+ "and l.tenant_id = :tenantId "
				+ "and unid.tipo not in ('SASC')", Long.class)
				.setParameter("tenantId", tenantId)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();
	}
	
	
public List<AtendimentoDTO> buscarTecnicoProdAcoesDTO(Unidade unidade, Long tenantId) {
		
		List<AtendimentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoDTO( "
				+ "u.nome, unid.nome, u.role) " + "FROM Acao a "
				+ " INNER JOIN Usuario u ON u.codigo = a.tecnico.codigo "				
				+ " INNER JOIN Unidade unid ON unid.codigo = u.unidade.codigo "
				+ " and unid = :unidade "
				+ " and a.tenant_id = :tenantId "
				+ " and unid.tipo not in ('SASC') "
				+ "ORDER BY u.nome"
				, AtendimentoDTO.class)
				.setParameter("tenantId", tenantId)
				.setParameter("unidade", unidade)
				.getResultList();
	
		return lista;
	}

	public List<AtendimentoDTO> buscarTecnicoProdAcoesDTO(Date ini, Date fim, Unidade unidade, Long tenantId) {
		
		List<AtendimentoDTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoDTO( "
				+ "u.nome, unid.nome, u.role) " + "FROM Acao a "
				+ " INNER JOIN Usuario u ON u.codigo = a.tecnico.codigo "				
				+ " INNER JOIN Unidade unid ON unid.codigo = u.unidade.codigo "
				+ " and unid = :unidade "
				+ " and a.tenant_id = :tenantId "
				+ " and unid.tipo not in ('SASC') "
				+ " ORDER BY u.nome"
				, AtendimentoDTO.class)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("unidade", unidade)
				.getResultList();

		return lista;
	}
	
	public Long buscarTotalGeralAcao(Date ini, Date fim, Long tenantId) {	
		return manager.createQuery("select count(a) from Acao a "
				+ "INNER JOIN Usuario u ON u.codigo = a.tecnico.codigo "				
				+ "INNER JOIN Unidade unid ON unid.codigo = u.unidade.codigo "
				+ "and a.dataAcao between :ini and :fim "
				+ "and a.tenant_id = :tenantId "
				+ "and unid.tipo not in ('SASC') ", Long.class)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getSingleResult();
	}
	
	public Long buscarTotalGeralAcao(Long tenantId) {	
		return manager.createQuery("select count(a) from Acao a "
				+ "INNER JOIN Usuario u ON u.codigo = a.tecnico.codigo "				
				+ "INNER JOIN Unidade unid ON unid.codigo = u.unidade.codigo "
				+ "and unid.tipo not in ('SASC') "
				+ "and a.tenant_id = :tenantId ", Long.class)
				.setParameter("tenantId", tenantId)
				.getSingleResult();
	}

}