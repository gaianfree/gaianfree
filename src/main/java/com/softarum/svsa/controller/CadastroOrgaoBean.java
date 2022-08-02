package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.modelo.Endereco;
import com.softarum.svsa.modelo.Orgao;
import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;
import com.softarum.svsa.modelo.enums.Uf;
import com.softarum.svsa.modelo.to.EnderecoTO;
import com.softarum.svsa.service.OrgaoService;
import com.softarum.svsa.service.rest.BuscaCEPService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Talita
 *
 */
@Getter
@Setter
@Named
@ViewScoped
public class CadastroOrgaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Orgao orgao;
	private List<Uf> ufs;
	private EnderecoTO enderecoTO;
	private List<CodigoEncaminhamento> codigos;
	
	@Inject
	private OrgaoService orgaoService;
	@Inject
	private BuscaCEPService buscaCEPService;
	@Inject
	private LoginBean loginBean;
	
	@PostConstruct
	public void inicializar() {
		this.limpar();
		this.ufs = Arrays.asList(Uf.values());
		this.codigos = Arrays.asList(CodigoEncaminhamento.values());
	}
	
	public void salvar() {
		try {
			this.orgaoService.salvar(orgao);
			MessageUtil.sucesso("Orgão salvo com sucesso!");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		
		this.limpar();
	}
	
	public void limpar() {
		this.orgao = new Orgao();
		this.orgao.setEndereco(new Endereco());
		this.orgao.setTenant_id(loginBean.getTenantId());
	}
	
	public void buscaEnderecoPorCEP() {
		
        try {
			enderecoTO  = buscaCEPService.buscaEnderecoPorCEP(orgao.getEndereco().getCep());
			
			/*
	         * Preenche o Endereco com os dados buscados
	         */	 
			//orgao.getEndereco().setCep(cep);
			orgao.getEndereco().setEndereco(enderecoTO.getTipoLogradouro().
	        		                concat(" ").concat(enderecoTO.getLogradouro()));
			orgao.getEndereco().setBairro(enderecoTO.getBairro());
			orgao.getEndereco().setMunicipio(enderecoTO.getCidade());
			orgao.getEndereco().setUf(enderecoTO.getEstado());
	        
	        if (enderecoTO.getResultado() != 1) {
	        	MessageUtil.erro("Endereço não encontrado para o CEP fornecido.");		            
	        }
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());		            
		}       
	}	 
}
