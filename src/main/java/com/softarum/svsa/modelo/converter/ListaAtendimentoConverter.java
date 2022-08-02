package com.softarum.svsa.modelo.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.dao.AgendamentoIndividualDAO;
import com.softarum.svsa.modelo.ListaAtendimento;
import com.softarum.svsa.util.cdi.CDIServiceLocator;

/**
 * @author murakamiadmin
 *
 */
@FacesConverter(forClass=ListaAtendimento.class)
public class ListaAtendimentoConverter implements Converter<Object> {

	private AgendamentoIndividualDAO listaDAO;
	
	public ListaAtendimentoConverter() {
		this.listaDAO = CDIServiceLocator.getBean(AgendamentoIndividualDAO.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		ListaAtendimento retorno = null;

		if (value != null && !value.isEmpty()) {
			retorno = this.listaDAO.buscarPeloCodigo(Long.valueOf(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((ListaAtendimento) value).getCodigo();
			String retorno = (codigo == null ? null : codigo.toString());
			
			return retorno;
		}
		
		return "";
	}

}