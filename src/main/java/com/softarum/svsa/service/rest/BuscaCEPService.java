package com.softarum.svsa.service.rest;

import java.io.Serializable;
import java.net.URL;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.softarum.svsa.modelo.to.EnderecoTO;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 *
 */
public class BuscaCEPService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private EnderecoTO enderecoTO = new EnderecoTO();

	@SuppressWarnings("rawtypes")
	public EnderecoTO buscaEnderecoPorCEP(String cep) throws NegocioException {

		try {
			URL url = new URL(
					"http://cep.republicavirtual.com.br/web_cep.php?cep=" + cep
							+ "&formato=xml");

			Document document = getDocumento(url);

			Element root = document.getRootElement();

			for (Iterator i = root.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();

				if (element.getQualifiedName().equals("uf"))
					enderecoTO.setEstado(element.getText());

				if (element.getQualifiedName().equals("cidade"))
					enderecoTO.setCidade(element.getText());

				if (element.getQualifiedName().equals("bairro"))
					enderecoTO.setBairro(element.getText());

				if (element.getQualifiedName().equals("tipo_logradouro"))
					enderecoTO.setTipoLogradouro(element.getText());

				if (element.getQualifiedName().equals("logradouro"))
					enderecoTO.setLogradouro(element.getText());

				if (element.getQualifiedName().equals("resultado"))
					enderecoTO.setResultado(Integer.parseInt(element.getText()));
				
				enderecoTO.setCep(cep);
			}
			
		} catch (Exception ex) {
			throw new NegocioException("Problemas com o servidor, tente mais tarde." );
		}
		
		return enderecoTO;
	}

	public Document getDocumento(URL url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);

		return document;
	}

	public EnderecoTO getEnderecoTO() {
		return enderecoTO;
	}
}
