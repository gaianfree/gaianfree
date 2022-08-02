package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * @author murakamiadmin
 *
 */
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name="Endereco.buscarTodos", query="select e from Endereco e where e.tenant_id = :tenantId")	
})
public class Endereco implements Cloneable, Serializable {

	private static final long serialVersionUID = 2060033487029113003L;
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@NotBlank(message="O endereco é obrigatório.")
	private String endereco;
	
	@NotNull(message="O número é obrigatório.")
	private Long numero;
	private String complemento;

	private String bairro;
	
	private String cep;
	
	private String municipio;
	
	@NotBlank(message="A UF é obrigatória.")
	private String uf;
	private String referencia;
	private String telefoneContato;
	
	@Override
	public Endereco clone() throws CloneNotSupportedException {
		return (Endereco) super.clone();
	}
	
	/*
	 * Datas de Criação e Modificação
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataModificacao;

	@PrePersist
	@PreUpdate
	public void configuraDatasCriacaoAlteracao() {
		this.setDataModificacao( new Date() );
				
		if (this.getDataCriacao() == null) {
			this.setDataCriacao( new Date() );
		}
	}

	public String toString() {
		return endereco + ", " + numero + ". " + bairro + " - " + municipio + "/" + uf + ". CEP: " + cep;
	}	
}
