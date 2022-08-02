package com.softarum.svsa.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.softarum.svsa.dao.RmaCrasDAO;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Mes;
import com.softarum.svsa.modelo.to.rma.Bloco1TO;
import com.softarum.svsa.modelo.to.rma.Bloco2TO;
import com.softarum.svsa.modelo.to.rma.Bloco3TO;
import com.softarum.svsa.modelo.to.rma.RmaTO;
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
public class RelatorioRMACrasService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date dataIni;
	private Date dataFim;
	private RmaTO rma;
	private Long tenantId;
	
	
	@Inject
	private RmaCrasDAO rmaDAO;


	public RmaTO gerarRelatorioRMA(Unidade unidade, Integer ano, Mes mesReferencia, Long tenantId) {
		
		this.tenantId = tenantId;
		/*
		 * Seta parametros de consulta
		 */
		rma = new RmaTO();		
		rma = setDados(rma, unidade, ano, mesReferencia);
		
		/*
		 * Realiza as consultas
		 */
		rma.setBloco1(carregarBloco1());
		rma.setBloco2(carregarBloco2());
		rma.setBloco3(carregarBloco3());
		
		return rma;
	}
	
	

	
	private Bloco1TO carregarBloco1() {
		
		Bloco1TO bloco1 = new Bloco1TO();
		//A.1. Total de famílias em acompanhamento pelo PAIF
		bloco1.setA1(rmaDAO.buscarA1(rma.getUnidade(), getDataFim(), tenantId).intValue());
		//A.2. Novas famílias inseridas no acompanhamento do PAIF durante o mês de referência		
		bloco1.setA2(rmaDAO.buscarA2(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.1. Famílias em situação de extrema pobreza durante o mês de referência
		bloco1.setB1(rmaDAO.buscarB1(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.2. Famílias beneficiárias do bolsa família durante o mês de referência
		bloco1.setB2(rmaDAO.buscarB2(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.3. Famílias beneficiárias do bolsa família, em descumprimento de condicionalidades durante o mês de referência
		bloco1.setB3(rmaDAO.buscarB3(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.4. Famílias com membros beneficiários do BPC durante o mês de referência
		bloco1.setB4(rmaDAO.buscarB4(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.5. Famílias crianças ou adolescentes em situação de trabalho infantil durante o mês de referência
		bloco1.setB5(rmaDAO.buscarB5(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.6. Famílias crianças ou adolescentes em serviço de acolhimento durante o mês de referência
		bloco1.setB6(rmaDAO.buscarB6(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.7. Outros durante o mês de referência
		bloco1.setB7(rmaDAO.buscarB7(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());		
		//B.8. Atende criterios dos PTR mas nao foi contemplada
		bloco1.setB8(rmaDAO.buscarB8(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.9. Em situacao de vulnerabilidade
		bloco1.setB9(rmaDAO.buscarB9(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//B.10. Com deficientes ou idosos
		bloco1.setB10(rmaDAO.buscarB10(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		
		return bloco1;		
	}
	
	private Bloco2TO carregarBloco2() {
		
		Bloco2TO bloco2 = new Bloco2TO();
		//C.1. Total de atendimentos particularizados, no mês
		bloco2.setC1(rmaDAO.buscarC1(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//C.2. Famílias encaminhadas para inclusão no Cadastro Único
		bloco2.setC2(rmaDAO.buscarC2(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//C.3. Famílias encaminhadas para atualização cadastral no Cadastro Único
		bloco2.setC3(rmaDAO.buscarC3(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//C.4. Indivíduos encaminhados para acesso ao BPC
		bloco2.setC4(rmaDAO.buscarC4(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//C.5. Famílias encaminhadas para o CREAS
		bloco2.setC5(rmaDAO.buscarC5(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//C.6. Visitas domiciliares realizadas
		bloco2.setC6(rmaDAO.buscarC6(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//C.7. Total de auxílios-natalidade concedidos/entregues durante o mês de referência
		bloco2.setC7(rmaDAO.buscarC7(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//C.8. Total de auxílios-funeral concedidos/entregues durante o mês de referência
		bloco2.setC8(rmaDAO.buscarC8(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//C.9. Outros benefícios eventuais concedidos/entregues durante o mês de referência
		bloco2.setC9(rmaDAO.buscarC9(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		
		return bloco2;		
	}

	private Bloco3TO carregarBloco3() {
		
		Bloco3TO bloco3 = new Bloco3TO();
		
		//D.1. Famílias participando regularmente de grupos no âmbito PAIF
		bloco3.setD1(rmaDAO.buscarD1(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//D.2. Crianças de 0 a 6 anos em Serviço de Convivência Familiar e Fortalecimento de Vínculos
		bloco3.setD2(rmaDAO.buscarD2(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//D.3. Crianças/adolescentes de 7 a 14 anos em Serviço de Convivência Familiar e Fortalecimento de Vínculos
		bloco3.setD3(rmaDAO.buscarD3(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//D.4. Adolescentes de 15 a 17 anos em Serviço de Convivência Familiar e Fortalecimento de Vínculos
		bloco3.setD4(rmaDAO.buscarD4(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//D.5. Adultos de 18 a 59 anos em Serviço de Convivência Familiar e Fortalecimento de Vínculos
		bloco3.setD5(rmaDAO.buscarD5(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//D.6. Idosos em Serviço de Convivência Familiar e Fortalecimento de Vínculos
		bloco3.setD6(rmaDAO.buscarD6(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//D.7. Pessoas que participam de palestras, oficinas e outras atividades coletivas de carater não continuado
		bloco3.setD7(rmaDAO.buscarD7(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());	
		//D.8. Pessoas com deficiência, participando dos Serviços de Convivência ou dos grupos do PAIF
		bloco3.setD8(rmaDAO.buscarD8(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		//D.9. Pessoas participando dos grupos do PAIF
		bloco3.setD9(rmaDAO.buscarD9(rma.getUnidade(), getDataIni(), getDataFim(), tenantId).intValue());
		
		return bloco3;		
	}
	
	
	
	
	private RmaTO setDados(RmaTO rma, Unidade unidade, Integer anoReferencia, Mes mesReferencia) {
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
		rmaDAO.setEntityManager(manager);
		
	}	
}