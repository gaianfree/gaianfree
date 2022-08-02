package com.softarum.svsa.controller.pront;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.MetaPlanoFamiliar;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.PlanoAcompanhamento;
import com.softarum.svsa.service.PlanoAcompanhamentoService;

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
public class MPAcompanhamentoFamiliarBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287181L;
	
	private List<PlanoAcompanhamento> planos = new ArrayList<>();
	private PlanoAcompanhamento plano;
	private List<MetaPlanoFamiliar> avaliacoes;
	private PessoaReferencia pessoaReferencia;	
	private MetaPlanoFamiliar avaliacao;		
	
	@Inject
	private LoginBean loginBean;
	@Inject
	private PlanoAcompanhamentoService planoService;
		

	public void buscarPlanos(){
		setPlanos(planoService.buscarPlanos(getPessoaReferencia().getFamilia().getProntuario(), loginBean.getTenantId()));
		if(planos != null && planos.size() > 0 ) {
			setPlano(planos.get(0));		
			setAvaliacoes(planoService.buscarMetas(getPlano(), loginBean.getTenantId()));
		}
	}	
	
	public void buscarAvaliacoes() {
		log.info("mpAcompanhamentoBean buscarAvaliações do plano: " + plano.getCodigo());
		setAvaliacoes(planoService.buscarMetas(plano, loginBean.getTenantId()));
	}
	
	public boolean isPessoaReferenciaSelecionada() {
        return pessoaReferencia != null && pessoaReferencia.getCodigo() != null;
    }
	
	public boolean isPlanoSelecionado() {
		if(getPlano() != null && getPlano().getCodigo() != null)
			return true;
		return false;
	}
	
	public void setPlano(PlanoAcompanhamento plano) {
		this.plano = plano;
		log.info("(MPAcompanhamentoFamiliarBean) setPlano : " + plano);		
	}
	
	public void setPessoaReferencia(PessoaReferencia pessoaReferencia) {
		this.pessoaReferencia = pessoaReferencia;
		buscarPlanos();
	}
	
}