package com.softarum.svsa.modelo.converter;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

import com.softarum.svsa.modelo.enums.Role;

/**
 * @author murakamiadmin
 *
 */
@FacesConverter(value="roleConverter")
public class RoleConverter extends EnumConverter {

    public RoleConverter() {
        super(Role.class);
    }

}
