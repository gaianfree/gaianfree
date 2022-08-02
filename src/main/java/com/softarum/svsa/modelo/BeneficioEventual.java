package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.softarum.svsa.modelo.enums.Beneficio;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author gabrielrodrigues
 *
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name = "BeneficioEventual.buscarBeneficios", query = "select a from BeneficioEventual a "
		+ "where a.prontuario = :prontuario and a.tenant_id = :tenantId") })
 
public class BeneficioEventual implements Serializable {

	private static final long serialVersionUID = -3276288201376060553L;
	
	@ToString.Include
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;

	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String relato;
	
	
	private String cpfFalecido;
	
	
	private String registroNascimento;

	/* Enums */
	
	@Enumerated(EnumType.STRING)
	private Beneficio beneficio;	

	/* relacionamentos */
	
	@ManyToOne
	@JoinColumn(name="codigo_prontuario")
	private Prontuario prontuario;
	
	@OneToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;

	
	@Transient
	public String getDataAnotacaoFormatada() {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(data);		
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