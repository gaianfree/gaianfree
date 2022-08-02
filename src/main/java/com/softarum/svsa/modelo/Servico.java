package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.softarum.svsa.modelo.enums.DiaSemana;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.modelo.enums.TipoServico;

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
	@NamedQuery(name="Servico.buscarTodos", query="select s from Servico s where s.tenant_id = :tenantId "),
	@NamedQuery(name="Servico.buscarServicosAno", query="select s from Servico s "
			+ "where s.unidade = :unidade "
			+ "and s.tenant_id = :tenantId "
			+ "and s.ano = :ano"),
	@NamedQuery(name="Servico.buscarServicosAnoExecutora", query="select s from Servico s "
			+ "where s.unidadeExecutora = :unidade "
			+ "and s.tenant_id = :tenantId "
			+ "and s.ano = :ano"),
	@NamedQuery(name="Servico.buscarServicosUnidade", query="select s from Servico s "
			+ "where s.unidade = :unidade "
			+ "and s.tenant_id = :tenantId "),
	@NamedQuery(name="Servico.buscarServicosUnidadeAtivos", query="select count(s) from Servico s "
			+ "where s.unidade = :unidade and s.status = :status "
			+ "and s.tenant_id = :tenantId "),	
	@NamedQuery(name="Servico.buscarServicosPeriodo", query="select s from Servico s "
			+ "where s.status = :status "
			+ "and s.unidade = :unidade "
			+ "and s.tenant_id = :tenantId "
			+ "and s.dataIni between :ini and :fim"),
	@NamedQuery(name="Servico.buscarServicosPeriodoExecutora", query="select s from Servico s "
			+ "where s.status = :status "
			+ "and s.unidadeExecutora = :unidade "
			+ "and s.tenant_id = :tenantId "
			+ "and s.dataIni between :ini and :fim"),
	@NamedQuery(name="Servico.buscarServicosAtivos", query="select s from Servico s "
			+ "where s.status = :status "
			+ "and s.unidade = :unidade "
			+ "and s.tenant_id = :tenantId "),
	@NamedQuery(name="Servico.buscarServicosAtivosExecutora", query="select s from Servico s "
			+ "where s.status = :status "
			+ "and s.unidadeExecutora = :unidade "
			+ "and s.tenant_id = :tenantId ")
})
public class Servico implements Cloneable, Serializable {

	private static final long serialVersionUID = 398494293575163570L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	private String nome;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataIni;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFim;
	
	private String objetivo;
	
	private Integer ano;
	
	private String cargaHoraria;
	
	/* enums */	
	@Enumerated(EnumType.STRING)
	private DiaSemana diaSemana;
	
	@Enumerated(EnumType.STRING)
	private TipoServico tipoServico;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	/* relacionamentos */
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnicoResponsavel;
	
	private String professor;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade_exec")
	private Unidade unidadeExecutora;
	
	@OneToMany (mappedBy="servico", fetch = FetchType.EAGER)
	private Set<Inscricao> inscricoes;
	
	@OneToMany (mappedBy="servico")
	private Set<Atividade> atividades;
	
	@Transient
	public Integer getTotalInscritos() {
		return inscricoes.size();
	}
	@Override
	public Servico clone() throws CloneNotSupportedException {
		return (Servico) super.clone();
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
