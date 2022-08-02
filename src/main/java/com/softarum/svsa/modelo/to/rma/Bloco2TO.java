package com.softarum.svsa.modelo.to.rma;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Relatorio RMA - Atendimentos particularizados realizados no CRAS
 * Referência SNAS: Bloco C
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class Bloco2TO implements Serializable {

	private static final long serialVersionUID = 1L;
	/*
	 * C - Volume de atendimentos particularizados realizados no CRAS no mês de referência
	 */	
	private Integer c1 = 0;  // Total de atendimentos particularizados, no mês
	private Integer c2 = 0;  // Famílias encaminhadas para inclusão no Cadastro Único
	private Integer c3 = 0;  // Famílias encaminhadas para atualização cadastral no Cadastro Único
	private Integer c4 = 0;  // Indivíduos encaminhados para acesso ao BPC
	private Integer c5 = 0;  // Famílias encaminhadas para o CREAS
	private Integer c6 = 0;  // Visitas domiciliares realizadas
	private Integer c7 = 0;  // Total de auxílios-natalidade concedidos/entregues durante o mês de referência
	private Integer c8 = 0;  // Total de auxílios-funeral concedidos/entregues durante o mês de referência
	private Integer c9 = 0;  // Outros benefícios eventuais concedidos/entregues durante o mês de referência
	
}
