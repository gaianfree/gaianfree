package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.modelo.Endereco;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.enums.Uf;
import com.softarum.svsa.modelo.to.EnderecoTO;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.service.rest.BuscaCEPService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

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
public class CadastroUnidadeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Unidade unidade;
	private List<Uf> ufs;
	private String cep = null;
	private EnderecoTO enderecoTO;	
	private List<TipoUnidade> tipos;
	private List<Unidade> unidades = new ArrayList<>();
	private boolean scfv = false;
	
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private BuscaCEPService buscaCEPService;
	
	
	
	/*
	 * INI tratamento Tenant
	 */
	@Inject
	private LoginBean usuarioLogado;
	private Long tenantId;	
	
	@PostConstruct
	public void inicializar() {
		tenantId = usuarioLogado.getUsuario().getTenant().getCodigo();
		log.info("Bean : tenant = " + tenantId + "-" + usuarioLogado.getUsuario().getTenant().getTenant());		
		this.limpar();
		this.tipos = Arrays.asList(TipoUnidade.values());
		this.ufs = Arrays.asList(Uf.values());
	}
	/*
	 * FIM tratamento Tenant
	 */
	
	
	public void salvar() {
		try {
			
			unidade.getEndereco().setMunicipio(unidade.getEndereco().getMunicipio());
			this.unidadeService.salvar(unidade);
			MessageUtil.sucesso("Unidade salva com sucesso!");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		
		this.limpar();
	}
	
	public void limpar() {
		this.unidade = new Unidade();
		this.unidade.setEndereco(new Endereco());
		this.unidade.setTenant_id(tenantId);
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
	
	public void carregarUnidades() {
		
		unidades = unidadeService.buscarCRAS(usuarioLogado.getTenantId());
		if(unidade.getTipo() == TipoUnidade.SCFV) {			
			log.info("scfv = true");
			scfv = true;
		}
		else {
			unidade.setUnidadeVinculada(null);
			log.info("scfv = false");
			scfv = false;
		}
	}

}
