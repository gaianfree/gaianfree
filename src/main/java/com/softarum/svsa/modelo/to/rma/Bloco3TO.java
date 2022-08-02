package com.softarum.svsa.modelo.to.rma;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Relatorio RMA - Atendimentos coletivos realizados no CRAS
 * Referência SNAS: Bloco D
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class Bloco3TO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/*
	 * D - Volume dos Serviços de Convivência e Fortalecimento de Vínculos, no mês de referência
	 */	
	private Integer d1 = 0;  // Famílias participando regularmente de grupos no âmbito PAIF
	private Integer d2 = 0;  // Crianças de 0 a 6 anos em Serviço de Convivência Familiar e Fortalecimento de Vínculos
	private Integer d3 = 0;  // Crianças/adolescentes de 7 a 14 anos em Serviço de Convivência Familiar e Fortalecimento de Vínculos
	private Integer d4 = 0;  // Adolescentes de 15 a 17 anos em Serviço de Convivência Familiar e Fortalecimento de Vínculos
	private Integer d5 = 0;  // Adultos de 18 a 59 anos em Serviço de Convivência Familiar e Fortalecimento de Vínculos
	private Integer d6 = 0;  // Idosos em Serviço de Convivência Familiar e Fortalecimento de Vínculos
	private Integer d7 = 0;  // Pessoas que participam de palestras, oficinas e outras atividades coletivas de carater não continuado
	private Integer d8 = 0;  // Pessoas com deficiência, participando dos Serviços de Convivência ou dos grupos do PAIF
	private Integer d9 = 0;  // Pessoas participando dos grupos do PAIF
	
}
