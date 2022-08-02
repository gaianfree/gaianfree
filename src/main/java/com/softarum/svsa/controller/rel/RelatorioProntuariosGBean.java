package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.softarum.svsa.modelo.to.ProntuarioUnidadeTO;
import com.softarum.svsa.service.CapaProntuarioService;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.util.MessageUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * @author murakamiadmin
 *
 */
@Getter
@Setter
@Named(value="relatorioProntuariosGBean")
@ViewScoped
public class RelatorioProntuariosGBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private PieChartModel graficoProntuarios;	
	private HorizontalBarChartModel barModel;
	private Long qdeProntuarios = 0L;
	private Long qde = 0L;
	private Long qdeAtivos = 0L;
	private List<ProntuarioUnidadeTO> listaProntuarios = new ArrayList<>();	
	private Unidade unidade;
	private List<Unidade> unidades = new ArrayList<>();	
		
	
	@Inject
	CapaProntuarioService prontuarioService;
	@Inject
	UnidadeService unidadeService;
	@Inject
	private LoginBean loginBean;		
	
	@PostConstruct
	public void inicializar() {			
		graficoProntuarios = new PieChartModel();
		barModel = new HorizontalBarChartModel();
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		listaProntuarios = prontuarioService.consultarProntuariosRelTO(unidade, unidades, loginBean.getTenantId());
		setQde(prontuarioService.encontrarTotalDeProntuarios(loginBean.getTenantId()));
		setQdeAtivos(prontuarioService.encontrarProntuariosAtivos(loginBean.getTenantId()));

	}
	
	public void initGraficoProntuarios() {
				
		if(listaProntuarios.isEmpty())
			listaProntuarios = prontuarioService.consultarProntuariosRelTO(unidade, unidades, loginBean.getTenantId());
		
		createPieModel();
	}
	
	public void initGraficoBarras() {
		
		if(listaProntuarios.isEmpty())
			listaProntuarios = prontuarioService.consultarProntuariosRelTO(unidade, unidades, loginBean.getTenantId());
		
		createBarModel();
	}

	private void createPieModel() {
		
		graficoProntuarios = new PieChartModel(); 
		qdeProntuarios = 0L;
        
        if(listaProntuarios != null && listaProntuarios.size() > 0) {
        	
        	//logUtil.info("Tamanho da lista: " + listaProntuarios.size());  
        	
        	for(ProntuarioUnidadeTO to: listaProntuarios) {   
        		
	        		graficoProntuarios.set(to.getNomeUnidade(), to.getQdeProntuarios());
	        		qdeProntuarios = qdeProntuarios + to.getQdeProntuarios();
	            	
	        }
	        //logUtil.info("criado o grafico"); 	       
	        graficoProntuarios.setLegendPosition("e");	        
	        
        }
        else
        	MessageUtil.alerta("Não existe prontuarios.");
       
    }
	
	private HorizontalBarChartModel initBarModel() {
       
		HorizontalBarChartModel model = new HorizontalBarChartModel();
		qdeProntuarios = 0L;
		ChartSeries unidades = new ChartSeries();
		unidades.setLabel("Prontuarios");
        
        if(listaProntuarios != null && listaProntuarios.size() > 0) {
        	
        	for(ProntuarioUnidadeTO to: listaProntuarios) {   
        		
        		unidades.set(to.getNomeUnidade(), to.getQdeProntuarios());
	        	qdeProntuarios = qdeProntuarios + to.getQdeProntuarios();
	            	
	        }  
        }
        else
        	MessageUtil.alerta("Não existe prontuarios.");
 
        model.addSeries(unidades);
         
        return model;
    }
 
    private void createBarModel() {
        barModel = initBarModel();
 
        barModel.setTitle("Qde de Prontuários");
        barModel.setLegendPosition("ne");
 
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Qde");
        xAxis.setMin(0);
        xAxis.setMax(10000);        
 
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Unidades"); 
        
    }
    
    public Long getQdeInativos() {
		return qde - qdeAtivos;
	}
	
}