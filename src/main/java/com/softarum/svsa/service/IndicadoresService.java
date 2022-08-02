package com.softarum.svsa.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softarum.svsa.dao.IndicadoresDAO;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Familia;
import com.softarum.svsa.modelo.IndProtecao;
import com.softarum.svsa.modelo.IndicadorFamilia;
import com.softarum.svsa.modelo.SituacaoProtecao;
import com.softarum.svsa.modelo.to.IndicadorTO;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 *
 */
public class IndicadoresService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(IndicadoresService.class);
	 
	
	@Inject
	private IndicadoresDAO indicadoresDAO;

	
	public void salvar(List<IndProtecao> indicadores, Familia familia, Long tenantId) throws NegocioException {		

		List<IndicadorFamilia> indFamilia = new ArrayList<>();
		for(IndProtecao i : indicadores) {
			IndicadorFamilia ind = new IndicadorFamilia(familia, i);
			ind.setTenant_id(tenantId);
			indFamilia.add(ind);
		}
		this.indicadoresDAO.salvar(indFamilia);
	}	

	
	public List<SituacaoProtecao> buscarSituacoes() {
		
		log.info("Carregando indicadores proteção...");       
		return indicadoresDAO.buscarSituacoes();		
	}	
	
	public List<IndProtecao> buscarIndicadoresProtecao(Familia familia, Long tenantId) {
		
		List<IndicadorFamilia> inds = indicadoresDAO.buscarIndicadoresProtecao(familia, tenantId);
		List<IndProtecao> indicadores = new ArrayList<>();
		
		for(IndicadorFamilia i : inds) {
			indicadores.add(i.getIndProtecao());
		}
		return indicadores;
	}


	/*
	 * Geral
	 */
	// 12 meses do ano
	public List<IndicadorTO> buscarSituacoesMensalTO(SituacaoProtecao sc, Long tenantId) {
		
		LocalDate data = LocalDate.now();
		LocalDate dataFim;		

		List<IndicadorTO> indsTO = new ArrayList<>();

				
		for(int mes = 1; mes <= 12; mes ++) {

			IndicadorTO to = new IndicadorTO();
			to = new IndicadorTO();
			to.setDescricao(sc.getDescricao());
			to.setMes(mes);
			to.setAno(data.getYear());
			// busca qde do mes
			LocalDate dataInicio = LocalDate.of(data.getYear(), mes, 1);
			
			dataFim = LocalDate.of(data.getYear(), mes, dataInicio.lengthOfMonth()); //dataInicio.lengthOfMonth()				
			
			log.debug("data inicio: " + dataInicio);
			log.debug("data fim: " + dataFim);
			to.setQde(indicadoresDAO.buscarQdeSituacao(sc, DateUtils.asDate(dataInicio), DateUtils.asDate(dataFim), tenantId));	
			indsTO.add(to);
		}
	
		return indsTO;
	}
	// ultimos 5 anos
	public List<IndicadorTO> buscarSituacoesAnualTO(SituacaoProtecao sc, Long tenantId) {
		
		/*
		 * Recuperar situações e quantidade de cada situacao ultimos 5 anos*/		

		List<IndicadorTO> indsTO = new ArrayList<>();	
			
		LocalDate data = LocalDate.now();
		LocalDate dataInicio = LocalDate.of(data.getYear(), 1, 1).minusYears(4);
		
		for(int i = 0; i < 5; i++) {
			
			LocalDate dataFim = dataInicio.plusYears(1);
			
			IndicadorTO to = new IndicadorTO();
			to = new IndicadorTO();
			to.setDescricao(sc.getDescricao());
			to.setAno(dataInicio.getYear());

			log.debug("data inicio: " + dataInicio);
			log.debug("data fim: " + dataFim);
			to.setQde(indicadoresDAO.buscarQdeSituacao(sc, DateUtils.asDate(dataInicio), DateUtils.asDate(dataFim), tenantId));	
			indsTO.add(to);
			
			dataInicio = dataFim;
		}
		
		return indsTO;
	}


	/*
	 * Geral Por Situação
	 */
	// 12 meses do ano
	public List<IndicadorTO> buscarIndsProtecaoMensalTO(IndProtecao ip, Long tenantId) {
		
		LocalDate data = LocalDate.now();
		LocalDate dataFim;		

		List<IndicadorTO> indsTO = new ArrayList<>();

				
		for(int mes = 1; mes <= 12; mes ++) {

			IndicadorTO to = new IndicadorTO();
			to = new IndicadorTO();
			to.setDescricao(ip.getDescricao());
			to.setMes(mes);
			to.setAno(data.getYear());
			// busca qde do mes
			LocalDate dataInicio = LocalDate.of(data.getYear(), mes, 1);
			
			dataFim = LocalDate.of(data.getYear(), mes, dataInicio.lengthOfMonth()); //dataInicio.lengthOfMonth()				
			
			log.debug("data inicio: " + dataInicio);
			log.debug("data fim: " + dataFim);
			to.setQde(indicadoresDAO.buscarQdeIndProtecao(ip, DateUtils.asDate(dataInicio), DateUtils.asDate(dataFim), tenantId));	
			indsTO.add(to);
		}
	
		return indsTO;
	}
	
	// ultimos 5 anos
	public List<IndicadorTO> buscarIndsProtecaoAnualTO(IndProtecao ip, Long tenantId) {
		
		/*
		 * Recuperar situações e quantidade de cada situacao ultimos 5 anos*/		

		List<IndicadorTO> indsTO = new ArrayList<>();	
			
		LocalDate data = LocalDate.now();
		LocalDate dataInicio = LocalDate.of(data.getYear(), 1, 1).minusYears(4);
		
		for(int i = 0; i < 5; i++) {
			
			LocalDate dataFim = dataInicio.plusYears(1);
			
			IndicadorTO to = new IndicadorTO();
			to = new IndicadorTO();
			to.setDescricao(ip.getDescricao());
			to.setAno(dataInicio.getYear());

			log.debug("data inicio: " + dataInicio);
			log.debug("data fim: " + dataFim);
			to.setQde(indicadoresDAO.buscarQdeIndProtecao(ip, DateUtils.asDate(dataInicio), DateUtils.asDate(dataFim), tenantId));	
			indsTO.add(to);
			
			dataInicio = dataFim;
		}
		
		return indsTO;
	}
	
	
	/*
	 * Geral por unidade
	 */
	// 12 meses do ano
	public List<IndicadorTO> buscarSituacoesUnidadeMensalTO(SituacaoProtecao sc, Unidade unidade, Long tenantId) {
		
		LocalDate data = LocalDate.now();
		LocalDate dataFim;		

		List<IndicadorTO> indsTO = new ArrayList<>();

				
		for(int mes = 1; mes <= 12; mes ++) {

			IndicadorTO to = new IndicadorTO();
			to = new IndicadorTO();
			to.setDescricao(sc.getDescricao());
			to.setMes(mes);
			to.setAno(data.getYear());
			// busca qde do mes
			LocalDate dataInicio = LocalDate.of(data.getYear(), mes, 1);
			
			dataFim = LocalDate.of(data.getYear(), mes, dataInicio.lengthOfMonth()); //dataInicio.lengthOfMonth()				
			
			log.debug("data inicio: " + dataInicio);
			log.debug("data fim: " + dataFim);
			to.setQde(indicadoresDAO.buscarQdeSituacaoUnidade(sc, unidade, DateUtils.asDate(dataInicio), DateUtils.asDate(dataFim), tenantId));	
			indsTO.add(to);
		}
	
		return indsTO;
	}
	
	// ultimos 5 anos
		public List<IndicadorTO> buscarSituacoesUnidadeAnualTO(SituacaoProtecao sc, Unidade unidade, Long tenantId) {
			
			/*
			 * Recuperar situações e quantidade de cada situacao ultimos 5 anos*/		

			List<IndicadorTO> indsTO = new ArrayList<>();	
				
			LocalDate data = LocalDate.now();
			LocalDate dataInicio = LocalDate.of(data.getYear(), 1, 1).minusYears(4);
			
			for(int i = 0; i < 5; i++) {
				
				LocalDate dataFim = dataInicio.plusYears(1);
				
				IndicadorTO to = new IndicadorTO();
				to = new IndicadorTO();
				to.setDescricao(sc.getDescricao());
				to.setAno(dataInicio.getYear());

				log.debug("data inicio: " + dataInicio);
				log.debug("data fim: " + dataFim);
				to.setQde(indicadoresDAO.buscarQdeSituacaoUnidade(sc, unidade, DateUtils.asDate(dataInicio), DateUtils.asDate(dataFim), tenantId));	
				indsTO.add(to);
				
				dataInicio = dataFim;
			}
			
			return indsTO;
		}
	
		/*
		 * Unidade por Situacao
		 */
		// 12 meses do ano
		public List<IndicadorTO> buscarIndsProtecaoUnidadeMensalTO(IndProtecao ip, Unidade unidade, Long tenantId) {
			
			LocalDate data = LocalDate.now();
			LocalDate dataFim;		

			List<IndicadorTO> indsTO = new ArrayList<>();

					
			for(int mes = 1; mes <= 12; mes ++) {

				IndicadorTO to = new IndicadorTO();
				to = new IndicadorTO();
				to.setDescricao(ip.getDescricao());
				to.setMes(mes);
				to.setAno(data.getYear());
				// busca qde do mes
				LocalDate dataInicio = LocalDate.of(data.getYear(), mes, 1);
				
				dataFim = LocalDate.of(data.getYear(), mes, dataInicio.lengthOfMonth()); //dataInicio.lengthOfMonth()				
				
				log.debug("data inicio: " + dataInicio);
				log.debug("data fim: " + dataFim);
				to.setQde(indicadoresDAO.buscarQdeIndProtecaoUnidade(ip, unidade, DateUtils.asDate(dataInicio), DateUtils.asDate(dataFim), tenantId));	
				indsTO.add(to);
			}
		
			return indsTO;
		}
		
		// ultimos 5 anos
		public List<IndicadorTO> buscarIndsProtecaoUnidadeAnualTO(IndProtecao ip, Unidade unidade, Long tenantId) {
			
			/*
			 * Recuperar situações e quantidade de cada situacao ultimos 5 anos*/		

			List<IndicadorTO> indsTO = new ArrayList<>();	
				
			LocalDate data = LocalDate.now();
			LocalDate dataInicio = LocalDate.of(data.getYear(), 1, 1).minusYears(4);
			
			for(int i = 0; i < 5; i++) {
				
				LocalDate dataFim = dataInicio.plusYears(1);
				
				IndicadorTO to = new IndicadorTO();
				to = new IndicadorTO();
				to.setDescricao(ip.getDescricao());
				to.setAno(dataInicio.getYear());

				log.debug("data inicio: " + dataInicio);
				log.debug("data fim: " + dataFim);
				to.setQde(indicadoresDAO.buscarQdeIndProtecaoUnidade(ip, unidade, DateUtils.asDate(dataInicio), DateUtils.asDate(dataFim), tenantId));	
				indsTO.add(to);
				
				dataInicio = dataFim;
			}
			
			return indsTO;
		}
		
}
