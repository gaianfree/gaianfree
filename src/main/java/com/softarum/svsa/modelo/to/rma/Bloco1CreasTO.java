package com.softarum.svsa.modelo.to.rma;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Relatorio RMA
 * Referência SNAS: Bloco I - Serviço de Proteção e Atendimento Especializado a Famílias e Indivíduos - PAEFI
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class Bloco1CreasTO implements Serializable {
		
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
	
	private VitimaCreasTO b6vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO b6vitimasFem = new VitimaCreasTO();
	
	/*
	 * C - Crianças ou adolescentes em situações de violência ou violações, que ingressaram no PAEFI durante o mês de referência
	 */
	private Integer c1 = 0;  // Crianças ou adolescentes vítimas de violência intrafamiliar (física ou psicológica)
	private VitimaCreasTO c1vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO c1vitimasFem = new VitimaCreasTO();
		
	private Integer c2 = 0;  // Crianças ou adolescentes vítimas de abuso sexual
	private VitimaCreasTO c2vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO c2vitimasFem = new VitimaCreasTO();
	
	private Integer c3 = 0;  // Crianças ou adolescentes vítimas de exploração sexual
	private VitimaCreasTO c3vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO c3vitimasFem = new VitimaCreasTO();
	
	private Integer c4;  // Crianças ou adolescentes vítimas de negligência ou abandono
	private VitimaCreasTO c4vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO c4vitimasFem = new VitimaCreasTO();
	
	private Integer c5 = 0;  // Crianças ou adolescentes em situação de trabalho infantil (até 15 anos)
	private VitimaCreasTO c5vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO c5vitimasFem = new VitimaCreasTO();
	
	private Integer c6 = 0;  // Não consta do manual do RMA
	private VitimaCreasTO c6vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO c6vitimasFem = new VitimaCreasTO();
	
	
	/*
	 * D. Idosos - 60 anos ou mais - em situações de violência ou violações que ingressaram no PAEFI durante o mês de referência
	 */
	private Integer d1 = 0;  //D.1. Pessoas idosas vítimas de violência intrafamiliar (física, psicológica ou sexual)
	private VitimaCreasTO d1vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO d1vitimasFem = new VitimaCreasTO();
	
	private Integer d2 = 0;  //D.2. Pessoas idosas vítimas de negligência ou abandono
	private VitimaCreasTO d2vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO d2vitimasFem = new VitimaCreasTO();
	
	
	/*
	 * E. Pessoas com deficiência em situações de violência ou violações que ingressaram no PAEFI durante o mês
	 */
	private Integer e1 = 0;  //E.1. Pessoas com deficiência vítimas de violência intrafamiliar (física, psicológica ou sexual)
	private VitimaCreasTO e1vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO e1vitimasFem = new VitimaCreasTO();

	private Integer e2 = 0;  //E.2. Pessoas com deficiência vítimas de negligência ou abandono
	private VitimaCreasTO e2vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO e2vitimasFem = new VitimaCreasTO();
	/*
	 * F. Mulheres adultas vítimas de violência intrafamiliar que ingressaram no PAEFI durante o mês de referência
	 */
	private Integer f1 = 0; //F.1. Mulheres adultas (18 a 59 anos) vítimas de violência intrafamiliar (física, psicológica ou sexual)
	private VitimaCreasTO f1vitimasFem = new VitimaCreasTO();
	
	/*
	 * G. Pessoas vítimas de tráficos de seres humanos que ingressaram no PAEFI durante o mês de referência
	 */
	private Integer g1 = 0; //G.1. Tráfico de pessoas
	private VitimaCreasTO g1vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO g1vitimasFem = new VitimaCreasTO();
	
	/*
	 * H. Pessoas vítimas de discriminação por orientação sexual que ingressaram no PAEFI durante o mês de referência
	 */
	private Integer h1 = 0; //G.1. Tráfico de pessoas
	
	/*
	 * I. Pessoas em situação de rua que ingressaram no PAEFI durante o mês de referência
	 */
	private Integer i1 = 0; //G.1. Tráfico de pessoas
	private VitimaCreasTO i1vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO i1vitimasFem = new VitimaCreasTO();
}
