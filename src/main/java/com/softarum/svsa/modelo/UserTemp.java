package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import javax.persistence.PreUpdate;

import com.softarum.svsa.modelo.enums.Status;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter

@Entity
@Audited
@NamedQueries({
	
	@NamedQuery(name="UserTemp.buscarPorEmail", query="select u from UserTemp u "
	+ "where u.email = :email"),
	
	
})

public class UserTemp implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long codigo;
	

    private String email;
	private String senha;
	
	
	@Enumerated(EnumType.STRING)
	private Status status;
	

	
	
}
