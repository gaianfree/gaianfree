package com.softarum.svsa.controller.paif;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
//import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.MetaPlanoFamiliar;
import com.softarum.svsa.modelo.PlanoAcompanhamento;
import com.softarum.svsa.modelo.enums.ResultadoMeta;
import com.softarum.svsa.service.PlanoAcompanhamentoService;
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
public class MetaPAIFBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private PlanoAcompanhamento plano;	
	private MetaPlanoFamiliar meta;
	private List<MetaPlanoFamiliar> metas;
	private List<ResultadoMeta> resultados;
		
	
	@Inject
	private PlanoAcompanhamentoService planoService;
	@Inject
	private LoginBean loginBean;	
			
	@PostConstruct
	public void inicializar() {	
		this.setMeta(new MetaPlanoFamiliar());	
		this.getMeta().setTecnico(loginBean.getUsuario());
		resultados = Arrays.asList(ResultadoMeta.values());
				
	}	
	
	public void salvar() {
		
		try {		
			
			meta.setPlanoAcompanhamento(plano);
			this.planoService.salvar(meta);
			metas = planoService.buscarMetas(plano, loginBean.getTenantId());
			
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
			log.info("Meta selecionada: " + meta.getResumo());
			
			this.metas.remove(meta);
			MessageUtil.sucesso("Meta " + meta.getResumo() + " excluída com sucesso.");
			
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}		
	}
	
	public void limpar() {
		this.setMeta(new MetaPlanoFamiliar());
		this.getMeta().setTecnico(loginBean.getUsuario());
		this.getMeta().setTenant_id(loginBean.getTenantId());
	}
	
	
	public void buscarMetas() {
		log.info("metaBean buscarMetas do plano: " + plano.getCodigo());
		metas = planoService.buscarMetas(plano, loginBean.getTenantId());
	}
	
	public boolean isPlanoSelecionado() {
		if(getPlano() != null && getPlano().getCodigo() != null)
			return true;
		return false;
	}
	
}
