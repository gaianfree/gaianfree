package com.softarum.svsa.modelo.converter;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.modelo.enums.DiaSemana;

/**
 * @author murakamiadmin
 *
 */
@FacesConverter(value="diaSemanaConverter")
public class DiaSemanaConverter extends EnumConverter {

    public DiaSemanaConverter() {
        super(DiaSemana.class);
    }

}
