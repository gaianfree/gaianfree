package com.softarum.svsa.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.dao.OficioEmitidoDAO;
import com.softarum.svsa.dao.OrgaoDAO;
import com.softarum.svsa.modelo.OficioEmitido;
import com.softarum.svsa.modelo.Orgao;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;
import com.softarum.svsa.util.NegocioException;

/**
 * @author Lauro
 *
 */
public class OficioEmitidoService implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(OficioEmitidoService.class);
	
	@Inject
	private OficioEmitidoDAO oficioEmitidoDAO;
	@Inject
	private OrgaoDAO orgaoDAO;
	@Inject
	private LoginBean loginBean;

	
	public OficioEmitido salvar(OficioEmitido oficioEmitido, Long tenantId) throws NegocioException, Exception {	
		
		// se oficioEmitido novo
		//log.info("Oficio antes de criar numero: " + oficioEmitido.getNrOficioEmitido());
		if(oficioEmitido.getCodigo() == null) {
			oficioEmitido = criarNumeroOficioEmitido(oficioEmitido, tenantId);
			//log.info("Oficio depois de criar numero: " + oficioEmitido.getNrOficioEmitido());
		}
		//log.info("Oficio na hora de salvar: " + oficioEmitido.getNrOficioEmitido());
		oficioEmitido.setTenant_id(loginBean.getTenantId());
		return this.oficioEmitidoDAO.salvar(oficioEmitido);
	}
	
	OficioEmitido criarNumeroOficioEmitido(OficioEmitido oficioEmitido, Long tenantId) throws NegocioException{

		try {

			// obter a data atual
			LocalDate data = LocalDate.now();
			// retirar um dia para verificar se mudou o ano
			Integer ano1 = data.minusDays(1).getYear();
			log.info("Um dia antes: " + ano1);

			Integer ano2 = data.getYear();
			log.info("Ano da criacao do oficio: " + ano2);

			/*
			*  se for no mesmo ano
			*   obter qde de oficios gerados na unidade
			*  senão
			*   reiniciar numeração do 1
			*/
			if(ano2.equals(ano1)) { 
				/* pega o maior numero de oficio e soma 1 */
				Long nr = oficioEmitidoDAO.obterUltimoOficio(oficioEmitido.getUnidade(), ano2, tenantId);
				log.info("nr oficio obtido: " + nr);
				
				// verifica se tem oficio emitido
				if(nr != null) {
					oficioEmitido.setNumero(nr + 1);
				}
				else {
					oficioEmitido.setNumero(1L);
				}
			}else {
				oficioEmitido.setNumero(1L);
			}
			
			oficioEmitido.setAno(ano2);
			
			log.info("Numero do oficio emitido = : " + oficioEmitido.getNrOficioEmitido());

		}catch(Exception e) {
			e.printStackTrace();
			throw new NegocioException ("Não foi possível gerar o número do Oficio");
		}
		
		return oficioEmitido;
	}

	public void excluir(OficioEmitido oficioEmitido) throws NegocioException {
		oficioEmitido.setExcluido(true);
		oficioEmitidoDAO.excluir(oficioEmitido);		
	}

	public List<Orgao> buscarCodigosEncaminhamento(CodigoEncaminhamento codigosEncaminhamento, Long tenantId){
		return orgaoDAO.buscarCodigosEncaminhamento(codigosEncaminhamento, tenantId);
	}
	
		
	/*
	 * Relatorio Oficios Emitidos
	 */	
	public List<OficioEmitido> buscarOficiosEmitidosUnidade(Unidade unidade, Date dataInicio, Date dataFim, Long tenantId) {
		
		if(dataInicio != null) {
			if(dataFim != null) {
				return oficioEmitidoDAO.buscarOficiosEmitidosUnidade(unidade, dataInicio, dataFim, tenantId);
			}
			else {
				return oficioEmitidoDAO.buscarOficiosEmitidosUnidade(unidade, dataInicio, new Date(), tenantId);
			}
		}
		return oficioEmitidoDAO.buscarOficiosEmitidosUnidade(unidade, tenantId);
	}
	
	
	public List<OficioEmitido> buscarOficiosEmitidos(Unidade unidade, Long tenantId) {
		
		return oficioEmitidoDAO.buscarOficiosEmitidos(unidade, tenantId);
	}
	

	public List<OficioEmitido> buscarOficiosEmitidosHist(Pessoa pessoa, Long tenantId) {
		
		return oficioEmitidoDAO.buscarOficiosEmitidosHist(pessoa, tenantId);
	}

	public List<OficioEmitido> buscarOficiosEmitidosGrafico(Unidade unidade, Date dataInicio, Date dataFim, Long tenantId) {			
		if(dataInicio != null) {
			if(dataFim != null) {
				return oficioEmitidoDAO.buscarOficiosEmitidosGrafico(unidade, dataInicio, dataFim, tenantId);
			}
			else {
				return oficioEmitidoDAO.buscarOficiosEmitidosGrafico(unidade, dataInicio, new Date(), tenantId);
			}
		}
		return	oficioEmitidoDAO.buscarOficiosEmitidosGrafico(unidade, tenantId);
	}
	
}