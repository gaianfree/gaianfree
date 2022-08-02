package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.service.CapaProntuarioService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author gabriel
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class RelatorioProntuariosNovosBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;

	private Long qdeProntuarios = (Long) 0L;
	private List<Prontuario> listaProntuarios;
	private Unidade unidade;
	private Date dataInicio;
	private Date dataFim;
	private Prontuario prontuario;

	@Inject
	CapaProntuarioService capaProntuarioService;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	public void inicializar() {

		unidade = loginBean.getUsuario().getUnidade();
	}

	public void consultarPeriodo() {
		log.info("Consultando  ... ");
		listaProntuarios = capaProntuarioService.buscarProntuariosPorData(unidade, dataInicio, dataFim, loginBean.getTenantId());
		qdeProntuarios = (long) listaProntuarios.size();
	}

}