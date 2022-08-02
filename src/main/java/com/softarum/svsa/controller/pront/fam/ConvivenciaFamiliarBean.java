package com.softarum.svsa.controller.pront.fam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.ConvivenciaFamiliar;
import com.softarum.svsa.modelo.ObsConvivenciaFamiliar;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.enums.ConvivenciaIntrafamiliar;
import com.softarum.svsa.service.ConvivenciaFamiliarService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

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
@Named(value="convivenciaBean")
@ViewScoped
public class ConvivenciaFamiliarBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private PessoaReferencia pessoaReferencia;	
	private ConvivenciaFamiliar convivenciaFamiliar;
	private List<ObsConvivenciaFamiliar> observacoes;
	private ObsConvivenciaFamiliar observacao;
	/* Enums*/
	private List<ConvivenciaIntrafamiliar> tiposConvivencia;
	
	@Inject
	private ConvivenciaFamiliarService convivenciaFamiliarService;
	@Inject
	private LoginBean loginBean;
	
	
	
	@PostConstruct
	public void inicializar() {			
		log.info("[LOG] " + loginBean.getUserName() + " -> " + this.getClass().getSimpleName());
		
		tiposConvivencia = Arrays.asList(ConvivenciaIntrafamiliar.values());
		
		observacao = new ObsConvivenciaFamiliar();
	}	

	
	public void salvar() {
		try {						
			convivenciaFamiliar.setTecnico(loginBean.getUsuario());
			
			convivenciaFamiliar = convivenciaFamiliarService.salvar(convivenciaFamiliar);
			MessageUtil.sucesso("Convivencia familiar gravada com sucesso.");	
			
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void salvarObservacao() throws NegocioException {
		
			observacao.setTecnico(loginBean.getUsuario());
			observacao.setTenant_id(loginBean.getTenantId());
			observacao.setConvivenciaFamiliar(convivenciaFamiliar);
			convivenciaFamiliar.getObservacoes().add(observacao);			
			convivenciaFamiliar.setObservacoes(convivenciaFamiliarService.salvarObservacao(convivenciaFamiliar));
			
			MessageUtil.sucesso("Observação gravada com sucesso.");
			
			observacao = new ObsConvivenciaFamiliar();
	}

	public void setPessoaReferencia(PessoaReferencia pessoaReferencia) {
		this.pessoaReferencia = pessoaReferencia;
		log.info("PF selecionada: " + getPessoaReferencia().getNome());
		
		// verificar se existe convivenciaFamiliar para o prontuário
		try {
			convivenciaFamiliar = convivenciaFamiliarService.obterConvivenciaFamiliar(pessoaReferencia.getFamilia().getProntuario(), loginBean.getTenantId());
		}
		catch(NoResultException e) {
			
			convivenciaFamiliar = new ConvivenciaFamiliar();
			convivenciaFamiliar.setProntuario(pessoaReferencia.getFamilia().getProntuario());
			observacoes = new ArrayList<ObsConvivenciaFamiliar>();
			convivenciaFamiliar.setObservacoes(observacoes);			
		}
	}

}