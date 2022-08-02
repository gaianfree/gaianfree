package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.PieChartModel;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.AtendimentoTO;
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
public class RelatorioAtendimentosUnidadesBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private PieChartModel graficoAtendimentos;	
	private HorizontalBarChartModel barModel;
	private Long qdeAtendimentos = 0L;
	private List<AtendimentoTO> listaAtendimentos = new ArrayList<>();
	private List<Unidade> unidades = new ArrayList<>();	
	private Unidade unidade;	
	private Date dataInicio;
	private Date dataFim;
	
	@Inject
	AgendamentoIndividualService listaAtendimentoService;
	@Inject
	UnidadeService unidadeService;
	@Inject
	LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	

		graficoAtendimentos = new PieChartModel();
		barModel = new HorizontalBarChartModel();
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
	}
	
	public void consultarAtendimentos() {
		listaAtendimentos = listaAtendimentoService.relatorioAtendimentosTOCodAux(unidade, dataInicio, dataFim, loginBean.getTenantId());
		
		qdeAtendimentos = 0L;
		
		for (AtendimentoTO to: listaAtendimentos) {
			qdeAtendimentos = qdeAtendimentos + to.getQdeAtendimentos();
		}
	}


	public void initGraficoAtendimentos() {		
		consultarAtendimentos();
		createPieModel();	
	}
	
	public void initGraficoBarras() {		
		consultarAtendimentos();		
		createBarModel();
	}


	private void createPieModel() {
		
		graficoAtendimentos = new PieChartModel(); 
		
		listaAtendimentos = listaAtendimentoService.relatorioAtendimentosTOCodAux(unidade, dataInicio, dataFim, loginBean.getTenantId());
        
		if (listaAtendimentos != null && listaAtendimentos.size() > 0) {

			log.info("Tamanho da lista: " + listaAtendimentos.size());

			for (AtendimentoTO to : listaAtendimentos) {

				graficoAtendimentos.set(to.getNome(), to.getQdeAtendimentos());

			}

			graficoAtendimentos.setLegendPosition("e");

		}
        else
        	MessageUtil.alerta("Não existe atendimentos.");
        
    }
	
	private HorizontalBarChartModel initBarModel() {
	       
		HorizontalBarChartModel model = new HorizontalBarChartModel();
		ChartSeries codAux = new ChartSeries();
		codAux.setLabel("Atendimentos");
        
        if(listaAtendimentos != null && listaAtendimentos.size() > 0) {
        	
        	for(AtendimentoTO to: listaAtendimentos) {   
        		
        		codAux.set(to.getNome(), to.getQdeAtendimentos());            	
	        }  
        }
        else
        	MessageUtil.alerta("Não existe atendimentos.");
 
        model.addSeries(codAux);
         
        return model;
    }
 
  
    private void createBarModel() {
        barModel = initBarModel();
 
        barModel.setTitle("Qde de Atendimentos");
        barModel.setLegendPosition("ne");
 
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Atendimentos");
        xAxis.setMin(0);
        xAxis.setMax(qdeAtendimentos);     
 
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Tipos"); 
        
    }
    
	public boolean isUnidadeSelecionada() {
		if(unidade != null)
			return true;
		return false;
	}

}