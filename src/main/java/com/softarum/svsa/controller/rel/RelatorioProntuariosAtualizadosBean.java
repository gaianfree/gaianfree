package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.service.CapaProntuarioService;
import com.softarum.svsa.service.UnidadeService;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gabriel
 *
 */
@Getter
@Setter
@Named("relProntuariosAttBean")
@ViewScoped
public class RelatorioProntuariosAtualizadosBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	private Logger log = Logger.getLogger(RelatorioProntuariosAtualizadosBean.class);
	private Long qdeProntuarios = (Long) 0L;
	private List<Prontuario> listaProntuarios;
	private Unidade unidade;
	private List<Unidade> unidades = new ArrayList<>();	
	private Date dataInicio;
	private Date dataFim;
	private Prontuario prontuario;

	@Inject
	CapaProntuarioService capaProntuarioService;
	@Inject
	private LoginBean loginBean;
	@Inject
	private UnidadeService unidadeService;

	@PostConstruct
	public void inicializar() {
		
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());

		unidade = loginBean.getUsuario().getUnidade();
	}

	public void consultarPeriodo() {
		log.info("Consultando  ... ");
		listaProntuarios = capaProntuarioService.buscarProntuariosPorDataModificacao(unidade, dataInicio, dataFim, loginBean.getTenantId());
		qdeProntuarios = (long) listaProntuarios.size();
	}
	
}