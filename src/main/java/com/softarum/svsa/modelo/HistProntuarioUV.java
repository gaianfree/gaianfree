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
	@NamedQuery(name="HistProntuarioUV.buscarTodos", query="select h from HistProntuarioUV h "
			+ "where h.tenant_id = :tenantId"),
	@NamedQuery(name="HistProntuarioUV.buscarTodosUnidade", query="select h from HistProntuarioUV h "
			+ "where h.unidade = :unidade "
			+ "and h.tenant_id = :tenantId "),
	@NamedQuery(name="HistProntuarioUV.buscarTodosPeriodo", query="select h from HistProntuarioUV h "
			+ "where h.unidade = :unidade "
			+ "and h.tenant_id = :tenantId "
			+ "and h.dataCriacao between :ini and :fim ")
	
})
public class HistProntuarioUV implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8864093951961543074L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;

	private Long tenant_id;	
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
	@ManyToOne
	@JoinColumn(name="codigo_usuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name="codigo_prontuario")
	private Prontuario prontuario;
		
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
