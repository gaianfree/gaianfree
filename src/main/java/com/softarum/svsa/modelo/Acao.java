package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.softarum.svsa.modelo.enums.StatusAtendimento;

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
	@NamedQuery(name="Acao.buscarTodos", query="select a from Acao a where a.tenant_id = :tenantId"),
	@NamedQuery(name="Acao.buscarAcoesPendentes", query="select la from Acao la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "order by la.data"),
	@NamedQuery(name="Acao.buscarAcoes", query="select a from Acao a where a.pessoa = :pessoa and a.tenant_id = :tenantId")
	
})
public class Acao implements Serializable {	

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;				//@Index(name="idx_data") - para buscas lazyAcao
	
	@ToString.Include
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String descricao;
		
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;
	
	@ManyToOne
	@JoinColumn(name="codigo_agendador")
	private Usuario agendador;
	
	@ManyToOne
	@JoinColumn(name="codigo_pessoa")
	private Pessoa pessoa;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="PessoaAcao", joinColumns={@JoinColumn(name="codigo_acao")}, 
		inverseJoinColumns={@JoinColumn(name="codigo_pessoa_acao")})
	private List<Pessoa> pessoas;
	
	@Enumerated(EnumType.STRING)
	private StatusAtendimento statusAtendimento;

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
