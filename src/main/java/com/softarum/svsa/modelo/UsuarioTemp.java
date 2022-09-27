package com.softarum.svsa.modelo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name="usuario_temp")//nome da tabela onde estarão persistidos os objetos de usuario
@SequenceGenerator(name = "seq", sequenceName = "seq_userTemp", allocationSize = 1, initialValue = 1)
public class UsuarioTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    private String login;
    private String senha;
    private String validacao;

}
