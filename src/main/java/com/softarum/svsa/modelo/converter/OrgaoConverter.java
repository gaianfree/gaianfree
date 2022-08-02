package com.softarum.svsa.modelo.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.dao.OrgaoDAO;
import com.softarum.svsa.modelo.Orgao;
import com.softarum.svsa.util.cdi.CDIServiceLocator;



/**
 * @author murakamiadmin
 *
 */
@FacesConverter(forClass=Orgao.class)
public class OrgaoConverter implements Converter<Object> {

	private OrgaoDAO orgaoDAO;
	
	public OrgaoConverter() {
		this.orgaoDAO = CDIServiceLocator.getBean(OrgaoDAO.class);
	}
	
	@Override    //converte tipo String para objeto - necessário mapear do modelo relacional para obj
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Orgao retorno = null;

		if (value != null && !value.isEmpty()) {
			retorno = this.orgaoDAO.buscarPeloCodigo(Long.valueOf(value));
		}

		return retorno;
	}

	@Override  //converte de objeto para codigo - necessário mapear do modelo obj para relacional
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((Orgao) value).getCodigo();
			String retorno = (codigo == null ? null : codigo.toString());
			
			return retorno;
		}
		
		return "";
	}

}