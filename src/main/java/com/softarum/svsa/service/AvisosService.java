package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.HistoricoTransferenciaDAO;
import com.softarum.svsa.dao.OficioDAO;
import com.softarum.svsa.modelo.HistoricoEncaminhamento;
import com.softarum.svsa.modelo.Oficio;
import com.softarum.svsa.modelo.Unidade;


/**
 * @author murakamiadmin
 *
 */
public class AvisosService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private HistoricoTransferenciaDAO historicoDAO;
	
	@Inject
	private OficioDAO oficioDAO;

	public List<HistoricoEncaminhamento> buscarEncaminhamentosPendentes(Unidade unidade, Long tenantId) {
		return historicoDAO.buscarEncaminhamentosPendentes(unidade, tenantId);
	}

	public List<HistoricoEncaminhamento> buscarEncaminhamentosRecebidos(Unidade unidade, Long tenantId) {
		return historicoDAO.buscarEncaminhamentosRecebidos(unidade, tenantId);
	}

	public List<Oficio> buscarOficiosPendentes(Unidade unidade, Long tenantId) {
		
		return oficioDAO.buscarOficiosPendentes(unidade, tenantId);
	}
	
	
}