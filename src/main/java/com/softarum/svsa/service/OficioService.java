package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.OficioDAO;
import com.softarum.svsa.dao.OrgaoDAO;
import com.softarum.svsa.modelo.Oficio;
import com.softarum.svsa.modelo.OficioEmitido;
import com.softarum.svsa.modelo.Orgao;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;
import com.softarum.svsa.util.NegocioException;

import lombok.extern.log4j.Log4j;

/**
 * @author Murakami
 *
 */
@Log4j
public class OficioService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private OficioEmitidoService oficioEmitidoSv;
	@Inject
	private OficioDAO oficioDAO;
	@Inject
	private OrgaoDAO orgaoDAO;
	
	
	public Oficio salvar(Oficio oficio) throws NegocioException {
		return this.oficioDAO.salvar(oficio);
	}
	
	public OficioEmitido salvarResposta(Oficio oficio, OficioEmitido oficioEmitido) throws NegocioException {
		log.debug("Salvando oficios...");
		if(oficioEmitido.getCodigo() == null) {
			oficioEmitido = oficioEmitidoSv.criarNumeroOficioEmitido(oficioEmitido, oficio.getTenant_id());
			log.debug("Oficio depois de criar numero: " + oficioEmitido.getNrOficioEmitido());
		}
		log.info("Salvando oficios " + oficioEmitido.getNrOficioEmitido());
		return this.oficioDAO.salvarResposta(oficio, oficioEmitido);
	}	
	
	public void excluir(Oficio oficio) throws NegocioException {
		oficioDAO.excluir(oficio);		
	}

	public List<Orgao> buscarCodigosEncaminhamento(CodigoEncaminhamento codigosEncaminhamento, Long tenantId){
		return orgaoDAO.buscarCodigosEncaminhamento(codigosEncaminhamento, tenantId);
	}
	
	
	/*
	 * RelatorioOficios
	 */	
	public List<Oficio> buscarOficiosUnidade(Unidade unidade, Date dataInicio, Date dataFim, Boolean sasc, Long tenantId) {
		
		if(dataInicio != null) {
			if(dataFim != null) {
				return (sasc) ? oficioDAO.buscarOficiosSasc(unidade, dataInicio, dataFim, tenantId) 
						: oficioDAO.buscarOficiosUnidade(unidade, dataInicio, dataFim, tenantId);
			}
			else {
				return (sasc) ? oficioDAO.buscarOficiosSasc(unidade, dataInicio, new Date(), tenantId) 
						: oficioDAO.buscarOficiosUnidade(unidade, dataInicio,  new Date(), tenantId);
			}
		}
		return (sasc) ? oficioDAO.buscarOficiosSasc(unidade, tenantId) 
				: oficioDAO.buscarOficiosUnidade(unidade, tenantId);
	}
	
	public List<Oficio> buscarOficiosRecebidos(Unidade unidade, Long tenantId) {
		
		return oficioDAO.buscarOficiosRecebidos(unidade, tenantId);
	}
	
	public List<Oficio> buscarTodosOficiosRecebidos(Unidade unidade, Boolean todos, Long tenantId) {
		
		return (todos) ? oficioDAO.buscarTodosOficiosRecebidos(unidade, tenantId) 
				: oficioDAO.buscarOficiosRecebidos(unidade, tenantId);
	}
	
	public List<Oficio> buscarTodosOficiosSasc(Long tenantId) {
		
		return oficioDAO.buscarTodosOficiosSasc(tenantId);
	}
	

	public List<Oficio> buscarOficiosHist(Pessoa pessoa, Long tenantId) {
		
		return oficioDAO.buscarOficiosHist(pessoa, tenantId);
	}

	public List<Oficio> buscarOficiosGrafico(Unidade unidade, Date dataInicio, Date dataFim, Boolean sasc, Long tenantId) {			
		if(dataInicio != null) {
			if(dataFim != null) {
				return (sasc) ? oficioDAO.buscarOficiosGraficoSasc(unidade, dataInicio, dataFim, tenantId) 
						: oficioDAO.buscarOficiosGrafico(unidade, dataInicio, dataFim, tenantId);
			}
			else {
				return (sasc) ? oficioDAO.buscarOficiosGraficoSasc(unidade, dataInicio, new Date(), tenantId) 
						: oficioDAO.buscarOficiosGrafico(unidade, dataInicio,  new Date(), tenantId);
			}
		}
		return (sasc) ? oficioDAO.buscarOficiosGraficoSasc(unidade, tenantId) 
				: oficioDAO.buscarOficiosGrafico(unidade, tenantId);
	}

	
}