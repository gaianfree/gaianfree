package com.softarum.svsa.modelo.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.service.ThemeService;
import com.softarum.svsa.service.ThemeService.Theme;

/**
 * @author murakamiadmin
 *
 */
@FacesConverter(forClass=Theme.class)
public class ThemeConverter implements Converter<Object> {	
	
	private ThemeService themeService = new ThemeService();
	
	private List<Theme> themes = themeService.getThemes();
		
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Theme retorno = null;

		if (value != null && !value.isEmpty()) {
			retorno = themes.get(Integer.valueOf(value).intValue());
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
				
		if (value != null) {
			Integer id = ((Theme) value).getId();
			String retorno = (id == null ? null : id.toString());
			return retorno;
		}
		
		return "";
	}

}