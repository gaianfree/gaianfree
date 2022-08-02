package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.IndProtecao;
import com.softarum.svsa.modelo.SituacaoProtecao;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.IndicadorTO;
import com.softarum.svsa.service.IndicadoresService;
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
public class RelIndicadorProtecaoBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private List<IndicadorTO> indicadores1 = new ArrayList<>();
	private List<IndicadorTO> indicadores2 = new ArrayList<>();
	private List<IndicadorTO> indicadoresGeral = new ArrayList<>();
	private List<Unidade> unidades = new ArrayList<>();
	private List<SituacaoProtecao> situacoesProtecao = new ArrayList<>();
	private List<IndProtecao> indsProtecao = new ArrayList<>();
	private Unidade unidade;
	private SituacaoProtecao sitProtecao;
	private String serie = "mensal";  // ou anual
	
	private LineChartModel  lineModel1 = new LineChartModel();
	private LineChartModel  lineModel2 = new LineChartModel();
	private LineChartModel  lineModel3 = new LineChartModel();
	private LineChartModel  lineModel4 = new LineChartModel();
		
	@Inject
	private IndicadoresService indicadoresService;
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {			
		
		situacoesProtecao = indicadoresService.buscarSituacoes();
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		createLineModelMensal();
	}
	
	public void consultarGeral() {
		log.info("consultar geral ");
		
		lineModel1 = new LineChartModel();
		
		if(serie.equals("mensal")) {
			createLineModelMensal();
		}
		else {
			createLineModelAnual();
		}
	}
	
	//mensal
	public void createLineModelMensal() {
		log.debug("createLineModelmensal");	
		
		ChartData data = new ChartData();	    
        CartesianScales cScales = new CartesianScales();
        indicadores1 = new ArrayList<>();   
        indicadoresGeral = new ArrayList<>();
        Random random = new Random();
        
        Integer id = 1;
        for (SituacaoProtecao sc : situacoesProtecao) { 
        	
        	indicadores1 = indicadoresService.buscarSituacoesMensalTO(sc, loginBean.getTenantId());
			log.debug("Qde indicadores1 mensal: " + indicadores1.size());
			
        	indicadoresGeral.addAll(indicadores1);  // para mostrar no dataTable
        	
        	LineChartDataSet dataSet = new LineChartDataSet(); 
        	List<Object> values = new ArrayList<>();        	
            
            for(IndicadorTO to: indicadores1) {            	
            	values.add(to.getQde());	            	
            }
            
            List<String> labels = new ArrayList<>();
            labels.add("Jan");
	        labels.add("Fev");
	        labels.add("Mar");
	        labels.add("Abr");
	        labels.add("Mai");
	        labels.add("Jun");
	        labels.add("Jul");
	        labels.add("Ago");
	        labels.add("Set");
	        labels.add("Out");
	        labels.add("Nov");
	        labels.add("Dez");
	        data.setLabels(labels);
            
            dataSet.setData(values);
            dataSet.setLabel(sc.getDescricao());
            dataSet.setFill(false);
            dataSet.setBorderColor("rgb(" + (id + random.nextInt(200)) + "," + (id + random.nextInt(200)) + ", " + (id + random.nextInt(200)) + ")");
            dataSet.setTension(0);            
            data.addChartDataSet(dataSet);
            
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setId("right-y-axis");
            cScales.addYAxesData(linearAxes);
            id++;
                       
        }        
       
        lineModel1.setData(data);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Gráfico por Situações de Proteção/Desproteção");
        
        LineChartOptions options = new LineChartOptions();
        options.setTitle(title);
        options.setScales(cScales);
        lineModel1.setOptions(options);
        
	}
	// anual
	public void createLineModelAnual() {
		log.info("createLineModelAnual");	
		
		ChartData data = new ChartData();	    
        CartesianScales cScales = new CartesianScales();
        indicadores1 = new ArrayList<>();
        indicadoresGeral = new ArrayList<>();
        Random random = new Random();
        
        Integer id = 1;
        for (SituacaoProtecao sc : situacoesProtecao) { 
        	
        	indicadores1 = indicadoresService.buscarSituacoesAnualTO(sc, loginBean.getTenantId());
			log.debug("Qde indicadores1 anual: " + indicadores1.size());
			
        	indicadoresGeral.addAll(indicadores1);  // para mostrar no dataTable
        	
        	LineChartDataSet dataSet = new LineChartDataSet(); 
        	List<Object> values = new ArrayList<>();        	
        	List<String> labels = new ArrayList<>();
        	 
            for(IndicadorTO to: indicadores1) {            	
            	values.add(to.getQde());
            	labels.add(String.valueOf(to.getAno()));
            }
            
	        data.setLabels(labels);
            
            dataSet.setData(values);
            dataSet.setLabel(sc.getDescricao());
            dataSet.setFill(false);
            dataSet.setBorderColor("rgb(" + (id + random.nextInt(200)) + "," + (id + random.nextInt(200)) + ", " + (id + random.nextInt(200)) + ")");
            dataSet.setTension(0);            
            data.addChartDataSet(dataSet);
            
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setId("right-y-axis");
            cScales.addYAxesData(linearAxes);
            id++;
                       
        }        
       
        lineModel1.setData(data);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Gráfico por Situações de Proteção/Desproteção");
        
        LineChartOptions options = new LineChartOptions();
        options.setTitle(title);
        options.setScales(cScales);
        lineModel1.setOptions(options);        
	}
	
	/*
	 * consulta indicadores geral por situação selecionada
	 */
	public void consultarPorSituacao() {
		log.debug("consultar geral por situacao = " + sitProtecao.getCodigo());
		
		lineModel2 = new LineChartModel();
		
		if(serie.equals("mensal")) {
			createLMSituacaoMensal();
			
		}
		else {
			createLMSituacaoAnual();
		}		
	}
	public void createLMSituacaoMensal() {
		
		log.info("createLMSituacaoMensal");	
		
		ChartData data = new ChartData();	    
        CartesianScales cScales = new CartesianScales();
        indicadores2 = new ArrayList<>();   
        indicadoresGeral = new ArrayList<>();
        Random random = new Random();
        
        indsProtecao = sitProtecao.getIndicadores();
        
        Integer id = 1;
        for(IndProtecao ind: indsProtecao) {
        	
        	indicadores2 = indicadoresService.buscarIndsProtecaoMensalTO(ind, loginBean.getTenantId());
        	indicadoresGeral.addAll(indicadores2);  // para mostrar no dataTable        	
        	
        	LineChartDataSet dataSet = new LineChartDataSet(); 
        	List<Object> values = new ArrayList<>();        	
            
            for(IndicadorTO to: indicadores2) {            	
            	values.add(to.getQde());	            	
            }
            
            List<String> labels = new ArrayList<>();
            labels.add("Jan");
	        labels.add("Fev");
	        labels.add("Mar");
	        labels.add("Abr");
	        labels.add("Mai");
	        labels.add("Jun");
	        labels.add("Jul");
	        labels.add("Ago");
	        labels.add("Set");
	        labels.add("Out");
	        labels.add("Nov");
	        labels.add("Dez");
	        data.setLabels(labels);
            
            dataSet.setData(values);
            dataSet.setLabel(ind.getDescricao());
            dataSet.setFill(false);
            dataSet.setBorderColor("rgb(" + (id + random.nextInt(200)) + "," + (id + random.nextInt(200)) + ", " + (id + random.nextInt(200)) + ")");
            dataSet.setTension(0);            
            data.addChartDataSet(dataSet);
            
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setId("right-y-axis");
            cScales.addYAxesData(linearAxes);
            id++;
                       
        }        
       
        lineModel2.setData(data);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Gráfico de Indicadores por Indicador de Proteção");
        
        LineChartOptions options = new LineChartOptions();
        options.setTitle(title);
        options.setScales(cScales);
        lineModel2.setOptions(options);

	}
	public void createLMSituacaoAnual() {	
		
		log.info("createLMSituacaoAnual");	
		
		ChartData data = new ChartData();	    
        CartesianScales cScales = new CartesianScales();
        indicadores2 = new ArrayList<>();
        indicadoresGeral = new ArrayList<>();
        Random random = new Random();
        
        indsProtecao = sitProtecao.getIndicadores();
        
        Integer id = 1;
        for(IndProtecao ind: indsProtecao) {
        	
        	indicadores2 = indicadoresService.buscarIndsProtecaoAnualTO(ind, loginBean.getTenantId());
        	indicadoresGeral.addAll(indicadores2);  // para mostrar no dataTable
        	
        	LineChartDataSet dataSet = new LineChartDataSet(); 
        	List<Object> values = new ArrayList<>();        	
        	List<String> labels = new ArrayList<>();
        	 
            for(IndicadorTO to: indicadores2) {            	
            	values.add(to.getQde());
            	labels.add(String.valueOf(to.getAno()));
            }
            
	        data.setLabels(labels);
            
            dataSet.setData(values);
            dataSet.setLabel(ind.getDescricao());
            dataSet.setFill(false);
            dataSet.setBorderColor("rgb(" + (id + random.nextInt(200)) + "," + (id + random.nextInt(200)) + ", " + (id + random.nextInt(200)) + ")");
            dataSet.setTension(0);            
            data.addChartDataSet(dataSet);
            
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setId("right-y-axis");
            cScales.addYAxesData(linearAxes);
            id++;
                       
        }        
       
        lineModel2.setData(data);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Gráfico de Indicadores por Indicador de Proteção");
        
        LineChartOptions options = new LineChartOptions();
        options.setTitle(title);
        options.setScales(cScales);
        lineModel2.setOptions(options);        
	}
	
	/*
	 * consulta indicadores por unidade geral
	 */
	
	public void consultarUnidadeGeral() {
		log.info("consultarUnidadeGeral");
		
		lineModel3 = new LineChartModel();
		
		if(serie.equals("mensal")) {
			createLMUnidadeMensal();
		}
		else {
			createLMUnidadeAnual();
		}
		
	}

	public void createLMUnidadeMensal() {
		log.info("createLMUnidadeMensal");	
		
		ChartData data = new ChartData();	    
        CartesianScales cScales = new CartesianScales();
        indicadores1 = new ArrayList<>();   
        indicadoresGeral = new ArrayList<>();
        Random random = new Random();
        
        log.info("unidade: " + unidade.getCodigo());
        
        Integer id = 1;
        for (SituacaoProtecao sc : situacoesProtecao) { 
        	
        	indicadores1 = indicadoresService.buscarSituacoesUnidadeMensalTO(sc, unidade, loginBean.getTenantId());
  
			log.debug("Qde indicadores1 mensal: " + indicadores1.size());
			
        	indicadoresGeral.addAll(indicadores1);  // para mostrar no dataTable
        	
        	LineChartDataSet dataSet = new LineChartDataSet(); 
        	List<Object> values = new ArrayList<>();        	
            
            for(IndicadorTO to: indicadores1) {            	
            	values.add(to.getQde());	            	
            }
            
            List<String> labels = new ArrayList<>();
            labels.add("Jan");
	        labels.add("Fev");
	        labels.add("Mar");
	        labels.add("Abr");
	        labels.add("Mai");
	        labels.add("Jun");
	        labels.add("Jul");
	        labels.add("Ago");
	        labels.add("Set");
	        labels.add("Out");
	        labels.add("Nov");
	        labels.add("Dez");
	        data.setLabels(labels);
            
            dataSet.setData(values);
            dataSet.setLabel(sc.getDescricao());
            dataSet.setFill(false);
            dataSet.setBorderColor("rgb(" + (id + random.nextInt(200)) + "," + (id + random.nextInt(200)) + ", " + (id + random.nextInt(200)) + ")");
            dataSet.setTension(0);            
            data.addChartDataSet(dataSet);
            
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setId("right-y-axis");
            cScales.addYAxesData(linearAxes);
            id++;
                       
        }        
       
        lineModel3.setData(data);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Gráfico por Situações de Proteção/Desproteção");
        
        LineChartOptions options = new LineChartOptions();
        options.setTitle(title);
        options.setScales(cScales);
        lineModel3.setOptions(options);
        
	}
	
	//unidade anual
	public void createLMUnidadeAnual() {
		log.info("createLineModelAnual");	
		
		ChartData data = new ChartData();	    
        CartesianScales cScales = new CartesianScales();
        indicadores1 = new ArrayList<>();
        indicadoresGeral = new ArrayList<>();
        Random random = new Random();
        
        Integer id = 1;
        for (SituacaoProtecao sc : situacoesProtecao) { 
        	
        	indicadores1 = indicadoresService.buscarSituacoesUnidadeAnualTO(sc, unidade, loginBean.getTenantId());
			log.debug("Qde indicadores1 anual: " + indicadores1.size());
			
        	indicadoresGeral.addAll(indicadores1);  // para mostrar no dataTable
        	
        	LineChartDataSet dataSet = new LineChartDataSet(); 
        	List<Object> values = new ArrayList<>();        	
        	List<String> labels = new ArrayList<>();
        	 
            for(IndicadorTO to: indicadores1) {            	
            	values.add(to.getQde());
            	labels.add(String.valueOf(to.getAno()));
            }
            
	        data.setLabels(labels);
            
            dataSet.setData(values);
            dataSet.setLabel(sc.getDescricao());
            dataSet.setFill(false);
            dataSet.setBorderColor("rgb(" + (id + random.nextInt(200)) + "," + (id + random.nextInt(200)) + ", " + (id + random.nextInt(200)) + ")");
            dataSet.setTension(0);            
            data.addChartDataSet(dataSet);
            
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setId("right-y-axis");
            cScales.addYAxesData(linearAxes);
            id++;
                       
        }        
       
        lineModel3.setData(data);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Gráfico por Situações de Proteção/Desproteção");
        
        LineChartOptions options = new LineChartOptions();
        options.setTitle(title);
        options.setScales(cScales);
        lineModel3.setOptions(options);        
	}

	/*
	 * consulta indicadores por unidade e situacao
	 */
	
	public void consultarUnidadePorSituacao() {
		log.info("consultarUnidadePorSituacao");
		
		lineModel4 = new LineChartModel();
		
		if(serie.equals("mensal")) {
			createLMSituacaoUnidadeMensal();
			
		}
		else {
			createLMSituacaoUnidadeAnual();
		}
	}
	
