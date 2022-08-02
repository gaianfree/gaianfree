package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.EncaminhamentoDAO;
import com.softarum.svsa.dao.OrgaoDAO;
import com.softarum.svsa.modelo.Encaminhamento;
import com.softarum.svsa.modelo.Orgao;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;
import com.softarum.svsa.util.NegocioException;


/**
 * @author gabrielrodrigues/Murakami
 *
 */
public class EncaminhamentoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EncaminhamentoDAO encaminhamentoDAO;
	@Inject
	private OrgaoDAO orgaoDAO;
	

	
	public Encaminhamento salvar(Encaminhamento encaminhamento) throws NegocioException {			
		return this.encaminhamentoDAO.salvar(encaminhamento);
	}	
	
	public List<Encaminhamento> buscarEncaminhamentos(Pessoa pessoa, Long tenantId) {
		return encaminhamentoDAO.buscarEncaminhamentos(pessoa, tenantId);
	}
	
	public void excluir(Encaminhamento encaminhamento) throws NegocioException {
		encaminhamentoDAO.excluir(encaminhamento);		
	}
	
	public List<Orgao> buscarCodigosEncaminhamento(CodigoEncaminhamento codigosEncaminhamento, Long tenantId){
		return orgaoDAO.buscarCodigosEncaminhamento(codigosEncaminhamento,tenantId);
	}
	
	/*
	 * RelatorioEncExterno
	 */
	public List<Encaminhamento> buscarEncaminhamentos(Unidade unidade, Date dataInicio, Date dataFim, Long tenantId) {
		
		if(dataInicio != null) {
			if(dataFim != null) {
				return encaminhamentoDAO.buscarEncaminhamentos(unidade, dataInicio, dataFim, tenantId);
			}
			else {
				return encaminhamentoDAO.buscarEncaminhamentos(unidade, dataInicio,  new Date(), tenantId);
			}
		}
		return encaminhamentoDAO.buscarEncaminhamentos(unidade, tenantId);
	}
	/*
	 * RelatorioEncExterno
	 */
	public List<Encaminhamento> buscarEncaminhamentosGrafico(Unidade unidade, Date dataInicio, Date dataFim, Long tenantId) {
		
		if(dataInicio != null) {
			if(dataFim != null) {
				return encaminhamentoDAO.buscarEncaminhamentosGrafico(unidade, dataInicio, dataFim, tenantId);
			}
			else {
				return encaminhamentoDAO.buscarEncaminhamentosGrafico(unidade, dataInicio,  new Date(), tenantId);
			}
		}
		return encaminhamentoDAO.buscarEncaminhamentosGrafico(unidade, tenantId);
	}
}