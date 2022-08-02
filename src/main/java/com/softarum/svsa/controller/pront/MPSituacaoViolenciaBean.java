package com.softarum.svsa.controller.pront;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.SituacaoViolencia;
import com.softarum.svsa.service.SituacaoViolenciaService;

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
@Named
@ViewScoped
public class MPSituacaoViolenciaBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287181L;
	
	private List<SituacaoViolencia> situacoesViolencia = new ArrayList<>();
	private List<Pessoa> membros;
	private Pessoa pessoa;
	@Inject
	private SituacaoViolenciaService situacaoService;
	@Inject
	private LoginBean loginBean;
	
	public void buscarSituacoesViolencia(){
		situacoesViolencia = situacaoService.buscarSituacoesViolencia(pessoa, loginBean.getTenantId());
	}	
			
	public void setPessoa(Pessoa pessoa) {
		log.info("Pessoa selecionada: " + pessoa.getNome());
		this.pessoa = pessoa;
		buscarSituacoesViolencia();
	}

	public void setMembros(List<Pessoa> membros) {
		this.membros = membros;
		if(this.membros != null && this.membros.size() > 0 )
			setPessoa(membros.get(0));
	}
	
}