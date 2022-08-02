package com.softarum.svsa.modelo;

import java.io.Serializable;
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
	@NamedQuery(name="Frequencia.buscarTodos", query="select f from Frequencia f where f.tenant_id = :tenantId"),	
	@NamedQuery(name="Frequencia.buscarFrequenciaPessoa", query="select f from Frequencia f "
			+ "where f.atividade = :atividade "
			+ "and f.pessoa = :pessoa "
			+ "and f.tenant_id = :tenantId")
})
public class Frequencia implements Serializable {

	private static final long serialVersionUID = 2697636588991856953L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	private boolean presente;
	
	/* relacionamentos */
	@ManyToOne
	@JoinColumn(name="codigo_Pessoa")
	private Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name="codigo_atividade")
	private Atividade atividade;
	
	public boolean isPresente() {
		return presente;
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
