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
import com.softarum.svsa.modelo.to.AtendimentoTO;
import com.softarum.svsa.service.ProdutividadeService;

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
public class RelatorioCadUnicoProdBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;

	private Integer qdeAtendimentos = 0;
	private List<AtendimentoTO> listaAtendimentos = new ArrayList<>();
	private Date dataInicio;
	private Date dataFim;
		
	

	@Inject
	LoginBean loginBean;
	@Inject
	ProdutividadeService prodService;
	
	@PostConstruct
	public void inicializar() {
		
	}
	
	public void consultarCadUnicoPeriodo() {
		log.info("Consultando  ... ");
		
		listaAtendimentos =  prodService.buscarAtendCaUnicoDTO(dataInicio, dataFim, loginBean.getTenantId());
		Collections.sort(listaAtendimentos);
		qdeAtendimentos = prodService.getTotalAtendimentos();
	}
		
}