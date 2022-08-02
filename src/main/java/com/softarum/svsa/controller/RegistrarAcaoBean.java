package com.softarum.svsa.controller;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
//import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.softarum.svsa.modelo.Acao;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.AcaoService;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.UsuarioService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */

@Getter
@Setter
@Log4j

@Named
@ViewScoped
public class RegistrarAcaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Acao> acoes = new ArrayList<>();
	private Acao acao = new Acao();
	private Pessoa pessoa;
	private List<Usuario> tecnicos;
	private boolean statusPoll = true;
	
	@Inject
	private PessoaService pessoaService;
	@Inject
	private AcaoService acaoService;
	@Inject
	LoginBean loginBean;
	@Inject 
	UsuarioService usuarioService;
				
	@PostConstruct
	public void inicializar() {		
		
		statusPoll = true;
		this.acoes = acaoService.buscarAcoesPendentes(loginBean.getUsuario().getUnidade(), loginBean.getTenantId());
		log.info("Ação: inicializar...");
		limpar();
	}
	
	public void salvar() {
		try {
			log.info("Ação: sanvando..." + loginBean.getUsuario().getNome() + "---unidade: " + loginBean.getUsuario().getUnidade().getCodigo());
			acao.setAgendador(loginBean.getUsuario());
			acao.setTenant_id(loginBean.getTenantId());
			
			if(acao.getTecnico() != null)
				acao.setTecnico(loginBean.getUsuario());
			
			if(acao.getPessoas().size() < 1 || acao.getPessoas().isEmpty() || acao.getPessoas() == null) {
				
				MessageUtil.erro("É necessário pelo menos uma pessoa!");
			} else if(acao.getPessoas().size() > 1) {	
				
				this.acaoService.salvar(acao);					
				log.info("Ação coletiva salva.");
				MessageUtil.sucesso("Ação coletiva gravada com sucesso!");	
				limpar();
			} else if (acao.getPessoas().size() == 1) {		
				
				acao.setPessoa(acao.getPessoas().get(0));
				acao.setPessoas(null);
				this.acaoService.salvar(acao);	
				log.info("Ação individual gravada com sucesso!" + acao.getPessoa().getNome());
				MessageUtil.sucesso("Ação individual gravada com sucesso!");
				limpar();
			}
			
			buscarAcao(loginBean.getUsuario().getUnidade());
						
		} catch (NegocioException e) {
			log.info("Problema na gravação da ação!");
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void autoSave() {
		try {
			Instant time = Instant.now();			
			log.info("auto save... : " + time);
			
			acao.setTenant_id(loginBean.getTenantId());
			acao.setTecnico(loginBean.getUsuario());
			
			if(acao.getPessoas().size() < 1 || acao.getPessoas().isEmpty() || acao.getPessoas() == null) {
				
				MessageUtil.erro("É necessário pelo menos uma pessoa!");
			} else if(acao.getPessoas().size() > 0) {	
				
				acao = this.acaoService.autosave(getAcao(), loginBean.getUsuario());
				MessageUtil.sucesso("Auto save executado.");
			}
								
		} catch (NegocioException e) {
			log.error(e.getMessage());
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void excluir() {
		try {
			log.info("acao excluida");
			this.acaoService.excluir(acao);
			this.acoes.remove(acao);
			MessageUtil.sucesso("Ação " + acao.getCodigo() + " excluída com sucesso.");
			
			limpar();
				
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	private void buscarAcao(Unidade unidade) {
		acoes = acaoService.buscarAcao(unidade, loginBean.getTenantId());		
	}
	
	public void atualizarDataTable() {
		this.acoes = acaoService.buscarAcoesPendentes(loginBean.getUsuario().getUnidade(), loginBean.getTenantId());
	}
	
	
	public void excluirPessoa(Pessoa p) {
		
		acao.getPessoas().remove(p);
	}
	
	public void abrirDialogo() {
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("draggable", true);
        options.put("responsive", true);
        options.put("closeOnEscape", true);
        PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPessoa", options, null);        	
    }	
	
	public boolean isPessoaSelecionada() {
        return acao.getPessoas() != null && !acao.getPessoas().isEmpty();
    }	
	
	public void selecionarPessoa(SelectEvent<?> event) {			

		PessoaDTO dto = (PessoaDTO) event.getObject();		
		Pessoa p = pessoaService.buscarPeloCodigo(dto.getCodigo());
		
		if(!acao.getPessoas().contains(p)) {
			acao.getPessoas().add(p);
		}
		else {
			MessageUtil.sucesso("Pessoa ja adicionada! -> " + p.getNome());	
		}		
		log.info("Pessoa adicionada" + p.getNome());
	}
    public void stopPoll() {
		log.debug("true");
		statusPoll = true;
	}
	public void startPoll() {
		log.debug("false");
		statusPoll = false;
	}
    
    public void limpar() {

		acao = new Acao();
		acao.setTenant_id(loginBean.getTenantId());
		acao.setPessoas(new ArrayList<Pessoa>());
		acao.setUnidade(loginBean.getUsuario().getUnidade());
		acao.setTecnico(loginBean.getUsuario());
	}	
}
