package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
	@NamedQuery(name="Calendario.buscarTodos", query="select c from Calendario c where c.unidade = :unidade "
			+ "and c.tenant_id = :tenantId "
			+ "and c.startDate > :data")	
})
public class Calendario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ToString.Include
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;

	private Long tenant_id;
	
	private String title;
	
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;
	
	public Calendario() {}
	
	public Calendario(String title, LocalDateTime date, LocalDateTime date2, Usuario tecnico, Unidade unidade) {
		this.title = title;
		this.setStartDate(date);
		this.setEndDate(date2);
		this.tecnico = tecnico;
		this.unidade = unidade;
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
