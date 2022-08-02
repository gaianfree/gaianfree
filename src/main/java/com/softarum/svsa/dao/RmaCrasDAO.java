package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.PerfilFamilia;
import com.softarum.svsa.modelo.enums.Prioridade;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.enums.TipoPcD;
import com.softarum.svsa.modelo.enums.TipoServico;
import com.softarum.svsa.util.DateUtils;

/**
 * @author murakamiadmin
 *
 */
public class RmaCrasDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(RmaCrasDAO.class);

	@Inject
	private EntityManager manager;

	
	/*
	 * Buscas A
	 */

	//A.1. Total de famílias em acompanhamento pelo PAIF
	public Long buscarA1(Unidade unidade, Date fim, Long tenantId) {
		
		log.info("RMA DAO unidade = " + unidade.getNome() + " - tenant_id = " + tenantId);
		
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
	
	/*
	 * Buscas B
	 */
	
	//B.1. Famílias em situação de extrema pobreza durante o mês de referência
	public Long buscarB1(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ "and pa.dataIngresso between :ini and :fim "
				+ "and pa.tenant_id = :tenantId "
				+ "and p.excluido = :exc", Long.class)		
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.SITUACAO_EXTREMA_POBREZA)
			.setParameter("exc", false)
			.getSingleResult();
		
	}

	//B.2. Famílias beneficiárias do bolsa família durante o mês de referência
	public Long buscarB2(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.tenant_id = :tenantId "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.BENEFICIARIA_BOLSA_FAMILIA)
			.setParameter("exc", false)
			.getSingleResult();
	}

	//B.3. Famílias beneficiárias do bolsa família, em descumprimento de condicionalidades durante o mês de referência
	public Long buscarB3(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.tenant_id = :tenantId "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.BENEFICIARIA_BOLSA_FAMILIA_EM_DESCUMPRIMENTO_COND)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	//B.4. Famílias com membros beneficiários do BPC durante o mês de referência
	public Long buscarB4(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.tenant_id = :tenantId "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.BENEFICIARIA_BPC)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	//B.5. Famílias crianças ou adolescentes em situação de trabalho infantil durante o mês de referência
	public Long buscarB5(Unidade unidade, Date ini, Date fim, Long tenantId) {
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
				+ " and pa.tenant_id = :tenantId "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.COM_CRIANCA_ADOL_EM_TRABALHO_INFANTIL)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	//B.6. Famílias crianças ou adolescentes em serviço de acolhimento durante o mês de referência
	public Long buscarB6(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.tenant_id = :tenantId "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.COM_CRIANCA_ADOL_EM_SV_ACOLHIMENTO)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	//B.7. Outros durante o mês de referência
	public Long buscarB7(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.tenant_id = :tenantId "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.OUTROS)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	//B.8. Atende criterios dos PTR mas nao foi contemplada
	public Long buscarB8(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.tenant_id = :tenantId "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.NAO_CONTEMPLADAS_PTR)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	//B.9. Em situacao de vulnerabilidade
	public Long buscarB9(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
				+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
				+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
				+ "WHERE u = :unidade "
				+ " and pa.tenant_id = :tenantId "
				+ " and pa.dataIngresso between :ini and :fim "
				+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.EM_SITUACAO_VULNERABILIDADE)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	//B.10. Com deficientes ou idosos
	public Long buscarB10(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento pa "
			+ "	 INNER JOIN Prontuario p ON pa.prontuario.codigo = p.codigo "  
			+ "	 INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
			+ "	 INNER JOIN pa.perfisFamilia pf ON pf = :pf "			
			+ "WHERE u = :unidade "
			+ " and pa.tenant_id = :tenantId "
			+ " and pa.dataIngresso between :ini and :fim "
			+ " and p.excluido = :exc", Long.class)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("pf", PerfilFamilia.COM_DEFICIENTES_OU_IDOSOS)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	/*
	 * Buscas C
	 */
	
	//atendimentos individualizados
	public Long buscarC1(Unidade unidade, Date ini, Date fim, Long tenantId) {		
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
	
	//encaminhadas para inclusão no Cadastro Único
	public Long buscarC2(Unidade unidade, Date ini, Date fim, Long tenantId) {
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
			.setParameter("codAux", CodigoAuxiliarAtendimento.CADASTRAMENTO_CADUNICO)					
			.getSingleResult();
	}
	
	public Long buscarC3(Unidade unidade, Date ini, Date fim, Long tenantId) {
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
			.setParameter("codAux", CodigoAuxiliarAtendimento.ATUALIZACAO_CADUNICO)					
			.getSingleResult();
	}
	
	public Long buscarC4(Unidade unidade, Date ini, Date fim, Long tenantId) {
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
			.setParameter("codAux", CodigoAuxiliarAtendimento.CADASTRAMENTO_CADUNICO_BPC)					
			.getSingleResult();
	}
	/*
	 * Encaminhadas para o CREAS
	*/
	public Long buscarC5(Unidade unidade, Date ini, Date fim, Long tenantId) {
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
	 
	// visitas domiciliares
	public Long buscarC6(Unidade unidade, Date ini, Date fim, Long tenantId) {
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
	
	public Long buscarC7(Unidade unidade, Date ini, Date fim, Long tenantId) {
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
			.setParameter("codAux", CodigoAuxiliarAtendimento.AUXILIO_NATALIDADE)					
			.getSingleResult();
	}
	
	public Long buscarC8(Unidade unidade, Date ini, Date fim, Long tenantId) {
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
			.setParameter("codAux", CodigoAuxiliarAtendimento.AUXILIO_FUNERAL)					
			.getSingleResult();
	}
	
	public Long buscarC9(Unidade unidade, Date ini, Date fim, Long tenantId) {
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
			.setParameter("codAux", CodigoAuxiliarAtendimento.OUTROS_BENEFICIOS)					
			.getSingleResult();
	}
	
	
	
	/*
	 * Buscas D
	 * 
	 * grupos com familias PAIF
	 */
	
		
	public Long buscarD1(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		/*
		SELECT count(DISTINCT familia.codigo) 
		  FROM svsa_salto.Familia familia
			INNER JOIN svsa_salto.Pessoa pessoa ON (pessoa.codigo_familia = familia.codigo)
			INNER JOIN svsa_salto.Frequencia frequencia ON (frequencia.codigo_pessoa = pessoa.codigo)
		    INNER JOIN svsa_salto.Atividade atividade ON (atividade.codigo = frequencia.codigo_atividade)
			INNER JOIN svsa_salto.Servico servico ON (servico.codigo = atividade.codigo_servico)
		  WHERE servico.status = "ATIVO";	
		*/
	
		return manager.createQuery("select count(DISTINCT familia.codigo) "
				+ "FROM Familia familia "
				+ "INNER JOIN Prontuario p ON (p.familia = familia) "
				+ "INNER JOIN Pessoa pessoa ON (pessoa.familia = familia) "
				+ "INNER JOIN Frequencia frequencia ON (frequencia.pessoa = pessoa) "
			    + "INNER JOIN Atividade atividade ON (atividade = frequencia.atividade) "
				+ "INNER JOIN Servico servico ON (servico = atividade.servico) "
			+ "WHERE servico.status = :status "				
				+ "and atividade.data between :ini and :fim "
				+ "and familia.tenant_id = :tenantId "
				+ "and servico.tipoServico in ('GRUPO_AMBITO_PAIF') "
				+ "and servico.unidade = :unidade "
				+ "and p.excluido = :exc ", Long.class)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("status", Status.ATIVO)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	public Long buscarD2(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(DISTINCT pessoa.codigo) "
			+ "FROM Pessoa pessoa "
				+ "INNER JOIN Frequencia frequencia ON (frequencia.pessoa = pessoa) "
			    + "INNER JOIN Atividade atividade ON (atividade = frequencia.atividade) "
				+ "INNER JOIN Servico servico ON (servico = atividade.servico) "
			+ "WHERE servico.status = :status "	
				+ "and servico.tipoServico = :tipoServico "
				+ "and pessoa.tenant_id = :tenantId "
				+ "and atividade.data between :ini and :fim "
				+ "and servico.unidade = :unidade "
				+ "and pessoa.excluida = :exc", Long.class)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("status", Status.ATIVO)
			.setParameter("tipoServico", TipoServico.CRIANCAS_ATE_6_ANOS)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	public Long buscarD3(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(DISTINCT pessoa.codigo) "
			+ "FROM Pessoa pessoa "
				+ "INNER JOIN Frequencia frequencia ON (frequencia.pessoa = pessoa) "
			    + "INNER JOIN Atividade atividade ON (atividade = frequencia.atividade) "
				+ "INNER JOIN Servico servico ON (servico = atividade.servico) "
			+ "WHERE servico.status = :status "	
				+ "and servico.tipoServico = :tipoServico "
				+ "and pessoa.tenant_id = :tenantId "
				+ "and atividade.data between :ini and :fim "
				+ "and servico.unidade = :unidade "
				+ "and pessoa.excluida = :exc", Long.class)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("status", Status.ATIVO)
			.setParameter("tipoServico", TipoServico.CRIANCAS_E_ADOLECENTES_DE_7_A_14_ANOS)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	public Long buscarD4(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(DISTINCT pessoa.codigo) "
			+ "FROM Pessoa pessoa "
				+ "INNER JOIN Frequencia frequencia ON (frequencia.pessoa = pessoa) "
			    + "INNER JOIN Atividade atividade ON (atividade = frequencia.atividade) "
				+ "INNER JOIN Servico servico ON (servico = atividade.servico) "
			+ "WHERE servico.status = :status "	
				+ "and servico.tipoServico = :tipoServico "
				+ "and pessoa.tenant_id = :tenantId "
				+ "and atividade.data between :ini and :fim "
				+ "and servico.unidade = :unidade "
				+ "and pessoa.excluida = :exc", Long.class)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("status", Status.ATIVO)
			.setParameter("tipoServico", TipoServico.ADOLESCENTES_E_JOVENS_DE_15_A_17_ANOS)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	public Long buscarD5(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(DISTINCT pessoa.codigo) "
			+ "FROM Pessoa pessoa "
				+ "INNER JOIN Frequencia frequencia ON (frequencia.pessoa = pessoa) "
			    + "INNER JOIN Atividade atividade ON (atividade = frequencia.atividade) "
				+ "INNER JOIN Servico servico ON (servico = atividade.servico) "
			+ "WHERE servico.status = :status "	
				+ "and servico.tipoServico in ('JOVENS_DE_18_A_29_ANOS', "
											 + "'ADULTOS_DE_30_A_59_ANOS') "
				+ "and atividade.data between :ini and :fim "
				+ "and pessoa.tenant_id = :tenantId "
				+ "and servico.unidade = :unidade "
				+ "and pessoa.excluida = :exc", Long.class)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("status", Status.ATIVO)	
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	
	
	public Long buscarD6(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		/*  
		 * SELECT count(DISTINCT pessoa.codigo) 
				FROM Pessoa pessoa 
					INNER JOIN Frequencia frequencia ON (frequencia.codigo_pessoa = pessoa.codigo) 
				    INNER JOIN Atividade atividade ON (atividade.codigo = frequencia.codigo_atividade)
					INNER JOIN Servico servico ON (servico.codigo = atividade.codigo_servico) 
				WHERE servico.status = "ATIVO"	
					and servico.tipoServico = "IDOSOS" 
					and atividade.data between "20200101" and "20200131" 
					and servico.codigo_unidade = 2;
		*/
		
		return manager.createQuery("SELECT count(DISTINCT pessoa.codigo) "
			+ "FROM Pessoa pessoa "
				+ "INNER JOIN Frequencia frequencia ON (frequencia.pessoa = pessoa) "
			    + "INNER JOIN Atividade atividade ON (atividade = frequencia.atividade) "
				+ "INNER JOIN Servico servico ON (servico = atividade.servico) "
			+ "WHERE servico.status = :status "	
				+ "and servico.tipoServico = :tipoServico "
				+ "and pessoa.tenant_id = :tenantId "
				+ "and atividade.data between :ini and :fim "
				+ "and servico.unidade = :unidade "
				+ "and pessoa.excluida = :exc", Long.class)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("status", Status.ATIVO)
			.setParameter("tipoServico", TipoServico.IDOSOS)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	public Long buscarD7(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		/* qde pessoas em atividades coleticas nao continuada 
		SELECT count(DISTINCT pessoa.codigo) 
			 FROM Pessoa pessoa
				 INNER JOIN Frequencia frequencia ON (frequencia.codigo_pessoa = pessoa.codigo) 
			     INNER JOIN Atividade atividade ON (atividade.codigo = frequencia.codigo_atividade) 
				 INNER JOIN Servico servico ON (servico.codigo = atividade.codigo_servico) 
			 WHERE servico.status = "ATIVO" 	
				 and servico.tipoServico in ('ATIVIDADES_CARATER_NAO_CONTINUADO', 'GRUPO_AMBITO_PAIF')
                 and atividade.data BETWEEN '2022-04-01' AND '2022-04-30'
                 and servico.codigo_unidade = 5
                 and pessoa.tenant_id = 1
                 and pessoa.excluida = 0b0;
		*/
		
		Long qdeScfv = manager.createQuery("SELECT count(DISTINCT pessoa.codigo) "
				+ "FROM Pessoa pessoa "
					+ "INNER JOIN Frequencia frequencia ON (frequencia.pessoa = pessoa) "
				    + "INNER JOIN Atividade atividade ON (atividade = frequencia.atividade) "
					+ "INNER JOIN Servico servico ON (servico = atividade.servico) "
				+ "WHERE servico.status = :status "	
					+ "and servico.tipoServico in ('ATIVIDADES_CARATER_NAO_CONTINUADO', 'GRUPO_AMBITO_PAIF') "
					+ "and atividade.data between :ini and :fim "
					+ "and servico.unidade = :unidade "
					+ "and pessoa.tenant_id = :tenantId "
					+ "and pessoa.excluida = :exc", Long.class)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("status", Status.ATIVO)
				.setParameter("exc", false)
				.getSingleResult();
		
		log.info("d7 scfv: " + qdeScfv);
		
		/*
		SELECT count(DISTINCT b.codigo)
				FROM AgendamentoColetivo a
				INNER JOIN PessoaAgendada r ON (r.codigo_agendamento = a.codigo)
				INNER JOIN Pessoa b ON (b.codigo = r.codigo_pessoa)
				INNER JOIN Usuario c ON (c.codigo = a.codigo_tecnico)
				INNER JOIN Unidade d ON (d.codigo = a.codigo_unidade)
				WHERE a.statusAtendimento = "ATENDIDO"
					and a.tenant_id = 1
					and a.codigoAuxiliar = "ATIVIDADE_COLETIVA_DE_CARATER_NAO_CONTINUADO"
					and a.dataAtendimento between '2022-04-01' AND '2022-04-30'  
					and a.codigo_unidade = 5
					and b.excluida = 0b0;

		*/
		
		Long qdeColetivo = manager.createQuery("SELECT count(DISTINCT b.codigo) "
				+ "FROM AgendamentoColetivo a "
				+    "INNER JOIN a.pessoas r "
				+    "INNER JOIN Pessoa b ON (b.codigo = r.codigo) "
				+    "INNER JOIN Usuario c ON (c.codigo = a.tecnico.codigo) "
				+    "INNER JOIN Unidade d ON (d.codigo = a.unidade.codigo) "
				+ "WHERE a.statusAtendimento = :status "
					+ "and a.tenant_id = :tenantId "
					+ "and a.codigoAuxiliar = :codAux "
					+ "and a.dataAtendimento between :ini and :fim "
					+ "and a.unidade = :unidade "
					+ "and b.excluida = :exc", Long.class)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.setParameter("tenantId", tenantId)
				.setParameter("codAux", CodigoAuxiliarAtendimento.ATIVIDADE_COLETIVA_DE_CARATER_NAO_CONTINUADO)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("unidade", unidade)				
				.setParameter("exc", false)
				.getSingleResult();
		
		
		log.info("d7 scfv: " + qdeColetivo);
		return qdeScfv + qdeColetivo;
	}
	
	public Long buscarD8(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		/*
		 * SELECT count(distinct pessoa.codigo)	
			FROM svsa_salto.Atividade atividade	
	            INNER JOIN svsa_salto.Servico servico ON (servico.codigo = atividade.codigo_servico)            
	            INNER JOIN svsa_salto.Frequencia frequencia ON (frequencia.codigo_atividade = atividade.codigo)            
	            INNER JOIN svsa_salto.pessoa pessoa ON (pessoa.codigo = frequencia.codigo_pessoa) 
	            INNER JOIN svsa_salto.Inscricao inscricao ON (inscricao.codigo_pessoa = pessoa.codigo)             
			WHERE atividade.data BETWEEN '2019-12-01 00:00:00' AND '2019-12-31 23:59:59'  
				 and servico.status = "ATIVO"
				and inscricao.prioridade = "VULNERABILIDADE_DEFICIENTE"
				and servico.codigo_unidade = 2;
		*/ 
		
		Long qdeDeficiente =  manager.createQuery("SELECT count(DISTINCT pessoa.codigo) "
			+ "FROM Atividade atividade "
				+ "INNER JOIN Servico servico ON (servico = atividade.servico) "
				+ "INNER JOIN Frequencia frequencia ON (frequencia.atividade = atividade) "
				+ "INNER JOIN Pessoa pessoa ON (pessoa = frequencia.pessoa) "
				+ "INNER JOIN Inscricao inscricao ON (inscricao.pessoa = pessoa) "
			+ "WHERE  servico.status = :status "
			    + "and atividade.data BETWEEN :ini and :fim "
			    + "and atividade.tenant_id = :tenantId "	
				+ "and inscricao.prioridade = :prioridade "
				+ "and servico.unidade = :unidade "
				+ "and pessoa.excluida = :exc", Long.class)
			.setParameter("status", Status.ATIVO)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("prioridade", Prioridade.VULNERABILIDADE_DEFICIENTE)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("exc", false)
			.getSingleResult();		
		
		Long qdeScfv =  manager.createQuery("SELECT count(DISTINCT pessoa.codigo) "
				+ "FROM Pessoa pessoa "
				+ "INNER JOIN CondicaoSaude cs ON cs.pessoa.codigo = pessoa.codigo "
				+ "INNER JOIN Frequencia frequencia ON (frequencia.pessoa = pessoa) "
			    + "INNER JOIN Atividade atividade ON (atividade = frequencia.atividade) "
				+ "INNER JOIN Servico servico ON (servico = atividade.servico) "
			+ "WHERE servico.status = :status "					
				+ "and (cs.tipoDeficiencia is not null or pessoa.tipoPcD is not :tipo) "
				+ "and atividade.data between :ini and :fim "
				+ "and pessoa.tenant_id = :tenantId "
				+ "and servico.unidade = :unidade "
				+ "and pessoa.excluida = :exc", Long.class)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("status", Status.ATIVO)
			.setParameter("tipo", TipoPcD.NÃO)
			.setParameter("exc", false)
			.getSingleResult();
		
		return qdeScfv + qdeDeficiente;
	}
	
	public Long buscarD9(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("SELECT count(DISTINCT pessoa.codigo) "
			+ "FROM Pessoa pessoa "
				+ "INNER JOIN Frequencia frequencia ON (frequencia.pessoa = pessoa) "
			    + "INNER JOIN Atividade atividade ON (atividade = frequencia.atividade) "
				+ "INNER JOIN Servico servico ON (servico = atividade.servico) "
			+ "WHERE servico.status = :status "	
				+ "and servico.tipoServico in ('GRUPO_AMBITO_PAIF') "
				+ "and pessoa.tenant_id = :tenantId "
				+ "and atividade.data between :ini and :fim "
				+ "and servico.unidade = :unidade "
				+ "and pessoa.excluida = :exc", Long.class)
			.setParameter("ini", ini, TemporalType.TIMESTAMP)
			.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
			.setParameter("unidade", unidade)
			.setParameter("tenantId", tenantId)
			.setParameter("status", Status.ATIVO)
			.setParameter("exc", false)
			.getSingleResult();
	}
	
	
	
	
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}

	

}