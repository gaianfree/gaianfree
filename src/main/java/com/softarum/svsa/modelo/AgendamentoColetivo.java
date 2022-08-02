package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.modelo.enums.StatusAtendimento;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * @author murakamiadmin
 *
 */
@Log4j
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name="AgendamentoColetivo.buscarTodos", query="select la from AgendamentoColetivo la where la.tenant_id = :tenantId "),
	@NamedQuery(name="AgendamentoColetivo.buscarAtendimentosAgendados", query="select la from AgendamentoColetivo la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "order by la.dataAgendamento"),
	@NamedQuery(name="AgendamentoColetivo.buscarAtendimentosCodAuxPeriodo", query="select a from AgendamentoColetivo a "
			+ "where a.statusAtendimento = :status "
			+ "and a.unidade = :unidade "
			+ "and a.tenant_id = :tenantId "
			+ "and a.dataAtendimento between :ini and :fim "
			+ "order by a.codigoAuxiliar"),
	@NamedQuery(name="AgendamentoColetivo.buscarAtendimentosCodAux", query="select a from AgendamentoColetivo a "
			+ "where a.statusAtendimento = :status "
			+ "and a.unidade = :unidade "
			+ "and a.tenant_id = :tenantId "
			+ "order by a.codigoAuxiliar")	
})
public class AgendamentoColetivo implements Serializable {

	private static final long serialVersionUID = 3504216302835076845L;
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAgendamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAtendimento;
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String resumoAtendimento;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Enumerated(EnumType.STRING)
	private CodigoAuxiliarAtendimento codigoAuxiliar;
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;
	
	@ManyToOne
	@JoinColumn(name="codigo_agendador")
	private Usuario agendador;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name="TecnicoAtendColetivo", joinColumns={@JoinColumn(name="codigo_atendimento")}, 
    									inverseJoinColumns={@JoinColumn(name="codigo_tecnico")})
	private List<Usuario> tecnicos;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="PessoaAgendada", joinColumns={@JoinColumn(name="codigo_agendamento")}, 
    									inverseJoinColumns={@JoinColumn(name="codigo_pessoa")})
	private List<Pessoa> pessoas;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="PessoaAgendadaFaltosa", joinColumns={@JoinColumn(name="codigo_agendamento")}, 
    									inverseJoinColumns={@JoinColumn(name="codigo_pessoa")})
	private Set<Pessoa> pessoasFaltosas;
	
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
		log.info("Datas de criação e modificação criados");
	}
	
}
