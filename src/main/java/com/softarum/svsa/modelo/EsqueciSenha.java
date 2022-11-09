package com.softarum.svsa.modelo;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity

public class EsqueciSenha implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String token;
	@Id
	private String email;
	
	private Boolean isExpired;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataReferencia;
	
	@PrePersist
	@PreUpdate
	public void configuraDataReferencia() {
		this.setDataReferencia(new Date());			
	}
}
