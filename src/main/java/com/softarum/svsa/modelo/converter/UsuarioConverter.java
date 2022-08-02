package com.softarum.svsa.modelo.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.dao.UsuarioDAO;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.util.cdi.CDIServiceLocator;

/**
 * @author murakamiadmin
 *
 */
@FacesConverter(forClass=Usuario.class)
public class UsuarioConverter implements Converter<Object> {

	private UsuarioDAO usuarioDAO;
	
	public UsuarioConverter() {
		this.usuarioDAO = CDIServiceLocator.getBean(UsuarioDAO.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Usuario retorno = null;

		if (value != null && !value.isEmpty()) {
			retorno = this.usuarioDAO.buscarPeloCodigo(Long.valueOf(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((Usuario) value).getCodigo();
			String retorno = (codigo == null ? null : codigo.toString());
			
			return retorno;
		}
		
		return "";
	}

}