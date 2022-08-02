package com.softarum.svsa.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.softarum.svsa.dao.RmaCreasDAO;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Mes;
import com.softarum.svsa.modelo.enums.Sexo;
import com.softarum.svsa.modelo.to.rma.Bloco1CreasTO;
import com.softarum.svsa.modelo.to.rma.Bloco2CreasTO;
import com.softarum.svsa.modelo.to.rma.Bloco3CreasTO;
import com.softarum.svsa.modelo.to.rma.PessoaTO;
import com.softarum.svsa.modelo.to.rma.RmaCreasTO;
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
public class RelatorioRMACreasService implements Serializable {

	private static final long serialVersionUID = 1L;	
	
	private Date dataIni;
	private Date dataFim;	
	private RmaCreasTO rma;
	private Long tenantId;
	
	@Inject
	private RmaCreasDAO rmaCreasDAO;

	public RmaCreasTO gerarRelatorioRMA(Unidade unidade, Integer ano, Mes mesReferencia, Long tenantId) {
		
		
		this.tenantId = tenantId;
		/*
		 * Seta parametros de consulta
		 */
		rma = new RmaCreasTO();		
		rma = setDados(rma, unidade, ano, mesReferencia);
		
		/*
		 * Realiza as consultas
		 */
		rma.setBloco1(carregarBloco1());
		rma.setBloco2(carregarBloco2());
		rma.setBloco3(carregarBloco3());
		
		
		return rma;
	}
	
	
	
