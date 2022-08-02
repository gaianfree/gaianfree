package com.softarum.svsa.modelo.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.dao.IndicadoresDAO;
import com.softarum.svsa.modelo.IndProtecao;
import com.softarum.svsa.util.cdi.CDIServiceLocator;



/**
 * @author murakamiadmin
 *
 */
@FacesConverter(value = "indicadorConverter")
public class IndicadorConverter implements Converter<Object> {

	private IndicadoresDAO indicadoresDAO;
	
	public IndicadorConverter() {
		this.indicadoresDAO = CDIServiceLocator.getBean(IndicadoresDAO.class);
	}
	
	@Override    //converte tipo String para objeto - necessário mapear do modelo relacional para obj
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		IndProtecao retorno = null;

		if (value != null && !value.isEmpty()) {
			retorno = this.indicadoresDAO.buscarIndPeloCodigo(Long.valueOf(value));
		}

		return retorno;
	}

	@Override  //converte de objeto para codigo - necessário mapear do modelo obj para relacional
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((IndProtecao) value).getCodigo();
			String retorno = (codigo == null ? null : codigo.toString());
			
			return retorno;
		}
		
		return "";
	}

}