public void createLMSituacaoUnidadeMensal() {
		
		ChartData data = new ChartData();	    
        CartesianScales cScales = new CartesianScales();
        indicadores2 = new ArrayList<>();   
        indicadoresGeral = new ArrayList<>();
        Random random = new Random();
        
        indsProtecao = sitProtecao.getIndicadores();
        
        Integer id = 1;
        for(IndProtecao ind: indsProtecao) {
        	
        	indicadores2 = indicadoresService.buscarIndsProtecaoUnidadeMensalTO(ind, unidade, loginBean.getTenantId());
        	indicadoresGeral.addAll(indicadores2);  // para mostrar no dataTable        	
        	
        	LineChartDataSet dataSet = new LineChartDataSet(); 
        	List<Object> values = new ArrayList<>();        	
            
            for(IndicadorTO to: indicadores2) {            	
            	values.add(to.getQde());	            	
            }
            
            List<String> labels = new ArrayList<>();
            labels.add("Jan");
	        labels.add("Fev");
	        labels.add("Mar");
	        labels.add("Abr");
	        labels.add("Mai");
	        labels.add("Jun");
	        labels.add("Jul");
	        labels.add("Ago");
	        labels.add("Set");
	        labels.add("Out");
	        labels.add("Nov");
	        labels.add("Dez");
	        data.setLabels(labels);
            
            dataSet.setData(values);
            dataSet.setLabel(ind.getDescricao());
            dataSet.setFill(false);
            dataSet.setBorderColor("rgb(" + (id + random.nextInt(200)) + "," + (id + random.nextInt(200)) + ", " + (id + random.nextInt(200)) + ")");
            dataSet.setTension(0);            
            data.addChartDataSet(dataSet);
            
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setId("right-y-axis");
            cScales.addYAxesData(linearAxes);
            id++;
                       
        }        
       
        lineModel4.setData(data);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Gráfico de Indicadores por Indicador de Proteção");
        
        LineChartOptions options = new LineChartOptions();
        options.setTitle(title);
        options.setScales(cScales);
        lineModel4.setOptions(options);

	}
	public void createLMSituacaoUnidadeAnual() {	
		
		ChartData data = new ChartData();	    
        CartesianScales cScales = new CartesianScales();
        indicadores2 = new ArrayList<>();
        indicadoresGeral = new ArrayList<>();
        Random random = new Random();
        
        indsProtecao = sitProtecao.getIndicadores();
        
        Integer id = 1;
        for(IndProtecao ind: indsProtecao) {
        	
        	indicadores2 = indicadoresService.buscarIndsProtecaoUnidadeAnualTO(ind, unidade, loginBean.getTenantId());
        	indicadoresGeral.addAll(indicadores2);  // para mostrar no dataTable
        	
        	LineChartDataSet dataSet = new LineChartDataSet(); 
        	List<Object> values = new ArrayList<>();        	
        	List<String> labels = new ArrayList<>();
        	 
            for(IndicadorTO to: indicadores2) {            	
            	values.add(to.getQde());
            	labels.add(String.valueOf(to.getAno()));
            }
            
	        data.setLabels(labels);
            
            dataSet.setData(values);
            dataSet.setLabel(ind.getDescricao());
            dataSet.setFill(false);
            dataSet.setBorderColor("rgb(" + (id + random.nextInt(200)) + "," + (id + random.nextInt(200)) + ", " + (id + random.nextInt(200)) + ")");
            dataSet.setTension(0);            
            data.addChartDataSet(dataSet);
            
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setId("right-y-axis");
            cScales.addYAxesData(linearAxes);
            id++;
                       
        }        
       
        lineModel4.setData(data);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Gráfico de Indicadores por Indicador de Proteção");
        
        LineChartOptions options = new LineChartOptions();
        options.setTitle(title);
        options.setScales(cScales);
        lineModel4.setOptions(options);        
	}
	
}