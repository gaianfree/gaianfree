package com.softarum.svsa.modelo.to;

import java.io.Serializable;

import com.softarum.svsa.modelo.PlanoAcompanhamento;

import lombok.Getter;
import lombok.Setter;

/**
 * @author murakamiadmin
 *
 */
@Getter
@Setter
public class PerfilFamiliaTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private PlanoAcompanhamento plano;
	private Long qdePerfis = 0L;
	
	public PerfilFamiliaTO() {}	
	
	/* usado por DashBoard */
	public PerfilFamiliaTO(PlanoAcompanhamento plano) {		
		this.plano = plano;		
	}
		
}
