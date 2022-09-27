package com.softarum.svsa.controller.autocad;

import java.io.Serializable;
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
import com.softarum.svsa.modelo.to.AutoCadSecTO;
import com.softarum.svsa.service.AutoCadSecService;
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
	private Usuario usuario;
	private Unidade unidade;
	private Tenant secretaria;
	@Inject
	private AutoCadSecService autocadService;
	
	private LoginBean usuarioLogado;
	private Long codigo; //coluna no banco de dados Tenant
	private boolean scfv = false;
	private boolean skip;
	
		@PostConstruct
	public void inicializar() {

		
	//	codigo = usuarioLogado.getUsuario().getTenant().getCodigo();
		//log.info("Bean : tenant = " + codigo + "-" + usuarioLogado.getUsuario().getTenant().getTenant());		
		this.limpar();
		autocadTO.setUsuario(usuario);
		
	}

	
	
	
	
	 
public void salvar() {
		try {
			unidade.getEndereco().getCep();
			this.autocadService.salvar(autocadTO);
			MessageUtil.sucesso("Salvo com sucesso!");
			this.limpar();
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		
		this.limpar();
	}
	
	public void limpar() {
		this.autocadTO = new AutoCadSecTO();
		
		Tenant secretaria = new Tenant();
		autocadTO.setSecretaria(secretaria);
		
		
		this.unidade = new Unidade();
		this.unidade.setEndereco(new Endereco());
		autocadTO.setUnidade(unidade);
		

	}
	
	public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }
	
	public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false; //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }
	
   /* public void carregarSecretaria() {
		
		secretaria = tenantService.buscarCRAS(usuarioLogado.getTenantId());
		 if(tenant.getTipo() == TipoUnidade.SCFV) {			
			log.info("scfv = true");
			scfv = true;
		}
		else {
			log.info("scfv = false");
			scfv = false;
		}
	}*/

}
