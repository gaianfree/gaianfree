package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.softarum.svsa.modelo.AgendamentoColetivo;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.AgendamentoColetivoService;
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
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class AgendamentoColetivoBean implements Serializable {

private static final long serialVersionUID = 1L;
	
	private List<AgendamentoColetivo> listaAtendimentos = new ArrayList<>();
	private AgendamentoColetivo item;
	private Pessoa pessoa;
		
	private List<Role> roles;	
	private List<Usuario> tecnicos;
	
	// para preenchimento dos lists dinamicamente
	private Role role;
	private Usuario roleTecnico;
	private String strRole;
	
	private Unidade unidade;			
	
	@Inject
	private PessoaService pessoaService;
	@Inject
	private AgendamentoColetivoService agColetivoService;
	@Inject 
	private UsuarioService usuarioService;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	public void inicializar() {
		
		unidade = loginBean.getUsuario().getUnidade();		
		
		buscarAtendimentoColetivo(unidade);
		log.debug("ATENDIMENTOS AGENDADOS = " + getListaAtendimentos().size());
		
		this.roles = Arrays.asList(Role.values());	
		limpar();
	}
	
	
	public void salvarAgendamento() {
		try {										
			item.setTenant_id(loginBean.getTenantId());
			item.setAgendador(loginBean.getUsuario());
			item.setUnidade(unidade);
			if(item.getTecnico() != null)
				item.setRole(item.getTecnico().getRole());
			
			if(item.getPessoas().size() > 0) {
				
				this.agColetivoService.salvarAgendamento(item, loginBean.getTenantId());
				MessageUtil.sucesso("Agendamento realizado com sucesso.");
				
				buscarAtendimentoColetivo(unidade);	
				
				limpar();
			}
			else {
				MessageUtil.erro("O agendamento deve ter pelo menos uma pessoa.");
			}			
						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}	
	
	
	private void buscarAtendimentoColetivo(Unidade unidade) {
		listaAtendimentos = agColetivoService.buscarAtendimentosAgendados(unidade, loginBean.getTenantId());
		
	}
	

	public void excluir() {
		try {			
			
			agColetivoService.excluir(item);
			
			this.listaAtendimentos.remove(item);
			MessageUtil.sucesso("Agendamento excluído com sucesso.");
			
			limpar();
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	
	
	public void carregarTecnicos() {

		this.tecnicos = usuarioService.buscarTecnicosRole(item.getRole(), unidade, loginBean.getTenantId());	
	}
	
	public void verificaDispTecnico() throws NegocioException {
		try {
			// verifica se técnico está agendado
			agColetivoService.verificarDisponibilidade(unidade, item, loginBean.getTenantId());	
		}
		catch (NegocioException e) {
			MessageUtil.alerta(e.getMessage());
		}			
	}
	
	
	public void limpar() {

		item = new AgendamentoColetivo();
		item.setTenant_id(loginBean.getTenantId());
		item.setUnidade(unidade);
		item.setPessoas(new ArrayList<Pessoa>());
		item.setPessoasFaltosas(new HashSet<Pessoa>());
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
        PrimeFaces.current().dialog().openDynamic("SelecionaPessoa", options, null);        	
    }	
	
	public boolean isItemSelecionado() {
        return item != null && item.getCodigo() != null;
    }	
	
	public void selecionarPessoa(SelectEvent<?> event) {            

        PessoaDTO dto = (PessoaDTO) event.getObject();		
		Pessoa p = pessoaService.buscarPeloCodigo(dto.getCodigo());
        
        if(!this.item.getPessoas().contains(p)) {
            if(!this.item.getPessoasFaltosas().contains(p)) {            
                this.item.getPessoas().add(p);
                MessageUtil.sucesso("Pessoa adicionada: " + p.getNome());
            }
            else {
                MessageUtil.erro("Essa pessoa está com falta! -> " + p.getNome());    
            }
        }
        else {
            MessageUtil.erro("Pessoa já adicionada! -> " + p.getNome());    
        }    

    }
	
	public void excluirPessoa(Pessoa p) {			

		if(this.item.getPessoas().size() > 1) {
			this.item.getPessoas().remove(p);
			log.debug("Pessoa removida" + p.getNome());		
			MessageUtil.sucesso("Pessoa excluída do atendimento: " + p.getNome());	
		}
		else {	
			MessageUtil.erro("O agendamento deve ter pelo menos uma pessoa.");
		}
		
	}	
	
	public void marcarFalta(Pessoa p) {
		
		if(this.item.getPessoas().size() > 1) {
			this.item.getPessoasFaltosas().add(p);
			log.info("Lista: " + item.getPessoasFaltosas());
			this.item.getPessoas().remove(p);
			
			MessageUtil.sucesso("Falta marcada: " + p.getNome());
		}
		else {	
			MessageUtil.erro("O agendamento deve ter pelo menos uma pessoa.");
		}
		
	}
	public void reverterFalta(Pessoa p) {
		
		this.item.getPessoas().add(p);
		this.item.getPessoasFaltosas().remove(p);		
		
		MessageUtil.sucesso("Falta revertida: " + p.getNome());
		
	}
}