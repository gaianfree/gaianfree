package com.softarum.svsa.modelo.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.dao.HistoricoTransferenciaDAO;
import com.softarum.svsa.modelo.HistoricoEncaminhamento;
import com.softarum.svsa.util.cdi.CDIServiceLocator;

/**
 * @author murakamiadmin
 *
 */
@FacesConverter(forClass=HistoricoEncaminhamento.class)
public class HistoricoEncaminhamentoConverter implements Converter<Object> {

	private HistoricoTransferenciaDAO historicoTransferenciaDAO;
	
	public HistoricoEncaminhamentoConverter() {
		this.historicoTransferenciaDAO = CDIServiceLocator.getBean(HistoricoTransferenciaDAO.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		HistoricoEncaminhamento retorno = null;

		if (value != null && !value.isEmpty()) {
			retorno = this.historicoTransferenciaDAO.buscarHEPeloCodigo(Long.valueOf(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((HistoricoEncaminhamento) value).getCodigo();
			String retorno = (codigo == null ? null : codigo.toString());
			
			return retorno;
		}
		
		return "";
	}

}