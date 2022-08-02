package com.softarum.svsa.modelo.to;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sigla;
	private String imagem;


	public LogoTO(String sigla, String imagem) {
		this.setSigla(sigla);
		this.setImagem(imagem);
	}	
	
}
