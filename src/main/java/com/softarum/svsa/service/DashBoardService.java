package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.AgendamentoColetivoDAO;
import com.softarum.svsa.dao.AgendamentoFamiliarDAO;
import com.softarum.svsa.dao.DashBoardDAO;
import com.softarum.svsa.dao.PlanoAcompanhamentoDAO;
import com.softarum.svsa.dao.ServicoDAO;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.AtendimentoTO;
import com.softarum.svsa.modelo.to.PerfilFamiliaTO;
import com.softarum.svsa.util.DateUtils;


/**
 * @author murakamiadmin
 *
 */
public class DashBoardService implements Serializable {

	private static final long serialVersionUID = 1L;
	

	@Inject
	private DashBoardDAO dashboardDAO;
	@Inject
	private AgendamentoColetivoDAO agendaDAO;
	@Inject
	private AgendamentoFamiliarDAO familiarDAO;
	@Inject
	AgendamentoIndividualService listaAtendimentoService;
	@Inject
	private PlanoAcompanhamentoDAO planoDAO;
	@Inject
	private ServicoDAO servicoDAO;
	
	
	/*
	 * DashBoard
	 */
	
	public Long dashboardProntuarios(Unidade unidade, Long tenantId) {
		return dashboardDAO.qdeProntuariosNovos(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}

	public Long dashboardIndividualizados(Unidade unidade, Long tenantId) {
		return dashboardDAO.buscarQuantidadeAtendidos(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}	
	
	
	
	public List<AtendimentoTO> dashboardQdesCodAuxIndiv(Unidade unidade, Long tenantId){
		return dashboardDAO.dashboardQdesCodAuxIndiv(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}
	
	public List<AtendimentoTO> dashboardQdesCodAuxColet(Unidade unidade, Long tenantId){
		return dashboardDAO.dashboardQdesCodAuxColet(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}
	public List<AtendimentoTO> dashboardQdesCodAuxFamiliar(Unidade unidade, Long tenantId){
		return dashboardDAO.dashboardQdesCodAuxFamiliar(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}
	
	public List<AtendimentoTO> dashboardQdesScfv(Unidade unidade, Long tenantId){
		return dashboardDAO.dashboardQdesScfv(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}
	
	public List<PerfilFamiliaTO> dashboardQdesPaif(Unidade unidade, Long tenantId){
		return dashboardDAO.dashboardQdesPaif(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}
	

	
	
	public Long dashboardColetivos(Unidade unidade, Long tenantId) {
		return agendaDAO.buscarAtendimentosAtendidos(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}
	
	public Long dashboardFamiliares(Unidade unidade, Long tenantId) {
		return familiarDAO.buscarAtendimentosAtendidos(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}
	
	public Long dashboardPaif(Unidade unidade, Long tenantId) {
		return planoDAO.buscarQdeAcompanhamento(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}
	
	public Long dashboardScfv(Unidade unidade, Long tenantId) {
		return servicoDAO.buscarQdeSCFV(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}

	/*
	 * Grafico Horizontal Atendimentos
	 */
	public List<AtendimentoTO> relatorioAtendimentosTOCodAux(Unidade unidade, Long tenantId) {			
		
		return listaAtendimentoService.relatorioAtendimentosTOCodAux(unidade, DateUtils.getDataIMesCorrente(), 
				DateUtils.getDataFMesCorrente(), tenantId);
	}

	
}