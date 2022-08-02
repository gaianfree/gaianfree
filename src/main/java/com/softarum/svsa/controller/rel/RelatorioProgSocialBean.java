package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.modelo.enums.ProgramaSocial;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.UnidadeService;

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
public class RelatorioProgSocialBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private List<Pessoa> pessoas = new ArrayList<>();
	private int qdeTotal = 0;
	private List<ProgramaSocial> programas = new ArrayList<>();	
	private Unidade unidade;
	private ProgramaSocial programa;
	
	@Inject
	PessoaService pessoaService;
	@Inject
	UnidadeService unidadeService;
	@Inject
	LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		
		this.programa = ProgramaSocial.AUXILIO_BRASIL;
		this.programas = Arrays.asList(ProgramaSocial.values());	
		setUnidade(loginBean.getUsuario().getUnidade());
		consultarPessoas();
	}
	
	public void consultarPessoas() {
		if(loginBean.getUsuario().getGrupo().equals(Grupo.GESTORES)) {
			pessoas = pessoaService.pesquisarPessoaPorProgSocial(getPrograma(), loginBean.getTenantId());
		}
		else {
			pessoas = pessoaService.pesquisarPessoaPorProgSocial(getPrograma(), getUnidade(), loginBean.getTenantId());		
		}
		
		this.setQdeTotal(pessoas.size());
		log.info("Qde total de pessoas : " + getQdeTotal() + " no programa: " + getPrograma());	
	}

	}