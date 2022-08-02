package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.softarum.svsa.dao.AbordagemDAO;
import com.softarum.svsa.modelo.Abordagem;
import com.softarum.svsa.modelo.PessoaAbordagem;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;

import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
public class AbordagemService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private AbordagemDAO abordagemDAO;

	public Abordagem salvar(Abordagem abordagem) throws NegocioException {
		log.debug("Service : tenant = " + abordagem.getTenant_id());
		
		return this.abordagemDAO.salvar(abordagem);
	}
	
	public void salvarAlterar(Abordagem abordagem, Long codigoUsuarioLogado) throws NegocioException {
		
		log.info("para verificar o problema de alteração de abordagem...criada por: " + abordagem.getTecnico().getCodigo() + " **** Tentativa de alteração por : " + codigoUsuarioLogado);
			
		if(codigoUsuarioLogado.longValue() == abordagem.getTecnico().getCodigo().longValue()) {
			if(new Date().after(DateUtils.plusDays(abordagem.getData(), 7)) ){
				throw new NegocioException("Prazo para alteração (7 dias) foi ultrapassado!");
					
			}
			else {
				abordagemDAO.salvar(abordagem);
			}
		}
		else {
			throw new NegocioException("Somente o técnico que registrou pode alterar a abordagem! E isso só pode ser feito antes de 7 dias do registro.");
		}	
	}
	
	public void excluir(Abordagem abordagem) throws NegocioException {
		abordagemDAO.excluir(abordagem);
		
	}
	
	
	/* Buscas */
	
	
	public List<String> buscarNomes(String query, Unidade unidade, Long tenantId) {		
		return abordagemDAO.buscarNomes(query, unidade, tenantId);
	}

	public Abordagem buscarPeloCodigo(long codigo) {
		return abordagemDAO.buscarPeloCodigo(codigo);
	}
	
	// buscar pessoa pelo nome
	public PessoaAbordagem buscarPeloNome(String nome) {
		return abordagemDAO.buscarPeloNome(nome);
	}
	

	public List<Abordagem> buscarTodos(Long tenantId) {
		return abordagemDAO.buscarTodos(tenantId);
	}
	
	public List<Abordagem> buscarTodosDia(Long tenantId, Unidade unidade) {
		return abordagemDAO.buscarTodosDia(tenantId, unidade);
	}
	
	public List<Abordagem> buscarAbordagensPeriodo(Unidade unidade, Date ini, Date fim, Long tenantId) {
		
		if(ini != null)
			if(fim != null) {
				log.info("abordagens periodo -- Ini: " + ini + " -- fim: " + fim);
				return abordagemDAO.buscarAbordagensPeriodo(unidade, ini, fim, tenantId);
			}
			else {
				log.info("abordagens periodo -- Ini: " + ini + " -- fim: " + fim);
				return abordagemDAO.buscarAbordagensPeriodo(unidade, ini, new Date(), tenantId);
			}
				
		return abordagemDAO.buscarAbordagensUnidade(unidade, tenantId);
	}
	
	
	
	
	

	public void setManager(EntityManager manager) {
		abordagemDAO.setEntityManager(manager);
		
	}

		
}