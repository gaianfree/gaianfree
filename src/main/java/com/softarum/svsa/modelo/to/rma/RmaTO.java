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
public class RmaTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String mesAnoReferencia;	
	private Unidade unidade;

	private Bloco1TO bloco1;
	private Bloco2TO bloco2;
	private Bloco3TO bloco3;
	
	public static RmaTO createRmaTO() {
		
		RmaTO rmaTO = new RmaTO();		
		rmaTO.setBloco1(new Bloco1TO());
		rmaTO.setBloco2(new Bloco2TO());
		rmaTO.setBloco3(new Bloco3TO());
		
		return rmaTO;		
	}
	
}
