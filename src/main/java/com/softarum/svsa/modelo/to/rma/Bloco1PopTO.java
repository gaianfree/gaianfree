package com.softarum.svsa.modelo.to.rma;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Relatorio RMA Pop
 * Referência SNAS: Serviço Especializado para Pessoas em Situação de Rua.
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class Bloco1PopTO implements Serializable {
		
	private static final long serialVersionUID = 1L;
	
	/*
	 * Bloco I - Serviço Especializado para Pessoas em Situação de Rua.
	 */
	
	/*
	 * A - Pessoas em situação de rua atendidas no serviço durante o mês de referência.
	 */
	private Integer a1 = 0;  // Quantidade e perfil das pessoas em situação de rua atendida no mes de referência.
	private VitimaCreasTO a1vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO a1vitimasFem = new VitimaCreasTO();
	// Atenção: Em A1 cada pessoa deve ser contada uma única vez em cada mês, mesmo que tenha sido atendidas várias vezes durante o mesmo mês.

	
	/*
	 * B - Características específicas identificadas em pessoas atendidas no serviço durante o mês de referência.
	 */
	private Integer b1 = 0;  // Pessoas usuárias de crack ou outras drogas ilícitas
	private Integer b2 = 0;  // Migrantes
	private Integer b3 = 0;  // Pessoas com doença ou transtorno mental
	private Integer b4 = 0;  // Usuárias de alcool	
		
	/*
	 * C - Cadastramento de pessoas em situação de rua durante o mês de referência.
	 */
	private Integer c1 = 0;  // Pessoas que foram incluídas no Cadastro Único para Programas Sociais, no mês.
	private Integer c2 = 0;  // Pessoas que realizaram atualização no Cadastro Único para Programas Sociais, no mês.
	
	/*
	 * D. Volume total de atendimentos realizados no mês de referência.
	 */
	private Integer d1 = 0;  //D.1. Quantidade total de atendimentos realizados (compreendida como a soma do número de atendimentos realizados a cada dia, durante o mês de referência)
	
}
