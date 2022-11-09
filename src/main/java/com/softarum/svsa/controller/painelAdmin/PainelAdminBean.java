package com.softarum.svsa.controller.painelAdmin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.service.TenantService;
import com.softarum.svsa.service.UnidadeService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Getter
@Setter
@Log4j
@Named
@ViewScoped
public class PainelAdminBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UnidadeService unidadeService;
	
	@Inject
	private LoginBean loginBean;
	
	private List<Unidade> unidades = new ArrayList<>();
	private List<Unidade> unidadesFiltradas = new ArrayList<>();
	
	private Tenant tenantId; 
	
	@Inject
	private TenantService tenantService;
	
	private List<Tenant> tenants;
	

//	loginBean.getTenantId(); 
	@PostConstruct
	public void inicializar() {
		tenants = tenantService.buscarTodos();
		unidades = unidadeService.buscarTodos(tenants.get(0).getCodigo());
		log.info(unidades.size());
		log.info(tenants.size());
	}
	
	public List<Unidade> buscaUnidades() {
		log.info(getTenantId().getCodigo());
		unidades = unidadeService.buscarTodos(getTenantId().getCodigo());
		return unidades;
	}
	
}
