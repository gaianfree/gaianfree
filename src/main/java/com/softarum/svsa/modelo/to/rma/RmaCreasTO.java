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
public class RmaCreasTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String mesAnoReferencia;
	private Unidade unidade;

	private Bloco1CreasTO bloco1 = new Bloco1CreasTO();
	private Bloco2CreasTO bloco2 = new Bloco2CreasTO();
	private Bloco3CreasTO bloco3 = new Bloco3CreasTO();
	
	public static RmaCreasTO createRmaCreasTO() {
		
		RmaCreasTO to = new RmaCreasTO();
		
		
		to.getBloco1().setB6vitimasFem(new VitimaCreasTO());
		to.getBloco1().setB6vitimasMasc(new VitimaCreasTO());
		
		to.getBloco1().setC1vitimasFem(new VitimaCreasTO());
		to.getBloco1().setC1vitimasMasc(new VitimaCreasTO());
		
		to.getBloco1().setC2vitimasFem(new VitimaCreasTO());
		to.getBloco1().setC2vitimasMasc(new VitimaCreasTO());
		
		to.getBloco1().setC3vitimasFem(new VitimaCreasTO());
		to.getBloco1().setC3vitimasMasc(new VitimaCreasTO());
		
		to.getBloco1().setC4vitimasFem(new VitimaCreasTO());
		to.getBloco1().setC4vitimasMasc(new VitimaCreasTO());
		
		to.getBloco1().setC5vitimasFem(new VitimaCreasTO());
		to.getBloco1().setC5vitimasMasc(new VitimaCreasTO());
		
		to.getBloco1().setC6vitimasFem(new VitimaCreasTO());
		to.getBloco1().setC6vitimasMasc(new VitimaCreasTO());

		to.getBloco1().setD1vitimasFem(new VitimaCreasTO());
		to.getBloco1().setD1vitimasMasc(new VitimaCreasTO());
		
		to.getBloco1().setD2vitimasFem(new VitimaCreasTO());
		to.getBloco1().setD2vitimasMasc(new VitimaCreasTO());
		
		to.getBloco1().setE1vitimasFem(new VitimaCreasTO());
		to.getBloco1().setE1vitimasMasc(new VitimaCreasTO());
		to.getBloco1().setE2vitimasFem(new VitimaCreasTO());
		to.getBloco1().setE2vitimasMasc(new VitimaCreasTO());
		
		to.getBloco1().setF1vitimasFem(new VitimaCreasTO());
		
		to.getBloco1().setG1vitimasFem(new VitimaCreasTO());
		to.getBloco1().setG1vitimasMasc(new VitimaCreasTO());
		
		to.getBloco1().setI1vitimasFem(new VitimaCreasTO());
		to.getBloco1().setI1vitimasMasc(new VitimaCreasTO());
		
		to.getBloco3().setJ4vitimasFem(new VitimaCreasTO());
		to.getBloco3().setJ4vitimasMasc(new VitimaCreasTO());
		to.getBloco3().setJ5vitimasFem(new VitimaCreasTO());
		to.getBloco3().setJ5vitimasMasc(new VitimaCreasTO());
		to.getBloco3().setJ6vitimasFem(new VitimaCreasTO());
		to.getBloco3().setJ6vitimasMasc(new VitimaCreasTO());
			
		return to;
	}
	
}
