package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import com.softarum.svsa.service.EsqueciSenhaService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

@Named
@SessionScoped
public class EsqueciSenhaBean implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Inject
	private EsqueciSenhaService esqueciSenhaService;

	private String novaSenha1;
	private String novaSenha2;
	private String email;
	private String token;
	private String senha;
	private Boolean valido = false;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getValido() {
		return this.valido;
	}

	public void setValido(Boolean valido) {
		this.valido = valido;
	}

	public void esqueciSenhaEnviar() throws NegocioException, NoResultException {

		try {
			esqueciSenhaService.novoToken(this.email);
			MessageUtil.sucesso("Verifique sua caixa de entrada.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}

	}

	public void validarToken() throws NegocioException {
		try {
			if(!this.valido) {
				Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
				String token = params.get("token");
				if (esqueciSenhaService.esqueciSenhaValidar(token)) {
					this.token = token;
					this.valido = true;
				} else {
					throw new NegocioException("Token inválido e/ou expirado.");		
				}
			}
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}

	public void enviar() throws NegocioException {

		try {
			if (novaSenha1.compareTo(this.novaSenha2) == 0) {
				if(esqueciSenhaService.esqueciSenhaValidar(token)) {
				esqueciSenhaService.alterarSenhaBanco(this.token, this.novaSenha1);
				MessageUtil.sucesso("Senha redefinida com sucesso!");
				} else {
					throw new NegocioException("Token inválido e/ou expirado.");
				}

			} else {
				throw new NegocioException("As senhas não coincidem.");
				
			}
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
					
		}
	}

	public String getNovaSenha1() {
		return novaSenha1;
	}

	public void setNovaSenha1(String novaSenha1) {
		this.novaSenha1 = novaSenha1;
	}

	public String getNovaSenha2() {
		return novaSenha2;
	}

	public void setNovaSenha2(String novaSenha2) {
		this.novaSenha2 = novaSenha2;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
