package com.softarum.svsa.controller.rel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.softarum.svsa.controller.LoginBean;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.modelo.PessoaReferencia;
import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.to.PessoaDTO;
import com.softarum.svsa.service.AgendamentoIndividualService;
import com.softarum.svsa.service.MPComposicaoService;
import com.softarum.svsa.service.PessoaService;
import com.softarum.svsa.util.MessageUtil;
import com.softarum.svsa.util.WordPOIGenerator;

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
public class RelatorioAtendimentoFamiliaBean implements Serializable {

	private static final long serialVersionUID = 1769116747361287180L;
	
	private Long qdeAtendimentos = 0L;
	private List<ListaAtendimento> listaAtendimentos = new ArrayList<>();
	private Unidade unidade;
	private PessoaReferencia pessoaReferencia;
	private String prontuario;

	@Inject
	private PessoaService pessoaService;
	@Inject
	AgendamentoIndividualService listaAtendimentoService;
	@Inject
	MPComposicaoService composicaoService;
	@Inject
	LoginBean sessaoBean;
	
	
	@PostConstruct
	public void inicializar() {	

		unidade = sessaoBean.getUsuario().getUnidade();
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
		
		setListaAtendimentos(listaAtendimentoService.relatorioAtendimentoFamilia(unidade, pessoaReferencia.getFamilia().getProntuario(), sessaoBean.getTenantId()));
		setProntuario("Prontuário:  " + pessoaReferencia.getFamilia().getProntuario().getCodigo() + " - " + pessoaReferencia.getNome());	
		
		MessageUtil.sucesso("Pessoa Referencia Selecionada: " + this.pessoaReferencia.getNome());			
	}
	
	public boolean isPessoaReferenciaSelecionada() {

    	if(getPessoaReferencia() != null && getPessoaReferencia().getCodigo() != null) {    		
    		return true;
    	}
        return false;        		
    }	
	
	public boolean isUnidadeSelecionada() {
		if(unidade != null)
			return true;
		return false;
	}
	
	/**
	 * Download file.
	 */
	
	public void downloadFile() throws IOException {	
		
		String nomeArquivo = criateWordDoc();
		
		String caminhoWebInf = WordPOIGenerator.getCaminhoWebInf();		
		
		try {			
			
			File file = new File(caminhoWebInf.concat(nomeArquivo));
			
			log.info(file.getName());
			
			InputStream fis = new FileInputStream(file);
			
			byte[] buf = new byte[1000000];
			int offset = 0;
			int numRead = 0;
			while ((offset < buf.length) && ((numRead = fis.read(buf, offset, buf.length - offset)) >= 0)) {
				offset += numRead;
			}
			
			fis.close();
			
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
	
			response.setContentType("application/word");
			response.setHeader("Content-Disposition", "attachment;filename=" + nomeArquivo);
			response.getOutputStream().write(buf);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			FacesContext.getCurrentInstance().responseComplete();
		}
		catch(FileNotFoundException fnf) {
			MessageUtil.erro("Não existe documento word.");
		}
		catch(Exception e) {
			MessageUtil.erro("Houve um problema na recuperação do Documento Word!");
		}		
	}
	
	private String criateWordDoc() {
		
		PessoaReferencia pf = getPessoaReferencia();
		List<ListaAtendimento> lista = getListaAtendimentos();
		
		Collections.sort(lista);
		
		WordPOIGenerator generator = new WordPOIGenerator("RelatorioAtendFamilia");		
		XWPFDocument document = generator.wordDocGenerate();
		
		generator.cabecalho(document, "Relatório de Atendimentos por Família");
		
		// corpo paragrafo
		generator.corpoParagrafo(document, "Prontuário: " + pf.getFamilia().getProntuario().getCodigo().toString());
		generator.corpoParagrafo(document, "Pessoa de Referência: " + pf.getNome());
		generator.corpoParagrafo(document, "Endereço: " + pf.getFamilia().getEndereco().getEndereco() + ", " +
				pf.getFamilia().getEndereco().getNumero());
		generator.corpoParagrafo(document, 
				pf.getFamilia().getEndereco().getBairro() + ", " +
				pf.getFamilia().getEndereco().getMunicipio() + "-" +
				pf.getFamilia().getEndereco().getUf());
		generator.corpoParagrafo(document, "Cep.: " + pf.getFamilia().getEndereco().getCep());		
		
		generator.corpoParagrafo(document, "------------------------------------------------------");
		generator.corpoParagrafo(document, " ");
		
		/*
		 * Corpo texto	 
		 */		
		ArrayList<String> linhas = new ArrayList<>();
		Long codigo = 0L;
		for(ListaAtendimento a : lista) {
			if(codigo != a.getPessoa().getCodigo()) {
				if(!linhas.isEmpty()) {
					generator.corpoTexto(document, linhas);
					linhas = new ArrayList<>();
				}
				generator.corpoParagrafo(document, "Codigo: " + a.getPessoa().getCodigo() + " - Nome: " + a.getPessoa().getNome());
				codigo = a.getPessoa().getCodigo();				
			}
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        String dataFormatada = dateFormat.format(a.getDataAtendimento());
	        
	        // Retira caracteres HTML
	        String resumo = StringEscapeUtils.unescapeHtml4(a.getResumoAtendimento()); 
	        resumo.replaceAll("\\<[^>]*>"," ");
	       
	        String linha = dataFormatada + " : " + resumo;
	        
	        linhas.add(linha);
		}				
		generator.corpoTexto(document, linhas);
		
		generator.rodape(document);		
		
		return generator.wordDocFinish(document);		// retorna o nome do arquivo a ser recuperado para download
	}
}