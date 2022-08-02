package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.util.MessageUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * @author murakamiadmin
 *
 */
@Getter
@Setter
@Named
@ViewScoped
public class SelecionaPessoaReferenciaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(SelecionaPessoaReferenciaBean.class);
	private String termoPesquisa;
	private String parametro = "nome";
	private List<PessoaDTO> listaPessoasReferencia = new ArrayList<>();	
	private Unidade unidade;

	@Inject
	PessoaService pessoaService;
	@Inject
	private LoginBean loginBean;
	
	
	@PostConstruct
	public void inicializar() {		
		unidade = loginBean.getUsuario().getUnidade();
	}
	
	public void pesquisar() {	
		
		//log.info("Termo no bean: "+termoPesquisa);	
		
		if(termoPesquisa != null && !termoPesquisa.equals("")) {
			
			if(getParametro().equals("endereco")) {
				listaPessoasReferencia = pessoaService.pesquisarPorEndereco(termoPesquisa, unidade, loginBean.getTenantId());		
			}
			else if(getParametro().equals("nome")){
				listaPessoasReferencia = pessoaService.pesquisarPorNome(termoPesquisa, unidade, loginBean.getTenantId());
			}
			else if(getParametro().equals("nomeSocial")){
				listaPessoasReferencia = pessoaService.pesquisarPorNomeSocial(termoPesquisa, unidade, loginBean.getTenantId());	
			}
			else if(getParametro().equals("prontuario")){
				try {
					listaPessoasReferencia = pessoaService.pesquisarPorProntuario(termoPesquisa, unidade, loginBean.getTenantId());
				}
				catch(Exception e) {
					MessageUtil.alerta("Digite um código de prontuário válido. Apenas números.");
				}			
			} 
		
			
			if (listaPessoasReferencia.isEmpty()) {
	            MessageUtil.alerta("Sua consulta não encontrou pessoas de referência nesta unidade.");
	        }
		}			
		else {
			MessageUtil.alerta("Digite um nome para a pesquisa.");
		}
	}	

	
	public void pesquisarGeral() {	
		
		log.info("Termo no bean: "+termoPesquisa);		
		
		if(termoPesquisa != null && !termoPesquisa.equals("")) {
			
			if(getParametro().equals("endereco")) {
				listaPessoasReferencia = pessoaService.pesquisarPorEndereco(termoPesquisa, loginBean.getTenantId());				
			}
			else if(getParametro().equals("nome")){
				listaPessoasReferencia = pessoaService.pesquisarPorNome(termoPesquisa, loginBean.getTenantId());
			} 
			else if(getParametro().equals("nomeSocial")){
				listaPessoasReferencia = pessoaService.pesquisarPorNomeSocial(termoPesquisa, loginBean.getTenantId());	
			}
			else if(getParametro().equals("prontuario")){
				try {
					listaPessoasReferencia = pessoaService.pesquisarPorProntuario(Long.valueOf(termoPesquisa), loginBean.getTenantId());
				}
				catch(Exception e) {
					MessageUtil.alerta("Digite um código de prontuário válido. Apenas números.");
				}			
			} 
			
			
			if (listaPessoasReferencia.isEmpty()) {
	            MessageUtil.alerta("Sua consulta não encontrou pessoas de referência nesta unidade.");
	        }			
		}
		else
			MessageUtil.alerta("Digite um nome para a pesquisa.");
		
	}
	
	/*
	public void selecionarPessoa(PessoaDTO pessoa) {
		log.info("pessoa selecionada: " + pessoa.getCodigo() + "-" + pessoa.getNome());
		PrimeFaces.current().dialog().closeDynamic(pessoa);
	}
	*/
	public void selecionarPessoaReferencia(PessoaDTO pessoaReferencia) {
		log.info("pessoaRef selecionada: " + pessoaReferencia.getCodigo() + "-" + pessoaReferencia.getNome());
		PrimeFaces.current().dialog().closeDynamic(pessoaReferencia);
	}	
	
}