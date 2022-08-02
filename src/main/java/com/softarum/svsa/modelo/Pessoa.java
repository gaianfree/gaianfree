package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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

import com.softarum.svsa.modelo.enums.CorRaca;
import com.softarum.svsa.modelo.enums.Genero;
import com.softarum.svsa.modelo.enums.Parentesco;
import com.softarum.svsa.modelo.enums.Sexo;
import com.softarum.svsa.modelo.enums.Status;
import com.softarum.svsa.modelo.enums.TipoPcD;
import com.softarum.svsa.modelo.enums.Uf;
import com.softarum.svsa.util.CalculoUtil;

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
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TIPO_PESSOA", discriminatorType=DiscriminatorType.STRING)
@NamedQueries({
	@NamedQuery(name="Pessoa.buscarTodos", query="select p from Pessoa p where p.excluida = :exc and p.tenant_id = :tenantId"),
	@NamedQuery(name="Pessoa.buscarPessoas", query="select p from Pessoa p "
			+ "where p.familia.prontuario = :prontuario "
			+ "and p.tenant_id = :tenantId "
			+ "and p.excluida = :exc")
})
public class Pessoa implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 9109362274647458688L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	private String nome;				//@Index(name="idx_nome") - para buscas com like% lazyPessoa
	
	private String nomeSocial;
	
	private String nomeMae;	
	
	private String rg;
	
	private String orgaoEmissor;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmissao;	
	
	private String cpf;
	
	private String nis;	
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;
	
	private String ufNascimento;
	
	private String municipioNascimento;
	
	private String observacao;
	
	@Temporal(TemporalType.DATE)
	private Calendar dataRegistroComposicaoFamiliar;
	
	private Boolean excluida = false;
	
	@Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;
	
	private String telefone;
	
	private String email;
	
	private String mse;   // Transient
	// enums
	//private Uf uf;
	@Enumerated(EnumType.STRING)
	private Uf ufEmissao;
	
	@Enumerated(EnumType.STRING)
	private CorRaca corRaca;
	
	@Enumerated(EnumType.STRING)
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	private Genero identidadeGenero;
	
	@Enumerated(EnumType.STRING)
	private Parentesco parentescoPessoaReferencia;
	
	@Enumerated(EnumType.STRING)
	private TipoPcD tipoPcD;
	
	// relacionamentos

	
	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="codigo_familia")
	private Familia familia;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_tipo_documento")
	private TipoDocumento tipoDocumento;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_forma_ingresso")
	private FormaIngresso formaIngresso;
	
	@ManyToOne
	@JoinColumn(name="pais_origem")
	private Pais paisOrigem;
	
	
	@Transient
	public int getIdade() {
		int idade = 0;
		if(this.getDataNascimento() != null)
			idade = CalculoUtil.calcularIdade(this.getDataNascimento());
		
		return idade;			
	}
	@Transient
	public String getMse() {
		return mse;
	}
	public void setMse(String mse) {
		this.mse = mse;
	}
	
	@Override
	public Pessoa clone() throws CloneNotSupportedException {
		return (Pessoa) super.clone();
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
		else {
			atzObjAssocidados();
		}		
	}
	// Atualiza as datas de modificação dos objetos associados também
	public void atzObjAssocidados() {
		if(this.getFamilia() != null) {	
			log.info("datas Pessoa, Familia e Prontuario atualizados.");
			this.getFamilia().setDataModificacao(this.getDataModificacao());
			if(this.getFamilia().getProntuario() != null) {
				this.getFamilia().getProntuario().setDataModificacao(this.getDataModificacao());	
			}
		}
	}	
}