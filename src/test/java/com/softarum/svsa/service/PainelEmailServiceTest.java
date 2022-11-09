package com.softarum.svsa.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.softarum.svsa.modelo.Unidade;
import com.softarum.svsa.modelo.Usuario;
import com.softarum.svsa.modelo.enums.Grupo;
import com.softarum.svsa.util.EmailUtil;

import lombok.extern.log4j.Log4j;

@Log4j
@TestInstance(Lifecycle.PER_CLASS)
public class PainelEmailServiceTest extends Mockito {
	
	@Mock
	UnidadeService unidadeService;
	
	@Mock
	TenantService tenantService;
	
	@Mock
	UsuarioService usuarioService;
	
	@Mock
	EmailUtil emailUtil;
	
	@InjectMocks
	PainelEmailService service;
	
	@BeforeAll
	void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testEnviarEmail() {
		String assunto = "assunto";
		String corpo = "corpo";
		
		Long tenantId = 1L;

		List<Unidade> unidades = new ArrayList<Unidade>();
		unidades.add(new Unidade());
		
		List<Grupo> perfis = new ArrayList<Grupo>();
		perfis.add(Grupo.ADMINISTRATIVOS);
		perfis.add(Grupo.TECNICOS);
		
		Usuario adm = new Usuario();
		adm.setEmail("adm@email.com");
		adm.setGrupo(Grupo.ADMINISTRATIVOS);
		
		Usuario tecnico = new Usuario();
		tecnico.setEmail("tecnico@email.com");
		tecnico.setGrupo(Grupo.TECNICOS);
		
		Usuario coordenador = new Usuario();
		coordenador.setEmail("coordenador@email.com");
		coordenador.setGrupo(Grupo.COORDENADORES);
		
		Usuario gestor = new Usuario();
		gestor.setEmail("gestor@email.com");
		gestor.setGrupo(Grupo.GESTORES);
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(adm);
		usuarios.add(tecnico);
		usuarios.add(coordenador);
		usuarios.add(gestor);
		
		List<String> destinatarios = new ArrayList<String>();
		destinatarios.add("adm@email.com");
		destinatarios.add("tecnico@email.com");
		
		Mockito.when(service.getUsuariosByUnidade(unidades.get(0), tenantId)).thenReturn(usuarios);

		try (MockedStatic<EmailUtil> mockedStatic = Mockito.mockStatic(EmailUtil.class)) {
			service.enviarEmail(assunto, corpo, tenantId, unidades, perfis);
			mockedStatic.verify(() -> EmailUtil.sendEmail("SSL", destinatarios, assunto, corpo));
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
	}
}
