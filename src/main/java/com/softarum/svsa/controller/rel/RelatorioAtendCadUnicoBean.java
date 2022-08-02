package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.PieChartModel;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.service.AgendamentoIndividualService;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.util.MessageUtil;

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
public class RelatorioAtendCadUnicoBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private PieChartModel graficoAtendimentos;	
	private int qdeAtendimentos = 0;
	private List<ListaAtendimento> listaAtendimentos = new ArrayList<>();
	private List<ListaAtendimento> listaAtendimentosGrafico = new ArrayList<>();
	private Unidade unidade;
	private List<Unidade> unidades = new ArrayList<>();	
	private Date dataInicio;
	private Date dataFim;
	private boolean cadUnico = false;
		
	
	@Inject
	AgendamentoIndividualService listaAtendimentoService;	
	@Inject
	UnidadeService unidadeService;
	@Inject
	LoginBean loginBean;
	
	@PostConstruct
	public void inicializar() {
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		graficoAtendimentos = new PieChartModel();
		
		unidade = loginBean.getUsuario().getUnidade();
		
		/*
		if( loginBean.getUsuario().getGrupo() == Grupo.CADUNICO) {
			setCadUnico(true);
			unidade=null;
		}
		*/
	}
	
	
	public void consultarCadUnicoPeriodo() {
		log.info("Consultando  ... ");
		
		listaAtendimentos =  listaAtendimentoService.buscarAtendCadUnicoPeriodo(unidade, dataInicio, dataFim, loginBean.getTenantId());
		qdeAtendimentos = listaAtendimentos.size();		
	}
	
	public void initGraficoAtendimentos() {	
		listaAtendimentosGrafico = listaAtendimentoService.buscarAtendCadUnicoPeriodo2(unidade, dataInicio, dataFim, loginBean.getTenantId());
		createPieModel();
	}

	private void createPieModel() {
		
		log.info("Criando grafico  ... ");	
		graficoAtendimentos = new PieChartModel(); 
        
        
        if(listaAtendimentosGrafico != null && listaAtendimentosGrafico.size() > 0) {

        	
        	log.info("Tamanho da lista: " + listaAtendimentosGrafico.size());        	
        	
        	String codigo = listaAtendimentosGrafico.get(0).getCodigoAuxiliar().name(); 
        	log.info("codAux: " + codigo);      
        	int qde = 0; 
	        for(ListaAtendimento l: listaAtendimentosGrafico) {   
        		
        		
	        	if(!l.getCodigoAuxiliar().name().equals(codigo)) {
	        		graficoAtendimentos.set(codigo, qde);
	            	
	            	codigo = l.getCodigoAuxiliar().name();
	            	qde = 1;	            	
	        	}
	        	else {
	        		qde++;
	        	}
	        }
	        log.info("criado o grafico");       
	        graficoAtendimentos.set(codigo, qde);	        
	        graficoAtendimentos.setLegendPosition("e");	        
	        
        }
        else
        	MessageUtil.alerta("Não existe atendimentos realizados.");
        
    }

}