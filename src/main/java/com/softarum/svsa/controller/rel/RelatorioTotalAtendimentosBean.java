package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.AtendimentoTO;
import com.softarum.svsa.service.AgendamentoIndividualService;
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
public class RelatorioTotalAtendimentosBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private Long qdeAtendimentos = 0L;
	private Long qdeGrafico = 0L;	
	private List<AtendimentoTO> listaAtendimentos = new ArrayList<>();
	private List<AtendimentoTO> listaGrafico = new ArrayList<>();	
	private HorizontalBarChartModel  barModel = new HorizontalBarChartModel ();
	private Unidade unidade;		
	
	@Inject
	private AgendamentoIndividualService listaAtendimentoService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		
		listaAtendimentos = listaAtendimentoService.relatorioAtendimentosTO(loginBean.getTenantId());	
		
		log.debug("Qde atendimentos: " + listaAtendimentos.size());
		for (AtendimentoTO to: listaAtendimentos) {
			qdeAtendimentos = qdeAtendimentos + to.getQdeAtendimentos();
		}

		initGraficoBarras();
	}	
	
	private void initGraficoBarras() {
		log.debug("initGraficoBarras");
		consultarAtendimentos() ;
		createBarModel();
	}
	
	private void consultarAtendimentos() {
		log.debug("consultarAtendimentos");
		listaGrafico = listaAtendimentoService.relatorioAtendimentosTOCodAux(loginBean.getTenantId());
		
		log.debug("Qde grafico: " + listaGrafico.size());
		qdeGrafico = 0L;
		
		for (AtendimentoTO to: listaGrafico) {
			qdeGrafico = qdeGrafico + to.getQdeAtendimentos();
		}
	}
	
	private void createBarModel() {
		
		barModel = new HorizontalBarChartModel ();
	    ChartData data = new ChartData();
	     
	    HorizontalBarChartDataSet barDataSet = new HorizontalBarChartDataSet();
	    barDataSet.setLabel("Tipo de Atendimento");
	    
	    List<Number> values = new ArrayList<>();
	    List<String> bgColor = new ArrayList<>();
	    List<String> borderColor = new ArrayList<>();
	    List<String> labels = new ArrayList<>();
	    
	    if(listaGrafico != null && listaGrafico.size() > 0) {

	    	values.add(0);
    		labels.add("");
	    	for(AtendimentoTO to: listaGrafico) {  
        		
        		//log.debug("codaux: " + to.getNome() + " = " + to.getQdeAtendimentos());
        		values.add(to.getQdeAtendimentos());
        		labels.add(to.getNome());
        		bgColor.add("rgba(255, 159, 64, 0.2)");
        		borderColor.add("rgb(255, 159, 64)");
	        }
	    	values.add(0);
    		labels.add("");
        	
        	barDataSet.setData(values);        	
        	barDataSet.setBackgroundColor(bgColor);
        	barDataSet.setBorderColor(borderColor);
    	    barDataSet.setBorderWidth(1);
    	    data.setLabels(labels);
    	    data.addChartDataSet(barDataSet);
    	    barModel.setData(data);
    	    
    	    //Options
    	    BarChartOptions options = new BarChartOptions();
    	    CartesianScales cScales = new CartesianScales();
    	    CartesianLinearAxes linearAxes = new CartesianLinearAxes();
    	    CartesianLinearTicks ticks = new CartesianLinearTicks();
    	    ticks.setBeginAtZero(true);
    	    linearAxes.setTicks(ticks);
    	    cScales.addYAxesData(linearAxes);
    	    options.setScales(cScales);
    	    
    	    Legend legend = new Legend();
    	    legend.setDisplay(true);
    	    legend.setPosition("top");
    	    LegendLabel legendLabels = new LegendLabel();
    	    legendLabels.setFontStyle("bold");
    	    legendLabels.setFontColor("#2980B9");
    	    legendLabels.setFontSize(24);
    	    legend.setLabels(legendLabels);
    	    options.setLegend(legend);
    	
    	    barModel.setOptions(options);
        }
        else
        	MessageUtil.alerta("Não existe atendimentos.");
	}

}