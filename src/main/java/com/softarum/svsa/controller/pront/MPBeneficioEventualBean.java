package com.softarum.svsa.controller.pront;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.BeneficioEventual;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.service.BeneficioEventualService;

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
public class MPBeneficioEventualBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287181L;
	
	private List<BeneficioEventual> beneficiosEventuais = new ArrayList<BeneficioEventual>();
	private PessoaReferencia pessoaReferencia;	
	
	@Inject
	LoginBean loginBean;
	@Inject
	private BeneficioEventualService beneficioService;
		

	public void buscarBeneficiosEventuais(){
			setBeneficiosEventuais(beneficioService.buscarBeneficios(getPessoaReferencia().getFamilia().getProntuario(), loginBean.getTenantId()));
			
			log.info("beneficios " + getBeneficiosEventuais().size());
	}
	
	public boolean isPessoaReferenciaSelecionada() {
        return pessoaReferencia != null && pessoaReferencia.getCodigo() != null;
    }
	
	public void setPessoaReferencia(PessoaReferencia pessoaReferencia) {
		this.pessoaReferencia = pessoaReferencia;
		buscarBeneficiosEventuais();
	}
	
}