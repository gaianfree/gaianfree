package com.softarum.svsa.controller;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

import com.softarum.svsa.modelo.Calendario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.service.CalendarioService;
import com.softarum.svsa.service.UsuarioService;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

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
public class CalendarScheduleBean implements Serializable {

	private static final long serialVersionUID = 6570142544541959632L;

	private ScheduleModel eventModel;
	private ScheduleEvent<?> event = new DefaultScheduleEvent<>();
	private List<Calendario> calendario = new ArrayList<>();
	private Unidade unidade;
	private List<Usuario> tecnicos;
	
	@Inject
	private CalendarioService calendarioService;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private LoginBean loginBean;

 
    @PostConstruct
    public void init() {
    	
    	unidade = loginBean.getUsuario().getUnidade(); 
    	this.tecnicos = usuarioService.buscarUsuarios(unidade, loginBean.getTenantId()); 
    	
    	event = DefaultScheduleEvent.builder()
				.startDate(DateUtils.asLocalDateTime(new Date()))
				.endDate(DateUtils.asLocalDateTime(new Date()))
				.data(new Calendario())
				.build();
    	
    	setEvent(event);
  
    	carregarCalendario();    	
    
    }
    
    
    private void carregarCalendario() {
    	
    	eventModel = new DefaultScheduleModel();
    	
    	try {
			calendario = calendarioService.buscarTodos(unidade, loginBean.getTenantId());
			
		} catch (ParseException e) {
			MessageUtil.erro("Problema com a data da consulta.");
			e.printStackTrace();
		}
    	
    	for(Calendario c : calendario) {  
    		    		
    		if(c.getTecnico() != null) {
    			event = DefaultScheduleEvent.builder()
    					.title(c.getTecnico().getNome() + " #" + c.getTitle())
    					.startDate(c.getStartDate())
    					.endDate(c.getEndDate())
    					.data(c)
    					.borderColor("orange")
    					.backgroundColor("orange")
    					.build();
    		}
    		else {
    			event = DefaultScheduleEvent.builder()
    					.title(c.getTitle())
    					.startDate(c.getStartDate())
    					.endDate(c.getEndDate())
    					.data(c)
    					.borderColor("red")
    					.backgroundColor("red")
    					.build();
    		}    		
			eventModel.addEvent(event);  
    	}
    	log.info("Carregado Calendário: " + calendario.size());
    }
     
    public void onEventSelect(SelectEvent<ScheduleEvent<?>> selectEvent) {
		event = selectEvent.getObject();	
        
        log.debug("event " + getEvent()); 
        
        if(( (Calendario)event.getData() ).getTecnico() != null) {
			
			try {
				// pega só o título, sem o nome do técnico
				String[] valorComSplit = event.getTitle().split("#", 2);
				((Calendario) event.getData()).setTitle(valorComSplit[1]); 
			}
			catch(ArrayIndexOutOfBoundsException e) {
				log.info("erro para limpar title " + event.getTitle()); 
			}
			
			log.debug("event.getTitle() " + event.getTitle()); 
        }
    }
    
 	public void onDateSelect(SelectEvent<?> selectEvent) { 
 		
 		event = DefaultScheduleEvent.builder()
 				.title("")
				.startDate((LocalDateTime) selectEvent.getObject())
				.endDate((LocalDateTime) selectEvent.getObject())
				.data(new Calendario())
				.build();
 		
 	    setEvent(event);
 	}

	public void addEvent() {       
		try {
			//log.info("adicionando novo....... " + event.getId()); 
	        if(event.getId() == null) {
	        	//log.info("event id = null....... " + event.getId()); 
	        	eventModel.addEvent(event);
	        	//log.info("event adicionado no eventmodel....... "); 
	        	
	            calendarioService.salvar(event, unidade, loginBean.getTenantId());
	            log.info("evento adicionado " + event.getId() + event.getTitle() + event.getStartDate()+ event.getEndDate()); 
	        }
	        else {	 
	        	//log.info("atualizando ....... " + event.getId());
	        	eventModel.updateEvent(event);
	        	//log.info("atualizado eventmodel....... " + event.getId());
	        	
	            calendarioService.atualizar(event);
	            log.info("evento alterado " + event.getId() + event.getTitle() + event.getStartDate()+ event.getEndDate()); 
	        }			
		} catch (NegocioException e) {
			MessageUtil.erro("Não foi possível salvar o evento : " + e.getMessage());
			e.printStackTrace();
		}
		//log.info("recarregando calendario....... " + event.getId());
		carregarCalendario();
		//log.info("criando novo event....... " );
        event = new DefaultScheduleEvent<>();
        //log.info("criado novo event....... " + event.getId());
    }
	
	public void deleteEvent() {  
		
		try {			
			calendarioService.excluir(event);
			eventModel.deleteEvent(event);
		} catch (NegocioException e) {
			MessageUtil.erro("Não foi possível excluir o evento " + event.getId());
			e.printStackTrace();
		}
		log.info("evento apagado " + event.getData());  
    }

	
}