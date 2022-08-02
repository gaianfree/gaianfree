package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.CondicaoHabitacionalDAO;
import com.softarum.svsa.modelo.CondicaoHabitacional;
import com.softarum.svsa.modelo.ObsCondicaoHabitacional;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.util.NegocioException;

/**
 * @author gabrielrodrigues  - refactored by Murakami
 *
 */
public class CondicaoHabitacionalService implements Serializable {

	private static final long serialVersionUID = 1L;
	//private LogUtil logUtil = new LogUtil(CondicaoHabitacionalService.class);
	@Inject
	private CondicaoHabitacionalDAO habitacionalDAO;


	

	public CondicaoHabitacional salvar(CondicaoHabitacional condicaoHabitacional) throws NegocioException {			
		return habitacionalDAO.salvar(condicaoHabitacional);
	}
	
	public List<ObsCondicaoHabitacional> salvarObservacao(CondicaoHabitacional condicaoHabitacional)
		throws NegocioException {
		return habitacionalDAO.salvarObservacao(condicaoHabitacional);
	}

	public CondicaoHabitacional obterCondicaohabitacional(Prontuario prontuario, Long tenantId) {
		
		return habitacionalDAO.obterCondicaohabitacional(prontuario, tenantId);
	}
	
	public void setHabitacionalDAO(CondicaoHabitacionalDAO habitacionalDAO) {
		this.habitacionalDAO = habitacionalDAO;
	}
	
}