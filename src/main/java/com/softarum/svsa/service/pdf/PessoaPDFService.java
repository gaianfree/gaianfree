package com.softarum.svsa.service.pdf;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.softarum.svsa.modelo.to.AtendimentoDTO;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 * 
 * Classe que utiliza o componente IText para gerar relatorios
 *
 */
public class PessoaPDFService implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(PessoaPDFService.class);

	
	
	/************************************************
	 * 
	 * PDF do historico/evolução da pessoa
	 * 
	 ************************************************/	
	
	// para impressão da evolução da pessoa
	public ByteArrayOutputStream generateStream(List<AtendimentoDTO> list) throws NegocioException {
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			PdfWriter writer = new PdfWriter(baos);
	        // Creating a PdfDocument  
	        PdfDocument pdf = new PdfDocument(writer);	        
	        // Creating a Document   
	        Document document = new Document(pdf); 		        
	        
	        // gera conteudo da Evolução da pessoa
	        generateContent(document, list);
	        return baos;
	        
		}catch (Exception ex) {
	    	log.error("error: " + ex.getMessage());
	    	ex.printStackTrace();
	    	throw new NegocioException("Erro na montagem do PDF: " + ex.getMessage());
	    }
	}			
	
	private void generateContent(Document document, List<AtendimentoDTO> list) throws Exception {
		
		PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
		TextAlignment align = TextAlignment.LEFT;
		
		/* Header 	*/		
		header(document, list.get(0));
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		for(AtendimentoDTO to : list) {
			
			
			// TODO fazer um tratamento da string to.getResumoAtendimento() para retirar os caracteres do editor 
			//      ou descobrir como o iText lida com esses caracteres
			
			// Body
	        
			Paragraph line = new Paragraph(dateFormat.format(to.getData()) + " :: " + to.getResumoAtendimento() + "\n");
			line.setFontSize(12);
			line.setFont(font);
			line.setTextAlignment(align);
			document.add(line);	
			
		}		

		document.close();		
	}
		
	
	private void header(Document document, AtendimentoDTO to) throws Exception {
		
		PdfFont fontTitulo = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
		
		Paragraph titulo = new Paragraph("EVOLUÇÃO:  " + to.getNomePessoa() + "\n\n");  
	    titulo.setBold();
	    titulo.setFontSize(18);
	    titulo.setFont(fontTitulo);
	    titulo.setTextAlignment(TextAlignment.CENTER);
	    titulo.getMarginTop();
	    document.add(titulo);    
		
	}

}