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
import com.softarum.svsa.modelo.CondicaoHabitacional;
import com.softarum.svsa.modelo.ObsCondicaoHabitacional;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.enums.AcessoEnergiaEletrica;
import com.softarum.svsa.modelo.enums.ColetaLixo;
import com.softarum.svsa.modelo.enums.EscoamentoSanitario;
import com.softarum.svsa.modelo.enums.FormaAbastecimento;
import com.softarum.svsa.modelo.enums.MaterialParede;
import com.softarum.svsa.modelo.enums.PossuiAcessibilidade;
import com.softarum.svsa.modelo.enums.TipoResidencia;
import com.softarum.svsa.service.CondicaoHabitacionalService;
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
@Named(value="habitacionalBean")
@ViewScoped
public class CondicaoHabitacionalBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private PessoaReferencia pessoaReferencia;	
	private CondicaoHabitacional condicaoHabitacional;
	private List<ObsCondicaoHabitacional> observacoes;
	private ObsCondicaoHabitacional observacao;
	/* Enums*/
	private List<TipoResidencia> tiposResidencias;
	private List<FormaAbastecimento> tiposAbastecimento;
	private List<AcessoEnergiaEletrica> tiposAcesso;
	private List<MaterialParede> tiposMateriais;
	private List<ColetaLixo> tiposColeta;
	private List<PossuiAcessibilidade> tiposAcessibilidade;
	private List<EscoamentoSanitario> tiposEscoamento;
	@Inject
	private CondicaoHabitacionalService condicaoHabitacionalService;
	@Inject
	private LoginBean loginBean;
	
	
	
	@PostConstruct
	public void inicializar() {			
		log.info("[LOG] " + loginBean.getUserName() + " -> " + this.getClass().getSimpleName());
		
		tiposResidencias = Arrays.asList(TipoResidencia.values());
		tiposAbastecimento = Arrays.asList(FormaAbastecimento.values());
		tiposAcesso = Arrays.asList(AcessoEnergiaEletrica.values());
		tiposMateriais = Arrays.asList(MaterialParede.values());
		tiposColeta = Arrays.asList(ColetaLixo.values());
		tiposAcessibilidade = Arrays.asList(PossuiAcessibilidade.values());
		tiposEscoamento = Arrays.asList(EscoamentoSanitario.values());	
		
		observacao = new ObsCondicaoHabitacional();
	}	

	
	public void salvar() {
		try {			
			
			condicaoHabitacional.setTecnico(loginBean.getUsuario());
			condicaoHabitacional.setTenant_id(loginBean.getTenantId());
			//prontuario.setCondicaoHabitacional(condicaoHabitacional);
			
			condicaoHabitacional = condicaoHabitacionalService.salvar(condicaoHabitacional);
			MessageUtil.sucesso("Condição Educacional gravada com sucesso.");	
			
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void salvarObservacao() throws NegocioException {
		
			observacao.setTecnico(loginBean.getUsuario());
			observacao.setCondicaoHabitacional(condicaoHabitacional);
			condicaoHabitacional.getObservacoes().add(observacao);			
			condicaoHabitacional.setObservacoes(condicaoHabitacionalService.salvarObservacao(condicaoHabitacional));
			
			MessageUtil.sucesso("Observação gravada com sucesso.");
			
			observacao = new ObsCondicaoHabitacional();
	}	

	public void setPessoaReferencia(PessoaReferencia pessoaReferencia) {
		this.pessoaReferencia = pessoaReferencia;
		
		log.info("PF selecionada: " + getPessoaReferencia().getNome());
		
		// verificar se existe condicaoHabitacional para o prontuário
		try {
			condicaoHabitacional = condicaoHabitacionalService.obterCondicaohabitacional(pessoaReferencia.getFamilia().getProntuario(), loginBean.getTenantId());
		}
		catch(NoResultException e) {
			
			condicaoHabitacional = new CondicaoHabitacional();
			condicaoHabitacional.setProntuario(pessoaReferencia.getFamilia().getProntuario());
			observacoes = new ArrayList<ObsCondicaoHabitacional>();
			condicaoHabitacional.setObservacoes(observacoes);			
		}
	}		

}