package com.softarum.svsa.service;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.primefaces.model.ScheduleEvent;

import com.softarum.svsa.dao.CalendarioDAO;
import com.softarum.svsa.modelo.Calendario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 *
 */
public class CalendarioService implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(CalendarioService.class);
	
	@Inject
	private CalendarioDAO calendarioDAO;


	public Calendario salvar(ScheduleEvent<?> event, Unidade unidade, Long tenantId) throws NegocioException {
		
		Calendario calendario;
		
		if(((Calendario)event.getData()).getTecnico() != null) {
			calendario = new Calendario(event.getTitle(),
					event.getStartDate(),
					event.getEndDate(), 
					((Calendario)event.getData()).getTecnico(),  unidade);
			log.info("event.getData() COM tecnico = " + ((Calendario)event.getData()).getTecnico().getNome()); 
		}
		else {
			calendario = new Calendario(event.getTitle(),
					event.getStartDate(),
					event.getEndDate(), 
					null,  unidade);
			log.info("event.getData() SEM tecnico = ");
		}
		calendario.setTenant_id(tenantId);

		return this.calendarioDAO.merge(calendario);
	}
	
	public void atualizar(ScheduleEvent<?> event) throws NegocioException {
		
		//log.info("evento atz service " + ((Calendario)event.getData()).getCodigo());
		
		Calendario calendario = calendarioDAO.buscarPeloCodigo( ((Calendario)event.getData()).getCodigo() );
		
		if(((Calendario)event.getData()).getTecnico() != null) {
			
			/*
			try {
				// pega só o título, sem o nome do técnico
				String[] valorComSplit = event.getTitle().split("#", 2);
				calendario.setTitle(valorComSplit[1]); 
			}
			catch(ArrayIndexOutOfBoundsException e) {
				throw new NegocioException("Não é permitido técnico em feriado!");
			}
			*/
			
			
			calendario.setTecnico( ((Calendario)event.getData()).getTecnico() );
			
			log.info("event.getData() tecnico = " + ((Calendario)event.getData()).getTecnico().getNome()); 
		}
		else {
			if(calendario.getTecnico() != null) {
				throw new NegocioException("Não é permitido converter para feriado!");
			}
			calendario.setTitle(event.getTitle());
			calendario.setTecnico( null );			
		}		
		
		calendario.setStartDate(event.getStartDate());
		calendario.setEndDate(event.getEndDate());
		
		//log.info("calendario recuperado " + calendario.getCodigo() + " end " + calendario.getEndDate()); 
		this.calendarioDAO.merge(calendario);
	}
	
	public void excluir(ScheduleEvent<?> event) throws NegocioException {
		Calendario calendario = calendarioDAO.buscarPeloCodigo( ((Calendario)event.getData()).getCodigo());
		calendarioDAO.excluir(calendario);		
	}

	public Calendario buscarPeloCodigo(Long codigo) {
		return calendarioDAO.buscarPeloCodigo(codigo);
	}	

	public List<Calendario> buscarTodos(Unidade unidade, Long tenantId) throws ParseException {
		return calendarioDAO.buscarTodos(unidade, tenantId);
	}
		

	public CalendarioDAO getCalendarioDAO() {
		return calendarioDAO;
	}
}