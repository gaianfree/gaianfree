package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name="Abordagem.buscarTodos", query="select a from Abordagem a where a.tenant_id = :tenantId"),
	@NamedQuery(name="Abordagem.buscarTodosDia", query="select a from Abordagem a where a.tenant_id = :tenantId "
			+ "and a.unidade = :unidade "
			+ "and a.data between :ini and :fim ")
	
})
public class Abordagem implements Serializable {	

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	
	@ManyToOne
	@JoinColumn(name="codigo_pessoa")
	private PessoaAbordagem pessoa;	

		
	@Enumerated(EnumType.STRING)
	private CodigoAuxiliarAtendimento situacao;
	
	@ToString.Include
	private String descricao;
		
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;		
	
	

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
		log.debug("Datas de criação e modificação criados");
	}
}
