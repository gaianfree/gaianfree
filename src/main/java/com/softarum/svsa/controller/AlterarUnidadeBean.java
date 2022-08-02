package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Uf;
import com.softarum.svsa.modelo.to.EnderecoTO;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.service.rest.BuscaCEPService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

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
public class AlterarUnidadeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Unidade unidade;
	private List<Uf> ufs;
	private EnderecoTO enderecoTO;	
	
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private BuscaCEPService buscaCEPService;	
	@Inject
	private LoginBean usuarioLogado;
	
	@PostConstruct
	public void inicializar() {
		unidade = usuarioLogado.getUsuario().getUnidade();
		this.ufs = Arrays.asList(Uf.values());
	}
	
	/* AlterarUnidade.xhtml */
	public void salvarAlteracao() {
		try {
			unidade.getEndereco().setMunicipio(unidade.getEndereco().getMunicipio());
			this.unidadeService.salvar(unidade);
			MessageUtil.sucesso("Unidade alterada com sucesso!");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}	
	}
		
	public void buscaEnderecoPorCEP() {
		
        try {
			enderecoTO  = buscaCEPService.buscaEnderecoPorCEP(unidade.getEndereco().getCep());
			
			/*
	         * Preenche o Endereco com os dados buscados
	         */	 
			//unidade.getEndereco().setCep(cep);
			unidade.getEndereco().setEndereco(enderecoTO.getTipoLogradouro().
	        		                concat(" ").concat(enderecoTO.getLogradouro()));
			unidade.getEndereco().setBairro(enderecoTO.getBairro());
			unidade.getEndereco().setMunicipio(enderecoTO.getCidade());
			unidade.getEndereco().setUf(enderecoTO.getEstado());
	        
	        if (enderecoTO.getResultado() != 1) {
	        	MessageUtil.erro("Endereço não encontrado para o CEP fornecido.");		            
	        }
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());		            
		}       
	}
	
}
