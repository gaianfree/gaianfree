package com.softarum.svsa.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.softarum.svsa.dao.RmaPopDAO;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Mes;
import com.softarum.svsa.modelo.enums.Sexo;
import com.softarum.svsa.modelo.to.rma.Bloco1PopTO;
import com.softarum.svsa.modelo.to.rma.Bloco2PopTO;
import com.softarum.svsa.modelo.to.rma.PessoaTO;
import com.softarum.svsa.modelo.to.rma.RmaPopTO;
import com.softarum.svsa.modelo.to.rma.VitimaCreasTO;
import com.softarum.svsa.util.DateUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
@Getter
@Setter
public class RelatorioRMAPopService implements Serializable {

	private static final long serialVersionUID = 1L;	
	
	private Date dataIni;
	private Date dataFim;	
	private RmaPopTO rma;
	private Long tenantId;
	
	@Inject
	private RmaPopDAO rmaPopDAO;

	public RmaPopTO gerarRelatorioRMA(Unidade unidade, Integer ano, Mes mesReferencia, Long tenantId) {
		
		//TODO
		
		this.tenantId = tenantId;
		/*
		 * Seta parametros de consulta
		 */
		rma = new RmaPopTO();
		rma = setDados(rma, unidade, ano, mesReferencia);
		
		/*
		 * Realiza as consultas
		 */
		rma.setBloco1(carregarBloco1());
		rma.setBloco2(carregarBloco2());

		
		return rma;
	}
	
	
	
	/*
	 * Bloco I - Serviço de Proteção e Atendimento Especializado a Famílias e Indivíduos - PAEFI
	 * 
	 */
	
	
	private Bloco1PopTO carregarBloco1() {
		
		Bloco1PopTO bloco1 = new Bloco1PopTO();
		
		bloco1.setA1vitimasFem(processarVitimasGeral( rmaPopDAO.buscarA1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setA1vitimasMasc(processarVitimasGeral( rmaPopDAO.buscarA1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setA1(bloco1.getA1vitimasFem().getTotal() + bloco1.getA1vitimasMasc().getTotal());
	
		bloco1.setB1(rmaPopDAO.buscarB1(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		bloco1.setB2(rmaPopDAO.buscarB2(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		bloco1.setB3(rmaPopDAO.buscarB3(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		bloco1.setB4(rmaPopDAO.buscarB4(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		
		bloco1.setC1(rmaPopDAO.buscarC1(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		bloco1.setC2(rmaPopDAO.buscarC2(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		
		bloco1.setD1(rmaPopDAO.buscarD1(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		
		return bloco1;		
	}
	
	
	/*
	 * Bloco II - Serviço Especializado em Abordagem Social no Centro POP. 
	 */
	
	
	
	private Bloco2PopTO carregarBloco2() {
		
		Bloco2PopTO bloco2 = new Bloco2PopTO();
			
		bloco2.setE1vitimasFem(processarVitimasGeral( rmaPopDAO.buscarE1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco2.setE1vitimasMasc(processarVitimasGeral( rmaPopDAO.buscarE1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco2.setE1(bloco2.getE1vitimasFem().getTotal() + bloco2.getE1vitimasMasc().getTotal());
		
		bloco2.setE2(rmaPopDAO.buscarE2(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		bloco2.setE3(rmaPopDAO.buscarE3(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		bloco2.setE4(rmaPopDAO.buscarE4(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		bloco2.setE5(rmaPopDAO.buscarE5(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		bloco2.setE6(rmaPopDAO.buscarE6(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		bloco2.setE7(rmaPopDAO.buscarE7(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		
		bloco2.setF1(rmaPopDAO.buscarF1(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		
		return bloco2;		
	}
	
	
	
	
	/* Comum para todos */
	
	
	
	private VitimaCreasTO processarVitimasGeral(List<PessoaTO> pessoas, Sexo sexo) {
				
		VitimaCreasTO vitima = new VitimaCreasTO();
		
		int  i2 = 0; //idade0a12
		int  i3 = 0; //idade13a17
		int  i4 = 0; //idade18a39
		int  i5 = 0; //idade40a59
		int  i6 = 0; //idade60mais
		
		for(PessoaTO pessoa : pessoas) {
			
			if(pessoa.getIdade() > 0 && pessoa.getIdade() < 13) {
				i2++;
			}
			else if(pessoa.getIdade() < 18) {
				i3++;
			}
			else if(pessoa.getIdade() < 40) {
				i4++;
			}	
			else if(pessoa.getIdade() < 60) {
				i5++;
			}			
			else {
				i6++;
			}
		}
		vitima.setSexo(sexo.name());
		vitima.setIdade0a6(i2);		
		vitima.setIdade0a12(i3);		
		vitima.setIdade13a17(i4);
		vitima.setIdade18a59(i5);
		vitima.setIdade60mais(i6);		
		vitima.setTotal(i2+i3+i4+i5+i6);		
		
		log.debug("Vitimas " + sexo.name() + ": " + vitima.getTotal() + " ----> Qde pessoas retornadas do banco: " + pessoas.size());
		return vitima;
	}
	
	
	private RmaPopTO setDados(RmaPopTO rma, Unidade unidade, Integer anoReferencia, Mes mesReferencia) {
		
		log.info("RMA unidade = " + unidade.getNome());
		rma.setUnidade(unidade);
		
		LocalDate dataCorrente = LocalDate.now();  // pega ano corrente
		
		if(anoReferencia == null) {		
			
			if(mesReferencia == null) {
				rma.setMesAnoReferencia(dataCorrente.getMonth().name() + "/" + dataCorrente.getYear());
				setDataIni(DateUtils.asDate(LocalDate.of(dataCorrente.getYear(), dataCorrente.getMonthValue(), 1)));
				setDataFim(DateUtils.asDate(LocalDate.of(dataCorrente.getYear(), dataCorrente.getMonthValue(), dataCorrente.lengthOfMonth())));
				log.info(getDataIni() + " - " + getDataFim());
				
			}
			else {
				rma.setMesAnoReferencia(mesReferencia.name() + "/" + dataCorrente.getYear());
				LocalDate dt = LocalDate.of(dataCorrente.getYear(), mesReferencia.ordinal()+1, 1);
				setDataIni(DateUtils.asDate(dt));
				setDataFim(DateUtils.asDate(LocalDate.of(dataCorrente.getYear(), dt.getMonthValue(), dt.lengthOfMonth())));
				log.info(getDataIni() + " - " + getDataFim());
				
			}
		} else {			
			
			if(mesReferencia == null) {
				rma.setMesAnoReferencia(dataCorrente.getMonth().name() + "/" + anoReferencia);
				setDataIni(DateUtils.asDate(LocalDate.of(anoReferencia, dataCorrente.getMonthValue(), 1)));
				setDataFim(DateUtils.asDate(LocalDate.of(anoReferencia, dataCorrente.getMonthValue(), dataCorrente.lengthOfMonth())));
				log.info(getDataIni() + " - " + getDataFim());
				
			}
			else {
				rma.setMesAnoReferencia(mesReferencia.name() + "/" + anoReferencia);
				LocalDate dt = LocalDate.of(anoReferencia, mesReferencia.ordinal()+1, 1);
				setDataIni(DateUtils.asDate(dt));
				setDataFim(DateUtils.asDate(LocalDate.of(anoReferencia, dt.getMonthValue(), dt.lengthOfMonth())));
				log.info(getDataIni() + " - " + getDataFim());
				
			}
		}
		
		return rma;
	}




	public void setManager(EntityManager manager) {
		rmaPopDAO.setEntityManager(manager);
		
	}	
	
}