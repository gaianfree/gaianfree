package com.softarum.svsa.controller.enc;

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

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.OficioEmitido;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;
import com.softarum.svsa.service.OficioEmitidoService;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.util.MessageUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author Lauro
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class RelatorioOficioEmitidoBean implements Serializable {

	private static final long serialVersionUID = 1L;	

	private int qdeTotal = 0;
	private List<OficioEmitido> listaOficiosEmitidos = new ArrayList<>();
	private List<Unidade> unidades = new ArrayList<>();	
	private List<OficioEmitido> listaGrafico = new ArrayList<>();

	private Unidade unidade;	
	private Date dataInicio;
	private Date dataFim;	
	private OficioEmitido oficioEmitido;	
	private HorizontalBarChartModel barModel;
	
	@Inject
	private OficioEmitidoService oService;
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		
		barModel = new HorizontalBarChartModel();
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		this.unidade = loginBean.getUsuario().getUnidade();
		//consultarOficiosEmitidos();
	}	

	public void consultarOficiosEmitidos() {

		listaOficiosEmitidos = oService.buscarOficiosEmitidosUnidade(unidade, dataInicio, dataFim, loginBean.getTenantId());
	
		qdeTotal = listaOficiosEmitidos.size();
		//log.info(" Tamanho da lista oficio emitido: " + qdeTotal);
		
	}
	
	public void initGraficoBarras() {
		
		listaGrafico = oService.buscarOficiosEmitidosGrafico(unidade, dataInicio, dataFim, loginBean.getTenantId());
		log.info("Qde encOutros: " + listaGrafico.size());
		createBarModel();
	}
	private void createBarModel() {
        barModel = initBarModel();
 
        barModel.setTitle("Encaminhamentos por Código");
        barModel.setLegendPosition("n");
        barModel.setShowPointLabels(true);
        
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Qde");
        xAxis.setMin(0);
        xAxis.setMax(50);  
 
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Encaminhamento"); 
        
    }
	private HorizontalBarChartModel initBarModel() {
	       
		HorizontalBarChartModel model = new HorizontalBarChartModel();
		ChartSeries orgaos = new ChartSeries();
		orgaos.setLabel("quantidades");
        
        if(listaGrafico != null && listaGrafico.size() > 0) {
        	
        	int qde = 0;
        	CodigoEncaminhamento codigo = listaGrafico.get(0).getCodigoEncaminhamento();
   	
        	for(OficioEmitido o: listaGrafico) {   
        		
        		if(o.getCodigoEncaminhamento().equals(codigo)) {        			
        			qde++;
        		}
        		else {
        			log.info(codigo + " : "  + qde);
        			orgaos.set(codigo, qde);
        			codigo = o.getCodigoEncaminhamento();        			
        			qde = 1;
        		}        		
        		orgaos.set(codigo, qde);
	        }  
        }
        else
        	MessageUtil.alerta("Não existe encaminhamentos.");
 
        model.addSeries(orgaos);         
        return model;
    }
	
	public boolean isUnidadeSelecionada() {
		if(unidade != null)
			return true;
		return false;
	}
	
}
