package com.softarum.svsa.modelo.to.rma;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Relatorio RMA
 * Referência SNAS: Bloco III – Serviço de Proteção Social a Adolescente em Cumprimento de Medida Socioeducativa (LA/PSC)
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class Bloco3CreasTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/*
	 * J - J. Volume de adolescentes em cumprimento de Medidas Socioeducativas
	 */	
	private Integer j1 = 0;  // J.1. Total de adolescentes em cumprimento de Medidas Socioeducativas (LA e/ou PSC)
	private Integer j2 = 0;  // J.2. Quantidade de adolescentes em cumprimento de Liberdade Assistida - LA
	private Integer j3 = 0;  // J.3. Quantidade de adolescentes em cumprimento de Prestação de Serviços à Comunidade - PSC
	
	private Integer j4 = 0;  // J.4. Total de novos adolescentes em cumprimento de Medidas Socioeducativas (LA e/ou PSC), inseridos em acompanhamento no mês de referência
	private VitimaCreasTO j4vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO j4vitimasFem = new VitimaCreasTO();
	
	private Integer j5;  // J.5. Novos adolescentes em cumprimento de LA, inseridos em acompanhamento, no mês de referência
	private VitimaCreasTO j5vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO j5vitimasFem = new VitimaCreasTO();
	
	private Integer j6;  // J.6. Novos adolescentes em cumprimento de PSC, inseridos em acompanhamento, no mês de referência
	private VitimaCreasTO j6vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO j6vitimasFem = new VitimaCreasTO();
	
	
	
	
}
