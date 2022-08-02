package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import com.softarum.svsa.dao.RmaDAO;
import com.softarum.svsa.modelo.RmaCras;
import com.softarum.svsa.modelo.RmaCreas;
import com.softarum.svsa.modelo.RmaPop;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.rma.RmaCreasTO;
import com.softarum.svsa.modelo.to.rma.RmaPopTO;
import com.softarum.svsa.modelo.to.rma.RmaTO;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
@Getter
@Setter
public class RMAService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private RmaDAO rmaDAO;
	
	private RmaAssembler rmaAssembler = new RmaAssembler();


	/* RMA Cras */
	
	
	public void salvarCras(RmaTO rmaTO, Long tenantId) throws NegocioException {	
		
		// converte TO to Entity
		RmaCras rmaCras = rmaAssembler.toCrasEntity(rmaTO); 
		rmaCras.setTenant_id(tenantId);
		
		log.info("salvando rmaCras: " + rmaCras);	
		
		try {
			
			if(rmaDAO.buscarRmaCrasFechado(rmaTO.getUnidade(), rmaTO.getMesAnoReferencia(), tenantId) != null) {
			
				throw new NegocioException("O RMA desse mês já foi fechado.");
			}			
		
		} catch (NoResultException e) {
			
			log.info("ainda nao gravado, gravando...");
			rmaDAO.salvarCras(rmaCras);
			
		} catch (PersistenceException e) {
			throw new NegocioException("Não foi possível salvar o RMA CRAS no banco de dados.");			
		}
	}
	
	public RmaTO recuperarCras(Long codigo) {
		
		log.info("recuperando rmaCras: " + codigo);
		
		RmaCras rmaCras = rmaDAO.buscarCras(codigo);
		
		RmaTO rmaTO = rmaAssembler.toRmaTO(rmaCras);
		
		return rmaTO;		
	}
	
	public RmaTO buscarRmaCrasFechado(Unidade unidade, String mesAnoRef, Long tenantId) {
		
		RmaCras rma = rmaDAO.buscarRmaCrasFechado(unidade, mesAnoRef, tenantId);
		
		RmaTO rmaTO = rmaAssembler.toRmaTO(rma);

		return rmaTO;
	}

	public List<String> buscarRmasCrasFechados(Unidade unidade, Long tenantId) {
				
		return rmaDAO.buscarRmasCrasFechados(unidade, tenantId);
	}
	
	
	
	
	/* RMA Creas */
	
	
	
	public void salvarCreas(RmaCreasTO rmaCreasTO, Long tenantId) throws NegocioException {		
		
		// converte TO to Entity
		RmaCreas rmaCreas = rmaAssembler.toCreasEntity(rmaCreasTO);
		rmaCreas.setTenant_id(tenantId);
		
		log.info("salvando rmaCreas: " + rmaCreas);
		
		try {
			// verifica se já foi fechado
			if(rmaDAO.buscarRmaCreasFechado(rmaCreasTO.getUnidade(), rmaCreasTO.getMesAnoReferencia(), tenantId) != null) {			
				throw new NegocioException("O RMA desse mês já foi fechado.");
			}
			
		} catch (NoResultException e) {
			//e.printStackTrace();
			log.info("ainda nao gravado, gravando...");
			
			// salva o RMA gerado
			rmaDAO.salvarCreas(rmaCreas);
			
		} catch (PersistenceException e) {
			throw new NegocioException("Não foi possível salvar o RMA CREAS no banco de dados.");			
		}
	}
	
	public RmaCreasTO recuperarCreas(Long codigo) {
		
		log.info("recuperando rmaCreas: " + codigo);
		
		RmaCreas rmaCreas = rmaDAO.buscarCreas(codigo);
		
		RmaCreasTO rmaCreasTO = rmaAssembler.toRmaCreasTO(rmaCreas);			
			
		return rmaCreasTO;		
	}
	

	public RmaCreasTO buscarRmaCreasFechado(Unidade unidade, String mesAnoRef, Long tenantId) {
		
		RmaCreas rma = rmaDAO.buscarRmaCreasFechado(unidade, mesAnoRef, tenantId);
		
		RmaCreasTO rmaTO = rmaAssembler.toRmaCreasTO(rma);
		
		return rmaTO;
	}

	public List<String> buscarRmasCreasFechados(Unidade unidade, Long tenantId) {
				
		return rmaDAO.buscarRmasCreasFechados(unidade, tenantId);
	}
	
	
	/* RMA Pop */
	
	
	
	public void salvarPop(RmaPopTO rmaPopTO, Long tenantId) throws NegocioException {		
		
		// converte TO to Entity
		RmaPop rmaPop = rmaAssembler.toPopEntity(rmaPopTO);
		rmaPop.setTenant_id(tenantId);
		
		log.info("salvando rmaPop: " + rmaPop);
		
		try {
			// verifica se já foi fechado
			if(rmaDAO.buscarRmaPopFechado(rmaPopTO.getUnidade(), rmaPopTO.getMesAnoReferencia(), tenantId) != null) {			
				throw new NegocioException("O RMA desse mês já foi fechado.");
			}
			
		} catch (NoResultException e) {
			//e.printStackTrace();
			log.info("ainda nao gravado, gravando...");
			
			// salva o RMA gerado
			rmaDAO.salvarPop(rmaPop);
			
		} catch (PersistenceException e) {
			throw new NegocioException("Não foi possível salvar o RMA Pop no banco de dados.");			
		}
	}
	
	public RmaPopTO recuperarPop(Long codigo) {
		
		log.info("recuperando rmaPop: " + codigo);
		
		RmaPop rmaPop = rmaDAO.buscarPop(codigo);
		
		RmaPopTO rmaPopTO = rmaAssembler.toRmaPopTO(rmaPop);			
			
		return rmaPopTO;		
	}
	

	public RmaPopTO buscarRmaPopFechado(Unidade unidade, String mesAnoRef, Long tenantId) {
		
		RmaPop rma = rmaDAO.buscarRmaPopFechado(unidade, mesAnoRef, tenantId);
		
		RmaPopTO rmaTO = rmaAssembler.toRmaPopTO(rma);
		
		return rmaTO;
	}

	public List<String> buscarRmasPopFechados(Unidade unidade, Long tenantId) {
				
		return rmaDAO.buscarRmasPopFechados(unidade, tenantId);
	}
	
	
	
	

	public void setManager(EntityManager manager) {
		rmaDAO.setEntityManager(manager);
		
	}
}