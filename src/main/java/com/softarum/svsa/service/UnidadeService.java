package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.softarum.svsa.dao.UnidadeDAO;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.NegocioException;

import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
public class UnidadeService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UnidadeDAO unidadeDAO;


	public void salvar(Unidade unidade) throws NegocioException {
		log.info("Service : tenant = " + unidade.getTenant_id());
		if (unidade.getTipo() == null) 
			throw new NegocioException("O tipo é obrigatório.");
		this.unidadeDAO.salvar(unidade);
	}

	public Unidade buscarPeloCodigo(long codigo) {
		return unidadeDAO.buscarPeloCodigo(codigo);
	}
	

	public List<Unidade> buscarTodos(Long tenantId) {
		return unidadeDAO.buscarTodos(tenantId);
	}
	
	public void excluir(Unidade unidade) throws NegocioException {
		unidadeDAO.excluir(unidade);
		
	}

	public List<Unidade> buscarCRAS(Long tenantId) {		
		return unidadeDAO.buscarTodosCRAS(tenantId);
	}
	
	public List<Unidade> buscarCREAS(Long tenantId) {		
		return unidadeDAO.buscarTodosCREAS(tenantId);
	}
	// job task
	public List<Unidade> buscarCRAS() {		
		return unidadeDAO.buscarTodosCRAS();
	}
	// job task
	public List<Unidade> buscarCREAS() {		
		return unidadeDAO.buscarTodosCREAS();
	}


	public UnidadeDAO getUnidadeDAO() {
		return unidadeDAO;
	}

	public void setManager(EntityManager manager) {
		unidadeDAO.setEntityManager(manager);
		
	}
}