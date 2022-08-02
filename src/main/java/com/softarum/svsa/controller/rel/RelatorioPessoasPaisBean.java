package com.softarum.svsa.controller.rel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.dao.lazy.LazyRelatorioPessoasPais;
import com.softarum.svsa.modelo.Pais;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.util.CronometroUtil;

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
public class RelatorioPessoasPaisBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LazyRelatorioPessoasPais lazyRelatorioPessoasPais;
	//private List<Pessoa> pessoas = new ArrayList<>();
	private int qdeTotal = 0;
	private List<Pais> paises = new ArrayList<>();	
	private Unidade unidade;
	private Pais pais;
	
	@Inject
	PessoaService pessoaService;
	@Inject
	UnidadeService unidadeService;
	@Inject
	LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		
		setUnidade(loginBean.getUsuario().getUnidade());
		
		this.pais = pessoaService.buscarPais(76L); //Brazil
		
		this.paises = this.pessoaService.buscarTodosPaises();
		
		//consultarPessoas();
	}
	
	public void consultarPessoas() {
		//lazyRelatorioPessoasPais = new LazyRelatorioPessoasPais(pessoaService, loginBean.getTenantId());
		
		
		if(loginBean.getUsuario().getGrupo().equals(Grupo.GESTORES)) {
			
			CronometroUtil crono = new CronometroUtil();
			crono.start();
			lazyRelatorioPessoasPais = new LazyRelatorioPessoasPais(pessoaService, null, getPais(), loginBean.getTenantId());
			crono.stop();
			log.info("Tempo de execução: seg:miliseg" + crono.getElapsedSeconds() + ":" + crono.getElapsedMilliseconds());
		}
		else {
			CronometroUtil crono = new CronometroUtil();
			crono.start();
			lazyRelatorioPessoasPais = new LazyRelatorioPessoasPais(pessoaService, getUnidade(), getPais(), loginBean.getTenantId());
			crono.stop();
			log.info("Tempo de execução: seg:miliseg" + crono.getElapsedSeconds() + ":" + crono.getElapsedMilliseconds());
		}

		log.info("Qde total de pessoas : " + getQdeTotal() + " no país: " + getPais().getPais());
		
	}

}