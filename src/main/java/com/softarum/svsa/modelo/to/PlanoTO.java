package com.softarum.svsa.modelo.to;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class PlanoTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long prontuario;
	private String pessoaReferencia;
	private Date dataIngresso;
	private long terminoDias;
	private String tecnicoResponsavel;
	
}
