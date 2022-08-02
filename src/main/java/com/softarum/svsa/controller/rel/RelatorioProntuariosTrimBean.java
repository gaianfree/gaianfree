package com.softarum.svsa.controller.rel;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Ano;
import com.softarum.svsa.modelo.to.ProntuarioUnidadeTO;
import com.softarum.svsa.service.CapaProntuarioService;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author Talita/Murakami
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class RelatorioProntuariosTrimBean implements Serializable{
	

	private static final long serialVersionUID = 1769116747361287180L;
	
	private LineChartModel lineModel;
	private List<ProntuarioUnidadeTO> prontuariosTO = new ArrayList<>();
	private Unidade unidade;
	private List<Unidade> unidades = new ArrayList<>();
	private List<String> anos = new ArrayList<>(Arrays.asList(Ano.getAnos()));
	private Integer ano;
	

	@Inject
	private CapaProntuarioService prontuarioService;
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private LoginBean loginBean;
	
	@PostConstruct
	public void inicializar() {
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		LocalDate data = LocalDate.now();
		setAno(data.getYear());		
		setUnidade(loginBean.getUsuario().getUnidade());		
		criarGrafico();		
    }
	
	public LineChartModel criarGrafico() {
		
		try {
			prontuariosTO = prontuarioService.buscarProntuariosGrafico(getUnidade(), getAno(), loginBean.getTenantId());
			log.debug("Qde TOs: " + prontuariosTO.size());
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.alerta(e.getMessage());
		}
		
		lineModel = createLineChart();
		
		return lineModel;
	}
	
	private LineChartModel createLineChart() {
		
		LineChartSeries novos = new LineChartSeries();
		novos.setLabel("Novos");
		LineChartSeries inativos = new LineChartSeries();
		inativos.setLabel("Inativos");
		LineChartSeries excluidos = new LineChartSeries();
		excluidos.setLabel("Excluídos");  
		LineChartSeries paif = new LineChartSeries();
		paif.setLabel("PAIF/PAEFI"); 
		
		int trimestre = 1;
		long qdemax = 0;
		for(ProntuarioUnidadeTO to : prontuariosTO) {
			
			novos.set("" + trimestre +"º Tri", to.getQdeProntuarios());			 
			inativos.set("" + trimestre +"º Tri", to.getQdeProntuariosAtivos()); 	        
			excluidos.set("" + trimestre +"º Tri", to.getQdeProntuariosExcluidos());
	        paif.set("" + trimestre +"º Tri", to.getQdeProntuariosPAIF());
	        log.debug(to.getQdeProntuarios());
	        trimestre++;
	        
	        if(qdemax < to.getQdeProntuarios()) {
	        	qdemax = to.getQdeProntuarios();
	        }
	  
		}
		
        lineModel = new LineChartModel();
        lineModel.addSeries(novos);
        lineModel.addSeries(inativos);
        lineModel.addSeries(excluidos);
        lineModel.addSeries(paif);
        lineModel.setTitle("Prontuários criados ao longo do ano");
        lineModel.setLegendPosition("e");
        lineModel.setShowPointLabels(true);
        lineModel.getAxes().put(AxisType.X, new CategoryAxis(ano.toString()));
        Axis yAxis = lineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Qde de Prontuários");
        yAxis.setMin(0);
        yAxis.setMax(qdemax);      
        
        return lineModel;
    }
	
}
