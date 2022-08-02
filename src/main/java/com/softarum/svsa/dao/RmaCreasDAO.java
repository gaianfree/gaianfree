package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.PerfilFamilia;
import com.softarum.svsa.modelo.enums.Sexo;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.enums.TipoPcD;
import com.softarum.svsa.modelo.to.rma.PessoaTO;
import com.softarum.svsa.util.DateUtils;

/**
 * @author murakamiadmin
 *
 */
public class RmaCreasDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(RmaCreasDAO.class);

	@Inject
	private EntityManager manager;
	
	
	/*
	 * Bloco I - Serviço de Proteção e Atendimento Especializado a Famílias e Indivíduos - PAEFI
	 * 
	 */
	
	//A.1. Total de famílias em acompanhamento pelo PAIF
	public Long buscarA1(Unidade unidade, Date fim, Long tenantId) {		
		/*
		   SELECT count(*)
			FROM svsa_salto.PlanoAcompanhamento plano
				INNER JOIN svsa_salto.Prontuario prontuario ON plano.codigo_prontuario = prontuario.codigo				
			WHERE prontuario.codigo_unidade = 2 
				and plano.dataDesligamento is null 
                and prontuario.excluido = 0b0;
		*/
		Long qde = manager.createQuery("SELECT count(prontuario)"
				+ "FROM PlanoAcompanhamento plano "
					+ "INNER JOIN Prontuario prontuario ON plano.prontuario.codigo = prontuario.codigo "				
					+ "WHERE prontuario.unidade.codigo = :codigo_unidade "
					+ "and plano.dataIngresso <= :fim "
					+ "and plano.tenant_id = :tenantId "
					+ "and plano.dataDesligamento is null "
					+ "and prontuario.excluido = :exc", Long.class)
				.setParameter("codigo_unidade", unidade.getCodigo())
				.setParameter("tenantId", tenantId)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("exc", false)
				.getSingleResult();
			return qde;	
	}	
	//A.2. Novas famílias inseridas no acompanhamento do PAIF durante o mês de referência	
	public Long buscarA2(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
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
		
	/*  item B  */
	
	public Long buscarB1(Unidade unidade, Date ini, Date fim, Long tenantId) {
		/*
		 * Todas as consultas do Bloco B usam essa query.
		 * 
		 * SELECT * FROM PlanoAcompanhamento pa 
				INNER JOIN Prontuario p ON pa.codigo_prontuario = p.codigo 
				INNER JOIN Unidade u ON p.codigo_unidade = u.codigo
				INNER JOIN PerfisFamilia pf ON pf.codigo_plano = pa.codigo    
			WHERE u.codigo = 7
				and pf.perfil = 'BENEFICIARIA_BOLSA_FAMILIA'
				and pa.dataIngresso between '2021-04-01' and '2021-05-01'
				and p.excluido = 0b0;
		 */
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and pa.tenant_id = :tenantId "
				+ " and p.excluido = :exc", Long.class)		
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.BENEFICIARIA_BOLSA_FAMILIA)
			.setParameter("exc", false)
			.getSingleResult();		
	}
	public Long buscarB2(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and pa.tenant_id = :tenantId "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.BENEFICIARIA_BPC)
			.setParameter("exc", false)
			.getSingleResult();
	}
	public Long buscarB3(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "
				+ "  INNER JOIN Familia f ON f.codigo = p.familia.codigo "
				+ "  INNER JOIN Pessoa pessoa ON pessoa.familia.codigo = f.codigo "
				+ "  INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pessoa.codigo "
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and sv.situacao = 'TRABALHO_INFANTIL' "
				+ " and sv.dataEncerramento is null "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and pa.tenant_id = :tenantId "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.COM_CRIANCA_ADOL_EM_TRABALHO_INFANTIL)
			.setParameter("exc", false)
			.getSingleResult();
	}	
	public Long buscarB4(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and pa.tenant_id = :tenantId "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.COM_CRIANCA_ADOL_EM_SV_ACOLHIMENTO)
			.setParameter("exc", false)
			.getSingleResult();
	}	
	public Long buscarB5(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and pa.tenant_id = :tenantId "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.SITUACAO_VIOLENCIA_PSICOATIVAS)
			.setParameter("exc", false)
			.getSingleResult();
	}	
	public Long buscarB7(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and pa.tenant_id = :tenantId "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.ADOLECENTE_MEDIDAS_SOCIOEDUCATIVAS)
			.setParameter("exc", false)
			.getSingleResult();
	}	
	// pessoas em situação de violencia
	public List<PessoaTO> buscarB6(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		
		/*
		 * SELECT pa.dataNascimento  FROM Pessoa pa
				INNER JOIN Pia pi ON pi.codigo_pessoa = pa.codigo
		        INNER JOIN Familia f ON f.codigo = pa.codigo_familia
				INNER JOIN Prontuario p ON p.codigo = f.codigo_prontuario	
			WHERE p.codigo_unidade = 7
				and pi.dataEncaminhamento between "2021-05-01 00:00:00" and "2021-05-31 00:00:00";
		
		
		log.info("Bucando B6...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN Pia pia ON pia.adolescente.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and pia.dataEncaminhamento between :ini and :fim ", PessoaTO.class)			
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
		 */
		
		/* SELECT *  FROM svsa.Pessoa pa
				INNER JOIN svsa.Familia f ON f.codigo = pa.codigo_familia
                INNER JOIN svsa.Prontuario p ON p.codigo = f.codigo_prontuario	
				INNER JOIN svsa.PlanoAcompanhamento pi ON pi.codigo_prontuario = p.codigo                
			WHERE p.codigo_unidade = 7
				and pi.dataIngresso between '2022-04-01' and '2022-05-01';
		*/
		
		log.info("Bucando B6...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)			
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
		
	/*  item C  */
	
	public List<PessoaTO> buscarC1(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		
		/*
		 	SELECT sv.situacao, pessoa.nome FROM salto.pessoa pessoa
				INNER JOIN salto.situacaoviolencia sv ON sv.codigo_pessoa = pessoa.codigo
				INNER JOIN salto.familia f ON f.codigo = pessoa.codigo_familia
				INNER JOIN salto.prontuario p ON p.codigo = f.codigo_prontuario
			where 	p.codigo_unidade = 7 and
			        (sv.situacao = "ATO_INFRACIONAL" or sv.situacao = "VIOLENCIA_PSICOLOGICA") and
			        pessoa.sexo = "MASCULINO" and
					(
						(sv.data between "2021-05-01 00:00:00" and "2021-05-31 00:00:00") 
						
					);
		 */
		log.debug("Bucando C1...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and (sv.situacao = 'VIOLENCIA_FISICA' or sv.situacao = 'VIOLENCIA_PSICOLOGICA') "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)			
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	public List<PessoaTO> buscarC2(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		
		
		log.debug("Bucando C2...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and sv.situacao = 'ABUSO_OU_E_VIOLENCIA_SEXUAL' "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	public List<PessoaTO> buscarC3(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		
		
		log.debug("Bucando C3...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and sv.situacao = 'EXPLORACAO_SEXUAL' "
				+ "and sv.dataEncerramento is null "
				+ "and pa.tenant_id = :tenantId "
				+ "and pa.sexo = :sexo "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	public List<PessoaTO> buscarC4(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		
		
		log.debug("Bucando C4...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and sv.situacao = 'NEGLIGENCIA_CONTRA_CRIANÇA' "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	public List<PessoaTO> buscarC5(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		
		
		log.debug("Bucando C5...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and sv.situacao = 'TRABALHO_INFANTIL' "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	public List<PessoaTO> buscarC6(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		
		
		log.debug("Bucando C6 não consta do RMA manual...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and (sv.situacao = 'ATO_INFRACIONAL' or sv.situacao = 'OUTRA') "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}

	/*  item D  */
	
	public List<PessoaTO> buscarD1(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		

		log.debug("Bucando D1...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and (sv.situacao = 'EXPLORACAO_SEXUAL' "
					+ "or sv.situacao = 'ABUSO_OU_E_VIOLENCIA_SEXUAL' "
					+ "or sv.situacao = 'VIOLENCIA_FISICA' "
					+ "or sv.situacao = 'VIOLENCIA_PSICOLOGICA') "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	public List<PessoaTO> buscarD2(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		
		
		log.debug("Bucando D2...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and (sv.situacao = 'NEGLIGENCIA_CONTRA_IDOSO' "
					+ "or sv.situacao = 'VIOLENCIA_PATRIMONIAL_CONTRA_IDOSO_OU_PCD') "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	
	/*  item E  */
	
	public List<PessoaTO> buscarE1(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		

		log.debug("Bucando E1...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN CondicaoSaude cs ON cs.pessoa.codigo = pa.codigo "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and (sv.situacao = 'EXPLORACAO_SEXUAL' "
					+ "or sv.situacao = 'ABUSO_OU_E_VIOLENCIA_SEXUAL' "
					+ "or sv.situacao = 'VIOLENCIA_FISICA' "
					+ "or sv.situacao = 'VIOLENCIA_PSICOLOGICA') "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and (cs.tipoDeficiencia is not null or pa.tipoPcD is not :tipo) "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("tipo", TipoPcD.NÃO)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	public List<PessoaTO> buscarE2(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		
		
		log.debug("Bucando E2...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN CondicaoSaude cs ON cs.pessoa.codigo = pa.codigo "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and (sv.situacao = 'NEGLIGENCIA_CONTRA_PCD' "
					+ "or sv.situacao = 'VIOLENCIA_PATRIMONIAL_CONTRA_IDOSO_OU_PCD') "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and (cs.tipoDeficiencia is not null or pa.tipoPcD is not :tipo) "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("tipo", TipoPcD.NÃO)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	
	/*  item F  */
	
	public List<PessoaTO> buscarF1(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		

		log.debug("Bucando F1...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and (sv.situacao = 'EXPLORACAO_SEXUAL' "
					+ "or sv.situacao = 'ABUSO_OU_E_VIOLENCIA_SEXUAL' "
					+ "or sv.situacao = 'VIOLENCIA_FISICA' "
					+ "or sv.situacao = 'VIOLENCIA_PSICOLOGICA') "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	
	/*  item G  */
	
	public List<PessoaTO> buscarG1(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		

		log.debug("Bucando G1...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and sv.situacao = 'TRAFICO_DE_PESSOAS' "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	
	/*  item H  */
	
	public Long buscarH1(Unidade unidade, Date ini, Date fim, Long tenantId) {		

		log.debug("Bucando H1...");	
		
		return manager.createQuery("SELECT count(pa) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and sv.situacao = 'DISCRIMINACAO_POR_ORIENTACAO_SEXUAL' "
				+ "and sv.dataEncerramento is null "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", Long.class)			
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getSingleResult();		

	}
	
	/*  item I  */
	
	public List<PessoaTO> buscarI1(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		

		log.debug("Bucando I1...");
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
				+ "INNER JOIN SituacaoViolencia sv ON sv.pessoa.codigo = pa.codigo "
				+ "INNER JOIN Familia f ON f.codigo = pa.familia.codigo "
				+ "INNER JOIN Prontuario p ON p.codigo = f.prontuario.codigo "
				+ "INNER JOIN PlanoAcompanhamento pla ON pla.prontuario.codigo = p.codigo "
			+ "WHERE p.unidade = :unidade "
				+ "and sv.situacao = 'TRAJETORIA_DE_RUA' "
				+ "and sv.dataEncerramento is null "
				+ "and pa.sexo = :sexo "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc "
				+ "and sv.data between :ini and :fim "
				+ "and pla.dataIngresso between :ini and :fim ", PessoaTO.class)			
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("exc", false)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	
	
	/*
	 * Bloco II Atendimentos realizados no CREAS.
	 * 
	 */
	
	/*  item M  */

	public Long buscarM1(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("select count(la) from ListaAtendimento la "
				+ "where la.statusAtendimento = :status "
				+ "and la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.dataAtendimento between :ini and :fim "
				+ "and la.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO')", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();
	}		
	public Long buscarM2(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("select count(ac) from AgendamentoColetivo ac "
			+ "where ac.statusAtendimento = :status "
			+ "and ac.unidade = :unidade "
			+ "and ac.tenant_id = :tenantId "
			+ "and ac.dataAtendimento between :ini and :fim ", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("status", StatusAtendimento.ATENDIDO)
			.getSingleResult();
	}
	public Long buscarM3(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("select count(he) from HistoricoEncaminhamento he "
			+ "where he.unidadeEncaminhou = :unidade "
			+ "and he.tenant_id = :tenantId "			
			+ "and he.dataEncaminhamento between :ini and :fim ", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)		
			.getSingleResult();
	}	
	public Long buscarM4(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("select count(la) from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "and la.dataAtendimento between :ini and :fim "
			+ "and la.codigoAuxiliar = :codAux", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("status", StatusAtendimento.ATENDIDO)
			.setParameter("codAux", CodigoAuxiliarAtendimento.VISITA_DOMICILIAR)					
			.getSingleResult();
	}	
	
	/*
	 * Bloco III – Serviço de Proteção Social a Adolescente em Cumprimento de Medida Socioeducativa (LA/PSC)	
	 * 
	 */
	
	/*  item J  */
	
	public Long buscarJ1(Unidade unidade, Date ini, Date fim, Long tenantId) {		

		// J.1. Total de adolescentes em cumprimento de Medidas Socioeducativas (LA e/ou PSC)
		/*
		 * SELECT pia.codigo_pessoa 
		 *   FROM salto.pia pia
				INNER JOIN salto.pessoa p ON p.codigo = pia.codigo_pessoa
				INNER JOIN salto.familia f ON f.codigo = p.codigo_familia
				INNER JOIN salto.prontuario pr ON pr.codigo = f.codigo_prontuario	
			 where dataDesligamento is null and pr.codigo_unidade = 7;
		 * 
		 */
		log.debug("Bucando J1...");	
		
		return manager.createQuery("SELECT count(pia.adolescente) "
			+ "FROM Pia pia "
			  + "INNER JOIN Pessoa p ON p.codigo = pia.adolescente.codigo "
			  + "INNER JOIN Familia f ON f.codigo = p.familia.codigo "
			  + "INNER JOIN Prontuario pr ON pr.codigo = f.prontuario.codigo "				
		    + "WHERE pia.dataDesligamento is null "
		    + "and pia.tenant_id = :tenantId "
		    + "and pr.excluido = :exc "
		     + "and pr.unidade = :unidade", Long.class)			
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("exc", false)
		.getSingleResult();
	}
	public Long buscarJ2(Unidade unidade, Long tenantId) {		

		log.debug("Bucando J2...");	
		
		return manager.createQuery("SELECT count(pia.adolescente) "
			+ "FROM Pia pia "
			  + "INNER JOIN Pessoa p ON p.codigo = pia.adolescente.codigo "
			  + "INNER JOIN Familia f ON f.codigo = p.familia.codigo "
			  + "INNER JOIN Prontuario pr ON pr.codigo = f.prontuario.codigo "				
		    + "WHERE pia.dataDesligamento is null "
		    	+ "and pia.tenant_id = :tenantId "
		    	+ "and pr.excluido = :exc "
		    	+ "and (pia.tipoMse = 'LA' or pia.tipoMse = 'LA_e_PSC')"
		    	+ "and pr.unidade = :unidade", Long.class)			
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("exc", false)
		.getSingleResult();
	}
	public Long buscarJ3(Unidade unidade, Long tenantId) {		

		log.debug("Bucando J3...");	
		
		return manager.createQuery("SELECT count(pia.adolescente) "
			+ "FROM Pia pia "
			  + "INNER JOIN Pessoa p ON p.codigo = pia.adolescente.codigo "
			  + "INNER JOIN Familia f ON f.codigo = p.familia.codigo "
			  + "INNER JOIN Prontuario pr ON pr.codigo = f.prontuario.codigo "				
		    + "WHERE pia.dataDesligamento is null "
		    	+ "and (pia.tipoMse = 'PSC' or pia.tipoMse = 'LA_e_PSC') "
		    	+ "and pia.tenant_id = :tenantId "
		    	+ "and pr.excluido = :exc "
		    	+ "and pr.unidade = :unidade", Long.class)			
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("exc", false)
		.getSingleResult();
	}
	public Long buscarJ4(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		

		log.debug("Bucando J4...");	
		
		return manager.createQuery("SELECT count(pia.adolescente) "
			+ "FROM Pia pia "
			  + "INNER JOIN Pessoa p ON p.codigo = pia.adolescente.codigo "
			  + "INNER JOIN Familia f ON f.codigo = p.familia.codigo "
			  + "INNER JOIN Prontuario pr ON pr.codigo = f.prontuario.codigo "				
		    + "WHERE pia.dataDesligamento is null "
		      + "and pr.unidade = :unidade "
		      + "and pia.tenant_id = :tenantId "
		      + "and p.sexo = :sexo "
		      + "and pr.excluido = :exc "
		      + "and pia.dataEncaminhamento between :ini and :fim ", Long.class)			
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("sexo", sexo)
			.setParameter("exc", false)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.getSingleResult();
	}
	public Long buscarJ5(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		

		log.debug("Bucando J5...");	
		
		return manager.createQuery("SELECT count(pia.adolescente) "
			+ "FROM Pia pia "
			  + "INNER JOIN Pessoa p ON p.codigo = pia.adolescente.codigo "
			  + "INNER JOIN Familia f ON f.codigo = p.familia.codigo "
			  + "INNER JOIN Prontuario pr ON pr.codigo = f.prontuario.codigo "				
		    + "WHERE pia.dataDesligamento is null "
		      + "and pr.unidade = :unidade "
		      + "and p.sexo = :sexo "
		      + "and pr.excluido = :exc "
		      + "and pia.tenant_id = :tenantId "
		      + "and (pia.tipoMse = 'LA' or pia.tipoMse = 'LA_e_PSC')"
		      + "and pia.dataEncaminhamento between :ini and :fim ", Long.class)			
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("sexo", sexo)
			.setParameter("exc", false)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.getSingleResult();
	}
	public Long buscarJ6(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {		

		log.debug("Bucando J6...");	
		
		return manager.createQuery("SELECT count(pia.adolescente) "
			+ "FROM Pia pia "
			  + "INNER JOIN Pessoa p ON p.codigo = pia.adolescente.codigo "
			  + "INNER JOIN Familia f ON f.codigo = p.familia.codigo "
			  + "INNER JOIN Prontuario pr ON pr.codigo = f.prontuario.codigo "				
		    + "WHERE pia.dataDesligamento is null "
		      + "and pr.unidade = :unidade "
		      + "and pia.tenant_id = :tenantId "
		      + "and p.sexo = :sexo "
		      + "and pr.excluido = :exc "
		      + "and (pia.tipoMse = 'PSC' or pia.tipoMse = 'LA_e_PSC')"
		      + "and pia.dataEncaminhamento between :ini and :fim ", Long.class)			
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("sexo", sexo)
			.setParameter("exc", false)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.getSingleResult();
	}
	
	
	
	
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}

	
}