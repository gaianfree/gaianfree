package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Grupo;

import gaian.mail.EmailUtil;


public class PainelEmailService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	@Inject
	private UnidadeService unidadeService;
	
	@Inject
	private UsuarioService usuarioService;
	
	@SuppressWarnings("unchecked")
	public List<Tenant> getMunicipios() {
		return manager.createNamedQuery("Tenant.buscarTodos")
				.getResultList();
	}
	
	public List<Unidade> getUnidadesByMunicipio(Long tenantId) {
		return unidadeService.buscarTodos(tenantId);
	}
	
	private Unidade getUnidadeById(Long unidadeId) {
		return unidadeService.buscarPeloCodigo(unidadeId);
	}
	
	private List<Usuario> getUsuariosByUnidade(Unidade unidade, Long tenantId) {		
		return usuarioService.buscarUsuarios(unidade, tenantId);
	}
	
	public void enviarEmail(String assunto, String corpo, Long tenantId, List<Long> unidades, List<Grupo> perfis) {
		List<String> destinatarios = new ArrayList<>();
		
		for(Long id : unidades) {
			Unidade currentUnidade = getUnidadeById(id);
			List<Usuario> currentUsuarios = getUsuariosByUnidade(currentUnidade, tenantId);
			for(Usuario currentUsuario : currentUsuarios) {
				if(perfis.contains(currentUsuario.getGrupo())) {
					destinatarios.add(currentUsuario.getEmail());
				}
			}
		}
		
		EmailUtil.sendEmail("SSL", destinatarios, assunto, corpo);
	}
	
}
