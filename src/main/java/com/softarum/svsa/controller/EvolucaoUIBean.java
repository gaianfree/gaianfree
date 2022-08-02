package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
//import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.service.AgendamentoIndividualService;

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
public class EvolucaoUIBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Pessoa pessoa;
	private List<AtendimentoDTO> evolucao = new ArrayList<>();	
	
	@Inject
	private LoginBean loginBean;
	@Inject
	private AgendamentoIndividualService evolucaoService;
	
	public void setPessoa(Pessoa pessoa) {		
		this.pessoa = pessoa;
		log.info("Buscando evolucao da pessoa : " + pessoa.getCodigo() + "-" + pessoa.getNome());
		this.evolucao = evolucaoService.buscarResumoAtendimentosDTO(pessoa, loginBean.getTenantId());
	}
}