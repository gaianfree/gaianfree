package com.softarum.svsa.modelo.to;

import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;

public class AutoCadSecTO {
	
	private Tenant secretaria;
	private Unidade unidade;
	private Usuario usuario;
	
	public Tenant getSecretaria() {
		return secretaria;
	}
	public void setSecretaria(Tenant secretaria) {
		this.secretaria = secretaria;
	}
	public Unidade getUnidade() {
		return unidade;
	}
	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	

}
