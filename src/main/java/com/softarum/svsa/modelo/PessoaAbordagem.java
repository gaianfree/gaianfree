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
import javax.validation.constraints.NotBlank;

import com.softarum.svsa.modelo.enums.Sexo;

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
	@NamedQuery(name="PessoaAbordagem.buscarTodos", query="select pa from PessoaAbordagem pa where pa.tenant_id = :tenantId"),
	@NamedQuery(name="PessoaAbordagem.buscarPeloNome", query="select pa from PessoaAbordagem pa where pa.nome = :nome "),
	@NamedQuery(name="PessoaAbordagem.buscarNomes", query="select pa.nome from PessoaAbordagem pa where pa.tenant_id = :tenantId "
			+ "and pa.unidade = :unidade "
			+ "and pa.nome LIKE :nome")	
	
})
public class PessoaAbordagem implements Serializable {	

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;
	
	@NotBlank(message="O nome é obrigatório")
	private String nome;
	
	@Enumerated(EnumType.STRING)
	private Sexo sexo;
		
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;	

	
	

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
