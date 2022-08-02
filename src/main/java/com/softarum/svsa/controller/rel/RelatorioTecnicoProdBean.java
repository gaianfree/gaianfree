package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.modelo.to.AtendimentoTO;
import com.softarum.svsa.service.ProdutividadeService;
import com.softarum.svsa.service.UnidadeService;

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
public class RelatorioTecnicoProdBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
		
	private List<Unidade> unidades = new ArrayList<>();	
	private List<AtendimentoTO> listaAtendimentos = new ArrayList<>();
	private Date dataInicio;
	private Date dataFim;
	private Unidade unidade;
	private Integer total = 0;
	private Float media = 0.0f;
	private Long totalGeral = 0L;
	private Float mediaGeral = 0.0f;
		
	
	@Inject
	private LoginBean loginBean;
	@Inject
	private ProdutividadeService prodService;
	@Inject
	private UnidadeService unidadeService;
	
	@PostConstruct
	public void inicializar() {		
		
		if(isGestor()) {
			unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		}
		else {
			unidade = loginBean.getUsuario().getUnidade();
		}
	}
	
	public void consultarProdPeriodo() {
		log.info("Consultando produtividade tecnicos ... da unidade = " + unidade);
		
		listaAtendimentos =  prodService.buscarAtendTecnicoDTO(dataInicio, dataFim, unidade, loginBean.getTenantId());				
		Collections.sort(listaAtendimentos);
		
		total = prodService.getTotalAtendimentos();
		media = prodService.getMediaUnidade();
		
		totalGeral = prodService.getTotalGeral();
		mediaGeral = prodService.getMediaGeral();
	}

	public boolean isGestor() {
		if(loginBean.getUsuario().getGrupo() == Grupo.GESTORES) {
			return true;
		}
		return false;
	}

}