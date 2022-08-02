package com.softarum.svsa.modelo.to.rma;

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
public class VitimaCreasTO {
		
	private String sexo = "";
	private Integer idade0a6 = 0;
	private Integer idade0a12 = 0;
	private Integer idade7a12 = 0;
	private Integer idade13a17 = 0;
	private Integer idade18a59 = 0;
	private Integer idade60mais = 0;
	private Integer total = 0;
	
}
