package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.AutoCadSecDAO;
import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
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
	/*public void salvar(AutoCadSecTO autocadTO) throws NegocioException {
		log.info("Service : tenant = " + autocadTO.getSecretaria().getCodigo() + autocadTO.getUnidade().getTenant_id());
		log.info("Service : unidade = " + autocadTO.getUnidade().getCodigo());
		log.info("Service : usuario = " + autocadTO.getUsuario().getCodigo());
		this.autocadDAO.salvar(autocadTO);
	}*/
	
	public Tenant salvarTenant(Tenant tenant) throws NegocioException {
			log.info("Service : tenant = " + tenant.getTenant());
			log.info("Service : tenant = " + tenant.getSecretaria());
			
			
			return this.autocadDAO.salvarTenant(tenant);
		}
	
	
	public Unidade salvarUnidade(Unidade unidade) throws NegocioException {
		
		log.info("Service : unidade = " + unidade.getTenant_id());
		
		log.info("Service : unidade = " + unidade.getNome());
		log.info("Service : unidade = " + unidade.getEndereco().getCep());
		log.info("Service : unidade = " + unidade.getEndereco().getEndereco());
		log.info("Service : unidade = " + unidade.getEndereco().getNumero());
		log.info("Service : unidade = " + unidade.getEndereco().getUf());
		log.info("Service : unidade = " + unidade.getTipo());
		
		
		
		return this.autocadDAO.salvarUnidade(unidade);
	}
	
	
	public Usuario salvarUsuario(Usuario usuario) throws NegocioException {
		
		log.info("Service : usuario = " + usuario.getTenant());
		log.info("Service : usuario = " + usuario.getUnidade());
		log.info("Service : usuario = " + usuario.getNome());
		log.info("Service : usuario = " + usuario.getEmail());
		log.info("Service : usuario = " + usuario.getSenha());
		log.info("Service : usuario = " + usuario.getRole());
		log.info("Service : usuario = " + usuario.getGrupo());
		
		return this.autocadDAO.salvarUsuario(usuario);
	}

	
	
	
	public Tenant buscarPeloCodigo(long codigo) {
		return autocadDAO.buscarPeloCodigo(codigo);
	}
	
	//acrescentei para mandar o id do tenant na coluna tenantid da tabela unidade
	public List<AutoCadSecTO> buscarTodos(Long tenantId) {
		return autocadDAO.buscarTodos(tenantId);
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