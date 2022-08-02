package com.softarum.svsa.service.rest;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softarum.svsa.modelo.to.MunicipioTO;
import com.softarum.svsa.modelo.to.UfTO;



/**
 *
 * Classe para teste com busca de UFs por WebService Rest
 *
 * @author murak
 *
 */
public class RestService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -387149089466883100L;
	private int HTTP_COD_SUCESSO = 200;
	private HttpURLConnection con;
	private URL url;
	private String URL_WS_UF = "http://ibge.herokuapp.com/estado/UF";	
	private  String URL_WS_MU = "http://ibge.herokuapp.com/municipio/?val=";

	
	
	public  HttpURLConnection conectaWSUF() throws Exception{
		url = null;
		con = null;
		try {
			url = new URL(URL_WS_UF);
			con = (HttpURLConnection) url.openConnection();

			if (con.getResponseCode() != HTTP_COD_SUCESSO) {
				throw new RuntimeException("HTTP error code : "+ con.getResponseCode());
			}

		} catch (MalformedURLException e) { //Exception para URL
			e.printStackTrace();

		} catch (IOException e) { // Exception para connection
			e.printStackTrace();
		}

		return con;

	}
	
	public List<UfTO> listarUfs() throws Exception{
		
		conectaWSUF();
		
		//Gson gson = new Gson();
		UfTO uf = null;
		List<UfTO> ufList = new ArrayList<UfTO>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

		//recebe a string com os valores Json
		String line = br.readLine();
		
		Map<String, String> retMap = new Gson().fromJson(line, new TypeToken<HashMap<String, String>>() {}.getType());
		
		for (String sigla_key : retMap.keySet()) {
			String codigo_value = retMap.get(sigla_key);
            uf = new UfTO();
            uf.setCodigo(codigo_value);
            uf.setSigla(sigla_key);
            
            //Adiciona a uf a um list
            ufList.add(uf);
		}
		
		Collections.sort(ufList, new Comparator<UfTO>() {

			@Override
			public int compare(UfTO uf1, UfTO uf2) {
				return uf1.getSigla().compareTo(uf2.getSigla());
			}
		
		});
		
		return ufList;

	}
	
	/*
	 * Municipios
	 */
	public  HttpURLConnection conectaWSMU(String uf) throws Exception{
		url = null;
		con = null;
		try {
			url = new URL(URL_WS_MU.concat(uf));
			con = (HttpURLConnection) url.openConnection();

			if (con.getResponseCode() != HTTP_COD_SUCESSO) {
				throw new RuntimeException("HTTP error code : "+ con.getResponseCode());
			}

		} catch (MalformedURLException e) { //Exception para URL
			e.printStackTrace();

		} catch (IOException e) { // Exception para connection
			e.printStackTrace();
		} 

		return con;

	}
	
	public  List<MunicipioTO> listarMunicipios(String uf) throws Exception{
		
		conectaWSMU(uf);
		
		//Gson gson = new Gson();
		MunicipioTO municipio = null;
		List<MunicipioTO> cidadesList = new ArrayList<MunicipioTO>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

		//recebe a string com os valores Json
		String line = br.readLine();
		
		Map<String, String> retMap = new Gson().fromJson(line, new TypeToken<HashMap<String, String>>() {}.getType());
		
		for (String nome_key : retMap.keySet()) {
			String codigo_value = retMap.get(nome_key);
            municipio = new MunicipioTO();
            municipio.setCodigo(codigo_value);
            municipio.setNome(nome_key);
            
            //Adiciona a uf a um list
            cidadesList.add(municipio);
		}
		
		Collections.sort(cidadesList, new Comparator<MunicipioTO>() {

			@Override
			public int compare(MunicipioTO municipio1, MunicipioTO municipio2) {
				return municipio1.getNome().compareTo(municipio2.getNome());
			}
		});
		
		return cidadesList;

	}
}
