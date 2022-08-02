package com.softarum.svsa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Inject;

import com.softarum.svsa.modelo.AgendamentoFamiliar;
import com.softarum.svsa.modelo.IndProtecao;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.SituacaoProtecao;
import com.softarum.svsa.service.IndicadoresService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * Classe para criar ordenação de colunas no DataTable das páginas
 * 
 * @author murak
 *
 */
@Log4j
@Getter
@Setter

public  class IndicadoresHelper implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private List<SituacaoProtecao> situacoes = new ArrayList<>();
	private List<IndProtecao> selectedIndicadores = new ArrayList<>();	
	private List<SelectItem> protecoes = new ArrayList<>();
	
	@Inject
	IndicadoresService indicadoresService;
	
	
	/* indicadores de proteção - inicio
	 * Atendimento Individual e Atendimento sem Agendamento
	 */
	
	public void carregarIndicadores(ListaAtendimento item, Long tenantId) {
		
		carregarSelectedIndicadores(item, tenantId);
		
		situacoes = indicadoresService.buscarSituacoes();
		log.info("situacoes carregadas: " + situacoes.size());
		
		protecoes = new ArrayList<>();
		
		SelectItemGroup grupo;
		
		for(SituacaoProtecao sp : situacoes) {
			grupo = new SelectItemGroup(sp.getDescricao());
			int tamanho = sp.getIndicadores().size();
			SelectItem[] itens = new SelectItem[tamanho];

			int i = 0;
			for(IndProtecao ind : sp.getIndicadores()) {
				itens[i] = new SelectItem(ind, ind.getDescricao());
				i++;
			}
			grupo.setSelectItems(itens);
			protecoes.add(grupo);
		}
	}
	
	public void carregarSelectedIndicadores(ListaAtendimento item, Long tenantId) {
		
		selectedIndicadores = indicadoresService.buscarIndicadoresProtecao(item.getPessoa().getFamilia(), tenantId);
		log.info("situacoes carregadas: " + situacoes.size());		
		
	}
	
	public void gravarIndicadores(ListaAtendimento item) throws Exception {
		
		log.info("Indicadores gravados para a familia de prontuario numero:  " + item.getPessoa().getFamilia().getProntuario().getCodigo());
		
		try {
			indicadoresService.salvar(selectedIndicadores, item.getPessoa().getFamilia(), item.getTenant_id());
		}
		catch(NegocioException ne) {
			ne.printStackTrace();
			throw ne;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new NegocioException("Problema na gravação dos indicadores de proteção.");
		}
		
		for(IndProtecao i : selectedIndicadores) {
			
			log.info(i.getCodigo());
		}
		
		MessageUtil.info("Indicadores gravados com sucesso!");
	}
	
	/* indicadores de proteção - fim*/
    
    
    /* indicadores de proteção - inicio
	 * Atendimento Familiar
	 */
	
	public void carregarIndicadores(AgendamentoFamiliar item, Long tenantId) {
		
		carregarSelectedIndicadores(item, tenantId);
		
		situacoes = indicadoresService.buscarSituacoes();
		log.info("situacoes carregadas: " + situacoes.size());
		
		protecoes = new ArrayList<>();
		
		SelectItemGroup grupo;
		
		for(SituacaoProtecao sp : situacoes) {
			grupo = new SelectItemGroup(sp.getDescricao());
			int tamanho = sp.getIndicadores().size();
			SelectItem[] itens = new SelectItem[tamanho];

			int i = 0;
			for(IndProtecao ind : sp.getIndicadores()) {
				itens[i] = new SelectItem(ind, ind.getDescricao());
				i++;
			}
			grupo.setSelectItems(itens);
			protecoes.add(grupo);
		}
	}
	
	public void carregarSelectedIndicadores(AgendamentoFamiliar item, Long tenantId) {
		
		selectedIndicadores = indicadoresService.buscarIndicadoresProtecao(item.getPessoaReferencia().getFamilia(), tenantId);
		log.info("situacoes carregadas: " + situacoes.size());		
		
	}
	
	public void gravarIndicadores(AgendamentoFamiliar item) throws Exception {
		
		log.info("Indicadores gravados para a familia de prontuario numero:  " + item.getPessoaReferencia().getFamilia().getProntuario().getCodigo());
		
		try {
			indicadoresService.salvar(selectedIndicadores, item.getPessoaReferencia().getFamilia(), item.getTenant_id());
		}
		catch(NegocioException ne) {
			ne.printStackTrace();
			throw ne;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new NegocioException("Problema na gravação dos indicadores de proteção.");
		}
		
		for(IndProtecao i : selectedIndicadores) {
			
			log.info(i.getCodigo());
		}
		
		MessageUtil.info("Indicadores gravados com sucesso!");
	}
	
	/* indicadores de proteção - fim*/
}
