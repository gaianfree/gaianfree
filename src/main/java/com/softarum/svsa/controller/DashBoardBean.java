package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.polar.PolarAreaChartDataSet;
import org.primefaces.model.charts.polar.PolarAreaChartModel;

import com.softarum.svsa.modelo.to.AtendimentoTO;
import com.softarum.svsa.service.DashBoardService;
import com.softarum.svsa.util.MessageUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * Essa classe cria o dashBoard do usuário com gráficos das qdes do mês de referência (RMA da unidade)
 * @author murakamiadmin
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class DashBoardBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
		
	private DashboardModel model;
	private BarChartModel barModel;
	private HorizontalBarChartModel  barModelAtend;
	private PolarAreaChartModel polarAreaModelInd;
	private PolarAreaChartModel polarAreaModelCol;
	private PolarAreaChartModel polarAreaModelFam;
	/*
	private PolarAreaChartModel polarAreaModelServ;
	private PolarAreaChartModel polarAreaModelPaif;
	*/
	private int qdeProntuariosNovos;		
	private int qdeAtendIndiv;
	private int qdeAtendColetivo;
	private int qdeAtendFamiliar;
	private long qdeGraficoHoriz;
	/*
	private int qdePaif;
	private int qdeScfv;
	*/
	
	private List<AtendimentoTO> listaGraficoHoriz = new ArrayList<>();
	
	@Inject
	DashBoardService dashService;
	@Inject
	LoginBean loginBean;
	
	
	@PostConstruct
	public void inicializar() {		
		
		model = new DefaultDashboardModel();
		
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        DashboardColumn column3 = new DefaultDashboardColumn();
                 
        column1.addWidget("qdes");       
        column2.addWidget("indiv");
        column3.addWidget("coletivo"); 
        column1.addWidget("familiar");
        column2.addWidget("paif");
        column3.addWidget("scfv");         
       
        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);
		
        criarGraficos();       
        
	} 
	
	private void criarGraficos() {
		
		/*
		 * Grafico de barras vertical atendimentos 
		 */
		/* prontuarios novos */
		setQdeProntuariosNovos((dashService.dashboardProntuarios(loginBean.getUsuario().getUnidade(), loginBean.getTenantId())).intValue());
		/* atendimentos individualizados */
		setQdeAtendIndiv((dashService.dashboardIndividualizados(loginBean.getUsuario().getUnidade(), loginBean.getTenantId())).intValue());
		/* atendimentos coletivos */
		setQdeAtendColetivo((dashService.dashboardColetivos(loginBean.getUsuario().getUnidade(), loginBean.getTenantId())).intValue());
		/* atendimentos coletivos */
		setQdeAtendFamiliar((dashService.dashboardFamiliares(loginBean.getUsuario().getUnidade(), loginBean.getTenantId())).intValue());
		/* acompanhamentos PAIF */
		//setQdePaif((dashService.dashboardPaif(loginBean.getUsuario().getUnidade())).intValue());	
		/* planos SCFV */
		//setQdeScfv((dashService.dashboardScfv(loginBean.getUsuario().getUnidade())).intValue());	
		
		createBarModel();
		
		/*
		 * Grafico de barras horizontal atendimentos 
		 */
		
		
		/*
		 * Grafico de Polar atendimentos 
		 */		
		createPolarModelIndiv(dashService.dashboardQdesCodAuxIndiv(loginBean.getUsuario().getUnidade(), loginBean.getTenantId()));
		createPolarModelColet(dashService.dashboardQdesCodAuxColet(loginBean.getUsuario().getUnidade(), loginBean.getTenantId()));
		createPolarModelFamiliar(dashService.dashboardQdesCodAuxFamiliar(loginBean.getUsuario().getUnidade(), loginBean.getTenantId()));
		/*
		createPolarModelServico(dashService.dashboardQdesScfv(loginBean.getUsuario().getUnidade()));
		createPolarModelPaif(dashService.dashboardQdesPaif(loginBean.getUsuario().getUnidade()));		
		*/
		
		consultaHorizAtend();		
	}
	
	

	/*
	 * Grafico Mensal 
	 */	
	public void createBarModel() {
		barModel = new BarChartModel();
	    ChartData data = new ChartData();
	     
	    BarChartDataSet barDataSet = new BarChartDataSet();
	    barDataSet.setLabel("Mês Corrente");
	     
	    List<Number> values = new ArrayList<>();
	    values.add(getQdeProntuariosNovos());
	    values.add(getQdeAtendIndiv());
	    values.add(getQdeAtendColetivo());
	    values.add(getQdeAtendFamiliar());
	    //values.add(getQdePaif());
	    //values.add(getQdeScfv());	    
	    barDataSet.setData(values);
	     
	    List<String> bgColor = new ArrayList<>();
	    bgColor.add("rgba(255, 99, 132, 0.2)");
	    bgColor.add("rgba(255, 159, 64, 0.2)");
	    bgColor.add("rgba(255, 205, 86, 0.2)");
	    bgColor.add("rgba(255, 160, 65, 0.2)");
	    bgColor.add("rgba(75, 192, 192, 0.2)");
	    bgColor.add("rgba(54, 162, 235, 0.2)");
	    barDataSet.setBackgroundColor(bgColor);
	     
	    List<String> borderColor = new ArrayList<>();
	    borderColor.add("rgb(255, 99, 132)");
	    borderColor.add("rgb(255, 159, 64)");
	    borderColor.add("rgb(255, 205, 86)");
	    borderColor.add("rgb(255, 160, 65)");
	    borderColor.add("rgb(75, 192, 192)");
	    borderColor.add("rgb(54, 162, 235)");
	    barDataSet.setBorderColor(borderColor);
	    barDataSet.setBorderWidth(1);
	     
	    data.addChartDataSet(barDataSet);
	     
	    List<String> labels = new ArrayList<>();
	    labels.add("Novos Prontuários");
	    labels.add("Atend. Individualizados");
	    labels.add("Atend. Coletivos");
	    labels.add("Atend. Familiares");
	    labels.add("Novas Famílias PAIF");
	    labels.add("Novos SCFV");
	    data.setLabels(labels);
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
	
	/*
	 * Barrra Horizontal Atendimentos
	 */
	private void consultaHorizAtend() {
		log.info("consultarAtendimentos");
		listaGraficoHoriz = dashService.relatorioAtendimentosTOCodAux(loginBean.getUsuario().getUnidade(), loginBean.getTenantId());
		qdeGraficoHoriz = 0L;
		
		log.info("Qde grafico: " + listaGraficoHoriz.size());
		 
		if(listaGraficoHoriz != null && listaGraficoHoriz.size() > 0) {	
			
			for (AtendimentoTO to: listaGraficoHoriz) {
				qdeGraficoHoriz = qdeGraficoHoriz + to.getQdeAtendimentos();
			}
			
			createBarHorizAtend();
		
		}		
	}
	
	private void createBarHorizAtend() {
		log.info("createBarHorizAtend");
		barModelAtend = new HorizontalBarChartModel();
	    ChartData data = new ChartData();
	     
	    HorizontalBarChartDataSet barDataSet = new HorizontalBarChartDataSet();
	    barDataSet.setLabel("Atendimentos mês corrente");
	    
	    List<Number> values = new ArrayList<>();
	    List<String> bgColor = new ArrayList<>();
	    List<String> borderColor = new ArrayList<>();
	    List<String> labels = new ArrayList<>();
	    

    	barDataSet.setLabel("nao tem atendimentos");
    	
    	values.add(0);
		labels.add("");
    	for(AtendimentoTO to: listaGraficoHoriz) {  
    		
    		//log.info("codaux: " + to.getNome() + " = " + to.getQdeAtendimentos());
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
	    barModelAtend.setData(data);
	    
	    //Options
	    BarChartOptions options = new BarChartOptions();
	    log.info("criou BarChartOptions");
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
	
	    barModelAtend.setOptions(options);        
	}

	
	/*
	 * Polar Atendimentos
	 */
    private void createPolarModelIndiv(List<AtendimentoTO> lista) {
    	polarAreaModelInd = new PolarAreaChartModel();
        ChartData data = new ChartData();
         
        PolarAreaChartDataSet dataSet = new PolarAreaChartDataSet();
        List<Number> values = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        
        if(!lista.isEmpty()) {
	        String codAux = lista.get(0).getNome();
	        int cont = 0;
	        for(AtendimentoTO to : lista) {
	        	
	        	if(!codAux.equals(to.getNome())) {
	        		values.add(cont);
	        		cont = 1;
	        		bgColors.add("rgb(" + getNumeroRgb() + ", "+ getNumeroRgb() + ", 0)");
	        		labels.add(codAux);
	        		codAux = to.getNome();        		
	        	}
	        	else {
	        		cont++;
	        	}        	
	        }
	        if(codAux.equals(lista.get(lista.size()-1).getNome())) {
	        	values.add(cont);
	        	bgColors.add("rgb(" + getNumeroRgb() + ", "+ getNumeroRgb() + ", 0)");
	    		labels.add(codAux);
	        }
        }
        
        dataSet.setData(values);        
        dataSet.setBackgroundColor(bgColors);         
        data.addChartDataSet(dataSet);
        data.setLabels(labels);        
         
        polarAreaModelInd.setData(data);
    }
    
    private void createPolarModelColet(List<AtendimentoTO> lista) {
    	polarAreaModelCol = new PolarAreaChartModel();
        ChartData data = new ChartData();
         
        PolarAreaChartDataSet dataSet = new PolarAreaChartDataSet();
        List<Number> values = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        
        if(!lista.isEmpty()) {
	        String codAux = lista.get(0).getNome();
	        int cont = 0;
	        for(AtendimentoTO to : lista) {
	        	
	        	if(!codAux.equals(to.getNome())) {
	        		values.add(cont);
	        		cont = 1;
	        		bgColors.add("rgb(" + getNumeroRgb() + ", "+ getNumeroRgb() + ", 0)");
	        		labels.add(codAux);
	        		codAux = to.getNome();        		
	        	}
	        	else {
	        		cont++;
	        	}        	
	        }
	        if(codAux.equals(lista.get(lista.size()-1).getNome())) {
	        	values.add(cont);
	        	bgColors.add("rgb(" + getNumeroRgb() + ", "+ getNumeroRgb() + ", 0)");
	    		labels.add(codAux);
	        }
        }
        
        dataSet.setData(values);        
        dataSet.setBackgroundColor(bgColors);         
        data.addChartDataSet(dataSet);
        data.setLabels(labels);        
         
        polarAreaModelCol.setData(data);
    }
    
    private void createPolarModelFamiliar(List<AtendimentoTO> lista) {
    	polarAreaModelFam = new PolarAreaChartModel();
        ChartData data = new ChartData();
         
        PolarAreaChartDataSet dataSet = new PolarAreaChartDataSet();
        List<Number> values = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        
        if(!lista.isEmpty()) {
	        String codAux = lista.get(0).getNome();
	        int cont = 0;
	        for(AtendimentoTO to : lista) {
	        	
	        	if(!codAux.equals(to.getNome())) {
	        		values.add(cont);
	        		cont = 1;
	        		bgColors.add("rgb(" + getNumeroRgb() + ", "+ getNumeroRgb() + ", 0)");
	        		labels.add(codAux);
	        		codAux = to.getNome();        		
	        	}
	        	else {
	        		cont++;
	        	}        	
	        }
	        if(codAux.equals(lista.get(lista.size()-1).getNome())) {
	        	values.add(cont);
	        	bgColors.add("rgb(" + getNumeroRgb() + ", "+ getNumeroRgb() + ", 0)");
	    		labels.add(codAux);
	        }
        }
        
        dataSet.setData(values);        
        dataSet.setBackgroundColor(bgColors);         
        data.addChartDataSet(dataSet);
        data.setLabels(labels);        
         
        polarAreaModelFam.setData(data);
    }
    
    /*
     * Retirado 
     * 
     * 
     *  <p:panel id="paif" header="Perfis PAIF/PAEFI">
	         <p:polarAreaChart model="#{dashBoardBean.polarAreaModelPaif}" style="width: 400px; height: 400px;"/>
	    </p:panel>
	    
	    <p:panel id="scfv" header="Serviços">
	         <p:polarAreaChart model="#{dashBoardBean.polarAreaModelServ}" style="width: 400px; height: 400px;"/>
	    </p:panel>
     
    
    private void createPolarModelServico(List<AtendimentoTO> lista) {
    	polarAreaModelServ = new PolarAreaChartModel();
        ChartData data = new ChartData();
         
        PolarAreaChartDataSet dataSet = new PolarAreaChartDataSet();
        List<Number> values = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        
        if(!lista.isEmpty()) {
	        String codAux = lista.get(0).getNome();
	        int cont = 0;
	        for(AtendimentoTO to : lista) {
	        	
	        	if(!codAux.equals(to.getNome())) {
	        		values.add(cont);
	        		cont = 1;
	        		bgColors.add("rgb(" + getNumeroRgb() + ", "+ getNumeroRgb() + ", 0)");
	        		labels.add(codAux);
	        		codAux = to.getNome();        		
	        	}
	        	else {
	        		cont++;
	        	}        	
	        }
	        if(codAux.equals(lista.get(lista.size()-1).getNome())) {
	        	values.add(cont);
	        	bgColors.add("rgb(" + getNumeroRgb() + ", "+ getNumeroRgb() + ", 0)");
	    		labels.add(codAux);
	        }
        }
        
        dataSet.setData(values);        
        dataSet.setBackgroundColor(bgColors);         
        data.addChartDataSet(dataSet);
        data.setLabels(labels);        
         
        polarAreaModelServ.setData(data);
    }
    
    private void createPolarModelPaif(List<PerfilFamiliaTO> lista) {
    	polarAreaModelPaif = new PolarAreaChartModel();
        ChartData data = new ChartData();
         
        PolarAreaChartDataSet dataSet = new PolarAreaChartDataSet();
        List<Number> values = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        
        List<PerfilFamilia> perfis = new ArrayList<>(); 
        
        for(PerfilFamiliaTO p : lista) {
        	perfis.addAll(p.getPlano().getPerfisFamilia());        	
        }
        Collections.sort(perfis);
        log.info("tamanho perfis = " + perfis.size());
        
        if(!perfis.isEmpty()) {
	        String perfil = perfis.get(0).getDescricao();
	        int cont = 0;
	        for(PerfilFamilia f : perfis) {
	        	
	        	if(!perfil.equals(f.getDescricao())) {
	        		values.add(cont);
	        		cont = 1;        		
	        		bgColors.add("rgb(" + getNumeroRgb() + ", "+ getNumeroRgb() + ", 0)");
	        		//log.info("cont = " + cont + " perfil= " + perfil);
	        		labels.add(perfil);
	        		perfil = f.getDescricao();        		
	        	}
	        	else {
	        		cont++;
	        	}        	
	        }
	        if(perfil.equals(perfis.get(perfis.size()-1).getDescricao())) {
	        	values.add(cont);
	        	bgColors.add("rgb(" + getNumeroRgb() + ", "+ getNumeroRgb() + ", 0)");
	        	//log.info("cont = " + cont + " perfil= " + perfil);
	    		labels.add(perfil);
	        }
        }
        
        dataSet.setData(values);        
        dataSet.setBackgroundColor(bgColors);         
        data.addChartDataSet(dataSet);
        data.setLabels(labels);        
         
        polarAreaModelPaif.setData(data);
    }
    */
    

    public void handleReorder(DashboardReorderEvent event) {
        MessageUtil.alerta("Reordenado.");
    }
     
    public void handleClose(CloseEvent event) {
    	 MessageUtil.alerta("Painel fechado.");
    }
     
    public void handleToggle(ToggleEvent event) {
    	MessageUtil.alerta("Toggle.");
    }
    
	private int getNumeroRgb() {
		return 5 + (int)(Math.random() * (240 - 5));
	}

}