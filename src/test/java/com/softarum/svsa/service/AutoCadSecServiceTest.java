package com.softarum.svsa.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.softarum.svsa.dao.AutoCadSecDAO;
import com.softarum.svsa.modelo.Tenant;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.modelo.enums.Role;

@TestInstance(Lifecycle.PER_CLASS)

class AutoCadSecServiceTest {

    //obj simulado
    @InjectMocks
    private AutoCadSecDAO autocadDAO;
    
    @InjectMocks
    AutoCadSecService service;
    
    @BeforeAll
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void salvarTenant() {
        Tenant tenant = new Tenant();
        tenant.setSecretaria("santos");
        tenant.setTenant("Secretaria de Ação Social");
    }
    
    @Test
    public void salvarUsuario() {

        Usuario usuario = new Usuario();
        usuario.setNome("Jorge dos Santos");
        usuario.setEmail("jorgedossantos@gaian.com");
        usuario.setSenha("1234");
        usuario.setRole(Role.ASSISTENTE_SOCIAL);
        usuario.setGrupo(Grupo.COORDENADORES);
    }

}
