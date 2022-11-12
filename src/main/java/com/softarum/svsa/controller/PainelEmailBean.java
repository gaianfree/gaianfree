package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.SelectEvent;

import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.service.PainelEmailService;
import com.softarum.svsa.util.MessageUtil;

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
	
	@Inject
	private LoginBean loginBean;
	
	@PostConstruct
	public void inicializar() {
		List<Tenant> municipios = painelEmailService.getMunicipios();
		this.setMunicipios(municipios);		
		
		this.setUnidades(
				painelEmailService.getUnidadesByMunicipio(municipios.get(0).getCodigo())
		);
		
		this.setGrupos(Arrays.asList(Grupo.values()));
	}
	
	public void atualizaUnidadesByMunicipio(final AjaxBehaviorEvent event) {
		Tenant municipioSelecionado = (Tenant) ((SelectOneMenu) event.getSource()).getValue();
		this.setUnidades(painelEmailService.getUnidadesByMunicipio(
				municipioSelecionado.getCodigo())
		);
	}
	
	public void enviarEmail() {
		try {
			log.info(this.getAssunto() + this.getCorpo() + this.getMunicipioSelecionado() + this.getUnidadesSelecionadas() + this.getGruposSelecionados());
			
			painelEmailService.enviarEmail(
					this.getAssunto(),
					this.getCorpo(),
					this.getMunicipioSelecionado().getCodigo(),
					this.getUnidadesSelecionadas(),
					this.getGruposSelecionados());
			
			MessageUtil.info("Mensagens enviadas com sucesso!");
		}		
		catch(Exception e) {			
			MessageUtil.erro(e.getMessage());
			e.printStackTrace();			
		}
		
	}
}
