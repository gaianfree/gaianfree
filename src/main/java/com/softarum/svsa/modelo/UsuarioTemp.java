package com.softarum.svsa.modelo;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Setter
@Getter
public class UsuarioTemp {
    private String nome;
    private String email;
    private String validacao;
    private String token;
}
