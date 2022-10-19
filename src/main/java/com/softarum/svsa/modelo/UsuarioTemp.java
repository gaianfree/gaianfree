package com.softarum.svsa.modelo;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Setter
@Getter
@Entity
public class UsuarioTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String nome;
    private String email;
    private String validacao;
    private String token;
}
