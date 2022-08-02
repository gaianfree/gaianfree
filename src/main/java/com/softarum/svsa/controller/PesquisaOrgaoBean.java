package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.modelo.Orgao;
import com.softarum.svsa.service.OrgaoService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Talita
 *
 */
@Getter
@Setter
@Named
@ViewScoped
public class PesquisaOrgaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Orgao> orgaos = new ArrayList<>();
	private Orgao orgaoSelecionado;
	
	@Inject
	private OrgaoService orgaoService;
	@Inject
	private LoginBean loginBean;

		
	@PostConstruct
	public void inicializar() {
		orgaos = orgaoService.buscarTodos(loginBean.getTenantId());
	}
	
	public void excluir() {
		try {
			orgaoService.excluir(orgaoSelecionado);			
			this.orgaos.remove(orgaoSelecionado);
			MessageUtil.sucesso("Orgao " + orgaoSelecionado.getNome() + " excluído com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
}