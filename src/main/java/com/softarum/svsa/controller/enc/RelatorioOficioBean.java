package com.softarum.svsa.controller.enc;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Oficio;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;
import com.softarum.svsa.service.OficioService;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.util.MessageUtil;

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
public class RelatorioOficioBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;

	private int qdeTotal = 0;
	private List<Oficio> listaOficios = new ArrayList<>();
	private List<Unidade> unidades = new ArrayList<>();	
	private List<Oficio> listaGrafico = new ArrayList<>();

	private Unidade unidade;	
	private Date dataInicio;
	private Date dataFim;	
	private Oficio oficio;	
	private HorizontalBarChartModel barModel;
	private Boolean sasc = false;
	
	@Inject
	private OficioService oService;
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		
		barModel = new HorizontalBarChartModel();
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		this.unidade = loginBean.getUsuario().getUnidade();
		//consultarOficios();
	}	

	public void consultarOficios() {

		listaOficios = oService.buscarOficiosUnidade(unidade, dataInicio, dataFim, sasc, loginBean.getTenantId());
	
		qdeTotal = listaOficios.size();
		//log.info(" Tamanho da lista oficio: " + qdeTotal);
		
	}
	
	public void initGraficoBarras() {
		
		listaGrafico = oService.buscarOficiosGrafico(unidade, dataInicio, dataFim, sasc, loginBean.getTenantId());
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
   	
        	for(Oficio o: listaGrafico) {   
        		
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
	
	public void redirectPdf() throws IOException {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(oficio.getUrlAnexo());
    }
	
	public boolean isUnidadeSelecionada() {
		if(unidade != null)
			return true;
		return false;
	}

}
