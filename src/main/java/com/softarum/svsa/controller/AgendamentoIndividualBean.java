package com.softarum.svsa.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.AgendamentoIndividualService;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.UsuarioService;
import com.softarum.svsa.util.DateUtils;
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
public class AgendamentoIndividualBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<ListaAtendimento> listaAtendimentos = new ArrayList<>();
	
	private ListaAtendimento item;
	private List<ListaAtendimento> listaFaltas = new ArrayList<>();
	private Date mesAno;
	
	private List<Role> roles;	
	private List<Usuario> tecnicos;
	
	// para preenchimento dos lists dinamicamente
	//private Role role;
	private Usuario roleTecnico;
	private String strRole;
	private String mse;
	
	private Unidade unidade;		
	
	@Inject
	private AgendamentoIndividualService listaAtendimentoService;
	@Inject
	private PessoaService pessoaService;
	

	@Inject 
	private UsuarioService usuarioService;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	public void inicializar() {		
		
		unidade = loginBean.getUsuario().getUnidade();
		
		// para carregar todos os agendamentos do mes/ano corrente
		LocalDate ld = LocalDate.now();
		LocalDateTime ldt = LocalDateTime.of(ld.getYear(), ld.getMonthValue(), 1, 0, 0, 0);
		mesAno = DateUtils.asDate(ldt);
		log.debug(mesAno);
		
		buscarListaAtendimento(unidade);
		
		this.roles = Arrays.asList(Role.values());	
		limpar();	
	}	
	
	
	public void salvar() {
		try {
			log.info("Salvando agendamento...");
			item.setUnidade(unidade);
			item.setAgendador(loginBean.getUsuario());
			if(item.getTecnico() != null)
				item.setRole(item.getTecnico().getRole());
			
			if(item.getPessoa() != null) {
				item.setTenant_id(loginBean.getTenantId());
				this.listaAtendimentoService.salvar(item, loginBean.getTenantId());
				MessageUtil.sucesso("Agendamento realizado com sucesso.");
				
				buscarListaAtendimento(unidade);	
				
				limpar();
			}
			else {
				MessageUtil.erro("Deve ser selecionada uma pessoa para o agendamento!");
			}
						
		} catch (NegocioException e) {
			log.error(e.getMessage());
			MessageUtil.erro(e.getMessage());
		}
	}	
	
	/*
	 * Verifica se é MSE e atualiza a lista recuperada
	 */
	private void buscarListaAtendimento(Unidade unidade) {
		
		log.debug(mesAno);
		listaAtendimentos = listaAtendimentoService.buscarAtendimentosAgendados(unidade, mesAno, loginBean.getTenantId());
		
		for(ListaAtendimento l : listaAtendimentos) {
			if(listaAtendimentoService.verificarMSE(l.getPessoa(), loginBean.getTenantId())) {
				l.getPessoa().setMse("MSE");
			}
		}		
	}	
	// filtro para consulta dos atendimentos paginado por mes/ano
	public void buscarMesAtend() {

		buscarListaAtendimento(unidade);
		log.info(mesAno);
	}	

	public void consultaFaltas(ListaAtendimento item) {
		
		log.info("pessoa consultada: " + item.getPessoa().getNome());
		setListaFaltas(listaAtendimentoService.consultaFaltas(item.getPessoa(), loginBean.getTenantId()));
		
	}

	public void excluir() {
		try {			
			
			if(item.getResumoAtendimento() != null && item.getDataAtendimento() != null) {
				MessageUtil.sucesso("Agendamento não pode ser excluído porque já existe registro de atendimento! É necessário encerrar o atendimento.");
			}
			else {
				listaAtendimentoService.excluir(item);
				//log.info("item selecionado: " + item.getPessoa().getNome());
				
				this.listaAtendimentos.remove(item);
				MessageUtil.sucesso("Agendamento " + item.getPessoa().getNome() + " excluída com sucesso.");
				
				limpar();
			}
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void excluirPorFalta() {
		try {
			
			item.setTecnico(loginBean.getUsuario());
			item.setResumoAtendimento("[Falta] " + item.getResumoAtendimento());
			listaAtendimentoService.atualizar(item); 
			log.info("ausente: " + item.getPessoa().getNome());
			
			this.listaAtendimentos.remove(item);
			MessageUtil.sucesso("Agendamento de " + item.getPessoa().getNome() + " excluído com sucesso.");
			
			limpar();
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void carregarTecnicos() {

		this.tecnicos = usuarioService.buscarTecnicosRole(item.getRole(), unidade, loginBean.getTenantId());
		
		log.debug("Tecnicos carregados role = " + item.getRole().name());
	}
	
	public void verificarDispTecnico() {
		
		try {
			log.info("verificar disponibilidade do tecnico");			
			listaAtendimentoService.verificarDisponibilidade(unidade, item, loginBean.getTenantId());			
		}
		catch (NegocioException e) {			
			MessageUtil.alerta(e.getMessage());
		}		
	}
	
	public void limpar() {

		item = new ListaAtendimento();
		item.setUnidade(unidade);
		item.setTenant_id(loginBean.getTenantId());
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
		
		this.item = new ListaAtendimento();
		item.setTenant_id(loginBean.getTenantId());
		
		PessoaDTO dto = (PessoaDTO) event.getObject();		
		Pessoa p = pessoaService.buscarPeloCodigo(dto.getCodigo());
		item.setPessoa(p);		
	
		log.info("Pessoa selecionada: " + this.item.getPessoa().getNome());
		
		MessageUtil.sucesso("Pessoa Selecionada: " + this.item.getPessoa().getNome());			
	}
	
	public List<ListaAtendimento> getListaAtendimentos() {
		buscarListaAtendimento(unidade);	
		return listaAtendimentos;
	}
}