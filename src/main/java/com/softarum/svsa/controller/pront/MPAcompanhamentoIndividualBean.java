package com.softarum.svsa.controller.pront;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.MetaPia;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Pia;
import com.softarum.svsa.service.PiaService;

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
public class MPAcompanhamentoIndividualBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287181L;
	
	private List<Pia> planos = new ArrayList<>();
	private List<Pessoa> membros;
	private Pessoa pessoa;
	private Pia plano;
	private Pia planoSelecionado; // Atributo criado para chamar um método diferente ao settar um plano (ver setter)
	private ArrayList<MetaPia> metas;
	@Inject
	private PiaService piaService;
	@Inject
	private LoginBean loginBean;
	
	public void buscarPlanos(){
		planos = piaService.buscarPlanos(pessoa, loginBean.getTenantId());
	}
	
	public void detalharPlano() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('mseWidget').show();");
	}
	
	public void detalharMetas(Long tenantId) {
		metas = (ArrayList<MetaPia>) piaService.buscarMetas(planoSelecionado, tenantId);
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('metasWidget').show();");
	}
	
	public void setPessoa(Pessoa pessoa) {
		log.info("Pessoa selecionada: " + pessoa.getNome());
		this.pessoa = pessoa;
		buscarPlanos();
	}
	
	public void setMembros(List<Pessoa> membros) {
		this.membros = membros;
		if(this.membros != null && this.membros.size() > 0 )
			setPessoa(membros.get(0));
	}
	
	public void setPlano(Pia plano) {
		this.plano = plano;
		detalharPlano();
	}
	
	public void setPlanoSelecionado(Pia planoSelecionado) {
		this.planoSelecionado = planoSelecionado;
		detalharMetas(loginBean.getTenantId());
	}

}