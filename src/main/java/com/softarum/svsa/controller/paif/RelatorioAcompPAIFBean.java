package com.softarum.svsa.controller.paif;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Ano;
import com.softarum.svsa.modelo.enums.Mes;
import com.softarum.svsa.modelo.to.AcompanhamentoDTO;
import com.softarum.svsa.modelo.to.DatasIniFimTO;
import com.softarum.svsa.service.CapaProntuarioService;
import com.softarum.svsa.service.PlanoAcompanhamentoService;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.util.DateUtils;

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
public class RelatorioAcompPAIFBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private List<AcompanhamentoDTO> listaDTO = new ArrayList<>();
	private int qdeTotal = 0;
	private List<Unidade> unidades = new ArrayList<>();	
	private Unidade unidade;
	private Long codigoPessoa;
	private ListaAtendimento ultimoAtendimento;
	
	private List<String> anos;
	private Integer ano;
	private List<Mes> meses;	
	private Mes mes;
	private DatasIniFimTO datasTO;
	
	
	@Inject
	private CapaProntuarioService prontuarioService;
	@Inject
	private PlanoAcompanhamentoService planoService;
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private LoginBean loginBean;
		
	
	@PostConstruct
	public void inicializar() {	
		
		LocalDate data = LocalDate.now();
		setAno(data.getYear());	
		
		anos = new ArrayList<>(Arrays.asList(Ano.getAnos()));		
		meses = Arrays.asList(Mes.values());
		
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		setUnidade(loginBean.getUsuario().getUnidade());
		consultarNovosCasos();
	}
	
	public void consultarFamilias() {
		
		listaDTO = planoService.buscarFamiliasAcompanhamentoDTO(getUnidade(), loginBean.getTenantId());		
		this.setQdeTotal(listaDTO.size());
		log.info("Qde total de familias em acompanhamento: " + getQdeTotal() + " na unidade: " + getUnidade().getNome());	
	}
	
	public void consultarNovosCasos() {
		
		datasTO = DateUtils.getDatasIniFim(getAno(), getMes());
		
		listaDTO = planoService.buscarFamiliasAcompanhamentoDTO(getUnidade(), datasTO, loginBean.getTenantId());		
		this.setQdeTotal(listaDTO.size());
		log.debug("Qde total de novos casos: " + getQdeTotal() + " na unidade: " + getUnidade().getNome());	
	}
	
	public void setCodigoPessoa(Long codigoPessoa) {
		log.info("codigoPessoa=" + codigoPessoa);
		this.codigoPessoa = codigoPessoa;
		setUltimoAtendimento(prontuarioService.ultimoAtendimento(codigoPessoa, loginBean.getTenantId()));		
	}
	
}