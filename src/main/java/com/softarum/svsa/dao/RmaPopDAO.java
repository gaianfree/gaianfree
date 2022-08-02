package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.Sexo;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.to.rma.PessoaTO;
import com.softarum.svsa.util.DateUtils;

import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
public class RmaPopDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	
	/*
	 * Bloco I - Serviço Especializado para Pessoas em Situação de Rua.
	 */
	
	
	
	/*
	 * A - Pessoas em situação de rua atendidas no serviço durante o mês de referência.
	 */
	public List<PessoaTO> buscarA1(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {
		log.info("rma pop a1");
		/*
		SELECT *  FROM svsa.pessoa pa
		INNER JOIN svsa.listaatendimento pi ON pi.codigo_pessoa = pa.codigo                
		WHERE pi.codigo_unidade = 10
			and pi.dataAtendimento between '2022-07-01' and '2022-07-31';		
		*/
		
		List<PessoaTO> tos = new ArrayList<PessoaTO>();		
		
		tos = manager.createQuery("SELECT distinct new com.softarum.svsa.modelo.to.rma.PessoaTO(pa.dataNascimento) "
			+ "FROM Pessoa pa "
			+ "	INNER JOIN ListaAtendimento la ON la.pessoa = pa "
			+ "WHERE la.unidade = :unidade "
			+ "and pa.sexo = :sexo "				
			+ "and pa.tenant_id = :tenantId "
			+ "and la.dataAtendimento between :ini and :fim ", PessoaTO.class)			
		.setParameter("unidade", unidade)
		.setParameter("tenantId", tenantId)
		.setParameter("sexo", sexo)
		.setParameter("ini", ini, TemporalType.TIMESTAMP)
		.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
		.getResultList();		
		
		return tos;
	}
	
	/*
	 * B - Características específicas identificadas em pessoas atendidas no serviço durante o mês de referência.
	 */
	
	// Pessoas usuárias de crack ou outras drogas ilícitas
	public Long buscarB1(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("select count(la) from ListaAtendimento la "
				+ "where la.statusAtendimento = :status "
				+ "and la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.codigoAuxiliar = :codigo "
				+ "and la.dataAtendimento between :ini and :fim ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("codigo", CodigoAuxiliarAtendimento.USUARIO_CRACK_OU_DROGRAS_ILICITAS)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();
	
	}
	
	// Migrantes
	public Long buscarB2(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("select count(la) from ListaAtendimento la "
				+ "where la.statusAtendimento = :status "
				+ "and la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.codigoAuxiliar = :codigo "
				+ "and la.dataAtendimento between :ini and :fim ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("codigo", CodigoAuxiliarAtendimento.MIGRANTE)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();
	
	}
	
	// Pessoas com doença ou transtorno mental
	public Long buscarB3(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("select count(la) from ListaAtendimento la "
				+ "where la.statusAtendimento = :status "
				+ "and la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.codigoAuxiliar = :codigo "
				+ "and la.dataAtendimento between :ini and :fim ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("codigo", CodigoAuxiliarAtendimento.DOENCA_OU_TRANSTORNO_MENTAL)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();
	
	}

	// Usuárias de alcool	
	public Long buscarB4(Unidade unidade, Date ini, Date fim, Long tenantId) {
	
		return manager.createQuery("select count(la) from ListaAtendimento la "
				+ "where la.statusAtendimento = :status "
				+ "and la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.codigoAuxiliar = :codigo "
				+ "and la.dataAtendimento between :ini and :fim ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("codigo", CodigoAuxiliarAtendimento.USUARIO_ALCOOL)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();

	}
	
	/*
	 * C - Cadastramento de pessoas em situação de rua durante o mês de referência.
	 */
	
	// Pessoas que foram incluídas no Cadastro Único para Programas Sociais, no mês.
	public Long buscarC1(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("select count(la) from ListaAtendimento la "
				+ "where la.statusAtendimento = :status "
				+ "and la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.dataAtendimento between :ini and :fim "
				+ "and (la.codigoAuxiliar = :codAux or la.codigoAuxiliar = :codAux2) ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.setParameter("codAux", CodigoAuxiliarAtendimento.CADASTRAMENTO_CADUNICO)
				.setParameter("codAux2", CodigoAuxiliarAtendimento.CADASTRAMENTO_CADUNICO_BPC)
				.getSingleResult();
	}
	
	// Pessoas que realizaram atualização no Cadastro Único para Programas Sociais, no mês.
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
				.setParameter("codAux", CodigoAuxiliarAtendimento.ATUALIZACAO_CADUNICO)					
				.getSingleResult();

	}
	
	/*
	 * D. Volume total de atendimentos realizados no mês de referência.
	 */
	
	// Quantidade total de atendimentos realizados (compreendida como a soma do número de atendimentos realizados 
	// a cada dia, durante o mês de referência)
	public Long buscarD1(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("select count(la) from ListaAtendimento la "
				+ "where la.statusAtendimento = :status "
				+ "and la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.dataAtendimento between :ini and :fim ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();

	}
	
	
	
	
	
	
	
	
	
	/*
	 * Bloco II - Serviço Especializado em Abordagem Social no Centro POP. 
	 */
	
		
	/*
	 * E. Quantidade e perfil das pessoas abordadas pela equipe do Serviço de Abordagem, no mês de referência.
	 */
	
	//E.1. Quantidade e perfil das pessoas abordadas pela equipe do Serviço de Abordagem, no mês de referência.
	public List<PessoaTO> buscarE1(Unidade unidade, Date ini, Date fim, Sexo sexo, Long tenantId) {
		log.info("rma pop e1");
		
		return new ArrayList<PessoaTO>();
	}
	
	//E.2. Crianças ou adolescentes em situação de trabalho infantil (até 15 anos)
	public Long buscarE2(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		//TODO
		
		return 1L;

	}
	
	//E.3. Crianças ou adolescentes em situação de exploração sexual
	public Long buscarE3(Unidade unidade, Date ini, Date fim, Long tenantId) {
			
		//TODO
		
		return 1L;

	}
	
	//E.4. Crianças ou adolescentes usuárias de crack ou outras drogras
	public Long buscarE4(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		//TODO
		
		return 1L;
	
	}
	
	//E.5. Pessoas adultas usuárias de crack ou outras drogas ilícitas
	public Long buscarE5(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		//TODO
		
		return 1L;
	
	}
	
	//E.6. Migrantes
	public Long buscarE6(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		//TODO
		
		return 1L;
	
	}

	//E.7. Usuárias de alcool
	public Long buscarE7(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		//TODO
		
		return 1L;
	
	}
	
	/*
	 * F. Volume de abordagens realizadas.
	 */
	
	// F.1. Quantidade total de abordagens realizadas (compreendida como o número de pessoas abordadas, 
	// multiplicado pelo número de vezes em que foram abordadas durante o mês)
	public Long buscarF1(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("select count(la) from Abordagem la "
				+ "where la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.data between :ini and :fim ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getSingleResult();
	}
	
	
	
	
	
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
	
}