	/*
	 * Bloco I - Serviço de Proteção e Atendimento Especializado a Famílias e Indivíduos - PAEFI
	 * 
	 */
	
	
	private Bloco1CreasTO carregarBloco1() {
		
		Bloco1CreasTO bloco1 = new Bloco1CreasTO();
		//A.1. Total de casos (famílias ou indivíduos) em acompanhamento pelo PAEFI
		bloco1.setA1(rmaCreasDAO.buscarA1(rma.getUnidade(), getDataFim(), tenantId).intValue());
		//A.2.  Novos casos (famílias ou indivíduos) inseridos no acompanhamento do PAEFI durante o mês de referência
		bloco1.setA2(rmaCreasDAO.buscarA2(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		
		//B.1. Famílias beneficiárias do Programa Bolsa Família
		bloco1.setB1(rmaCreasDAO.buscarB1(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.2. Famílias com membros beneficiários do BPC
		bloco1.setB2(rmaCreasDAO.buscarB2(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.3. Famílias com crianças ou adolescentes em situação de trabalho infantil
		bloco1.setB3(rmaCreasDAO.buscarB3(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.4. Famílias com crianças ou adolescentes em Serviços de Acolhimento
		bloco1.setB4(rmaCreasDAO.buscarB4(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.5. Famílias cuja situação de violência/ violação esteja associada ao uso abusivo de substâncias psicoativas
		bloco1.setB5(rmaCreasDAO.buscarB5(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());		
		//B.7.  Famílias com adolescente em cumprimento de Medidas Socioeducativas em meio aberto
		bloco1.setB7(rmaCreasDAO.buscarB7(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());		
		/* 
		 * As Situacao de Violencia (de b1 a b7 devem ser computadas. Mas PIA são computadas as pessoas
		 *  e não a familia, pois pode haver mais de uma pessoa vitimada na familia)
		 */		
		//B.6. Quantidade de pessoas vitimadas, que ingressaram no PAEFI, durante o mês de referência (apenas para os novos casos)
		bloco1.setB6vitimasFem(processarVitimasGeral( rmaCreasDAO.buscarB6( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setB6vitimasMasc(processarVitimasGeral( rmaCreasDAO.buscarB6( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setB6(bloco1.getB6vitimasFem().getTotal() + bloco1.getB6vitimasMasc().getTotal());
		/*
		 * Processamento dos itens C a I
		 * 
		 * Do item 'C' ao item 'I' devem ser informadas as situações de violência ou violações de direitos 
		 * identificadas entre as pessoas que ingressaram no PAEFI no mês de referência (novos casos). 
		 * Uma mesma pessoa pode ter sido vítima de múltiplas violências/violações.
		 */
		
		processarItemC(bloco1);
		processarItemD(bloco1);
		processarItemE(bloco1);
		processarItemF(bloco1);
		processarItemG(bloco1);
		bloco1.setH1( rmaCreasDAO.buscarH1( rma.getUnidade(), getDataIni(), getDataFim(), tenantId ).intValue() );
		processarItemI(bloco1);
		
		return bloco1;		
	}


	/*
	 * Item C do Bloco 1
	 */
	private Bloco1CreasTO processarItemC(Bloco1CreasTO bloco1) {
		
		//C.1. Crianças ou adolescentes vítimas de violência intrafamiliar (física ou psicológica)
		/* ArrayList<VitimaCreasTO> vitimas = new ArrayList<>();
		vitimas.add(processarVitimas( rmaCreasDAO.buscarC1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO, tenantId ), Sexo.FEMININO) );
		vitimas.add(processarVitimas( rmaCreasDAO.buscarC1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO, tenantId ), Sexo.MASCULINO));
		bloco1.setC1vitimas(vitimas);
		bloco1.setC1(bloco1.getC1vitimas().get(0).getTotal() + bloco1.getC1vitimas().get(1).getTotal()); */
		
		bloco1.setC1vitimasFem(processarVitimasCria( rmaCreasDAO.buscarC1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setC1vitimasMasc(processarVitimasCria( rmaCreasDAO.buscarC1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setC1(bloco1.getC1vitimasFem().getTotal() + bloco1.getC1vitimasMasc().getTotal());
		
				
		//C.2. Crianças ou adolescentes vítimas de abuso sexual
		bloco1.setC2vitimasFem(processarVitimasCria( rmaCreasDAO.buscarC2( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setC2vitimasMasc(processarVitimasCria( rmaCreasDAO.buscarC2( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setC2(bloco1.getC2vitimasFem().getTotal() + bloco1.getC2vitimasMasc().getTotal());
		
		printVitimas(bloco1.getC2vitimasFem(), bloco1.getC2vitimasMasc());
		
		//C.3. Crianças ou adolescentes vítimas de exploração sexual
		bloco1.setC3vitimasFem(processarVitimasCria( rmaCreasDAO.buscarC3( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setC3vitimasMasc(processarVitimasCria( rmaCreasDAO.buscarC3( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setC3(bloco1.getC3vitimasFem().getTotal() + bloco1.getC3vitimasMasc().getTotal());
		
		
		//C.4. Crianças ou adolescentes vítimas de negligência ou abandono
		bloco1.setC4vitimasFem(processarVitimasCria( rmaCreasDAO.buscarC4( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setC4vitimasMasc(processarVitimasCria( rmaCreasDAO.buscarC4( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setC4(bloco1.getC4vitimasFem().getTotal() + bloco1.getC4vitimasMasc().getTotal());
		
		//C.5. Crianças ou adolescentes em situação de trabalho infantil (até 15 anos)
		bloco1.setC5vitimasFem(processarVitimas15( rmaCreasDAO.buscarC5( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setC5vitimasMasc(processarVitimas15( rmaCreasDAO.buscarC5( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setC5(bloco1.getC5vitimasFem().getTotal() + bloco1.getC5vitimasMasc().getTotal());
		
		
		//C.6. não consta do manual do RMA
		bloco1.setC6vitimasFem(processarVitimasGeral( rmaCreasDAO.buscarC6( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setC6vitimasMasc(processarVitimasGeral( rmaCreasDAO.buscarC6( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setC6(bloco1.getC6vitimasFem().getTotal() + bloco1.getC6vitimasMasc().getTotal());
		
		return bloco1;
	}
	
	private void printVitimas(VitimaCreasTO c1vitimasFem, VitimaCreasTO c1vitimasMasc) {
		log.debug(c1vitimasMasc.getSexo());
		log.debug(c1vitimasMasc.getIdade0a6());
		log.debug(c1vitimasMasc.getIdade7a12());
		log.debug("0-12: " + c1vitimasMasc.getIdade0a12());
		log.debug(c1vitimasMasc.getIdade13a17());
		log.debug(c1vitimasMasc.getIdade18a59());
		log.debug(c1vitimasMasc.getIdade60mais());
		log.debug(c1vitimasMasc.getTotal());
		log.debug("----------------");
		log.debug(c1vitimasFem.getSexo());
		log.debug(c1vitimasFem.getIdade0a6());
		log.debug(c1vitimasFem.getIdade7a12());
		log.debug("0-12: " + c1vitimasFem.getIdade0a12());
		log.debug(c1vitimasFem.getIdade13a17());
		log.debug(c1vitimasFem.getIdade18a59());
		log.debug(c1vitimasFem.getIdade60mais());
		log.debug(c1vitimasFem.getTotal());
		
		log.debug("Total: " + (c1vitimasFem.getTotal() + c1vitimasMasc.getTotal()));
		
	}



	/*
	 * Item D do Bloco 1
	 */
	private Bloco1CreasTO processarItemD(Bloco1CreasTO bloco1) {
		
		//D.1. Pessoas idosas vítimas de violência intrafamiliar (física, psicológica ou sexual)
		bloco1.setD1vitimasFem(processarVitimasIdoso( rmaCreasDAO.buscarD1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setD1vitimasMasc(processarVitimasIdoso( rmaCreasDAO.buscarD1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setD1(bloco1.getD1vitimasFem().getTotal() + bloco1.getD1vitimasMasc().getTotal());
		
		
		printVitimas(bloco1.getD1vitimasFem(), bloco1.getD1vitimasMasc());
		
		//D.2. Pessoas idosas vítimas de negligência ou abandono
		bloco1.setD2vitimasFem(processarVitimasIdoso( rmaCreasDAO.buscarD2( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setD2vitimasMasc(processarVitimasIdoso( rmaCreasDAO.buscarD2( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setD2(bloco1.getD2vitimasFem().getTotal() + bloco1.getD2vitimasMasc().getTotal());  
		
		return bloco1;
	}
	
	/*
	 * Item E do Bloco 1
	 */
	private Bloco1CreasTO processarItemE(Bloco1CreasTO bloco1) {
		
		//E.1. Pessoas com deficiência vítimas de violência intrafamiliar (física, psicológica ou sexual)
		bloco1.setE1vitimasFem(processarVitimasGeral( rmaCreasDAO.buscarE1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setE1vitimasMasc(processarVitimasGeral( rmaCreasDAO.buscarE1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setE1(bloco1.getE1vitimasFem().getTotal() + bloco1.getE1vitimasMasc().getTotal());  
		
		
		//E.2. Pessoas com deficiência vítimas de negligência ou abandono
		bloco1.setE2vitimasFem(processarVitimasGeral( rmaCreasDAO.buscarE2( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setE2vitimasMasc(processarVitimasGeral( rmaCreasDAO.buscarE2( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setE2(bloco1.getE2vitimasFem().getTotal() + bloco1.getE2vitimasMasc().getTotal());  
		
		
		return bloco1;
	}
	
	/*
	 * Item F do Bloco 1
	 */
	private Bloco1CreasTO processarItemF(Bloco1CreasTO bloco1) {
		
		//F.1. Mulheres adultas (18 a 59 anos) vítimas de violência intrafamiliar (física, psicológica ou sexual)
		bloco1.setF1vitimasFem(processarVitimasMulher( rmaCreasDAO.buscarF1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setF1(bloco1.getF1vitimasFem().getTotal());  
		
		return bloco1;
	}
	
	/*
	 * Item G do Bloco 1
	 */
	private Bloco1CreasTO processarItemG(Bloco1CreasTO bloco1) {
		
		//G.1. Pessoas vítimas de tráficos de seres humanos
		bloco1.setG1vitimasFem(processarVitimasGeral( rmaCreasDAO.buscarG1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setG1vitimasMasc(processarVitimasGeral( rmaCreasDAO.buscarG1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setG1(bloco1.getG1vitimasFem().getTotal() + bloco1.getG1vitimasMasc().getTotal());  
		
		return bloco1;
	}
	
	/*
	 * Item I do Bloco 1
	 */
	private Bloco1CreasTO processarItemI(Bloco1CreasTO bloco1) {
		
		//I.1. Pessoas em situação de rua
		bloco1.setI1vitimasFem(processarVitimasGeral( rmaCreasDAO.buscarI1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO , tenantId), Sexo.FEMININO));
		bloco1.setI1vitimasMasc(processarVitimasGeral( rmaCreasDAO.buscarI1( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO , tenantId), Sexo.MASCULINO));
		bloco1.setI1(bloco1.getI1vitimasFem().getTotal() + bloco1.getI1vitimasMasc().getTotal());  
		
		return bloco1;
	}
	
	/*para criancas */
	private VitimaCreasTO processarVitimasCria(List<PessoaTO> pessoas, Sexo sexo) {
				
		VitimaCreasTO vitima = new VitimaCreasTO();
		
		int  i1 = 0; //idade0a6		
		int  i3 = 0; //idade7a12
		int  i4 = 0; //idade13a17
				
		for(PessoaTO pessoa : pessoas) {
			
			if(pessoa.getIdade() > 0 && pessoa.getIdade() < 7) {
				i1++;
			}
			else if(pessoa.getIdade() > 6 && pessoa.getIdade() < 13) {
				i3++;
			}
			else if(pessoa.getIdade() < 18) {
				i4++;
			}			
			
		}
		vitima.setSexo(sexo.name());
		vitima.setIdade0a6(i1);		
		vitima.setIdade0a12(i1 + i3);		
		vitima.setIdade7a12(i3);		
		vitima.setIdade13a17(i4);	
		
		vitima.setTotal(i1+i3+i4);		
		
		log.debug("Vitimas " + sexo.name() + ": " + vitima.getTotal() + " ----> Qde pessoas retornadas do banco: " + pessoas.size());
		return vitima;
	}
	
	/*para adolescentes */
	private VitimaCreasTO processarVitimas15(List<PessoaTO> pessoas, Sexo sexo) {
				
		VitimaCreasTO vitima = new VitimaCreasTO();
		
		int  i2 = 0; //idade0a12
		int  i4 = 0; //idade13a17
		
		for(PessoaTO pessoa : pessoas) {
			
			if(pessoa.getIdade() > 0 && pessoa.getIdade() < 13) {
				i2++;
			}			
			else if(pessoa.getIdade() < 18) {
				i4++;
			}			
			
		}
		vitima.setSexo(sexo.name());
		vitima.setIdade0a12(i2);		
		vitima.setIdade13a17(i4);		
	
		vitima.setTotal(i2+i4);		
		
		log.debug("Vitimas " + sexo.name() + ": " + vitima.getTotal() + " ----> Qde pessoas retornadas do banco: " + pessoas.size());
		return vitima;
	}
	/* Comum para todos */
	private VitimaCreasTO processarVitimasGeral(List<PessoaTO> pessoas, Sexo sexo) {
				
		VitimaCreasTO vitima = new VitimaCreasTO();
		
		int  i2 = 0; //idade0a12
		int  i4 = 0; //idade13a17
		int  i5 = 0; //idade18a59
		int  i6 = 0; //idade60mais
		
		for(PessoaTO pessoa : pessoas) {
			
			if(pessoa.getIdade() > 0 && pessoa.getIdade() < 13) {
				i2++;
			}
			else if(pessoa.getIdade() < 18) {
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
		vitima.setIdade0a12(i2);		
		vitima.setIdade13a17(i4);		
		vitima.setIdade18a59(i5);		
		vitima.setIdade60mais(i6);		
		vitima.setTotal(i2+i4+i5+i6);		
		
		log.debug("Vitimas " + sexo.name() + ": " + vitima.getTotal() + " ----> Qde pessoas retornadas do banco: " + pessoas.size());
		return vitima;
	}
	/* Idoso */
	private VitimaCreasTO processarVitimasIdoso(List<PessoaTO> pessoas, Sexo sexo) {
				
		VitimaCreasTO vitima = new VitimaCreasTO();		
		
		int  i6 = 0; //idade60mais
		
		for(PessoaTO pessoa : pessoas) {			
			if(pessoa.getIdade() > 59) {
				i6++;
			}
		}
		vitima.setSexo(sexo.name());	
		vitima.setIdade60mais(i6);		
		vitima.setTotal(i6);		
		
		log.info("Vitimas " + sexo.name() + ": " + vitima.getTotal() + " ----> Qde pessoas retornadas do banco: " + pessoas.size());
		return vitima;
	}
	/* Mulher */
	private VitimaCreasTO processarVitimasMulher(List<PessoaTO> pessoas, Sexo sexo) {
				
		VitimaCreasTO vitima = new VitimaCreasTO();		
		
		int  i7 = 0; //idade60mais
		
		for(PessoaTO pessoa : pessoas) {			
			if(pessoa.getIdade() > 18 && pessoa.getIdade() < 60) {
				i7++;
			}
		}
		vitima.setSexo(sexo.name());	
		vitima.setIdade60mais(i7);		
		vitima.setTotal(i7);		
		
		log.debug("Vitimas " + sexo.name() + ": " + vitima.getTotal() + " ----> Qde pessoas retornadas do banco: " + pessoas.size());
		return vitima;
	}
	
	
	
	/*
	 * Bloco II Atendimentos realizados no CREAS.
	 * 
	 */
	
	
	private Bloco2CreasTO carregarBloco2() {
		
		Bloco2CreasTO bloco2 = new Bloco2CreasTO();
		//M.1. Total de atendimentos individualizados realizados no mês de referência
		bloco2.setM1(rmaCreasDAO.buscarM1(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//M.2. Total de atendimentos em grupo realizados no mês de referência
		bloco2.setM2(rmaCreasDAO.buscarM2(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//M.3. Famílias encaminhadas para o CRAS durante no mês de referência
		bloco2.setM3(rmaCreasDAO.buscarM3(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//M.4. Visitas domiciliares realizadas no mês de referência
		bloco2.setM4(rmaCreasDAO.buscarM4(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		
		
		return bloco2;		
	}
	
	/*
	 *  Bloco III – Serviço de Proteção Social a Adolescente em Cumprimento de Medida Socioeducativa (LA/PSC)
	 *  
	 */
	

	
	private Bloco3CreasTO carregarBloco3() {
		
		Bloco3CreasTO bloco3 = new Bloco3CreasTO();
		//J.1. Total de adolescentes em cumprimento de Medidas Socioeducativas (LA e/ou PSC)
		bloco3.setJ1( rmaCreasDAO.buscarJ1( rma.getUnidade(), getDataIni(), getDataFim(), tenantId ).intValue() );
		//J.2. Quantidade de adolescentes em cumprimento de Liberdade Assistida - LA
		bloco3.setJ2( rmaCreasDAO.buscarJ2( rma.getUnidade(), tenantId ).intValue() );
		//J.3. Quantidade de adolescentes em cumprimento de Prestação de Serviços à Comunidade - PSC
		bloco3.setJ3( rmaCreasDAO.buscarJ3( rma.getUnidade(), tenantId ).intValue() );
		
		//J.4. Total de novos adolescentes em cumprimento de Medidas Socioeducativas (LA e/ou PSC), inseridos em acompanhamento no mês de referência
		VitimaCreasTO vitimaF4 = new VitimaCreasTO();
		vitimaF4.setSexo(Sexo.FEMININO.name());
		vitimaF4.setTotal(rmaCreasDAO.buscarJ4( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO, tenantId ).intValue());
		bloco3.setJ4vitimasFem(vitimaF4);
		VitimaCreasTO vitimaM4 = new VitimaCreasTO();
		vitimaM4.setSexo(Sexo.MASCULINO.name());
		vitimaM4.setTotal(rmaCreasDAO.buscarJ4( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO, tenantId ).intValue());
		bloco3.setJ4vitimasMasc(vitimaM4);		
		bloco3.setJ4(vitimaF4.getTotal() + vitimaM4.getTotal());		
		
		//J.5. Novos adolescentes em cumprimento de LA, inseridos em acompanhamento, no mês de referência
		VitimaCreasTO vitimaF5 = new VitimaCreasTO();
		vitimaF5.setSexo(Sexo.FEMININO.name());
		vitimaF5.setTotal(rmaCreasDAO.buscarJ5( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO, tenantId ).intValue());
		bloco3.setJ5vitimasFem(vitimaF5);
		VitimaCreasTO vitimaM5 = new VitimaCreasTO();
		vitimaM5.setSexo(Sexo.MASCULINO.name());
		vitimaM5.setTotal(rmaCreasDAO.buscarJ5( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO, tenantId ).intValue());
		bloco3.setJ5vitimasMasc(vitimaM5);		
		bloco3.setJ5(vitimaF5.getTotal() + vitimaM5.getTotal());
			
		//J.6. Novos adolescentes em cumprimento de PSC, inseridos em acompanhamento, no mês de referência
		VitimaCreasTO vitimaF6 = new VitimaCreasTO();
		vitimaF6.setSexo(Sexo.FEMININO.name());
		vitimaF6.setTotal(rmaCreasDAO.buscarJ6( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.FEMININO, tenantId ).intValue());
		bloco3.setJ6vitimasFem(vitimaF6);
		VitimaCreasTO vitimaM6 = new VitimaCreasTO();
		vitimaM6.setSexo(Sexo.MASCULINO.name());
		vitimaM6.setTotal(rmaCreasDAO.buscarJ6( rma.getUnidade(), getDataIni(), getDataFim(), Sexo.MASCULINO, tenantId ).intValue());
		bloco3.setJ6vitimasMasc(vitimaM6);		
		bloco3.setJ6(vitimaF6.getTotal() + vitimaM6.getTotal());
				
		return bloco3;
	}

	private RmaCreasTO setDados(RmaCreasTO rma, Unidade unidade, Integer anoReferencia, Mes mesReferencia) {
		
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
		rmaCreasDAO.setEntityManager(manager);
		
	}	
	
}