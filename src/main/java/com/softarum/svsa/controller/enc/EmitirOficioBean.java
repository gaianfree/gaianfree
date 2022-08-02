package com.softarum.svsa.controller.enc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.OficioEmitido;
import com.softarum.svsa.modelo.Orgao;
import com.softarum.svsa.modelo.Pessoa;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.CodigoEncaminhamento;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.OficioEmitidoService;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.service.pdf.OficioPDFService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.NegocioException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author lauro
 *
 */
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class EmitirOficioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<OficioEmitido> listaOficiosEmitidos = new ArrayList<>();
	private OficioEmitido oficioEmitido;
	private List<Orgao> orgaos;
	private Orgao orgao;

	private List<Usuario> tecnicos;
	private List<CodigoEncaminhamento> codigosEncaminhamento;
	
	private Unidade unidade;
	private boolean pessoaSelecionada = false;
	
	@Inject
	private PessoaService pessoaService;
	@Inject
	private OficioEmitidoService oficioEmitidoService;
	@Inject
	private OficioPDFService pdfService;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	public void inicializar() {
		
		unidade = loginBean.getUsuario().getUnidade();
		
		this.codigosEncaminhamento = Arrays.asList(CodigoEncaminhamento.values());
		
		listaOficiosEmitidos = oficioEmitidoService.buscarOficiosEmitidos(unidade, loginBean.getTenantId());
		
		limpar();	
	}
	

	public void salvar() {
		try {										
			log.info("Salvando emissão de oficio...");
			
			oficioEmitido.setUnidade(unidade);
			oficioEmitido.setTecnico(loginBean.getUsuario());
					
			if(!isPessoaSelecionada()) {
				if(oficioEmitido.getNome() == "") {
					throw new NegocioException("Digite o nome da pessoa.");
				}				
			}			
			
			OficioEmitido oficio = this.oficioEmitidoService.salvar(oficioEmitido, loginBean.getTenantId());
			MessageUtil.sucesso("ofício " + oficio.getNrOficioEmitido() + " emitido com sucesso.");
			
			listaOficiosEmitidos = oficioEmitidoService.buscarOficiosEmitidos(unidade, loginBean.getTenantId());
			
			limpar();
						
		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.erro("Erro na gravação do Oficio!");
		}
	}	
	
	
	public void excluir() {
		try {			
			
				oficioEmitidoService.excluir(oficioEmitido);
				//log.info("oficio selecionado: " + item.getPessoa().getNome());
				
				this.listaOficiosEmitidos.remove(oficioEmitido);				
				MessageUtil.sucesso("Oficio excluído com sucesso.");
				
				limpar();

		} catch (NegocioException e) {
			e.printStackTrace();
			MessageUtil.erro(e.getMessage());
		}
	}
	
	public void limpar() {
		
		oficioEmitido = new OficioEmitido();
		oficioEmitido.setTenant_id(loginBean.getTenantId());
		pessoaSelecionada = false;
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
        PrimeFaces.current().dialog().openDynamic("/restricted/agenda/SelecionaPessoa", options, null);        	
    }	
	public void selecionarPessoa(SelectEvent<?> event) {		
		
		this.oficioEmitido = new OficioEmitido();
		
		PessoaDTO dto = (PessoaDTO) event.getObject();		
		Pessoa p = pessoaService.buscarPeloCodigo(dto.getCodigo());
		oficioEmitido.setPessoa(p);		
		
		pessoaSelecionada = true;
		
		log.info("Pessoa selecionada: " + this.oficioEmitido.getPessoa().getNome());
		
		MessageUtil.sucesso("Pessoa Selecionada: " + this.oficioEmitido.getPessoa().getNome());			
	}
	
	public void showPDF(OficioEmitido oficioEmitido) {

		try {
			/*
			if(oficioEmitido.getNome() == null || oficioEmitido.getNome() == "") {
				if (oficioEmitido.isNomeSocial()) {
					if( oficioEmitido.getPessoa().getNomeSocial() == null || oficioEmitido.getPessoa().getNomeSocial().equals("") ){
						oficioEmitido.setNomeSocial(false);
						throw new NegocioException("Pessoa não possui nome social. Altere o ofício ou o cadastro da pessoa.");
					}
				}
			}
			*/
			
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline=filename=file.pdf");

		
			
			// Emissão em nome de quem está imprimindo
			oficioEmitido.setTecnico(loginBean.getUsuario());
			// Creating a PdfWriter
			ByteArrayOutputStream baos = pdfService.generateStream(oficioEmitido, 
					loginBean.getUsuario().getTenant().getS3Key(),
					loginBean.getUsuario().getTenant().getSecretaria());

			// setting some response headers
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			// setting the content type
			response.setContentType("application/pdf");
			// the contentlength
			response.setContentLength(baos.size());
			// write ByteArrayOutputStream to the ServletOutputStream
			ServletOutputStream os = response.getOutputStream();

			baos.writeTo(os);
			os.flush();
			os.close();
			context.responseComplete();
		} catch (NegocioException ne) {
			ne.printStackTrace();
			MessageUtil.erro(ne.getMessage());
		}catch (IOException e) {
			e.printStackTrace();
			MessageUtil.erro("Problema na escrita do PDF.");
		} catch (Exception ex) {
			ex.printStackTrace();
			MessageUtil.erro("Problema na geração do PDF.");
		}
		
		log.info("PDF gerado!");
	}
	
	public void downloadPDF(OficioEmitido oficioEmitido){
		try {
			String nomeArquivo = "Oficio ".concat(oficioEmitido.getCodigo().toString().concat(".pdf"));
			// no contexto da aplicacao
			String caminhoWebInf = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/oficios/");
			/* cria o arquivo em pasta temporaria pra download*/
			
			/* cria o arquivo */
			pdfService.generatePDF(caminhoWebInf.concat(nomeArquivo), nomeArquivo, oficioEmitido, 
					loginBean.getUsuario().getTenant().getS3Key(),
					loginBean.getUsuario().getTenant().getSecretaria());	
			
			File file = new File(caminhoWebInf.concat(nomeArquivo));
			
			log.info(file.getAbsolutePath());
			
			FileInputStream fis = new FileInputStream(file);
			
			byte[] buf = new byte[1000000];
			int offset = 0;
			int numRead = 0;
			while ((offset < buf.length) && ((numRead = fis.read(buf, offset, buf.length - offset)) >= 0)) {
				offset += numRead;
			}
			
			fis.close();
			
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
	
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment;filename=" + nomeArquivo);
			response.getOutputStream().write(buf);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			FacesContext.getCurrentInstance().responseComplete();
		}
		catch(FileNotFoundException fnf) {
			MessageUtil.erro("Não existe PDF!");
		}
		catch(Exception e) {
			MessageUtil.erro("Houve um problema na recuperação do PDF !");
		}
	}

	public void carregarOrgaos() {
		this.orgaos = oficioEmitidoService.buscarCodigosEncaminhamento(oficioEmitido.getCodigoEncaminhamento(), loginBean.getTenantId());
	}
	
	public void selecionarOrgao() {

		if (orgao != null) {
			oficioEmitido.setNomeOrgao(orgao.getNome());
			oficioEmitido.setEndereco(orgao.getEndereco().toString());
		}else {
			oficioEmitido.setNomeOrgao("");
			oficioEmitido.setEndereco("");
		}
	}
	
	public boolean isOficioSelecionado() {
        return oficioEmitido != null && oficioEmitido.getCodigo() != null;
    }

	public List<OficioEmitido> getListaOficios() {
		return listaOficiosEmitidos;
	}
	
	public List<CodigoEncaminhamento> getcodigoEncaminhamento() {
		return codigosEncaminhamento;
	}
	
}