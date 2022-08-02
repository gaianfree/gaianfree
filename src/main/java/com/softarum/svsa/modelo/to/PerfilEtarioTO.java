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
public class PerfilEtarioTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int qde0a6anos = 0;
	private int qde7a14anos = 0;
	private int qde15a17anos = 0;
	private int qde18a29anos = 0;
	private int qde30a59anos = 0;
	private int qde60a64anos = 0;
	private int qde65a69anos = 0;
	private int mais70anos = 0;
	
	public int getQdePessoas() {
		return  qde0a6anos +
				qde7a14anos +
				qde15a17anos +
				qde18a29anos +
				qde30a59anos +
				qde60a64anos +
				qde65a69anos +
				mais70anos;
	}	
}
