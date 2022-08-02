package com.softarum.svsa.modelo.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.dao.MPComposicaoDAO;
import com.softarum.svsa.modelo.TipoDocumento;
import com.softarum.svsa.util.cdi.CDIServiceLocator;

/**
 * @author murakamiadmin
 *
 */
@FacesConverter(forClass=TipoDocumento.class )
public class TipoDocumentoConverter implements Converter<Object> {

	private MPComposicaoDAO composicaoDAO;
	
	public TipoDocumentoConverter() {
		this.composicaoDAO = CDIServiceLocator.getBean(MPComposicaoDAO.class);
	}

	@Override    //converte tipo String para objeto - necessário mapear do modelo relacional para obj
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		TipoDocumento retorno = null;

		if (value != null && !value.isEmpty()) {
			retorno = this.composicaoDAO.buscarTipoDocumentoPeloCodigo(Long.valueOf(value));
		}

		return retorno;
	}

	@Override  //converte de objeto para codigo - necessário mapear do modelo obj para relacional
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((TipoDocumento) value).getCodigo();
			String retorno = (codigo == null ? null : codigo.toString());
			
			return retorno;
		}
		
		return "";
	}
	
	
}
