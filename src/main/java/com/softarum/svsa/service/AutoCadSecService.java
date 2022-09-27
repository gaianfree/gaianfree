package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.softarum.svsa.dao.AutoCadSecDAO;
import com.softarum.svsa.dao.UnidadeDAO;
import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.AutoCadSecTO;
import com.softarum.svsa.util.NegocioException;

import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
public class AutoCadSecService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private AutoCadSecDAO autocadDAO;

	
	
	//salvar no banco
	public void salvar(AutoCadSecTO autocadTO) throws NegocioException {
		log.info("Service : tenant = " + autocadTO.getSecretaria().getCodigo());
		log.info("Service : unidade = " + autocadTO.getUnidade().getCodigo());
		this.autocadDAO.salvar(autocadTO);
	}

	public Tenant buscarPeloCodigo(long codigo) {
		return autocadDAO.buscarPeloCodigo(codigo);
	}
	
	public AutoCadSecDAO getAutoCadDAO() {
		return autocadDAO;
	}
	

	/*public List<Tenant> buscarTodos(Long codigo) {
		return tenantDAO.buscarTodos(codigo);
	}*/
	
	/*public void excluir(Tenant tenant) throws NegocioException {
		unidadeDAO.excluir(unidade);
		
	}*/

	/*public List<Tenant> buscarCRAS(Long codigo) {		
		return tenantDAO.buscarTodosCRAS(codigo);
	}
	
	public List<Tenant> buscarCREAS(Long codigo) {		
		return tenantDAO.buscarTodosCREAS(codigo);
	}
	// job task
	public List<Tenant> buscarCRAS() {		
		return tenantDAO.buscarTodosCRAS();
	}
	// job task
	public List<Tenant> buscarCREAS() {		
		return tenantDAO.buscarTodosCREAS();
	}*/


	

	/*public void setManager(EntityManager manager) {
		TenantDAO
		UnidadeDAO.setEntityManager(manager);
		
	}*/
}