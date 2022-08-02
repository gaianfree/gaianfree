package com.softarum.svsa.service.pdf;

import java.io.Serializable;
import java.net.URL;

import org.apache.log4j.Logger;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 * 
 * Classe que utiliza o componente IText para gerar relatorios
 *
 */
public class ProntuarioPDFService implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(ProntuarioPDFService.class);

	
	
	/************************************************
	 * Capa de Prontuario
	 * 
	 ************************************************/	
	
	// para impressão da capa do prontuario
	public ByteArrayOutputStream generateStream(Prontuario prontuario, String s3Key) throws NegocioException {
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			PdfWriter writer = new PdfWriter(baos);
	        // Creating a PdfDocument  
	        PdfDocument pdf = new PdfDocument(writer);	        
	        // Creating a Document   
	        Document document = new Document(pdf); 		        
	        
	        // gera Capa de prontuario
	        generateContent(document, prontuario, s3Key);
	        
	        return baos;
	        
		}catch (Exception ex) {
	    	log.error("error: " + ex.getMessage());
	    	ex.printStackTrace();
	    	throw new NegocioException("Erro na montagem do PDF: " + ex.getMessage());
	    }
	}	
	
	// para download de capa de prontuario
	public void generatePDF(String dest, String nome, Prontuario prontuario, String s3Key) throws Exception {
		
		// Creating a PdfWriter			  
		PdfWriter writer = new 
		PdfWriter(dest);		  
		// Creating a PdfDocument       
		PdfDocument pdf = new PdfDocument(writer);
		// Adding an empty page 
        pdf.addNewPage();        
		// Creating a Document
		Document document = new Document(pdf);
		
		generateContent(document, prontuario, s3Key);
		
	}
	
	// para print capa de prontuario na tela
	private void generateContent(Document document, Prontuario prontuario, String s3Key) throws Exception {
		
		try {
			
		
		/* 
	     * Header 
	     */
		image(document, prontuario.getUnidade().getEndereco().getMunicipio(), s3Key);
		header(document, "CAPA DE PRONTUÁRIO");
		
	      
	    // Creating a list
	    List list = new List();
	    list.setSymbolIndent(12);
	    list.setListSymbol("\u2022");
	    
	    
	    // capa do prontuario
	    list.add("PRONTUÁRIO");
	    list.add("Código do Prontuario: " + prontuario.getCodigo() + " (" + prontuario.getStatus() + ")");
	    list.add("Número do Prontuario Físico: " + prontuario.getProntuario());
	    if(prontuario.getProntuarioVinculado() != null)
	    	list.add("Prontuario Vinculado: " + prontuario.getProntuarioVinculado().getCodigo());
	    else
	    	list.add("Prontuario Vinculado: ");
	    list.add("Data de Entrada: " + DateUtils.parseDateToString(prontuario.getDataEntrada()));	    
	    // pessoa referencia
	    list.add("PESSOA DE REFERÊNCIA");
	    list.add("Nome Pessoa de Referência: " + prontuario.getFamilia().getPessoaReferencia().getNome());
	    list.add("Vínculo familiar: " + prontuario.getFamilia().getPessoaReferencia().getParentescoPessoaReferencia());
	    list.add("Nome da mãe: " + prontuario.getFamilia().getPessoaReferencia().getNomeMae());
	    list.add("Nome Social: " + prontuario.getFamilia().getPessoaReferencia().getNomeSocial());
	    list.add("Idade: " + prontuario.getFamilia().getPessoaReferencia().getIdade());
	    if(prontuario.getFamilia().getPessoaReferencia().getPaisOrigem() != null)
	    	list.add("País de Origem: " + prontuario.getFamilia().getPessoaReferencia().getPaisOrigem().getPais());
	    else
	    	list.add("País de Origem: ");
	    list.add("Telefone: " + prontuario.getFamilia().getPessoaReferencia().getTelefone());       
	    list.add("E-mail: " + prontuario.getFamilia().getPessoaReferencia().getEmail());  
	    list.add("Identidade: " + prontuario.getFamilia().getPessoaReferencia().getRg() + "   UF: " + prontuario.getFamilia().getPessoaReferencia().getUfEmissao());
	    list.add("Data de nascimento: " + DateUtils.parseDateToString(prontuario.getFamilia().getPessoaReferencia().getDataNascimento()));
	    list.add("CPF: " + prontuario.getFamilia().getPessoaReferencia().getCpf());
	    list.add("NIS: " + prontuario.getFamilia().getPessoaReferencia().getNis());
	    list.add("Sexo: " + prontuario.getFamilia().getPessoaReferencia().getSexo());
	    list.add("Identidade de Gênero: " + prontuario.getFamilia().getPessoaReferencia().getIdentidadeGenero());
	    list.add("Cor/Raça: " + prontuario.getFamilia().getPessoaReferencia().getCorRaca());
	    list.add("Observação: " + prontuario.getFamilia().getPessoaReferencia().getObservacao());
	    
	    // endereço
	    list.add("ENDEREÇO");
	    list.add("Rua: " + prontuario.getFamilia().getEndereco().getEndereco() + ", " + prontuario.getFamilia().getEndereco().getNumero());
	    list.add("Bairro: " + prontuario.getFamilia().getEndereco().getBairro());
	    list.add("Complemento: " + prontuario.getFamilia().getEndereco().getComplemento());
	    list.add("Cep: " + prontuario.getFamilia().getEndereco().getCep());
	    list.add("Município: " + prontuario.getFamilia().getEndereco().getMunicipio());
	    list.add("Uf: " + prontuario.getFamilia().getEndereco().getUf());
	    list.add("Referência: " + prontuario.getFamilia().getEndereco().getReferencia());	    
	   
	    // Adding list to the document       
	    document.add(list);	
		
		document.close();
		}catch(Exception e) {
			e.printStackTrace();
			log.error("error generate pdf: " + e.getMessage());
			throw e;
		}
		
	}
	
		
	/*
	 * Header e Footer
	 */
	private void image(Document document, String municipio, String s3Key) throws NegocioException {		
		
		//log.info("image.....");
		try {
			//String imageFile = FacesContext.getCurrentInstance().getExternalContext().getRealPath(AppImages.PREF_SALTO);
			log.info("image....." + s3Key);
			URL url = new URL(s3Key);
			ImageData data = ImageDataFactory.create(url);
			// Creating an Image object 
			Image img = new Image(data);
			document.add(img);	

		} catch (Exception e) {
			throw new NegocioException("Logotipo da unidade não encontrado.");
		}
	}
	
	private void header(Document document, String assunto) throws Exception {
		
		PdfFont fontTitulo = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
		
		// Creating a title       
	    Paragraph titulo = new Paragraph(assunto);  
	    titulo.setBold();
	    titulo.setFontSize(18);
	    titulo.setFont(fontTitulo);
	    titulo.setTextAlignment(TextAlignment.CENTER);
	    titulo.getMarginTop();
	    document.add(titulo);

	}

}