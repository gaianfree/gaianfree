package com.softarum.svsa.controller.enc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.HistoricoEncaminhamento;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.service.HistoricoEncaminhamentoService;
import com.softarum.svsa.service.UnidadeService;

import lombok.Getter;
import lombok.Setter;

/**
 * @author murakamiadmin
 *
 */
@Getter
@Setter
@Named
@ViewScoped
public class RelatorioEncaminhamentoBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	//private LogUtil logUtil = new LogUtil(RelatorioEncaminhamentosBean.class);	

	private int qdeTotal = 0;
	private List<HistoricoEncaminhamento> listaEncaminhamentos = new ArrayList<>();
	private List<Unidade> unidades = new ArrayList<>();	
	private HistoricoEncaminhamento historicoEncaminhamento;
	private Boolean recebidos = false;

	private Unidade unidade;	
	private Date dataInicio;
	private Date dataFim;
	
	@Inject
	private HistoricoEncaminhamentoService encaminhamentoService;
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		this.unidade = loginBean.getUsuario().getUnidade();
		//consultarEncaminhamentos();
	}	

	public void consultarEncaminhamentos() {

		listaEncaminhamentos = encaminhamentoService.buscarEncaminhamentos(unidade, dataInicio, dataFim, recebidos, loginBean.getTenantId());
	
		qdeTotal = listaEncaminhamentos.size();
		
	}
	
	public boolean isUnidadeSelecionada() {
		if(unidade != null)
			return true;
		return false;
	}
	
}