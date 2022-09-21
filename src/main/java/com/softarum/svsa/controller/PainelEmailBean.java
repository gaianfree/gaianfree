package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.softarum.svsa.service.PainelEmailService;
import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Grupo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PainelEmailBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int QTD_GRUPOS = 4;
	
	private List<Tenant> municipios;
	private List<Unidade> unidades;
	private List<Grupo> grupos;
	
	private String assunto;
	private String corpo;
	
	private Tenant municipioSelecionado;
	private List<Unidade> unidadesSelecionadas;
	private List<Grupo> gruposSelecionados;
	
	@Inject
	private PainelEmailService painelEmailService;
	
	@PostConstruct
	public void inicializar() {
		this.setMunicipios(painelEmailService.getMunicipios());
		
		Tenant primeiroMunicipio = this.getMunicipios().get(0);
		this.setUnidades(
				painelEmailService.getUnidadesByMunicipio(primeiroMunicipio.getCodigo())
		);
		
		this.setGrupos(new ArrayList<Grupo>(QTD_GRUPOS));
		this.getGrupos().add(Grupo.ADMINISTRATIVOS);
		this.getGrupos().add(Grupo.TECNICOS);
		this.getGrupos().add(Grupo.COORDENADORES);
		this.getGrupos().add(Grupo.GESTORES);
	}
	
	public void atualizaUnidadesByMunicipio() {
		this.setUnidades(painelEmailService.getUnidadesByMunicipio(
				this.getMunicipioSelecionado().getCodigo())
		);
	}
	
	public void enviarEmail() {
		List<Long> idsUnidades = new ArrayList<Long>(this.getUnidadesSelecionadas().size());
		
		for (Unidade unidade : this.getUnidadesSelecionadas()) {
			idsUnidades.add(unidade.getCodigo());
		}
		
		painelEmailService.enviarEmail(
				this.getAssunto(),
				this.getCorpo(),
				this.getMunicipioSelecionado().getCodigo(),
				idsUnidades,
				this.getGruposSelecionados()
		);
	}
}
