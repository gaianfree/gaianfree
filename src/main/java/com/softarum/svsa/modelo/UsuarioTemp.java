package com.softarum.svsa.modelo;
<<<<<<< HEAD
import com.softarum.svsa.util.GenerateValidation;
=======

>>>>>>> b0267cc28b78e8d049ddf3430d547d8c62320560
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class UsuarioTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< HEAD
    private String nome;
    private String email;
    private String validacao;
    private String token;
=======
    private Long id;
    private String nome;
    private String email;
    private String validacao;
>>>>>>> b0267cc28b78e8d049ddf3430d547d8c62320560

}
