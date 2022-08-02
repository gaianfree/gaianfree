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
public class ProntuarioUnidadeTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String nomeUnidade = "";
	private Long qdeProntuarios = 0L;
	private Long qdeProntuariosInativos = 0L;
	private Long qdeProntuariosPAIF = 0L;
	private Long qdeProntuariosExcluidos = 0L;
	
	public Long getQdeProntuariosAtivos() {
		return qdeProntuarios - qdeProntuariosInativos;	
	}
}
