package com.softarum.svsa.modelo.to.rma;

import java.io.Serializable;

import com.softarum.svsa.modelo.Unidade;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe para geração do relatorio RMA
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class RmaPopTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String mesAnoReferencia;	
	private Unidade unidade;

	private Bloco1PopTO bloco1;
	private Bloco2PopTO bloco2;	
	
	public static RmaPopTO createRmaPopTO() {
		
		RmaPopTO rmaTO = new RmaPopTO();		
		rmaTO.setBloco1(new Bloco1PopTO());
		rmaTO.setBloco2(new Bloco2PopTO());
		
		return rmaTO;		
	}
	
}
