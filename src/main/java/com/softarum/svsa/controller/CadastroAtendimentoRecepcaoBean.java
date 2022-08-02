package com.softarum.svsa.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
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
public class CadastroAtendimentoRecepcaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ListaAtendimento item;
	private String nomePessoaAtendida;

	@Inject
	AgendamentoIndividualService listaAtendimentoService;	
	@Inject
	LoginBean loginBean;
	
		
	@PostConstruct
	public void inicializar() {
		
		this.limpar();		
		
	}
	
	public void salvar() {
		try {			
			item.setCodigoAuxiliar(CodigoAuxiliarAtendimento.ATENDIMENTO_RECEPCAO);
			item.setRole(loginBean.getUsuario().getRole());
			item.setTecnico(loginBean.getUsuario());
			item.setUnidade(loginBean.getUsuario().getUnidade());
			String str = " : ";
			String resumoNome = nomePessoaAtendida.concat(str).concat(item.getResumoAtendimento());
			item.setResumoAtendimento(resumoNome);
			
			this.listaAtendimentoService.salvarResumoRecepcao(item);
			MessageUtil.sucesso("Atendimento Receção salvo com sucesso!");
			this.limpar();
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.erro("Erro desconhecido. Contatar o administrador");
		}
		
		this.limpar();
	}
	
	public void limpar() {
		this.item = new ListaAtendimento();
		this.item.setTenant_id(loginBean.getTenantId());
	}

}