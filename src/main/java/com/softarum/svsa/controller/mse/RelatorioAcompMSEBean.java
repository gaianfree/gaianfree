package com.softarum.svsa.controller.mse;

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
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.enums.Ano;
import com.softarum.svsa.modelo.enums.Mes;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.AcompMseDTO;
import com.softarum.svsa.modelo.to.DatasIniFimTO;
import com.softarum.svsa.service.PiaService;
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
public class RelatorioAcompMSEBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private List<AcompMseDTO> listaDTO = new ArrayList<>();
	private int qdeTotal = 0;
	private List<Unidade> unidades = new ArrayList<>();	
	private Unidade unidade;
	
	private List<String> anos;
	private Integer ano;
	private List<Mes> meses;	
	private Mes mes;
	private DatasIniFimTO datasTO;
	
	@Inject
	private PiaService piaService;
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
		
		unidades = unidadeService.buscarCREAS(loginBean.getTenantId());
		setUnidade(loginBean.getUsuario().getUnidade());
		
		consultarNovosCasos();
	}
	
	public void consultarAdolescentes() {
		
		listaDTO = piaService.buscarAdolAcompanhamentoDTO(getUnidade(), loginBean.getTenantId());		
		this.setQdeTotal(listaDTO.size());
		log.info("Qde total de adolescentes em acompanhamento: " + getQdeTotal() + " na unidade: " + getUnidade());	
	}
	
	public void consultarNovosCasos() {
		
		datasTO = DateUtils.getDatasIniFim(getAno(), getMes());
		
		listaDTO = piaService.buscarAdolAcompanhamentoDTO(getUnidade(), datasTO, loginBean.getTenantId());		
		this.setQdeTotal(listaDTO.size());
		log.info("Qde total de novos casos: " + getQdeTotal() + " na unidade: " + getUnidade().getNome());	
	}
	
	public boolean isCras() {
		if(loginBean.getUsuario().getUnidade().getTipo() != TipoUnidade.CREAS)
			return true;
		return false;
	}
	
}