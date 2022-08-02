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
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.softarum.svsa.modelo.Encaminhamento;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 * 
 * Classe que utiliza o componente IText para gerar relatorios
 *
 */
public class EncamPDFService implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(EncamPDFService.class);

	
	/*
	 * Encaminhamento
	 * 
	 */
	
	// para stream e impressão de encaminhamento
	public ByteArrayOutputStream generateStream(Encaminhamento enc, String s3Key) throws NegocioException {
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			PdfWriter writer = new PdfWriter(baos);
	        // Creating a PdfDocument  
	        PdfDocument pdf = new PdfDocument(writer);	        
	        // Creating a Document   
	        Document document = new Document(pdf); 		        
	        
			document.setMargins(50, 50, 50, 50);

	        
	        // gera impressão de encaminhamento
	        generateContent(document, enc, s3Key);
	        
	        return baos;
	        
		}catch (Exception ex) {
			ex.printStackTrace();
	    	log.error("error: " + ex.getMessage());	    	
	    	throw new NegocioException("Erro na montagem do PDF: " + ex.getMessage());
	    }
	}

	// para download de Emissão de Ofício
		public void generatePDF(String dest, String nome, Encaminhamento encaminhamento, String s3Key) throws Exception {
			
			// Creating a PdfWriter			  
			PdfWriter writer = new 
			PdfWriter(dest);		  
			// Creating a PdfDocument       
			PdfDocument pdf = new PdfDocument(writer);
			// Adding an empty page 
	        pdf.addNewPage();        
			// Creating a Document
			Document document = new Document(pdf);		
			
			document.setMargins(50, 50, 50, 50);		
			
			generateContent(document, encaminhamento, s3Key);
			
		}
	
	
	// para print na tela
	private void generateContent(Document document, Encaminhamento enc, String s3Key) throws Exception {
		
		PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
		TextAlignment align = TextAlignment.JUSTIFIED;
		
		/* 
	     * Header 
	     */
		image(document, s3Key);
		header(document, "\n" + enc.getTecnico().getUnidade().getTipo() + " - " + enc.getTecnico().getUnidade().getNome());
	
		
		// Body
		if(enc.isNomeSocial()) {
			Paragraph line = new Paragraph("\n\nEncaminho o Sr(a) " + enc.getPessoa().getNomeSocial() + 
					" e solicito atenção para seu atendimento, no(a) " + enc.getOrgaoUnidadeDestino() + 
					" localizado(a) na " + enc.getEnderecoUnidadeDestino() + 
					" tendo em consideração as necessidades identificadas pela Assistência Social e expostas a seguir: " +
					enc.getMotivo() + ".");
			
			line.setFontSize(14);
			line.setFont(font);
			line.setTextAlignment(align);
			document.add(line);
		}
		else {
			Paragraph line = new Paragraph("\n\nEncaminho o Sr(a) " + enc.getPessoa().getNome() + 
					" e solicito atenção para seu atendimento, no(a) " + enc.getOrgaoUnidadeDestino() + 
					" localizado(a) na " + enc.getEnderecoUnidadeDestino() + 
					" tendo em consideração as necessidades identificadas pela Assistência Social e expostas a seguir: " +
					enc.getMotivo() + ".");
			
			line.setFontSize(14);
			line.setFont(font);
			line.setTextAlignment(align);
			document.add(line);
		}
		

		
		if(enc.getAnotacaoComplementar() != null && !enc.getAnotacaoComplementar().equals("") ) {
			Paragraph line2 = new Paragraph("Anotação Complementar:  " + enc.getAnotacaoComplementar() + ".");
			line2.setFontSize(14);
			line2.setFont(font);
			line2.setTextAlignment(align);
			document.add(line2);
		}
		   
		Paragraph line3 = new Paragraph("\nDATA: " + DateUtils.parseDateToString(enc.getDataCriacao()) + "\n"
				+ "UNIDADE: " + enc.getTecnico().getUnidade().getNome() + "\n"
				+ "TELEFONE: " + enc.getTecnico().getUnidade().getEndereco().getTelefoneContato() );
		line3.setFontSize(10);
		line3.setFont(font);
		line3.setTextAlignment(align);
		document.add(line3);
		
		Paragraph line4 = null;
		
		if(enc.getTecnico().getGrupo() == Grupo.COORDENADORES) {
			if(enc.getTecnico().getRegistroProfissional() != null && !enc.getTecnico().getRegistroProfissional().equals("")) {
				line4 = new Paragraph("\n\n_________________________________________\n" 
					+ enc.getTecnico().getNome() + "\n"
					+ enc.getTecnico().getRole() + " - Coordenador(a) "
					+ enc.getTecnico().getUnidade().getNome() + "\n" 
					+ enc.getTecnico().getRegistroProfissional());
			}
			else {
				line4 = new Paragraph("\n\n_________________________________________\n" 
						+ enc.getTecnico().getNome() + "\n"
						+ enc.getTecnico().getRole() + " - Coordenador(a) "
						+ enc.getTecnico().getUnidade().getNome());
						
			}
		}
		else {
			if(enc.getTecnico().getRegistroProfissional() != null && !enc.getTecnico().getRegistroProfissional().equals("")) {
				line4 = new Paragraph("\n\n_________________________________________\n" 
					+ enc.getTecnico().getNome() + "\n" 
					+ enc.getTecnico().getRole() + "\n" 
					+ enc.getTecnico().getRegistroProfissional());
			}
			else {
				line4 = new Paragraph("\n\n_________________________________________\n" 
						+ enc.getTecnico().getNome() + "\n" 
						+ enc.getTecnico().getRole()); 
			}
		}
		

		line4.setFontSize(12);
		line4.setFont(font);
		line4.setTextAlignment(TextAlignment.CENTER);
		document.add(line4);

		document.close();		
	}
			
			
	/*
	 * Header e Footer 
	 */
	//TODO criar uma classe para obter dinamicamente as imagesn das prefeituras
	private void image(Document document, String s3Key) throws NegocioException {
		
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