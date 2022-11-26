package com.softarum.svsa.service;

import com.softarum.svsa.modelo.UsuarioTemp;
import com.softarum.svsa.util.GenerateValidation;
import lombok.extern.log4j.Log4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class AutoCadUserServiceTest {

    final static UsuarioTemp usuarioTemp = new UsuarioTemp();
    final static AutoCadUserService AUTO_CAD_USER_SERVICE = new AutoCadUserService();

    static String tokenCorreto = null;
    static String tokenErrado = null;

    @Test
    void shouldShowSimpleAssertion() {
        Assertions.assertEquals(1, 1);
    }

    @BeforeAll
    static void setup() {
        usuarioTemp.setNome("Teste");
        usuarioTemp.setEmail("teste@teste.com");
        usuarioTemp.setValidacao(GenerateValidation.keyValidation());
        tokenErrado = GenerateValidation.keyValidation();
        tokenCorreto = usuarioTemp.getValidacao();
    }

    @Test
    public void imprimeDadosUsuario() {
        log.info("Nome: " + usuarioTemp.getNome());
        log.info("E-mail: " + usuarioTemp.getEmail());
        log.info("Validação: " + usuarioTemp.getValidacao());
        log.info("Token Correto: " + tokenCorreto);
        log.info("Token errado: " + tokenErrado);
    }

    @Test
    public void testEnvia() {

        String msgCorpo = "corpo validação: " + usuarioTemp.getValidacao();
        String assunto = "assunto";
        List<String> destinatario = new ArrayList<>();
        destinatario.add(usuarioTemp.getEmail());

        try {
            AUTO_CAD_USER_SERVICE.envia(usuarioTemp);
            log.info("E-mail enviado:");
            log.info("Destinatario: " + destinatario);
            log.info("Assunto: " + assunto);
            log.info("Corpo: " + msgCorpo);
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testVerificaTokenC() {
        if(testVerifyToken(usuarioTemp.getValidacao(), tokenCorreto)) {
            log.info("Código válido.\nO e-mail confirmado.");
        } else {
            log.info("Código inválido.\nO e-mail não foi confirmado.");
        }
    }

    @Test
    public void testVerificaTokenE() {
        if(testVerifyToken(usuarioTemp.getValidacao(), tokenErrado)) {
            log.info("Código válido.\nO e-mail confirmado.");
        } else {
            log.info("Código inválido.\nO e-mail não foi confirmado.");
        }
    }
    public Boolean testVerifyToken(String validacao, String token) {

        if(validacao.equals(token)) {
            return true;
        }
        else {
            return false;
        }
    }
}
