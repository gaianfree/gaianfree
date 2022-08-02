package com.softarum.svsa.controller.pront;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.controller.pront.fam.BeneficioEventualBean;
import com.softarum.svsa.controller.pront.fam.CondicaoHabitacionalBean;
import com.softarum.svsa.controller.pront.fam.ConvivenciaFamiliarBean;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.util.MessageUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * @author murakamiadmin
 *
 */
@Getter
@Setter
@Named(value="cfBean")
@ViewScoped
public class CondicaoFamiliarBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	//private LogUtil logUtil = new LogUtil(ManterProntuarioBean.class);
	
	private PessoaReferencia pessoaReferencia;
	private List<PessoaReferencia> listaPessoasReferencia = new ArrayList<>();
	private Usuario usuarioLogado;
	
	@Inject
	private PessoaService pessoaService;
	@Inject
	private LoginBean sessaoBean;
	@Inject
	private BeneficioEventualBean beneficioBean;	
	@Inject
	private CondicaoHabitacionalBean habitacionalBean;
	@Inject
	private ConvivenciaFamiliarBean convivenciaBean;
	
	
	
	@PostConstruct
	public void inicializar() {		
		
		setUsuarioLogado(sessaoBean.getUsuario());		
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
        PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPessoaReferencia", options, null);
        	
    }
	
	
	public void selecionarPessoaReferencia(SelectEvent<?> event) {
		
		PessoaDTO dto = (PessoaDTO) event.getObject();		
		this.pessoaReferencia = pessoaService.buscarPFPeloCodigo(dto.getCodigo());
		
		/* dados da familia */
		beneficioBean.setPessoaReferencia(this.pessoaReferencia);
		habitacionalBean.setPessoaReferencia(this.pessoaReferencia);
		convivenciaBean.setPessoaReferencia(this.pessoaReferencia);		
		
		MessageUtil.sucesso("Pessoa Referencia Selecionada: " + this.pessoaReferencia.getNome());			
	}	

	public boolean isPessoaReferenciaSelecionada() {
        return pessoaReferencia != null && pessoaReferencia.getCodigo() != null;
    }
	
}