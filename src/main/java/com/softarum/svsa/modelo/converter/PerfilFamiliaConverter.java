package com.softarum.svsa.modelo.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.modelo.enums.PerfilFamilia;

/**
 * @author murakamiadmin
 *
 */
@FacesConverter(value="perfilFamiliaConverter")
public class PerfilFamiliaConverter extends EnumConverter {
	
	private static final String ATTRIBUTE_ENUM_TYPE = "GenericEnumConverter.enumType";

    public PerfilFamiliaConverter() {
        super(PerfilFamilia.class);
    }   
    
    

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Class<Enum> enumType = (Class<Enum>) component.getAttributes().get(ATTRIBUTE_ENUM_TYPE);
        try {
            return Enum.valueOf(enumType, value);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new ConverterException(new FacesMessage("Exception:" + e.getMessage() + "\n" + "Value is not an enum of type: " + enumType));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Enum) {
            component.getAttributes().put(ATTRIBUTE_ENUM_TYPE, value.getClass());
            return ((Enum<?>) value).name();
        } else {
            throw new ConverterException(new FacesMessage("Value is not an enum: " + value.getClass()));
        }
    }

}
