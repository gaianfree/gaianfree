package com.softarum.svsa.modelo.to;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class IndicadorTO implements Serializable, Comparable<IndicadorTO> {
	
	private static final long serialVersionUID = 1L;
	
	private String descricao = "";
	private int mes = 0;
	private int ano = 0;
	private Long qde = 0L;
	
	public IndicadorTO() {}	

	public IndicadorTO(String descricao, Long qde) {		
		this.setDescricao(descricao);	
		this.setQde(qde);
	}

	@Override
	public int compareTo(IndicadorTO ind) {
        return this.getQde().compareTo(ind.getQde());
    }
}
