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
import com.softarum.svsa.modelo.OficioEmitido;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.util.DateUtils;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 * 
 * Classe que utiliza o componente IText para gerar relatorios
 *
 */
public class OficioPDFService implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(OficioPDFService.class);

		
	/*
	 * Emissão de Ofício
	 * 
	 */
	
	// para stream e impressão de Emissão de Ofício
	public ByteArrayOutputStream generateStream(OficioEmitido oficioEmitido, String s3Key, String secretaria) throws NegocioException {
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			PdfWriter writer = new PdfWriter(baos);
	        // Creating a PdfDocument  
	        PdfDocument pdf = new PdfDocument(writer);	        
	        // Creating a Document   
	        Document document = new Document(pdf); 		        
	        
			document.setMargins(50, 50, 50, 50);

	        
	        // gera impressão do Ofício
	        generateContent(document, oficioEmitido, s3Key, secretaria);
	        
	        return baos;
	        
		}catch (Exception ex) {
	    	log.error("error: " + ex.getMessage());	    	
	    	throw new NegocioException("Erro na montagem do PDF: " + ex.getMessage());
	    }
	}
	
	// para download de Emissão de Ofício
	public void generatePDF(String dest, String nome, OficioEmitido oficioEmitido, String s3Key, String secretaria) throws Exception {
		
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
		
		generateContent(document, oficioEmitido, s3Key, secretaria);
		
	}

		
	private void generateContent(Document document, OficioEmitido oficioEmitido, String s3Key, String secretaria) throws Exception {
		
		PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
		TextAlignment align = TextAlignment.JUSTIFIED;
		
		/* 
	     * Header 
	     */
		image(document, oficioEmitido.getTecnico().getUnidade().getEndereco().getMunicipio(), s3Key);
		headerOficio(document, oficioEmitido.getTecnico().getUnidade().getNome(), oficioEmitido, secretaria);
		//header(document, "Sistema Único de Assistência Social - SUAS");
	
		
		// Body
		Paragraph line = new Paragraph("\nOfício N° " + oficioEmitido.getNrOficioEmitido() + " - " + 
				oficioEmitido.getTecnico().getUnidade().getNome() + "\n " + 
				oficioEmitido.getNomeOrgao() + "\n\n\n");
		line.setFontSize(12);
		line.setFont(font);
		line.setTextAlignment(align);
		document.add(line);
				
		Paragraph line2 = new Paragraph(oficioEmitido.getAssunto());
		line2.setFontSize(12);
		line2.setFont(font);
		line2.setTextAlignment(align);
		document.add(line2);		
		 
		Paragraph line3 = new Paragraph("\nDATA: " + DateUtils.parseDateToString(oficioEmitido.getDataEmissao()) );
		line3.setFontSize(10);
		line3.setFont(font);
		line3.setTextAlignment(align);
		document.add(line3);
		
		Paragraph line4 = null;
		
		if(oficioEmitido.getTecnico().getGrupo() == Grupo.COORDENADORES) {
			if(oficioEmitido.getTecnico().getRegistroProfissional() != null && !oficioEmitido.getTecnico().getRegistroProfissional().isEmpty() ) {
				line4 = new Paragraph("\n\n_________________________________________\n" 
					+ oficioEmitido.getTecnico().getNome() + "\n"
					+ oficioEmitido.getTecnico().getRole() + " - Coordenador(a) "
					+ oficioEmitido.getTecnico().getUnidade().getNome() + "\n" 
					+ oficioEmitido.getTecnico().getRegistroProfissional());
			}
			else {
				line4 = new Paragraph("\n\n_________________________________________\n" 
						+ oficioEmitido.getTecnico().getNome() + "\n"
						+ oficioEmitido.getTecnico().getRole() + " - Coordenador(a) "
						+ oficioEmitido.getTecnico().getUnidade().getNome());
						
			}
		}
		else {
			if(oficioEmitido.getTecnico().getRegistroProfissional() != null && !oficioEmitido.getTecnico().getRegistroProfissional().isEmpty() ) {
				line4 = new Paragraph("\n\n_________________________________________\n" 
					+ oficioEmitido.getTecnico().getNome() + "\n" 
					+ oficioEmitido.getTecnico().getRole() + "\n" 
					+ oficioEmitido.getTecnico().getRegistroProfissional());
			}
			else {
				line4 = new Paragraph("\n\n_________________________________________\n" 
						+ oficioEmitido.getTecnico().getNome() + "\n" 
						+ oficioEmitido.getTecnico().getRole()); 
			}
		}	
		
		
		line4.setFontSize(12);
		line4.setFont(font);
		line4.setTextAlignment(TextAlignment.CENTER);
		document.add(line4);

		document.close();		
	}
	
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

	private void headerOficio(Document document, String unidade, OficioEmitido oficioEmitido, String secretaria) throws Exception {
		
		try {
			PdfFont fontTitulo = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			log.info(unidade);
			if(unidade.equals("CREAS")) {      
				Paragraph titulo = new Paragraph(
						"\n" + secretaria.toUpperCase() + "\nCENTRO DE REFERÊNCIA ESPECIALIZADO DE ASSISTÊNCIA SOCIAL"
						+ "\n" + oficioEmitido.getUnidade().getEndereco()
						+ "\nFone: " + oficioEmitido.getUnidade().getEndereco().getTelefoneContato());
				titulo.setFontSize(12);
				titulo.setFont(fontTitulo);
				titulo.setTextAlignment(TextAlignment.CENTER);
				titulo.getMarginTop();
				document.add(titulo);
			}
			else if(unidade.equals("Espaço POP")) {      
				Paragraph titulo = new Paragraph(
						"\n" + secretaria.toUpperCase() + "\nESPAÇO ESPECIALIZADO PARA POPULAÇÃO"
						+ "\n" + oficioEmitido.getUnidade().getEndereco()
						+ "\nFone: " + oficioEmitido.getUnidade().getEndereco().getTelefoneContato());
				titulo.setFontSize(12);
				titulo.setFont(fontTitulo);
				titulo.setTextAlignment(TextAlignment.CENTER);
				titulo.getMarginTop();
				document.add(titulo);
			}
			else {
				Paragraph titulo = new Paragraph(
						"\n" + secretaria.toUpperCase() + "\nCENTRO DE REFERÊNCIA DE ASSISTÊNCIA SOCIAL"
						+ "\n" + oficioEmitido.getUnidade().getEndereco()
						+ "\nFone: " + oficioEmitido.getUnidade().getEndereco().getTelefoneContato());
				titulo.setFontSize(12);
				titulo.setFont(fontTitulo);
				titulo.setTextAlignment(TextAlignment.CENTER);
				titulo.getMarginTop();
				document.add(titulo);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Não foi possivel localizar a unidade.");
		}
	}

}