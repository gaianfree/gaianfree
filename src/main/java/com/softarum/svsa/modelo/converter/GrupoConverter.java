package com.softarum.svsa.modelo.converter;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.modelo.enums.Grupo;

/**
 * @author murakamiadmin
 *
 */
@FacesConverter(value="grupoConverter")
public class GrupoConverter extends EnumConverter {

    public GrupoConverter() {
        super(Grupo.class);
    }    

}
