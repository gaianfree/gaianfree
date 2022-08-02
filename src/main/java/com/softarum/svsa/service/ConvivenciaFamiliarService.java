package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.ConvivenciaFamiliarDAO;
import com.softarum.svsa.modelo.ConvivenciaFamiliar;
import com.softarum.svsa.modelo.ObsConvivenciaFamiliar;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.util.NegocioException;

/**
 * @author gabrielrodrigues
 *
 */
public class ConvivenciaFamiliarService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private ConvivenciaFamiliarDAO convivenciaDAO;

	public ConvivenciaFamiliar salvar(ConvivenciaFamiliar convivenciaFamiliar) throws NegocioException {			
		return convivenciaDAO.salvar(convivenciaFamiliar);
	}
	
	public List<ObsConvivenciaFamiliar> salvarObservacao(ConvivenciaFamiliar convivenciaFamiliar)
		throws NegocioException {
		return convivenciaDAO.salvarObservacao(convivenciaFamiliar);
	}

	public ConvivenciaFamiliar obterConvivenciaFamiliar(Prontuario prontuario, Long tenantId) {
		
		return convivenciaDAO.obterConvivenciaFamiliar(prontuario, tenantId);
	}
	
	public void setConvivenciaDAO(ConvivenciaFamiliarDAO convivenciaDAO) {
		this.convivenciaDAO = convivenciaDAO;
	}
}