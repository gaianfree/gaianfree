package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.util.MessageUtil;

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
@Named
@ViewScoped
public class SelecionaPessoaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String termoPesquisa;
	private String parametro = "nome";
	//private List<Pessoa> listaPessoas = new ArrayList<>();
	private List<PessoaDTO> listaPessoasDTO = new ArrayList<>();
	
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

		if(termoPesquisa != null && !termoPesquisa.equals("")) {
			
			if(getParametro().equals("endereco")) {
				listaPessoasDTO = pessoaService.pesquisarPessoaPorEnderecoDTO(termoPesquisa, unidade, loginBean.getTenantId());				
			}
			else if(getParametro().equals("nome")){
				log.info("seleciona pessoa por nome");
				listaPessoasDTO = pessoaService.pesquisarPessoaDTO(termoPesquisa, unidade, loginBean.getTenantId());
				log.info("listaPessoasDTO qde: " + listaPessoasDTO.size());
			}
			else if(getParametro().equals("nomeSocial")){
				listaPessoasDTO = pessoaService.pesquisarPessoaPorNomeSocialDTO(termoPesquisa, unidade, loginBean.getTenantId());
			}
			else if(getParametro().equals("prontuario")){
				try {
					listaPessoasDTO = pessoaService.pesquisarPorProntuarioDTO(termoPesquisa, unidade, loginBean.getTenantId());
				}
				catch(Exception e) {
					MessageUtil.alerta("Digite um código de prontuário válido. Apenas números.");
				}			
			} 
			
			if (listaPessoasDTO.isEmpty()) {
	            MessageUtil.alerta("Sua consulta não encontrou pessoas nesta unidade.");
	        }
		}			
		else
			MessageUtil.alerta("Digite um nome para a pesquisa.");        
	}	
	public void pesquisarGeral() {			

		if(termoPesquisa != null && !termoPesquisa.equals("")) {
			
			if(getParametro().equals("endereco")) {
				listaPessoasDTO = pessoaService.pesquisarPessoaPorEnderecoDTO(termoPesquisa, loginBean.getTenantId());				
			}
			else if(getParametro().equals("nome")){
				listaPessoasDTO = pessoaService.pesquisarPessoaDTO(termoPesquisa, loginBean.getTenantId());
			}
			else if(getParametro().equals("nomeSocial")){
				listaPessoasDTO = pessoaService.pesquisarPessoaPorNomeSocialDTO(termoPesquisa, loginBean.getTenantId());
			}
			else if(getParametro().equals("prontuario")){
				try {
					listaPessoasDTO = pessoaService.pesquisarPorProntuarioDTO(termoPesquisa, loginBean.getTenantId());
				}
				catch(Exception e) {
					MessageUtil.alerta("Digite um código de prontuário válido. Apenas números.");
				}			
			} 
			
			if (listaPessoasDTO.isEmpty()) {
	            MessageUtil.alerta("Sua consulta não encontrou essa pessoa.");
	        }
		}			
		else
			MessageUtil.alerta("Digite um nome para a pesquisa.");        
	}
	
	
	
	public void selecionarPessoa(PessoaDTO pessoa) {
		log.info("pessoa selecionada: " + pessoa.getCodigo() + "-" + pessoa.getNome());
		PrimeFaces.current().dialog().closeDynamic(pessoa);
	}
	
}