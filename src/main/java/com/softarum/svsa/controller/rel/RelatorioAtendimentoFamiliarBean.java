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
import com.softarum.svsa.modelo.AgendamentoFamiliar;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.service.AgendamentoFamiliarService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

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
public class RelatorioAtendimentoFamiliarBean implements Serializable {
	
	private static final long serialVersionUID = 1769116747361287180L;

	private Long qdeTotal = 0L;
	private List<AgendamentoFamiliar> listaAtendimentoFamiliar = new ArrayList<>();
	private List<AgendamentoFamiliar> listaAtendimentoFamiliarGrafico = new ArrayList<>();
	private Unidade unidade;
	private AgendamentoFamiliar itemAlterar = new AgendamentoFamiliar();
	private AgendamentoFamiliar itemExcluir;
	
	private Date dataInicio;
	private Date dataFim;
	private AgendamentoFamiliar agendamentoFamiliar;
	
	private PieChartModel graficoAtendimentoFamiliar;
	
	
	@Inject
	AgendamentoFamiliarService agendamentoFamiliarService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		graficoAtendimentoFamiliar = new PieChartModel();
		this.unidade = loginBean.getUsuario().getUnidade();
		//consultarAtendimentos();
		
	}
	
	public void consultarAtendimentos() {
		
		listaAtendimentoFamiliar = agendamentoFamiliarService.buscarAtendimentosCodAux(unidade, dataInicio, dataFim, loginBean.getTenantId());
		
		qdeTotal = (long) listaAtendimentoFamiliar.size();	
	}
	
	public void initGraficoAtendimentoFamiliar() {		
		listaAtendimentoFamiliarGrafico =  listaAtendimentoFamiliar; 
		qdeTotal = Long.valueOf(listaAtendimentoFamiliarGrafico.size());
		createPieModel();
	}


	private void createPieModel() {
		
		log.info("Criando grafico  ... ");	
		graficoAtendimentoFamiliar = new PieChartModel(); 
        
        
        if(listaAtendimentoFamiliarGrafico != null && listaAtendimentoFamiliarGrafico.size() > 0) {

        	
        	log.info("Tamanho da lista: " + listaAtendimentoFamiliarGrafico.size());        	
        	
        	String codigo = listaAtendimentoFamiliarGrafico.get(0).getCodigoAuxiliar().toString();        	
        	int qde = 0; 
	        for(AgendamentoFamiliar l: listaAtendimentoFamiliarGrafico) {   
        		
        		
	        	if(!l.getCodigoAuxiliar().toString().equals(codigo)) {
	        		graficoAtendimentoFamiliar.set(codigo, qde);
	            	
	            	codigo = l.getCodigoAuxiliar().toString();
	            	qde = 1;	            	
	        	}
	        	else {
	        		qde++;
	        	}
	        }
	        log.info("criado o grafico");       
	        graficoAtendimentoFamiliar.set(codigo, qde);	        
	        graficoAtendimentoFamiliar.setLegendPosition("e");	        
	        
        }
        else
        	MessageUtil.alerta("Não existe atendimentos realizados.");
        
    }
	
	public void alterar() {
		
		try {
			agendamentoFamiliarService.salvarAlterar(itemAlterar, loginBean.getUsuario() );
			log.info("Atendimento Alterado por " + loginBean.getUserName());
			
			MessageUtil.sucesso("Atendimento familiar número (" + itemAlterar.getCodigo() + ") alterado com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void excluir() {
		try {
			agendamentoFamiliarService.excluir(itemExcluir);
			log.info("Atendimento DELETED por " + loginBean.getUserName());
			this.listaAtendimentoFamiliar.remove(itemExcluir);
			MessageUtil.sucesso("Atendimento familiar número (" + itemExcluir.getCodigo() + ") excluído com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}

	public boolean isCoordenador() {
		if(loginBean.getUsuario().getGrupo() == Grupo.COORDENADORES)
			return true;
		
		return false;
	}

}