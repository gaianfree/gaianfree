package com.softarum.svsa.controller.pront.fam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.BeneficioEventual;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.enums.Beneficio;
import com.softarum.svsa.service.BeneficioEventualService;
import com.softarum.svsa.service.MPComposicaoService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;


/**
 * @author gabrielrodrigues
 *
 */
@Log4j
@Getter
@Setter
@Named(value="beneficioBean")
@ViewScoped
public class BeneficioEventualBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private PessoaReferencia pessoaReferencia;
	private List<PessoaReferencia> listaPessoasReferencia = new ArrayList<>();
	private BeneficioEventual beneficioEventual;
	

	private List<Beneficio> beneficios;
	private List<BeneficioEventual> beneficiosEventuais;
	private Prontuario prontuario;
	private boolean auxilioFuneral;
	private boolean auxilioNatalidade;
	
	@Inject
	private MPComposicaoService composicaoService;
	@Inject
	private BeneficioEventualService beneficioEventualService;
	@Inject
	private LoginBean loginBean;
	

	@PostConstruct
	public void inicializar() {
		this.setBeneficios(Arrays.asList(Beneficio.values()));	
		
		this.limpar();	
	}

	public void setPessoaReferencia(PessoaReferencia pessoaReferencia) {
		this.pessoaReferencia = pessoaReferencia;
		log.info("selecionar pessoa ref" + pessoaReferencia.getFamilia().getProntuario());
		this.prontuario = (Prontuario) this.pessoaReferencia.getFamilia().getProntuario();
		beneficioEventual.setProntuario(this.prontuario);
		beneficiosEventuais = beneficioEventualService.buscarBeneficios(beneficioEventual.getProntuario(), loginBean.getTenantId());
	}

	public void todasPessoasReferencia() {
        listaPessoasReferencia = composicaoService.todasPessoasReferencia(loginBean.getTenantId());
    }
	
	public void salvar() {
		try {
			beneficioEventual.setTecnico(loginBean.getUsuario());
			this.beneficioEventualService.salvar(beneficioEventual);
			beneficiosEventuais = beneficioEventualService.buscarBeneficios(beneficioEventual.getProntuario(), loginBean.getTenantId());

			MessageUtil.sucesso("Registrado com sucesso.");

			limpar();

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void limpar() {
		this.beneficioEventual = new BeneficioEventual();
		beneficioEventual.setProntuario(this.prontuario);
		beneficioEventual.setTenant_id(loginBean.getTenantId());
	}
	
	public void verificaAuxilio() {
		log.info("Verifica Auxilios");
		if (beneficioEventual.getBeneficio() == Beneficio.AUXILIO_FUNERAL) {
			auxilioFuneral = true;
			auxilioNatalidade = false;
		}
		else if (beneficioEventual.getBeneficio() == Beneficio.AUXILIO_NATALIDADE) {
			auxilioNatalidade = true;
			auxilioFuneral = false;
		}
		else {
			auxilioFuneral = false;
			auxilioNatalidade = false;
		}
	}
	
}