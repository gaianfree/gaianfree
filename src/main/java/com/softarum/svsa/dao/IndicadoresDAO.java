package com.softarum.svsa.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.Familia;
import com.softarum.svsa.modelo.IndProtecao;
import com.softarum.svsa.modelo.IndicadorFamilia;
import com.softarum.svsa.modelo.SituacaoProtecao;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.NegocioException;
import com.softarum.svsa.util.jpa.Transactional;

/**
 * @author murakamiadmin
 *
 */
public class IndicadoresDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	
	@Transactional
	public void salvar(List<IndicadorFamilia> indicadores) throws NegocioException {	
		try {
			for(IndicadorFamilia ind : indicadores) {
				manager.merge(ind);
			}			
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

	
	
	public SituacaoProtecao buscarPeloCodigo(Long codigo) {
		return manager.find(SituacaoProtecao.class, codigo);
	}
	
	public IndProtecao buscarIndPeloCodigo(Long codigo) {
		return manager.find(IndProtecao.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<SituacaoProtecao> buscarSituacoes() {
		return manager.createNamedQuery("SituacaoProtecao.buscarTodos").getResultList();
	}

	
	/* 
	 * IndProtecao 
	 */
	
	public List<IndicadorFamilia> buscarIndicadoresProtecao(Familia familia, Long tenantId) {
		return manager.createNamedQuery("IndicadorFamilia.buscarIndicadores", IndicadorFamilia.class)
				.setParameter("familia", familia)
				.setParameter("tenantId", tenantId)
				.getResultList();
	}
	
	
	public Long buscarQdeSituacao(SituacaoProtecao situacao, Date dataInicio, Date dataFim, Long tenantId) {
		/*
		 SELECT count(*)
			FROM svsa_salto.IndicadorFamilia ifa
				INNER JOIN svsa_salto.Familia fa ON ifa.codigo_familia = fa.codigo
				INNER JOIN svsa_salto.IndProtecao ip ON ip.codigo = ifa.codigo_indicador
				INNER JOIN svsa_salto.SituacaoProtecao sp ON sp.codigo = ip.codigo_situacao
			WHERE sp.codigo = 3 and ifa.dataCriacao between "2021-01-01 00:00:00" and "2021-12-31 00:00:00";
		 */

		Query q = manager.createQuery("Select count(ifa) from IndicadorFamilia ifa  "
				+ "INNER JOIN Familia fa ON fa.codigo = ifa.familia.codigo  "
				+ "INNER JOIN IndProtecao ip ON ip.codigo = ifa.indProtecao.codigo "
				+ "INNER JOIN SituacaoProtecao sp ON sp.codigo = ip.situacaoProtecao.codigo "
				+ "WHERE sp.codigo = :situacao "
				+ "and ifa.tenant_id = :tenantId "
				+ "and ifa.dataCriacao between :dataInicio and :dataFim");
		q.setParameter("situacao", situacao.getCodigo());
		q.setParameter("tenantId", tenantId);
		q.setParameter("dataInicio", dataInicio, TemporalType.TIMESTAMP);
		q.setParameter("dataFim", dataFim, TemporalType.TIMESTAMP);
		Long qde = (Long) q.getSingleResult();
		
		return qde;
	}
	
	public Long buscarQdeIndProtecao(IndProtecao ind, Date dataInicio, Date dataFim, Long tenantId) {
		/*
		 SELECT count(*)
			FROM svsa_salto.IndicadorFamilia ifa
				INNER JOIN svsa_salto.Familia fa ON ifa.codigo_familia = fa.codigo
				INNER JOIN svsa_salto.IndProtecao ip ON ip.codigo = ifa.codigo_indicador
			WHERE ip.codigo = 1;
		 */

		Query q = manager.createQuery("Select count(ifa) from IndicadorFamilia ifa  "
				+ "INNER JOIN Familia fa ON fa.codigo = ifa.familia.codigo  "
				+ "INNER JOIN IndProtecao ip ON ip.codigo = ifa.indProtecao.codigo "
				+ "WHERE ip.codigo = :ind "
				+ "and ifa.tenant_id = :tenantId "
				+ "and ifa.dataCriacao between :dataInicio and :dataFim");
		q.setParameter("ind", ind.getCodigo());
		q.setParameter("tenantId", tenantId);
		q.setParameter("dataInicio", dataInicio, TemporalType.TIMESTAMP);
		q.setParameter("dataFim", dataFim, TemporalType.TIMESTAMP);
		Long qde = (Long) q.getSingleResult();
		
		return qde;
	}
	
	
	/*
	 * Geral por unidade 
	 */
	public Long buscarQdeSituacaoUnidade(SituacaoProtecao situacao, Unidade unidade, Date dataInicio, Date dataFim, Long tenantId) {
		/*
		 SELECT count(*)
			FROM svsa_salto.IndicadorFamilia ifa
				INNER JOIN svsa_salto.Familia fa ON ifa.codigo_familia = fa.codigo
				INNER JOIN svsa_salto.IndProtecao ip ON ip.codigo = ifa.codigo_indicador
				INNER JOIN svsa_salto.SituacaoProtecao sp ON sp.codigo = ip.codigo_situacao
                INNER JOIN svsa_salto.Prontuario p on p.codigo =  ifa.codigo_familia
                INNER JOIN svsa_salto.Unidade u ON u.codigo = p.codigo_unidade
			WHERE sp.codigo = 1 and ifa.dataCriacao between "2021-01-01 00:00:00" and "2021-12-31 00:00:00"
            and u.codigo = 1;
		 */

		Query q = manager.createQuery("Select count(ifa) from IndicadorFamilia ifa  "
				+ "INNER JOIN Familia fa ON fa.codigo = ifa.familia.codigo  "
				+ "INNER JOIN Prontuario p ON p.codigo = ifa.familia.prontuario.codigo "
				+ "INNER JOIN Unidade u ON u.codigo = p.unidade.codigo "
				+ "INNER JOIN IndProtecao ip ON ip.codigo = ifa.indProtecao.codigo "
				+ "INNER JOIN SituacaoProtecao sp ON sp.codigo = ip.situacaoProtecao.codigo "
				+ "WHERE sp.codigo = :situacao "
				+ "and ifa.tenant_id = :tenantId "
				+ "and u.codigo = :unidade "
				+ "and ifa.dataCriacao between :dataInicio and :dataFim");
		q.setParameter("situacao", situacao.getCodigo());
		q.setParameter("tenantId", tenantId);
		q.setParameter("dataInicio", dataInicio, TemporalType.TIMESTAMP);
		q.setParameter("dataFim", dataFim, TemporalType.TIMESTAMP);
		q.setParameter("unidade", unidade.getCodigo());
		Long qde = (Long) q.getSingleResult();
		
		return qde;
	}
	
	public Long buscarQdeIndProtecaoUnidade(IndProtecao ind, Unidade unidade, Date dataInicio, Date dataFim, Long tenantId) {

		Query q = manager.createQuery("Select count(ifa) from IndicadorFamilia ifa  "
				+ "INNER JOIN Familia fa ON fa.codigo = ifa.familia.codigo  "
				+ "INNER JOIN Prontuario p ON p.codigo = ifa.familia.prontuario.codigo "
				+ "INNER JOIN Unidade u ON u.codigo = p.unidade.codigo "
				+ "INNER JOIN IndProtecao ip ON ip.codigo = ifa.indProtecao.codigo "
				+ "WHERE ip.codigo = :ind "
				+ "and ifa.tenant_id = :tenantId "
				+ "and u.codigo = :unidade "
				+ "and ifa.dataCriacao between :dataInicio and :dataFim");
		q.setParameter("ind", ind.getCodigo());
		q.setParameter("tenantId", tenantId);
		q.setParameter("dataInicio", dataInicio, TemporalType.TIMESTAMP);
		q.setParameter("dataFim", dataFim, TemporalType.TIMESTAMP);
		q.setParameter("unidade", unidade.getCodigo());
		Long qde = (Long) q.getSingleResult();
		
		return qde;
	}
	
}