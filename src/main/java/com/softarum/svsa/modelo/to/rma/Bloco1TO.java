package com.softarum.svsa.modelo.to.rma;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Relatorio RMA - Familias em acompanhamento pelo PAIF
 * Referência SNAS: Blocos A e B
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class Bloco1TO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/*
	 * A - Volume de famílias em acompanhamento pelo PAIF
	 */
	private Integer a1 = 0;  // Total de famílias em acompanhamento pelo PAIF
	private Integer a2 = 0;  // Novas famílias inseridas no acompanhamento do PAIF durante o mês de referência
	
	/*
	 * B - Perfil das novas famílias inseridas no acompanhamento do PAIF durante o mês de referência
	 */
	private Integer b1 = 0;  // Famílias em situação de extrema pobreza
	private Integer b2 = 0;  // Famílias beneficiárias do bolsa família
	private Integer b3 = 0;  // Famílias beneficiárias do bolsa família, em descumprimento de condicionalidades
	private Integer b4 = 0;  // Famílias com membros beneficiários do BPC
	private Integer b5 = 0;  // Famílias crianças ou adolescentes em situação de trabalho infantil
	private Integer b6 = 0;  // Famílias crianças ou adolescentes em serviço de acolhimento
	private Integer b7 = 0;  // Outros
	private Integer b8 = 0;  // Atende criterios dos PTR mas nao foi contemplada
	private Integer b9 = 0;  // Em situacao de vulnerabilidade
	private Integer b10 = 0; // Com deficientes ou idosos
	
	
}
