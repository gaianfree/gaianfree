package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @author murakamiadmin
 *
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name="Familia.buscarTodos", query="select f from Familia f where f.tenant_id = :tenantId")	
})
public class Familia implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6819021888822630122L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	private BigDecimal rendaFamiliar;
	
	private Boolean insuficienciaAlimentar;
	
	@ToString.Include
	@Temporal(TemporalType.DATE)
	private Calendar dataInsuficienciaAlimentar;
	
	@OneToMany (mappedBy="familia", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Pessoa> membros;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_endereco")
	private Endereco endereco;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_pessoa_referencia")
	private PessoaReferencia pessoaReferencia;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_prontuario")
	private Prontuario prontuario;
	private String programaTransferenciaRenda;

	
	@Override
	public Familia clone() throws CloneNotSupportedException {
		return (Familia) super.clone();
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
}