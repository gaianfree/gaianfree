package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.service.PainelEmailService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class PainelEmailBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<Tenant> municipios;
	private List<Unidade> unidades;
	private List<Grupo> grupos;
	
	private String assunto = "";
	private String corpo = "";
	
	private Tenant municipioSelecionado = new Tenant();
	private List<Unidade> unidadesSelecionadas = new ArrayList<Unidade>();
	private List<Grupo> gruposSelecionados = new ArrayList<Grupo>();
	
	@Inject
	private PainelEmailService painelEmailService;
	
	@PostConstruct
	public void inicializar() {
		this.setMunicipios(painelEmailService.getMunicipios());
		
		Tenant primeiroMunicipio = this.getMunicipios().get(0);
		this.setUnidades(
				painelEmailService.getUnidadesByMunicipio(primeiroMunicipio.getCodigo())
		);
		
		this.setGrupos(Arrays.asList(Grupo.values()));
	}
	
	public void atualizaUnidadesByMunicipio() {
		this.setUnidades(painelEmailService.getUnidadesByMunicipio(
				this.getMunicipioSelecionado().getCodigo())
		);
	}
	
	public void enviarEmail() {
		log.info(this.getAssunto() + this.getCorpo() + this.getMunicipioSelecionado() + this.getUnidadesSelecionadas() + this.getGruposSelecionados());
		painelEmailService.enviarEmail(
				this.getAssunto(),
				this.getCorpo(),
				this.getMunicipioSelecionado().getCodigo(),
				this.getUnidadesSelecionadas(),
				this.getGruposSelecionados()
		);
	}
}
