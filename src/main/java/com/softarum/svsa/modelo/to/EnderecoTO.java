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
public class EnderecoTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String cep = "";
	private String estado = "";
	private String cidade = "";
	private String bairro = "";
	private String tipoLogradouro = "";
	private String logradouro = "";
	
	private int resultado = 0;
	
}
