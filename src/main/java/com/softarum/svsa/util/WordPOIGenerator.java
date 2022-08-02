package com.softarum.svsa.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * Classe utilitária para gerar documentos word.
 * @author murakamiadmin
 *
 */
public class WordPOIGenerator {
	
	private Logger log = Logger.getLogger(WordPOIGenerator.class);
	
	private String nomeRelatorioWord;
	private String nomeDocWord;
	private static final String caminhoWebInf = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/worddocs/");
	//private static final String caminhoWebInf = "\\opt\\tomcat\\bk\\worddocs\\";
	private FileOutputStream out;
	
	
	public WordPOIGenerator(String nomeRelatorio) {
		this.nomeRelatorioWord = nomeRelatorio;
	}
	
	
	public XWPFDocument wordDocGenerate() {
		
		XWPFDocument document = new XWPFDocument(); // Blank Document
		
		try {
			/*
			 * Define nome e cria o arquivo word
			 */
			Date date = new Date();
			String extensao = " " + date.getTime() + ".doc";
			
			setNomeDocWord(this.nomeRelatorioWord.concat(extensao));
			File file = new File(caminhoWebInf.concat(getNomeDocWord()));
			this.out = new FileOutputStream(file);		
			
			log.info(file.getAbsolutePath());
			
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		
		return document;
	}
	
	
	public XWPFDocument cabecalho(XWPFDocument document, String cabecalho) {
		
		// Create paragraph
		XWPFParagraph paragraph = document.createParagraph();

		paragraph.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun run = paragraph.createRun();
		run.setBold(true);
		run.setFontSize(20);
		run.setText(cabecalho);
		run.addCarriageReturn(); run.addCarriageReturn();
		
		return document;
	}
	
	public XWPFDocument corpoParagrafo(XWPFDocument document, String linha) {
		
		// Create paragraphs
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun run = paragraph.createRun();
		run.setText(linha);
		
		return document;
	}
	
	public XWPFDocument corpoTexto(XWPFDocument document, ArrayList<String> linhas) {
		
		// Create paragraphs
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun run = paragraph.createRun();
		
		run.addCarriageReturn(); run.addCarriageReturn();
		
		for(String linha: linhas) {
			run.setText(linha);
			run.addCarriageReturn();
		}
		
		run.addCarriageReturn(); run.addCarriageReturn();
		
		return document;
	}
	
	
	public XWPFDocument corpoTabela(XWPFDocument document, ArrayList<String[]> linhas, ArrayList<String> nomesCol) {
		
		// create table
		XWPFTable table = document.createTable();
		
		// create first row
		XWPFTableRow tableRowOne = table.getRow(0);
		tableRowOne.getCell(0).setText(nomesCol.get(0));
		
		int colunas = nomesCol.size();
		int i = 1;
		for(i=1; i < colunas; i++) {
			tableRowOne.addNewTableCell().setText(nomesCol.get(i));
		}		

		for(String[] linha : linhas) {
			// create lines	
			XWPFTableRow tableRow = table.createRow();
			for(i=0; i < colunas; i++) {				
				tableRow.getCell(i).setText(linha[i]);
			}
		}
		
		return document;
	}
	
	public XWPFDocument rodape(XWPFDocument document) {
		
		// create paragraph		
		XWPFParagraph paragraph = document.createParagraph();
		
		paragraph.setAlignment(ParagraphAlignment.RIGHT);
		XWPFRun run = paragraph.createRun();		

		run.setFontSize(8);
		run.setItalic(true);
		run.setText("by SVSA");
		
		return document;
	}
	
	public String wordDocFinish(XWPFDocument document) {
		
		String response;
		try {
			// Write the Document
			document.write(this.out);
			this.out.close();
			response = getNomeDocWord();
			
		}catch(Exception e) {
			log.error(e.getMessage());
			response = "Problema na criação do documento Word.";
		}

		return response;			
	}
	
	

	public static final String getCaminhoWebInf() {
		return caminhoWebInf;
	}
	
	public String getNomeDocWord() {
		return nomeDocWord;
	}

	public void setNomeDocWord(String nomeDocWord) {
		this.nomeDocWord = nomeDocWord;
	}


}
