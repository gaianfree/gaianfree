package com.softarum.svsa.controller.scfv;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
//import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Inscricao;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Servico;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.service.ServicoService;

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
public class UsuariosSCFVUIBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Servico servico;
	private Unidade unidade;
	private Pessoa pessoa;
	private Set<Inscricao> inscricoes;
	
	@Inject
	private LoginBean loginBean;
	@Inject
	private ServicoService servicoService;

	@PostConstruct
	public void inicializar() {			
		setUnidade(loginBean.getUsuario().getUnidade());
	}	
	
	public void setServico(Servico servico) {		
		this.servico = servico;
		inscricoes = servicoService.buscarInscricoesAtivas(servico, loginBean.getTenantId());
	}
}