package com.softarum.svsa.modelo.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.dao.CapaProntuarioDAO;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.util.cdi.CDIServiceLocator;


/**
 * @author murakamiadmin
 *
 */
@FacesConverter(forClass=Prontuario.class)
public class ProntuarioConverter implements Converter<Object> {

	private CapaProntuarioDAO prontuarioDAO;
	
	public ProntuarioConverter() {
		this.prontuarioDAO = CDIServiceLocator.getBean(CapaProntuarioDAO.class);
	}
	
	@Override    //converte tipo String para objeto - necessário mapear do modelo relacional para obj
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Prontuario retorno = null;

		if (value != null && !value.isEmpty()) {
			retorno = this.prontuarioDAO.buscarPeloCodigo(Long.valueOf(value));
		}

		return retorno;
	}

	@Override  //converte de objeto para codigo - necessário mapear do modelo obj para relacional
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((Prontuario) value).getCodigo();
			String retorno = (codigo == null ? null : codigo.toString());
			
			return retorno;
		}
		
		return "";
	}

}