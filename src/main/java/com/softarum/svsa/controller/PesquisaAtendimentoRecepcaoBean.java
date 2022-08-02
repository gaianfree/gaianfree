package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
//import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.service.AgendamentoIndividualService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

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
public class PesquisaAtendimentoRecepcaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<ListaAtendimento> itensAtendimento = new ArrayList<>();
	
	private ListaAtendimento itemExcluir;
	
	@Inject
	AgendamentoIndividualService listaAtendimentoService;	
	@Inject
	LoginBean loginBean;
	
	@PostConstruct
	public void inicializar() {
		
		itensAtendimento = listaAtendimentoService.buscarAtendimentosRecepcao(loginBean.getUsuario(), loginBean.getTenantId());
	}
	
	public void excluir() {
		try {
			listaAtendimentoService.excluir(itemExcluir);
			
			this.itensAtendimento.remove(itemExcluir);
			MessageUtil.sucesso("Atendimento recepção número (" + itemExcluir.getCodigo() + ") excluído com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
}