package com.softarum.svsa.controller.pront;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.ConvivenciaFamiliar;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.service.ConvivenciaFamiliarService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author gabriel rodrigues
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class MPConvivenciaFamiliarBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287181L;
	
	private ConvivenciaFamiliar convivenciaFamiliar = null;
	private PessoaReferencia pessoaReferencia;	
	
	@Inject
	LoginBean loginBean;
	@Inject
	private ConvivenciaFamiliarService convivenciaService;
		
	
	public void buscarConvivenciaFamiliar(){
		try {
			convivenciaFamiliar = convivenciaService.obterConvivenciaFamiliar(getPessoaReferencia().getFamilia().getProntuario(), loginBean.getTenantId());
			log.info(convivenciaFamiliar.toString());
		}
		catch(NoResultException e) {
			convivenciaFamiliar = null;
		}
	}
	
	public boolean isPessoaReferenciaSelecionada() {
        return pessoaReferencia != null && pessoaReferencia.getCodigo() != null;
    }
	
	public void setPessoaReferencia(PessoaReferencia pessoaReferencia) {
		this.pessoaReferencia = pessoaReferencia;
		buscarConvivenciaFamiliar();
	}
	
}