package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.modelo.enums.StatusAtendimento;
import com.softarum.svsa.modelo.to.AtendimentoTO;
import com.softarum.svsa.modelo.to.PerfilFamiliaTO;
import com.softarum.svsa.util.DateUtils;

/**
 * @author murakamiadmin
 *
 */
public class DashBoardDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;	
	
	
	
	/*
	 * DashBoard
	 */
	
	public Long buscarQuantidadeAtendidos(Unidade unidade, Date ini, Date fim, Long tenantId) {
		return manager.createQuery("select count(la) from ListaAtendimento la "
				+ "where la.statusAtendimento = :status "
				+ "and la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.dataAtendimento between :ini and :fim", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getSingleResult();
	}
	
	
	public Long qdeProntuariosNovos(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("select count(p) from Prontuario p "
				+ "where p.dataEntrada between :ini and :fim "
				+ "and p.unidade = :unidade "
				+ "and p.tenant_id = :tenantId "
				+ "and p.status = :status", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", Status.ATIVO)
				.getSingleResult();
	}
	
	public Long buscarQdeAcompanhamento(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		return manager.createQuery("SELECT count(p) FROM PlanoAcompanhamento p "
					+ "INNER JOIN Prontuario prontuario ON p.prontuario.codigo = prontuario.codigo "
					+ "WHERE p.dataIngresso between :ini and :fim "
					+ "and prontuario.unidade = :unidade "
					+ "and p.tenant_id = :tenantId ", Long.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getSingleResult();
	
	}
	
	
	
	
	
	
	
	public List<AtendimentoTO> dashboardQdesCodAuxIndiv(Unidade unidade, Date ini, Date fim, Long tenantId){		
		
		List<AtendimentoTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoTO(la.codigoAuxiliar) "
			+ "FROM ListaAtendimento la "				
			+ "WHERE la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.dataAtendimento between :ini and :fim "
				+ "and la.statusAtendimento = :status "
				+ "order by la.codigoAuxiliar", AtendimentoTO.class)			
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();
		
		return lista;
	}
	
	public List<AtendimentoTO> dashboardQdesCodAuxColet(Unidade unidade, Date ini, Date fim, Long tenantId){		
		
		List<AtendimentoTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoTO(la.codigoAuxiliar) "
			+ "FROM AgendamentoColetivo la "				
			+ "WHERE la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.dataAtendimento between :ini and :fim "
				+ "and la.statusAtendimento = :status "				
				+ "order by la.codigoAuxiliar", AtendimentoTO.class)			
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();
		
		return lista;
	}
	
	public List<AtendimentoTO> dashboardQdesCodAuxFamiliar(Unidade unidade, Date ini, Date fim, Long tenantId){		
		
		List<AtendimentoTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoTO(la.codigoAuxiliar) "
			+ "FROM AgendamentoFamiliar la "				
			+ "WHERE la.unidade = :unidade "
				+ "and la.tenant_id = :tenantId "
				+ "and la.dataAtendimento between :ini and :fim "
				+ "and la.statusAtendimento = :status "				
				+ "order by la.codigoAuxiliar", AtendimentoTO.class)			
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusAtendimento.ATENDIDO)
				.getResultList();
		
		return lista;
	}
	
	public List<AtendimentoTO> dashboardQdesScfv(Unidade unidade, Date ini, Date fim, Long tenantId){		
		
		List<AtendimentoTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.AtendimentoTO(la.tipoServico) "
			+ "FROM Servico la "				
			+ "WHERE la.unidade = :unidade "
				+ "and la.dataIni between :ini and :fim "
				+ "and la.tenant_id = :tenantId "
				+ "order by la.tipoServico", AtendimentoTO.class)			
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getResultList();
		
		return lista;
	}
	
	public List<PerfilFamiliaTO> dashboardQdesPaif(Unidade unidade, Date ini, Date fim, Long tenantId){		
		
		/*
			SELECT a.codigo as codigo,
			   a.dataIngresso as dataIngresso,
               p.codigo as prontuario,
               u.nome as unidade
			FROM svsa_salto.planoacompanhamento a 
				INNER JOIN svsa_salto.Prontuario p ON a.codigo_prontuario = p.codigo  
				INNER JOIN svsa_salto.Unidade u ON p.codigo_unidade = u.codigo 				
				WHERE u.codigo = 1;		 */
		

		List<PerfilFamiliaTO> lista = manager.createQuery("SELECT new com.softarum.svsa.modelo.to.PerfilFamiliaTO(a) "
				+ "FROM PlanoAcompanhamento a "
				+   "INNER JOIN Prontuario p ON a.prontuario.codigo = p.codigo "
				+   "INNER JOIN Unidade u ON p.unidade.codigo = u.codigo "
				+ "WHERE u = :unidade "
					+ "and a.tenant_id = :tenantId "
					+ "and a.dataIngresso between :ini and :fim ", PerfilFamiliaTO.class)
				.setParameter("unidade", unidade)
				.setParameter("tenantId", tenantId)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.getResultList();
		
		return lista;
	}
		
	
	
	



	// criado para realização de testes unitários com JIntegrity
	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}
	
	public void setManager(EntityManager manager2) {
		this.manager = manager2;
		
	}
	
	
	
	
}