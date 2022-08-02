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
import com.softarum.svsa.modelo.AgendamentoColetivo;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.service.AgendamentoColetivoService;
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
public class RelatorioAtendimentoColetivoBean implements Serializable {
	
	private static final long serialVersionUID = 1769116747361287180L;

	private Long qdeTotal = 0L;
	private List<AgendamentoColetivo> listaAtendimentoColetivo = new ArrayList<>();
	//private List<AgendamentoColetivo> listaAtendimentoColetivoGrafico = new ArrayList<>();
	private Unidade unidade;
	private AgendamentoColetivo agendamentoColetivo;

	private Date dataInicio;
	private Date dataFim;
	
	private PieChartModel graficoAtendimentoColetivo;
	
	@Inject
	AgendamentoColetivoService agendamentoColetivoService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		graficoAtendimentoColetivo = new PieChartModel();
		this.unidade = loginBean.getUsuario().getUnidade();
		//consultarAtendimentos();
		
	}
	
	public void consultarAtendimentos() {
		
		listaAtendimentoColetivo = agendamentoColetivoService.buscarAtendimentosCodAux(unidade, dataInicio, dataFim, loginBean.getTenantId());
		
		qdeTotal = (long) listaAtendimentoColetivo.size();	
	}
	
	
	public void initGraficoAtendimentoColetivo() {		
		
		qdeTotal = Long.valueOf(listaAtendimentoColetivo.size());
		createPieModel();
	}
	
	private void createPieModel() {
		
		log.info("Criando grafico  ... ");	
		graficoAtendimentoColetivo = new PieChartModel(); 
        
        
        if(listaAtendimentoColetivo != null && listaAtendimentoColetivo.size() > 0) {

        	
        	log.info("Tamanho da lista: " + listaAtendimentoColetivo.size());        	
        	
        	String codigo = listaAtendimentoColetivo.get(0).getCodigoAuxiliar().toString();        	
        	int qde = 0; 
	        for(AgendamentoColetivo l: listaAtendimentoColetivo) {   
        		
        		
	        	if(!l.getCodigoAuxiliar().toString().equals(codigo)) {
	        		graficoAtendimentoColetivo.set(codigo, qde);
	            	
	            	codigo = l.getCodigoAuxiliar().toString();
	            	qde = 1;	            	
	        	}
	        	else {
	        		qde++;
	        	}
	        }
	        log.info("criado o grafico");       
	        graficoAtendimentoColetivo.set(codigo, qde);	        
	        graficoAtendimentoColetivo.setLegendPosition("e");	        
	        
        }
        else
        	MessageUtil.alerta("Não existe atendimentos realizados.");
        
    }
	
}
	
	