package com.softarum.svsa.controller.mse;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
//import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.MetaPia;
import com.softarum.svsa.modelo.Pia;
import com.softarum.svsa.modelo.enums.MedidaMse;
import com.softarum.svsa.modelo.enums.ResultadoMeta;
import com.softarum.svsa.service.PiaService;
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
public class MetaMSEBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Pia plano;	
	private MetaPia meta;
	private List<MetaPia> metas;
	private List<MedidaMse> medidas;
	private List<ResultadoMeta> resultados;
	
	@Inject
	private LoginBean loginBean;
	@Inject
	private PiaService planoService;

	
			
	@PostConstruct
	public void inicializar() {	
		
		log.info("Iniciando MetaMSEBean ...");
		MetaPia meta = new MetaPia();
		meta.setTenant_id(loginBean.getTenantId());
		this.setMeta( meta );			
		this.medidas = Arrays.asList(MedidaMse.values());
		resultados = Arrays.asList(ResultadoMeta.values());
		buscarMetas();
			
	}	
	
	public void salvar() {
		
		try {		
			
			meta.setPia(plano);
			meta.setTecnico(loginBean.getUsuario());
			meta = this.planoService.salvar(meta);
			buscarMetas();
			
			MessageUtil.sucesso("Meta gravada com sucesso.");			
		
			limpar();
						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}	
	}
	
	public void excluir() {
		try {			
			
			this.planoService.excluir(meta);
			log.info("Meta selecionada: " + meta.getMedidaMse().name());
			
			this.metas.remove(meta);
			MessageUtil.sucesso("Meta " + meta.getMedidaMse().name() + " excluída com sucesso.");
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}		
	}
	
	public void limpar() {
		
		MetaPia meta = new MetaPia();
		meta.setTenant_id(loginBean.getTenantId());
		this.setMeta( meta );
	}
	
	
	public void buscarMetas() {
		metas = planoService.buscarMetas(plano, loginBean.getTenantId());
	}	
	
	public boolean isPlanoSelecionado() {
		if(getPlano() != null && getPlano().getCodigo() != null)
			return true;
		return false;
	}
		
}
