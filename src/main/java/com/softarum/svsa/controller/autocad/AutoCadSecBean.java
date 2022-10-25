package com.softarum.svsa.controller.autocad;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Endereco;
import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.enums.Uf;
import com.softarum.svsa.modelo.to.AutoCadSecTO;
import com.softarum.svsa.modelo.to.EnderecoTO;
import com.softarum.svsa.service.AutoCadSecService;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.service.rest.BuscaCEPService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author isadora
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class AutoCadSecBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private AutoCadSecTO autocadTO;
	private EnderecoTO enderecoTO;

	@Inject
	private AutoCadSecService autocadService;
	private LoginBean usuarioLogado;
	private Long tenantId;

	@Inject
	private BuscaCEPService buscaCEPService;

	private List<Uf> ufs;
	private boolean skip;

	@PostConstruct
	public void inicializar() {

		this.limpar();
		this.ufs = Arrays.asList(Uf.values());

	}


	public void salvarTenant() {

		try {
			
			Tenant secretaria = this.autocadService.salvarTenant(autocadTO.getSecretaria());
			autocadTO.setSecretaria(secretaria);
			MessageUtil.sucesso("Secretaria salva com sucesso!");
			
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}

	}
	
	
	public void salvarUnidade() {

		try {
			
			autocadTO.getUnidade().setTenant_id(autocadTO.getSecretaria().getCodigo());
			autocadTO.getUnidade().setTipo(TipoUnidade.CRAS);
			Unidade unidade = this.autocadService.salvarUnidade(autocadTO.getUnidade());			
			autocadTO.setUnidade(unidade);
			
			
			MessageUtil.sucesso("Unidade Salva com sucesso!");
			
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}

	}
	
	
	public void salvarUsuario() {

		try {
			
			autocadTO.getUsuario().setUnidade(autocadTO.getUnidade());
			autocadTO.getUsuario().setTenant(autocadTO.getSecretaria());
			autocadTO.getUsuario().setRole(Role.ASSISTENTE_SOCIAL);
			autocadTO.getUsuario().setGrupo(Grupo.COORDENADORES);
			Usuario usuario = this.autocadService.salvarUsuario(autocadTO.getUsuario());
			autocadTO.setUsuario(usuario);
			MessageUtil.sucesso("Usuario salvo com sucesso!");
			
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}

	}
	

	public void limpar() {
		this.autocadTO = new AutoCadSecTO();

		autocadTO.setSecretaria(new Tenant());

		autocadTO.setUnidade(new Unidade());
		autocadTO.getUnidade().setEndereco(new Endereco());
		
		//TODO setar o usuario recebido
		autocadTO.setUsuario(new Usuario());
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String onFlowProcess(FlowEvent event) {

			if (event.getOldStep().equals("secretaria")){
	            log.info(event.getNewStep());
				this.salvarTenant();
	           
	        }
			else if (event.getOldStep().equals("unidade")){
				this.salvarUnidade();
				
			}
			
			else {
				this.salvarUsuario();
				
			}
				
			
			return event.getNewStep();
	}

	public void buscaEnderecoPorCEP() {

		try {
			enderecoTO = buscaCEPService.buscaEnderecoPorCEP(autocadTO.getUnidade().getEndereco().getCep());

			/*
			 * Preenche o Endereco com os dados buscados
			 */
			// unidade.getEndereco().setCep(cep);
			autocadTO.getUnidade().getEndereco()
					.setEndereco(enderecoTO.getTipoLogradouro().concat(" ").concat(enderecoTO.getLogradouro()));
			autocadTO.getUnidade().getEndereco().setUf(enderecoTO.getEstado());
			
			if (enderecoTO.getResultado() != 1) {
				MessageUtil.erro("Endereço não encontrado para o CEP fornecido.");
			}
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}

}
