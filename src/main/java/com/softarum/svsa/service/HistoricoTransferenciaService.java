package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.HistoricoTransferenciaDAO;
import com.softarum.svsa.modelo.HistoricoTransferencia;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 *
 */
public class HistoricoTransferenciaService implements Serializable {

	private static final long serialVersionUID = 1L;
	//private LogUtil logUtil = new LogUtil(HistoricoTransferenciaService.class);
	
	@Inject
	private HistoricoTransferenciaDAO historicoDAO;
	
	
	/*
	 * Historico de Transferencia
	 */
	
	public void salvar(HistoricoTransferencia historico) throws NegocioException {
		historico.setDataTransferencia(new Date());
		this.historicoDAO.salvar(historico);
	}
	
	public List<HistoricoTransferencia> buscarTodos(Long tenantId) {
		return historicoDAO.buscarTodos(tenantId);
	}

	public List<HistoricoTransferencia> buscarTodos(Prontuario prontuario, Long tenantId) {
		return historicoDAO.buscarTodos(prontuario, tenantId);
	}
	
	/*
	 * Relatorio
	 */
	public List<HistoricoTransferencia> buscarTodosPeriodo(Unidade unidade, Date dataInicio, Date dataFim, Long tenantId) {
		
		if(dataInicio != null) {
			if(dataFim != null) {
				return historicoDAO.buscarTodosPeriodo(unidade, dataInicio, dataFim, tenantId);
			}
			else {
				return historicoDAO.buscarTodosPeriodo(unidade, dataInicio,  new Date(), tenantId);
			}
		}
		return historicoDAO.buscarTodosUnidade(unidade, tenantId);
	}
	
}
