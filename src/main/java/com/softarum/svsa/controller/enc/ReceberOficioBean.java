package com.softarum.svsa.controller.enc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.Oficio;
import com.softarum.svsa.modelo.OficioEmitido;
import com.softarum.svsa.modelo.Orgao;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;
import com.softarum.svsa.modelo.enums.TipoUnidade;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.OficioService;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.UnidadeService;
import com.softarum.svsa.service.UsuarioService;
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
@Named
@ViewScoped
public class ReceberOficioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Oficio> listaOficios = new ArrayList<>();
	private List<Unidade> unidades = new ArrayList<>();
	private Oficio oficio;
	private OficioEmitido oficioEmitido;
	private List<Orgao> orgaos;
	private Orgao orgao;
	private Boolean todos = false;
	
	private List<Usuario> tecnicos;
	private List<CodigoEncaminhamento> codigoEncaminhamento;
	
	private Usuario usuarioLogado;
	private Unidade unidade;
	private String unidadeEncaminhada;
	
	private Part file;
	
	@Inject
	private PessoaService pessoaService;
	@Inject
	private UnidadeService unidadeService;
	@Inject
	private OficioService oficioService;
	@Inject 
	private UsuarioService usuarioService;
	@Inject
	private AmazonS3Service s3;
	@Inject
	private LoginBean loginBean;
	
	
	@PostConstruct
	public void inicializar() {
		
		usuarioLogado = loginBean.getUsuario();
		unidade = usuarioLogado.getUnidade();
		unidades = unidadeService.buscarTodos(loginBean.getTenantId());
		this.codigoEncaminhamento = Arrays.asList(CodigoEncaminhamento.values());
		
		carregarOficios();
		
		tecnicos = usuarioService.buscarTecnicos(unidade, loginBean.getTenantId());
		
		limpar();
	}
	
	
	public void carregarOficios() {
		
		if(usuarioLogado.getUnidade().getTipo() == TipoUnidade.SASC) {
			listaOficios = oficioService.buscarTodosOficiosSasc(loginBean.getTenantId());
		}
		else {
			listaOficios = oficioService.buscarTodosOficiosRecebidos(unidade, todos, loginBean.getTenantId());
		}
	}
	
	public void salvar() throws IOException {
	
		try {										
			log.info("Salvando recebimento de oficio...");
			
			//Verifica se foi recebido pela SASC e ativa FLAG
			if(unidade.getTipo() == TipoUnidade.SASC ) {
				oficio.setSasc(true);				
			}
			else {
				oficio.setCoordenador(usuarioLogado);
				oficio.setUnidade(usuarioLogado.getUnidade());
			}
									
			//Verifica se existe ao menos um orgão
			if(oficio.getNomeOrgao() != "" && oficio.getNomeOrgao() != null) {
				
				if(oficio.getUnidade() == null) {
					throw new NegocioException("A unidade é obrigatória!");
				}
				else {					
					oficio.setTenant_id(loginBean.getTenantId());
					oficio = this.oficioService.salvar(oficio);
					
					//Verifica se foi carregado algum arquivo
					if(getFile() != null) {
						processFileUpload();
						oficio = this.oficioService.salvar(oficio);
						// grava a chave de acesso ao arquivo no s3
					}
				}
				
				
				MessageUtil.sucesso("ofício recebido com sucesso.");
			}
			else {			
				throw new NegocioException("Selecione ou insira um orgão!");
			}
			carregarOficios();
			
			limpar();
						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}	
	
	public void salvarResposta(){
		
		try {										
			log.info("Salvando resposta de oficio recebido...");	
			
			completarOficios();
			OficioEmitido emitido = oficioService.salvarResposta(oficio, oficioEmitido);

			MessageUtil.sucesso("Operação realizada com sucesso! Número do ofício emitido: " + emitido.getNrOficioEmitido());
			
			carregarOficios();
			
			limpar();
						
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	private void completarOficios() {
		log.info("Criando resposta de oficio (oficioEmitido)...");
		oficioEmitido.setCodigoEncaminhamento(oficio.getCodigoEncaminhamento());
		oficioEmitido.setEndereco(oficio.getEndereco());
		if(oficio.getPessoa() != null) {
			oficioEmitido.setPessoa(oficio.getPessoa());
			oficioEmitido.setNome(oficio.getPessoa().getNome());
		}		
		oficioEmitido.setNomeOrgao(oficio.getNomeOrgao());		
		oficioEmitido.setTecnico(usuarioLogado);
		oficioEmitido.setTenant_id(loginBean.getTenantId());
		oficioEmitido.setUnidade(unidade);
		
		oficio.setDataResposta(new Date());
		oficio.setTecnico(usuarioLogado);
		oficio.setAssunto(oficioEmitido.getAssunto());
	}
	
	public void excluir() {
		try {			
			
				oficioService.excluir(oficio);
				//log.info("oficio selecionado: " + item.getPessoa().getNome());
				
				this.listaOficios.remove(oficio);
				MessageUtil.sucesso("Oficio excluído com sucesso.");
				
				
				limpar();

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void limpar() {
		
		oficio = new Oficio();
		oficio.setTenant_id(loginBean.getTenantId());
		oficioEmitido = new OficioEmitido();
		oficioEmitido.setTenant_id(loginBean.getTenantId());
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
        
        /* se usuario for de SASC chama selecionaPessoaGeral */
        if(usuarioLogado.getUnidade().getTipo() == TipoUnidade.SASC) {
        	PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPessoaGeral", options, null);
        }
        else {
        	PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPessoa", options, null);
        }      	
    }	
	public void selecionarPessoa(SelectEvent<?> event) {		
		
		oficio = new Oficio();
		
		PessoaDTO dto = (PessoaDTO) event.getObject();		
		Pessoa p = pessoaService.buscarPeloCodigo(dto.getCodigo());
		oficio.setPessoa(p);	
				
		oficio.setUnidade(oficio.getPessoa().getFamilia().getProntuario().getUnidade());
		unidadeEncaminhada = oficio.getPessoa().getFamilia().getProntuario().getUnidade().getNome();
		log.debug("Pessoa selecionada: " + oficio.getPessoa().getNome() + " - " + unidadeEncaminhada);
		MessageUtil.sucesso("Pessoa Selecionada: " + oficio.getPessoa().getNome());			
	}
	
	public void carregarOrgaos() {
		this.orgaos = oficioService.buscarCodigosEncaminhamento(oficio.getCodigoEncaminhamento(), loginBean.getTenantId());
	}
	
	public void selecionarOrgao() {

		if (orgao != null) {
			oficio.setNomeOrgao(orgao.getNome());
			oficio.setEndereco(orgao.getEndereco().toString());
		}else {
			oficio.setNomeOrgao("");
			oficio.setEndereco("");
		}
		log.debug("nome: " + oficio.getNomeOrgao());
		log.debug("endereco: " + oficio.getEndereco());
	}
	
    
    /*
     * Faz upload do arquivo no bucket do Amazon S3 
     */
    private void processFileUpload() throws IOException, NegocioException{    	   
    	
    	try {
    		
    		oficio = s3.gravaAnexoOficio(oficio, getFile());
    		
        } catch (NegocioException e) {  
        	e.printStackTrace();
            MessageUtil.erro(e.getMessage());
        }

	}
	public Part getFile() {
		log.info("getFile()" + file);
        return file;
    } 
    public void setFile(Part file) {
    	log.info("setFile()" + file);
        this.file = file;
    }
    
    
    
    /*
     * Faz download do arquivo no bucket do Amazon S3 
     * 
     * .xhtml
     * 
		    <p:commandButton title="Download Oficio Pdf" icon="pi pi-file-o"  
		    	rendered="#{oficio.s3Key != null}"	
		    	ajax="false"
		    	process="@this"
		    	immediate="true"
		    	action="#{receberOficioBean.processFileDownload(oficio)}">		            	
			</p:commandButton>	       
     * 
     */
    public void processFileDownload(Oficio oficio) throws IOException, NegocioException{		
    	    	
    	try {  
    		
    		// S3
			InputStream pdfInputStream = s3.leAnexoOficio(oficio);
    		
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

			response.reset(); // Algum filtro pode ter configurado alguns cabeçalhos no buffer de antemão.								
			response.setHeader("Content-Type", "application/pdf"); // Define apenas o tipo de conteúdo, Utilize se
																	// necessário ServletContext#getMimeType() para
																	// detecção automática com base em nome de arquivo.
			OutputStream responseOutputStream = response.getOutputStream();			

			// Lê o conteúdo do PDF e grava para saída
			byte[] bytesBuffer = new byte[2048];
			int bytesRead;
			while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
				responseOutputStream.write(bytesBuffer, 0, bytesRead);
			}
			responseOutputStream.flush();

			// Fecha os streams
			pdfInputStream.close();
			responseOutputStream.close();
			facesContext.responseComplete();

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
	    } catch (Exception e) {
	    	e.printStackTrace();
			MessageUtil.erro("Problema na leitura do arquivo anexado.");
		}
	}
    
    public void redirectPdf(Oficio of) throws IOException {
    	log.info("redirectPdf" + of);
    	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(of.getUrlAnexo());
    }
    
	public boolean isSascSelecionado() {
		return usuarioLogado.getUnidade().getTipo() == TipoUnidade.SASC ;
	}
	
	public boolean isOficioSelecionado() {
        return oficio != null && oficio.getCodigo() != null;
    }	
	
}