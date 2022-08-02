package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.HistoricoTransferencia;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.service.HistoricoTransferenciaService;
import com.softarum.svsa.service.UnidadeService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author Talita
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class RelatorioProntuariosTransferidosBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;

	private int qdeTotal = 0;
	private List<HistoricoTransferencia> listaTransferencias = new ArrayList<>();
	private List<Unidade> unidades = new ArrayList<>();	
	private HistoricoTransferencia historicoTransferencia;

	private Unidade unidade;
	private Date dataInicio;
	private Date dataFim;
	
	@Inject
	private HistoricoTransferenciaService historicoTransferenciaService;
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		this.unidade = loginBean.getUsuario().getUnidade();
		//consultarHistoricoTransferencia();
	}	

	public void consultarHistoricoTransferencia() {

		listaTransferencias = historicoTransferenciaService.buscarTodosPeriodo(unidade, dataInicio, dataFim, loginBean.getTenantId());
		
		qdeTotal = listaTransferencias.size();	
		log.info("Qde transferidos: " + qdeTotal);
		
	}
	
	public boolean isUnidadeSelecionada() {
		if(unidade != null)
			return true;
		return false;
	}

}