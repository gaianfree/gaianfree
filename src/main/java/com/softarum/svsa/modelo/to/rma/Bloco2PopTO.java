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
public class Bloco2PopTO implements Serializable {
		
	private static final long serialVersionUID = 1L;
	
	
	/*
	 * Bloco II - Serviço Especializado em Abordagem Social no Centro POP. 
	 */
	
		
	/*
	 * E. Quantidade e perfil das pessoas abordadas pela equipe do Serviço de Abordagem, no mês de referência.
	 */
	private Integer e1 = 0;  //E.1. Quantidade e perfil das pessoas abordadas pela equipe do Serviço de Abordagem, no mês de referência.
	private VitimaCreasTO e1vitimasMasc = new VitimaCreasTO();
	private VitimaCreasTO e1vitimasFem = new VitimaCreasTO();
	// Atenção: Em E1 cada pessoa deve ser contada uma única vez em cada mês, mesmo que tenha sido abordada várias vezes durante o mesmo mês.

	/*
	 * Situações identificadas pelo Serviço Especializado em Abordagem Social, no mês de referência.
	 */
	private Integer e2 = 0;  //E.2. Crianças ou adolescentes em situação de trabalho infantil (até 15 anos)
	private Integer e3 = 0;  //E.3. Crianças ou adolescentes em situação de exploração sexual
	private Integer e4 = 0;  //E.4. Crianças ou adolescentes usuárias de crack ou outras drogras
	private Integer e5 = 0;  //E.5. Pessoas adultas usuárias de crack ou outras drogas ilícitas
	private Integer e6 = 0;  //E.6. Migrantes
	private Integer e7 = 0;  //E.7. Usuárias de alcool
	// Atenção: Os itens E2 a E6 buscam identificar apenas alguns "perfis/condições" das pessoas abordadas, portanto é normal que algumas pesoas contadas no item E1 não se enquadrem em nenhum dos "perfis/confições" descritos, enquanto outras pessoas podem se enquadrar simultaneamente em mais de um, portanto, a soma de E2 a E6 não terá, necessariamente, o mesmo valor relatado no total de E1.
	
	/*
	 * F. Volume de abordagens realizadas.
	 */
	private Integer f1 = 0;  //F.1. Quantidade total de abordagens realizadas (compreendida como o número de pessoas abordadas, multiplicado pelo número de vezes em que foram abordadas durante o mês)
	// Atenção: Quando a abordagem é feita a um grupo, cada pessoa do grupo é contada como uma abordagem.


}
