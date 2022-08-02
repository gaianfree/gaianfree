package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.softarum.svsa.modelo.AgendamentoColetivo;
import com.softarum.svsa.modelo.AgendamentoFamiliar;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.service.AgendamentoColetivoService;
import com.softarum.svsa.service.AgendamentoFamiliarService;
import com.softarum.svsa.service.AgendamentoIndividualService;
import com.softarum.svsa.util.DateUtils;

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
public class AgendaScheduleBean implements Serializable {

	private static final long serialVersionUID = 6570142544541959632L;

	private ScheduleModel eventModel;
	private ScheduleEvent<?> event = new DefaultScheduleEvent<>();
	private List<ListaAtendimento> listaAtendimentos = new ArrayList<>();
	private List<AgendamentoColetivo> coletivos = new ArrayList<>();
	private List<AgendamentoFamiliar> familiares = new ArrayList<>();
	
		
	@Inject
	AgendamentoIndividualService listaAtendimentoService;
	@Inject
	AgendamentoColetivoService coletivoService;
	@Inject
	AgendamentoFamiliarService familiarService;
	@Inject
	LoginBean loginBean;
    
 
    @PostConstruct
    public void init() {
    	
    	listaAtendimentos = listaAtendimentoService.buscarAtendimentosAgendados(loginBean.getUsuario().getUnidade(), loginBean.getTenantId());
    	//log.info("Qde agendamentos ind: " + listaAtendimentos.size());
    	coletivos = coletivoService.buscarAtendimentosAgendados(loginBean.getUsuario().getUnidade(), loginBean.getTenantId());
    	//log.info("Qde agendamentos col: " + coletivos.size());
    	familiares = familiarService.buscarAtendimentosAgendados(loginBean.getUsuario().getUnidade(), loginBean.getTenantId());
    	//log.info("Qde agendamentos fam: " + familiares.size());
    	
    	eventModel = new DefaultScheduleModel();
    	
    	carregarAgenda();
    	carregarAgendaCol();
    	carregarAgendaFamiliar();
    }
    
    private void carregarAgenda() {
    	
    	log.debug("Qde agendamentos: " + listaAtendimentos.size());    	
    	
    	for(ListaAtendimento l : listaAtendimentos) { 
            
    		if( l.getRole() == Role.CADASTRADOR ) {    
    			log.debug("cadastrador");

    			DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
    					.title("[CAD] " + l.getPessoa().getNome())
    					.startDate(DateUtils.asLocalDateTime(l.getDataAgendamento()))
    					.endDate(DateUtils.asLocalDateTime(l.getDataAgendamento()))    					
    					.borderColor("blue")
    					.backgroundColor("blue")
    					.build();
    			eventModel.addEvent(event);  
    		}
    		else {
    			if(l.getTecnico() != null) {
    				log.debug("com tecnico");
    				event = DefaultScheduleEvent.builder()
        					.title("[IND] " + l.getPessoa().getNome())
        					.startDate(DateUtils.asLocalDateTime(l.getDataAgendamento()))
        					.endDate(DateUtils.asLocalDateTime(l.getDataAgendamento()))   
        					.description(""+l.getTecnico().getNome())
        					.borderColor("blue")
        					.backgroundColor("blue")
        					.build();    				
    				eventModel.addEvent(event);  
    				//eventModel.addEvent(new DefaultScheduleEvent("[IND] " + l.getPessoa().getNome() + " (" + l.getTecnico().getNome() + ")", l.getDataAgendamento(), l.getDataAgendamento()));
    			}
    			else {
    				log.debug("sem tecnico");
    				event = DefaultScheduleEvent.builder()
        					.title("[IND] " + l.getPessoa().getNome())
        					.startDate(DateUtils.asLocalDateTime(l.getDataAgendamento()))
        					.endDate(DateUtils.asLocalDateTime(l.getDataAgendamento()))   
        					.borderColor("blue")
        					.backgroundColor("blue")
        					.build();
    				eventModel.addEvent(event);     				
    			}
    		}    		 		
    	}    	
    }
    
    private void carregarAgendaCol() {
    	
    	log.debug("Qde agendamentos coletivos: " + coletivos.size());
    	
    	for(AgendamentoColetivo a : coletivos) { 
    		
    		log.debug("hora col db " + a.getDataAgendamento());
    		log.debug("a " + a.getCodigo());
    		log.debug("a " + a.getResumoAtendimento());
    		log.debug("a pessoas " + a.getPessoas().size());
    		log.debug("a tecnicos " + a.getTecnicos().size());
    		
    		
    		if(a.getTecnico() != null) {
				log.debug("com tecnico");
				event = DefaultScheduleEvent.builder()
    					.title("[COL] " + a.getPessoas().get(0).getNome())
    					.startDate(DateUtils.asLocalDateTime(a.getDataAgendamento()))
    					.endDate(DateUtils.asLocalDateTime(a.getDataAgendamento()))   
    					.description("" + a.getTecnico().getNome())
    					.borderColor("magenta")
    					.backgroundColor("magenta")
    					.build(); 
				eventModel.addEvent(event);  			
			}
			else {
				log.debug("sem tecnico");    			
				event = DefaultScheduleEvent.builder()
    					.title("[COL] " + a.getPessoas().get(0).getNome())
    					.startDate(DateUtils.asLocalDateTime(a.getDataAgendamento()))
    					.endDate(DateUtils.asLocalDateTime(a.getDataAgendamento()))   
    					.borderColor("magenta")
    					.backgroundColor("magenta")
    					.build(); 
				eventModel.addEvent(event);  
			}
    	}    	
    }
    
    private void carregarAgendaFamiliar() {
    	
    	log.debug("Qde agendamentos familiar: " + familiares.size());
    	
    	for(AgendamentoFamiliar a : familiares) { 
    		
    		log.debug("hora fam db " + a.getDataAgendamento());
    		
    		if(a.getTecnico() != null) {
				log.debug("com tecnico");
				event = DefaultScheduleEvent.builder()
    					.title("[FAM] " + a.getPessoas().get(0).getNome())
    					.startDate(DateUtils.asLocalDateTime(a.getDataAgendamento()))
    					.endDate(DateUtils.asLocalDateTime(a.getDataAgendamento()))   
    					.description("" + a.getTecnico().getNome())
    					.borderColor("green")
    					.backgroundColor("green")
    					.build(); 
				eventModel.addEvent(event);  				
			}
			else {
				log.debug("sem tecnico");    			
				event = DefaultScheduleEvent.builder()
    					.title("[FAM] " + a.getPessoas().get(0).getNome())
    					.startDate(DateUtils.asLocalDateTime(a.getDataAgendamento()))
    					.endDate(DateUtils.asLocalDateTime(a.getDataAgendamento()))   
    					.borderColor("green")
    					.backgroundColor("green")
    					.build(); 
				eventModel.addEvent(event);  
			}
    	}    	
    }
	
	public void onEventSelect(SelectEvent<?> selectEvent) {
       setEvent((DefaultScheduleEvent<?>) selectEvent.getObject());
	}

}