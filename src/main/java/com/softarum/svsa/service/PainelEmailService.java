package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.util.EmailUtil;

import lombok.extern.log4j.Log4j;

@Log4j
public class PainelEmailService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UnidadeService unidadeService;
	
	@Inject
	private TenantService tenantService;
	
	@Inject
	private UsuarioService usuarioService;
	
	public List<Tenant> getMunicipios() {		
		return tenantService.buscarTodos();
	}
	
	public List<Unidade> getUnidadesByMunicipio(Long tenantId) {
		return unidadeService.buscarTodos(tenantId);
	}
	
	private List<Usuario> getUsuariosByUnidade(Unidade unidade, Long tenantId) {		
		return usuarioService.buscarUsuarios(unidade, tenantId);
	}
	
	public void enviarEmail(String assunto, String corpo, Long tenantId, List<Unidade> unidades, List<Grupo> perfis) {
		List<String> destinatarios = new ArrayList<>();	
		
		for(Unidade currentUnidade : unidades) {
			List<Usuario> currentUsuarios = getUsuariosByUnidade(currentUnidade, tenantId);
			
			for(Usuario currentUsuario : currentUsuarios) {
				if(perfis.contains(currentUsuario.getGrupo())) {
					destinatarios.add(currentUsuario.getEmail());
					log.info(currentUsuario.getEmail());
				}
			}
		}

		EmailUtil.sendEmail("SSL", destinatarios, assunto, corpo);
	}
	
}
