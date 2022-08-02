package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.OrgaoDAO;
import com.softarum.svsa.modelo.Orgao;
import com.softarum.svsa.util.NegocioException;

/**
 * @author Talita
 *
 */
public class OrgaoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private OrgaoDAO orgaoDAO;


	public void salvar(Orgao orgao) throws NegocioException {
			
		this.orgaoDAO.salvar(orgao);
	}

	public Orgao buscarPeloCodigo(long codigo) {
		return orgaoDAO.buscarPeloCodigo(codigo);
	}
	

	public List<Orgao> buscarTodos(Long tenantId) {
		return orgaoDAO.buscarTodos(tenantId);
	}

	
	public void excluir(Orgao orgao) throws NegocioException {
		orgaoDAO.excluir(orgao);
		
	}


	public OrgaoDAO getOrgaoDAO() {
		return orgaoDAO;
	}
	
}