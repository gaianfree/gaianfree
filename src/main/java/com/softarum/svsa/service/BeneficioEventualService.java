package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.BeneficioEventualDAO;
import com.softarum.svsa.modelo.BeneficioEventual;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.util.NegocioException;

/**
 * @author gabrielrodrigues
 *
 */
public class BeneficioEventualService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private BeneficioEventualDAO beneficioDAO;
	
	public BeneficioEventualDAO getAcessoDAO() {
		return beneficioDAO;
	}

	public void setSituacaoDAO(BeneficioEventualDAO beneficioDAO) {
		this.beneficioDAO = beneficioDAO;
	}

	public void salvar(BeneficioEventual beneficio) throws NegocioException {			
		this.beneficioDAO.salvar(beneficio);
	}	
	
	public List<BeneficioEventual> buscarBeneficios(Prontuario prontuario, Long tenantId) {
		return beneficioDAO.buscarBeneficios(prontuario, tenantId);
	}
}