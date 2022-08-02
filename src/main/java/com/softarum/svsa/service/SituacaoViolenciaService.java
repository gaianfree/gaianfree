package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.SituacaoViolenciaDAO;
import com.softarum.svsa.modelo.SituacaoViolencia;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.util.NegocioException;

/**
 * @author gabrielrodrigues  - refactored by Murakami
 *
 */
public class SituacaoViolenciaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private SituacaoViolenciaDAO situacaoDAO;
	

	
	public void salvar(SituacaoViolencia situacao) throws NegocioException {			
		this.situacaoDAO.salvar(situacao);
	}	
	
	public List<SituacaoViolencia> buscarSituacoesViolencia(Pessoa pessoa, Long tenantId) {
		return situacaoDAO.buscarSituacoesViolencia(pessoa, tenantId);
	}

	public void excluir(SituacaoViolencia situacao) throws NegocioException {
		situacaoDAO.excluir(situacao);		
	}
	
	public void encerrar(SituacaoViolencia situacao) throws NegocioException {
		situacao.setDataEncerramento(new Date());
		situacaoDAO.salvar(situacao);		
	}
}