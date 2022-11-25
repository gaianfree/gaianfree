package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.service.TenantService;
import com.softarum.svsa.service.UnidadeService;
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

public class PesquisaAdmBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Usuario usuarioSelecionado;
	private Usuario usuarioSelecionadoExcluir;
	private List<Tenant> tenants;
	private List<Usuario> usuarios = new ArrayList<>();
	private List<Unidade> unidades = new ArrayList<>();
	private Unidade unidade;
	private long tenant;

	@Inject
	private TenantService tenantService;
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private LoginBean loginBean;
	
	@PostConstruct
	public void inicializar() {
		tenant = 4;
		usuarios = usuarioService.buscarUsuarios(loginBean.getUsuario().getUnidade(), tenant);
		log.info(usuarios);
	}
	public void setUsuarioSelecionadoExcluir(Usuario usuarioSelecionadoExcluir) {
		this.usuarioSelecionadoExcluir = usuarioSelecionadoExcluir;
		log.info("usuarioSelecionadoExcluir setado = " + usuarioSelecionadoExcluir.getNome());
	}
	
	public void excluir() {
		try {
			usuarioService.excluir(usuarioSelecionadoExcluir);
			this.usuarios.remove(usuarioSelecionadoExcluir);
			MessageUtil.sucesso("Usuario " + usuarioSelecionadoExcluir.getNome() + " excluído com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void reset() {
		try {
			usuarioService.reset(usuarioSelecionadoExcluir);	
			MessageUtil.sucesso("Senha do usuário " + usuarioSelecionadoExcluir.getNome() + " reinicializada com sucesso.");
			
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public boolean isCoordenador() {
		if((loginBean.getUsuario().getGrupo() == Grupo.COORDENADORES) || 
				(loginBean.getUsuario().getGrupo() == Grupo.GESTORES))
			return true;
		
		return false;
	}



}
