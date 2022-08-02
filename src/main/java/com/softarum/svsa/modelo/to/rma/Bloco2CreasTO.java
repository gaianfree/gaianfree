package com.softarum.svsa.modelo.to.rma;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Relatorio RMA
 * Referência SNAS: Bloco II Atendimentos realizados no CREAS.
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class Bloco2CreasTO implements Serializable {		

	private static final long serialVersionUID = 1L;
	/*
	 * M. Atendimentos realizados no mês de referência
	 */	
	private Integer m1 = 0;  // M.1. Total de atendimentos individualizados realizados no mês de referência
	private Integer m2 = 0;  // M.2. Total de atendimentos em grupo realizados no mês de referência
	private Integer m3 = 0;  // M.3. Famílias encaminhadas para o CRAS durante no mês de referência
	private Integer m4 = 0;  // M.4. Visitas domiciliares realizadas no mês de referência
	
}
