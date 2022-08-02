package com.softarum.svsa.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.softarum.svsa.dao.PessoaDAO;
import com.softarum.svsa.dao.PiaDAO;
import com.softarum.svsa.modelo.Pais;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.ProgramaSocial;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.modelo.to.PessoaDTO;


/**
 * @author murakamiadmin
 *
 */
public class PessoaService implements Serializable {

	private static final long serialVersionUID = 1L;
	//private LogUtil logUtil = new LogUtil(PessoaService.class);
	
	@Inject
	private PessoaDAO pessoaDAO;
	@Inject
	private AgendamentoIndividualHelper helper;
	@Inject
	private PiaDAO piaDAO;
	
	public Pessoa buscarPeloCodigo(Long codigo) {
		return pessoaDAO.buscarPeloCodigo(codigo);
	}
	public PessoaReferencia buscarPFPeloCodigo(Long codigo) {
		return pessoaDAO.buscarPFPeloCodigo(codigo);
	}
	
	public Pessoa buscarPessoa(Long codigo, Unidade unidade, Long tenantId) {
		return pessoaDAO.buscarPessoa(codigo, unidade, tenantId);
	}
	
	
	/* SelecionaPessoa por unidade */
	
	
	public List<PessoaDTO> pesquisarPessoaDTO(String termoPesquisa, Unidade unidade, Long tenantId) {
		return pessoaDAO.pesquisarPessoaDTO(termoPesquisa, unidade, tenantId);
	}	
	public List<PessoaDTO> pesquisarPessoaPorEnderecoDTO(String termoPesquisa, Unidade unidade, Long tenantId) {
		return pessoaDAO.pesquisarPessoaPorEnderecoDTO(termoPesquisa, unidade, tenantId);
	}	
	public List<PessoaDTO> pesquisarPessoaPorNomeSocialDTO(String termoPesquisa, Unidade unidade, Long tenantId) {
		return pessoaDAO.pesquisarPessoaPorNomeSocialDTO(termoPesquisa, unidade, tenantId);
	}	
	public List<PessoaDTO> pesquisarPorProntuarioDTO(String termoPesquisa, Unidade unidade, Long tenantId) {
		return pessoaDAO.pesquisarPessoaPorProntuarioDTO(termoPesquisa, unidade, tenantId);
	}	
	
	/* SelecionaPessoa Geral */
	
	public List<PessoaDTO> pesquisarPessoaDTO(String termoPesquisa, Long tenantId) {
		return pessoaDAO.pesquisarPessoaDTO(termoPesquisa, tenantId);
	}	
	public List<PessoaDTO> pesquisarPessoaPorEnderecoDTO(String termoPesquisa, Long tenantId) {
		return pessoaDAO.pesquisarPessoaPorEnderecoDTO(termoPesquisa, tenantId);
	}	
	public List<PessoaDTO> pesquisarPessoaPorNomeSocialDTO(String termoPesquisa, Long tenantId) {
		return pessoaDAO.pesquisarPessoaPorNomeSocialDTO(termoPesquisa, tenantId);
	}	
	public List<PessoaDTO> pesquisarPorProntuarioDTO(String termoPesquisa, Long tenantId) {
		return pessoaDAO.pesquisarPessoaPorProntuarioDTO(termoPesquisa, tenantId);
	}
	
	
		
	

	/*
	 * SelecionaPessoaReferencia
	 */
	
	public List<PessoaDTO> pesquisarPorNome(String termoPesquisa, Unidade unidade, Long tenantId) {
		return pessoaDAO.pesquisarPorNome(termoPesquisa, unidade, tenantId);
	}
	public List<PessoaDTO> pesquisarPorEndereco(String termoPesquisa, Unidade unidade, Long tenantId) {
		return pessoaDAO.pesquisarPorEndereco(termoPesquisa, unidade, tenantId);
	}		
	public List<PessoaDTO> pesquisarPorNomeSocial(String termoPesquisa, Unidade unidade, Long tenantId) {
		return pessoaDAO.pesquisarPorNomeSocial(termoPesquisa, unidade, tenantId);
	}	
	public List<PessoaDTO> pesquisarPorProntuario(String termoPesquisa, Unidade unidade, Long tenantId) {
		return pessoaDAO.pesquisarPorProntuario(termoPesquisa, unidade, tenantId);
	}
	
	/*
	 * SelecionaPessoaReferenciaGeral
	 */
	public List<PessoaDTO> pesquisarPorNome(String termoPesquisa, Long tenantId) {
		return pessoaDAO.pesquisarPorNome(termoPesquisa, tenantId);
	}
	public List<PessoaDTO> pesquisarPorEndereco(String termoPesquisa, Long tenantId) {
		return pessoaDAO.pesquisarPorEndereco(termoPesquisa, tenantId);
	}
	public List<PessoaDTO> pesquisarPorNomeSocial(String termoPesquisa, Long tenantId) {
		return pessoaDAO.pesquisarPorNomeSocial(termoPesquisa, tenantId);
	}
	public List<PessoaDTO> pesquisarPorProntuario(Long termoPesquisa, Long tenantId) {
		return pessoaDAO.pesquisarPorProntuario(termoPesquisa, tenantId);
	}
	

	
	
	
	
	
	
	
	
	
	
	/* Programa Social */
	
	public List<Pessoa> pesquisarPessoaPorProgSocial(ProgramaSocial programa, Unidade unidade, Long tenantId) {
		return pessoaDAO.pesquisarPessoaPorProgSocial(programa, unidade, tenantId);
	}
	
	public List<Pessoa> pesquisarPessoaPorProgSocial(ProgramaSocial programa, Long tenantId) {
		return pessoaDAO.pesquisarPessoaPorProgSocial(programa, tenantId);
	}
	
	
	
	/* Busca dos países */
	public List<Pais> buscarTodosPaises() {
		return pessoaDAO.buscarTodosPaises();
	}
	public Pais buscarPais(Long codigo) {
		return pessoaDAO.buscarPais(codigo);
	}
	public List<Pessoa> buscarPessoasPais(Pais pais, Unidade unidade, Long tenantId) {
		return pessoaDAO.pesquisarPessoasPais(pais, unidade, tenantId);
	}
	public List<Pessoa> buscarPessoasPais(Pais pais, Long tenantId) {
		return pessoaDAO.pesquisarPessoasPais(pais, tenantId);
	}
	
	
	/* Validações */
	
	public Boolean verificarMSE(Pessoa pessoa, Long tenantId) {
		
		if(piaDAO.verificarPlanoAtivo(pessoa.getCodigo(), tenantId) != 0)
			return true;
		return false;
	}
	
	
	
	public PessoaDAO getPessoaDAO() {
		return pessoaDAO;
	}

	public List<AtendimentoDTO> consultarResumoAtendimentos(Pessoa pessoa, Long tenantId) {

		return helper.buscarResumoAtendimentosDTO(pessoa, tenantId);
	}

}