package com.softarum.svsa.util.jobs;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Mes;
import com.softarum.svsa.modelo.to.rma.RmaCreasTO;
import com.softarum.svsa.modelo.to.rma.RmaTO;
import com.softarum.svsa.service.RMAService;
import com.softarum.svsa.service.RelatorioRMACrasService;
import com.softarum.svsa.service.RelatorioRMACreasService;
import com.softarum.svsa.service.UnidadeService;

import lombok.extern.log4j.Log4j;

@Log4j
public class RmaJobTask implements Job {
	
	private static final EntityManager manager = Persistence.createEntityManagerFactory("svsaPU").createEntityManager();
	private Integer ano;
	private Mes mes;
	
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private RelatorioRMACrasService rmaCrasService;
	@Inject
	private RelatorioRMACreasService rmaCreasService;
	@Inject
	private RMAService rmaService;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		// fechamento dos RMAs
		
		definirDatas();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy – hh:mm:ss");
		log.info("Fechando RMAs..." + dateFormat.format( new Date() ));
		
		inicializarDAO();
		
		fecharRmaCras();
		fecharRmaCreas();
	}
	
	private void inicializarDAO() {	
		
		unidadeService.setManager(manager);
		rmaCrasService.setManager(manager);
		rmaCreasService.setManager(manager);
		rmaService.setManager(manager);
		
		
	}
	
	private void fecharRmaCras() {		
		
		try {
			
			List<Unidade> unidades = unidadeService.buscarCRAS();
			
			for(Unidade uni : unidades) {
				
				log.info("fechando RMA CRAS do mes = " + mes + "  unidade = " + uni.getNome());
				RmaTO rma = rmaCrasService.gerarRelatorioRMA(uni, ano, mes, uni.getTenant_id());				
				rmaService.salvarCras(rma, uni.getTenant_id());		
				log.info("RMA CRAS do mês " + mes + " fechado com sucesso! "  + "  unidade = " + uni.getNome()) ;
			}	
		} catch (Exception e) {
			log.info("Problemas no fechamento dos Rmas CRAS.");
			e.printStackTrace();
		}
	}
	
	private void fecharRmaCreas() {	
		
		try {
			List<Unidade> unidades = unidadeService.buscarCREAS();
			
			for(Unidade uni : unidades) {		
				
				log.info("fechando RMA CREAS do mes = " + mes + "  unidade = " + uni.getNome());
				RmaCreasTO rma = rmaCreasService.gerarRelatorioRMA(uni, ano, mes, uni.getTenant_id());				
				rmaService.salvarCreas(rma, uni.getTenant_id());		
				log.info("RMA CREAS do mês " + mes + " fechado com sucesso! "  + "  unidade = " + uni.getNome()) ;
			}
				
		} catch (Exception e) {
			log.info("Problemas no fechamento dos Rmas CREAS.");
			e.printStackTrace();
		}
	}
	
	private void definirDatas() {
		/*
		 * A geração de RMA ocorre todo dia 10 de cada mes subsequente do qual deve ser gerado o RMA
		 */
		LocalDate data = LocalDate.now();
		
		/* se mes for janeiro usa dezembro do ano anterior */
		if(data.getMonthValue() == 1) {
			ano = data.getYear() - 1;
			mes = Mes.porCodigo(12);
		}
		else {
			ano = data.getYear();
			mes = Mes.porCodigo(data.getMonthValue() - 1);			
		}
		
		log.info("Mes/ano fechamento = " + mes.name() + "/" + ano);
	}
}
