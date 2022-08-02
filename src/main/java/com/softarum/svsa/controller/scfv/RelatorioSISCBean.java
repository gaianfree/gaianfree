package com.softarum.svsa.controller.scfv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Servico;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.service.ServicoService;
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
public class RelatorioSISCBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private int qdeServicos = 0;
	private int qdePessoas = 0;
	private List<Servico> listaServicos = new ArrayList<>();
	private List<Unidade> unidades = new ArrayList<>();	

	private Unidade unidade;	
	private Date dataInicio;
	private Date dataFim;
	private Servico servico;
	private Pessoa pessoa;
	
	@Inject
	ServicoService servicoService;
	@Inject
	UnidadeService unidadeService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		this.unidade = loginBean.getUsuario().getUnidade();
		//consultarServicos();
	}
	
	public void consultarServicos() {
		if(getUnidade().getTipo() == TipoUnidade.SCFV) {
			listaServicos = servicoService.buscarServicosExec(unidade, dataInicio, dataFim, loginBean.getTenantId());
			qdePessoas = (int) servicoService.buscarQdePessoasExec(getUnidade(), dataInicio, dataFim, loginBean.getTenantId());
		}
		else {
			listaServicos = servicoService.buscarServicos(unidade, dataInicio, dataFim, loginBean.getTenantId());
			qdePessoas = (int) servicoService.buscarQdePessoas(getUnidade(), dataInicio, dataFim, loginBean.getTenantId());
		}
		qdeServicos = listaServicos.size();
		log.info("qde servicos: " + qdeServicos + "qde pessoas: " + qdePessoas );
		 
	}

	public boolean isUnidadeSelecionada() {
		if(unidade != null)
			return true;
		return false;
	}

}