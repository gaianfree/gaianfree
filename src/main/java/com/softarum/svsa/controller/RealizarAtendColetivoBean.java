package com.softarum.svsa.controller;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import com.softarum.svsa.modelo.AgendamentoColetivo;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.EnumUtil;
import com.softarum.svsa.service.AgendamentoColetivoService;
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
public class RealizarAtendColetivoBean implements Serializable {

private static final long serialVersionUID = 1L;
	
	private List<AgendamentoColetivo> listaAtendimentos = new ArrayList<>();
	private List<AgendamentoColetivo> resumoAtendimentos = new ArrayList<>();
	private AgendamentoColetivo item;
	private List<CodigoAuxiliarAtendimento> codigosAuxiliares;	
	private DualListModel<Usuario> tecnicos;
	private Usuario usuarioLogado;
	
	private boolean statusPoll = true;
	
	@Inject
	AgendamentoColetivoService agendamentoColetivoService;
	@Inject 
	UsuarioService usuarioService;
	@Inject
	LoginBean loginBean;

	@PostConstruct
	public void inicializar() {	
		
		usuarioLogado = loginBean.getUsuario();		
		log.info("1. Usuario logado: " + usuarioLogado.getNome());		
		if(usuarioLogado == null) {
			usuarioLogado = loginBean.getUsuario();
			log.info("2. Usuario logado: " + usuarioLogado.getNome());
		}
		
		carregarCodAux();
		
		carregarTecnicos();
					
		buscarAtendimentosColetivos();
		
		limpar();	
	}
	
	private void carregarCodAux() {
		
		this.codigosAuxiliares = EnumUtil.getCodigosAtendColetivo();
	}
	
	private void carregarTecnicos() {		
		
		List<Usuario>tecsSource = new ArrayList<Usuario>();
		tecsSource = usuarioService.buscarTecnicos(usuarioLogado.getUnidade(), loginBean.getTenantId());
		tecsSource.remove(usuarioLogado);
		List<Usuario> tecsTarget = new ArrayList<Usuario>();
       
        tecnicos = new DualListModel<Usuario>(tecsSource, tecsTarget);
	}
	
	public void onTransfer(TransferEvent event) {

        for(Object tecnico : event.getItems()) {
        	MessageUtil.sucesso("Técnico " + ((Usuario) tecnico).getNome() + " selecionado.");
        }         
    }
	
	
	public void salvarAtendimento() {
		try {		
			
			item.setTecnico(usuarioLogado);
			item.setTecnicos(tecnicos.getTarget());
			item.setTenant_id(loginBean.getTenantId());
			
			this.agendamentoColetivoService.salvarAtendColetivo(item);
			this.listaAtendimentos.remove(item);
			limpar();
			MessageUtil.sucesso("Atendimento coletivo gravado com sucesso!");
								
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void autoSave() {
		try {
			
			Instant time = Instant.now();
			log.info("auto save atendimento coletivo... " + time);
			
			item.setTecnico(usuarioLogado);			
			item.setTecnicos(tecnicos.getTarget());
			item.setTenant_id(loginBean.getTenantId());
			
			log.info("ANTES...auto save atendimento coletivo codigo = " + item.getCodigo());			
			this.agendamentoColetivoService.autoSave(item);
			log.info("DEPOIS...auto save atendimento coletivo codigo = " + item.getCodigo());
			
			MessageUtil.sucesso("Auto save executado.");
								
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public String agendamento() {
		return "/restricted/agenda/AgendamentoColetivo.xhtml";
	}
	
	public void buscarAtendimentosColetivos() {
		
		this.listaAtendimentos =  agendamentoColetivoService.buscarAtendimentosAgendados(usuarioLogado.getUnidade(), loginBean.getTenantId());		
	}	
	
	public void limpar() {
		item = new AgendamentoColetivo();
		item.setTenant_id(loginBean.getTenantId());
	}
	
	public boolean isItemSelecionado() {
		
        return item != null && item.getCodigo() != null;
    }
	
	public void stopPoll() {
		log.info("true");
		statusPoll = true;
	}
	public void startPoll() {
		log.info("false");
		statusPoll = false;
	}
	
	public void atualizarDataTable() {
		buscarAtendimentosColetivos();
	}
	
}