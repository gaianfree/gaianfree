package com.softarum.svsa.modelo.to;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gabriel
 *
 */
@Getter
@Setter
public class ProntuarioTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String nome = "";
	private Long qdeProntuariosNovos = 0L;
	
}
