package com.softarum.svsa.controller.pront.ind;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.SituacaoViolencia;
import com.softarum.svsa.modelo.enums.Violencia;
import com.softarum.svsa.service.SituacaoViolenciaService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;


/**
 * @author gabrielrodrigues - refactored by Murakami
 *
 */
@Log4j
@Getter
@Setter
@Named(value="violenciaBean")
@ViewScoped
public class SituacaoViolenciaBean implements Serializable {

	
	private static final long serialVersionUID = 1769116747361287180L;
	
	private SituacaoViolencia situacaoViolencia;
	private List<SituacaoViolencia> listaSituacoes;	
	private List<Violencia> situacoes;	
	private Pessoa pessoa;
	@Inject
	private SituacaoViolenciaService situacaoViolenciaService;
	@Inject
	private LoginBean loginBean;
	
	
	
	@PostConstruct
	public void inicializar() {		
		log.debug("[LOG] " + loginBean.getUserName() + " -> " + this.getClass().getSimpleName());
		
		situacoes = Arrays.asList(Violencia.values());	
		
		this.limpar();	
	}		

	public void salvar() {
		try {
			
			this.situacaoViolencia.setPessoa(pessoa);
			situacaoViolencia.setTenant_id(loginBean.getTenantId());
			this.situacaoViolenciaService.salvar(situacaoViolencia);
			carregarSituacoesViolencia();

			MessageUtil.sucesso("Situação gravada com sucesso.");

			limpar();

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void excluir() {
		try {			
			
			this.situacaoViolenciaService.excluir(situacaoViolencia);
			
			this.listaSituacoes.remove(situacaoViolencia);
			MessageUtil.sucesso("Situação de Violência " + situacaoViolencia.getCodigo() + " excluída com sucesso.");
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		
	}
	
	public void encerrar() {
		try {			
			log.info("Encerrando situacao de violencia");
			this.situacaoViolenciaService.encerrar(situacaoViolencia);			
			
			carregarSituacoesViolencia();
			MessageUtil.sucesso("Situação de Violência " + situacaoViolencia.getCodigo() + " encerrada com sucesso.");
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
		
	}
	
	public void limpar() {
		this.situacaoViolencia = new SituacaoViolencia();
		situacaoViolencia.setTecnico(loginBean.getUsuario());
		situacaoViolencia.setTenant_id(loginBean.getTenantId());
	}
	
	
	public void carregarSituacoesViolencia() {
		listaSituacoes = situacaoViolenciaService.buscarSituacoesViolencia(pessoa, loginBean.getTenantId());
	}
	
		
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
		
		listaSituacoes = situacaoViolenciaService.buscarSituacoesViolencia(pessoa, loginBean.getTenantId());		
	}
	
	public boolean isSituacaoSelecionada() {
		
		return  situacaoViolencia != null && situacaoViolencia.getCodigo() != null;
	}
	
}