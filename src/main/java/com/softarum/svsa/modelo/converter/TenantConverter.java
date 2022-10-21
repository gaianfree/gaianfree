package com.softarum.svsa.modelo.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.dao.TenantDAO;
import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.util.cdi.CDIServiceLocator;

/**
 * @author murakamiadmin
 *
 */
@FacesConverter(forClass=Tenant.class)
public class TenantConverter implements Converter<Object> {

	private TenantDAO tenantDAO;
	
	public TenantConverter() {
		this.tenantDAO = CDIServiceLocator.getBean(TenantDAO.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Tenant retorno = null;

		if (value != null && !value.isEmpty()) {
			retorno = this.tenantDAO.buscarPeloCodigo(Long.valueOf(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((Tenant) value).getCodigo();
			String retorno = (codigo == null ? null : codigo.toString());
			
			return retorno;
		}
		
		return "";
	}

}