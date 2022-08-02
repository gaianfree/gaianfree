package com.softarum.svsa.controller.pront;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
//import javax.faces.bean.ViewScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PersistenceException;
import javax.servlet.http.Part;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.dao.lazy.LazyProntuario;
import com.softarum.svsa.modelo.HistoricoTransferencia;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.service.CapaProntuarioService;
import com.softarum.svsa.service.HistoricoTransferenciaService;
import com.softarum.svsa.service.s3.AmazonS3Service;
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
@Named(value="pesquisaProntuarioBean")
@ViewScoped   
public class PesquisaCapaProntuarioBean implements Serializable {

private static final long serialVersionUID = 1L;
	
	private List<Prontuario> prontuarios = new ArrayList<>();
	private List<HistoricoTransferencia> listaTransferencias = new ArrayList<>();
	private Prontuario prontuarioSelecionado;
	private LazyProntuario lazyProntuarios;	
	private Part file;
	private ListaAtendimento ultimoAtendimento;
	
	@Inject
	private CapaProntuarioService prontuarioService;
	@Inject
	HistoricoTransferenciaService transferenciaService;
	@Inject
	private LoginBean loginBean;
	@Inject
	private AmazonS3Service s3;

	@PostConstruct
	public void inicializar() {
		lazyProntuarios = new LazyProntuario(prontuarioService, loginBean.getUsuario().getUnidade(), loginBean.getTenantId());
		//prontuarios = prontuarioService.buscarTodos(loginBean.getUsuario().getUnidade());
		
	}
	
	public void excluir() {
		try {
			prontuarioService.excluir(prontuarioSelecionado);
			log.info("prontuarioSelecionado: " + prontuarioSelecionado.getProntuario());
			log.info("lista: " + prontuarios.size());
			this.prontuarios.remove(prontuarioSelecionado);
			MessageUtil.sucesso("Prontuario " + prontuarioSelecionado.getCodigo() + " excluído com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());		
		} catch (PersistenceException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void ativar() {
		try {
			prontuarioService.ativar(prontuarioSelecionado);
			log.info("prontuarioSelecionado para ativar: " + prontuarioSelecionado.getCodigo());
			MessageUtil.sucesso("Prontuario " + prontuarioSelecionado.getCodigo() + " ativado com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());		
		} catch (PersistenceException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	public void inativar() {
		try {
			prontuarioService.inativar(prontuarioSelecionado);
			log.info("prontuarioSelecionado para inativar: " + prontuarioSelecionado.getCodigo());
			MessageUtil.sucesso("Prontuario " + prontuarioSelecionado.getCodigo() + " inativado com sucesso.");
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());		
		} catch (PersistenceException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void consultaTransferencias(Prontuario p) {		
		log.info("prontuario hist transf: " + p.getCodigo());
		setListaTransferencias(transferenciaService.buscarTodos(p, loginBean.getTenantId()));
	}
	
	public boolean isCoordenador() {
		if(loginBean.getUsuario().getGrupo() == Grupo.COORDENADORES)
			return true;
		
		return false;
	}

	public void setProntuarioSelecionado(Prontuario prontuarioSelecionado) {
		this.prontuarioSelecionado = prontuarioSelecionado;
		setUltimoAtendimento(prontuarioService.ultimoAtendimento(prontuarioSelecionado, loginBean.getTenantId()));		
	}
	
	/*
	 * upload file PDF
	 */
 
    public String gravarPdf() throws IOException{
    	
    	try {
	    	//Verifica se foi carregado algum arquivo
			if(getFile() != null) {
				prontuarioSelecionado = s3.gravaPdfCadUnico(prontuarioSelecionado, getFile());
				// grava a chave de acesso ao arquivo no s3
				prontuarioSelecionado = this.prontuarioService.salvarComPdf(prontuarioSelecionado);
			}
			MessageUtil.sucesso("Pdf CadÚnico gravado com sucesso.");
			
    	} catch (NegocioException e) {  
    		e.printStackTrace();
            MessageUtil.erro(e.getMessage());
	    } catch (Exception e) { 
	    	e.printStackTrace();
	        MessageUtil.erro(e.getMessage());
	    }
    	return "";
    }
    
    public void redirectPdf(Prontuario prontuario) throws IOException {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(prontuario.getUrlAnexo());
    }

}