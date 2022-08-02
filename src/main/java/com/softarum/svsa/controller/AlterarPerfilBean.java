package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.service.ThemeService;
import com.softarum.svsa.service.ThemeService.Theme;
import com.softarum.svsa.service.UsuarioService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;



/**
 * @author murakamiadmin
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class AlterarPerfilBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	private String senhaAntiga;
	private Theme theme;
	private List<Theme> themes;
	
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private ThemeService themeService;
	@Inject
	private LoginBean usuarioLogado;
	
	@PostConstruct
	public void inicializar() {
		
		log.info("Alterar Perfil " + usuarioLogado.getUsuario());
		this.usuario = usuarioLogado.getUsuario();
		themes = themeService.getThemes();
		
		if(usuario.getTheme() == null) {
			theme = ThemeService.getThemeDefault(); // Default = Cupertino
		}
		else {
			theme = getThemes().get(usuario.getTheme());
		}
	}
	
	public void salvar() {
		try {
			this.usuarioService.trocarSenha(usuario, senhaAntiga);
			MessageUtil.sucesso("Senha alterada com sucesso! Saia do sistema e faça login novamente.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		} catch (Exception le) {
			le.printStackTrace();
			MessageUtil.erro(le.getMessage());
		}

	}
	
	public void salvarTema() {
		try {
			log.info("salvarTema()..." + theme.getDisplayName());
			
			usuario.setTheme(theme.getId());
			
			this.usuarioService.alterarPerfil(usuario);
			MessageUtil.sucesso("Tema alterado com sucesso! Saia do sistema e faça login novamente.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		} catch (Exception le) {
			le.printStackTrace();
			MessageUtil.erro(le.getMessage());
		}
	}
	public void salvarPerfil() {
		try {
			log.info("salvarTema()..." + theme.getDisplayName());
			
			if(usuario.getTheme() != theme.getId()) {
				log.info("Alterando tema...de " + usuario.getTheme() + " para " + theme.getId());
				usuario.setTheme(theme.getId());
			}
			
			this.usuarioService.alterarPerfil(usuario);
			MessageUtil.sucesso("Perfil alterado com sucesso! Saia do sistema e faça login novamente.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		} catch (Exception le) {
			le.printStackTrace();
			MessageUtil.erro(le.getMessage());
		}

	}

}
