package com.softarum.svsa.modelo.converter;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.modelo.enums.TipoServico;

/**
 * @author murakamiadmin
 *
 */
@FacesConverter(value="tipoServicoConverter")
public class TipoServicoConverter extends EnumConverter {

    public TipoServicoConverter() {
        super(TipoServico.class);
    }

}
