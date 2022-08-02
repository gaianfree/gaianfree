package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.softarum.svsa.modelo.enums.CodigoAuxiliarAtendimento;
import com.softarum.svsa.modelo.enums.Role;
import com.softarum.svsa.modelo.enums.StatusAtendimento;

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
@Audited
@NamedQueries({
	@NamedQuery(name="ListaAtendimento.buscarTodos", query="select la from ListaAtendimento la where la.tenant_id = :tenantId"),
	@NamedQuery(name="ListaAtendimento.buscarAtendimentosAgendados", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "order by la.dataAgendamento"),
	// usado para em atendimento individual sem agendamento
	@NamedQuery(name="ListaAtendimento.buscarAtendimentosPendentes", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "order by la.dataAgendamento"),
	@NamedQuery(name="ListaAtendimento.buscarAtendAgendados", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.dataAgendamento between :ini and :fim "
			+ "and la.tenant_id = :tenantId "
			+ "order by la.dataAgendamento"),
	@NamedQuery(name="ListaAtendimento.buscarAgendaUsuario", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.tecnico = :tecnico "
			+ "and la.tenant_id = :tenantId "),
	@NamedQuery(name="ListaAtendimento.buscarAtendimentosRole", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "and la.role = :role "
			+ "order by la.dataAgendamento"),
	@NamedQuery(name="ListaAtendimento.buscarAtendimentosTecnicos", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			//+ "and la.role in ('ADVOGADO', 'ASSISTENTE_SOCIAL', 'PSICOLOGO', 'ORIENTADOR_SOCIAL') "  /* cuidado */
			+ "order by la.dataAgendamento"),
	@NamedQuery(name="ListaAtendimento.buscarAtendimentosRecepcao", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "and la.dataAtendimento between :ini and :fim "
			+ "and la.codigoAuxiliar = :codigoAux "),	
	@NamedQuery(name="ListaAtendimento.buscarResumoAtendimentos", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.pessoa = :pessoa "
			+ "and la.tenant_id = :tenantId "),	
	
	@NamedQuery(name="ListaAtendimento.consultaFaltas", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.pessoa = :pessoa "
			+ "and la.tenant_id = :tenantId "),
	
	
	/*
	 * relatorios atendimentos	
	 */
	@NamedQuery(name="ListaAtendimento.buscarAtendimentosCodAux", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "and la.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO') "
			+ "order by la.codigoAuxiliar"),	
	@NamedQuery(name="ListaAtendimento.buscarAtendimentosCodAuxGrafico", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "and la.dataAtendimento between :ini and :fim "
			+ "and la.codigoAuxiliar not in ('ATENDIMENTO_RECEPCAO') "
			+ "order by la.codigoAuxiliar"),
	
	
	/*
	 * RelatorioAtendimentoFamilia
	 */
	@NamedQuery(name="ListaAtendimento.buscarAtendimentoFamilia", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "and la.pessoa.familia.prontuario = :prontuario "
			+ "order by la.dataAtendimento"),
	
	/*
	 * RelatorioAtendimentos CAdUnico
	 */
	@NamedQuery(name="ListaAtendimento.buscarAtendCadUnicoDataPeriodo", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "and la.dataAtendimento between :ini and :fim "
			+ "and la.codigoAuxiliar in ('CADASTRAMENTO_CADUNICO', 'CADASTRAMENTO_CADUNICO_BPC', 'ATUALIZACAO_CADUNICO', 'OUTROS_CADUNICO') "), /* cuidado */
	@NamedQuery(name="ListaAtendimento.buscarAtendidosCadUnico", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "and la.codigoAuxiliar in ('CADASTRAMENTO_CADUNICO', 'CADASTRAMENTO_CADUNICO_BPC', 'ATUALIZACAO_CADUNICO', 'OUTROS_CADUNICO') "), /* cuidado */
	@NamedQuery(name="ListaAtendimento.buscarAtendCadUnicoDataPeriodo2", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "and la.dataAtendimento between :ini and :fim "
			+ "and la.codigoAuxiliar in ('CADASTRAMENTO_CADUNICO', 'CADASTRAMENTO_CADUNICO_BPC', 'ATUALIZACAO_CADUNICO', 'OUTROS_CADUNICO') " /* cuidado */
			+ "order by la.codigoAuxiliar"),
	@NamedQuery(name="ListaAtendimento.buscarAtendidosCadUnico2", query="select la from ListaAtendimento la "
			+ "where la.statusAtendimento = :status "
			+ "and la.unidade = :unidade "
			+ "and la.tenant_id = :tenantId "
			+ "and la.codigoAuxiliar in ('CADASTRAMENTO_CADUNICO', 'CADASTRAMENTO_CADUNICO_BPC', 'ATUALIZACAO_CADUNICO', 'OUTROS_CADUNICO') " /* cuidado */
			+ "order by la.codigoAuxiliar"),
})
public class ListaAtendimento implements Serializable, Comparable<ListaAtendimento>{

	private static final long serialVersionUID = 145526938705551405L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;

	private Long tenant_id;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	@NotAudited
	private Date dataAgendamento;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	//@NotAudited
	private Date dataAtendimento; 						//@Index(name="idx_dataAtendimento") para buscas lazyAtendimento
	
	@Column(length = 512000,columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	private String resumoAtendimento;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	@NotFound( action = NotFoundAction.IGNORE )
	@NotAudited
	private Unidade unidade;
	
	@Enumerated(EnumType.STRING)
	@NotAudited
	private Role role;
	
	@Enumerated(EnumType.STRING)
	//@NotAudited
	private CodigoAuxiliarAtendimento codigoAuxiliar;	//@Index(name="idx_codigoAuxiliar") para buscas lazyAtendimento
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	@NotFound( action = NotFoundAction.IGNORE )
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Usuario tecnico;
	
	@ManyToOne
	@JoinColumn(name="codigo_agendador")
	@NotFound( action = NotFoundAction.IGNORE )
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Usuario agendador;
	
	@ManyToMany(fetch = FetchType.EAGER, 
			cascade = {CascadeType.PERSIST,	CascadeType.MERGE}	)
		@JoinTable(	name="TecnicoAtendimento", 
					joinColumns={@JoinColumn(name="codigo_atendimento")}, 
					inverseJoinColumns={@JoinColumn(name="codigo_tecnico")}	)
		@NotFound( action = NotFoundAction.IGNORE )
		//@NotAudited
	private Set<Usuario> tecnicos = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name="codigo_pessoa")
	@NotFound( action = NotFoundAction.IGNORE )
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private Pessoa pessoa;
	
	@Enumerated(EnumType.STRING)
	@NotAudited
	private StatusAtendimento statusAtendimento;		//@Index(name="idx_cstatusAtendimento") para buscas lazyAtendimento
	
	//private Date createdOn;  // Para auditoria
	
	@Override
	public int compareTo(ListaAtendimento atendimento) {
		//ListaAtendimento a = (ListaAtendimento) atendimento;
        //return this.pessoa.getNome().compareToIgnoreCase(a.pessoa.getNome()); // string
		
		if (this.pessoa.getCodigo() > atendimento.getPessoa().getCodigo()) {
			return 1;
		}
		if (this.pessoa.getCodigo() < atendimento.getPessoa().getCodigo()) {
			return -1;
		}
		return 0; 
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
