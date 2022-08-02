package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

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
	@NamedQuery(name="RmaCreas.buscarTodos", query="select rma from RmaCreas rma where rma.unidade = :unidade and rma.tenant_id = :tenantId"),
	@NamedQuery(name="RmaCreas.buscarRmaFechado", query="select rma from RmaCreas rma "
			+ "where rma.unidade = :unidade "
			+ "and rma.mesAnoReferencia = :mesAnoRef "
			+ "and rma.tenant_id = :tenantId"),
	@NamedQuery(name="RmaCreas.buscarRmasFechados", query="select rma.mesAnoReferencia from RmaCreas rma "
			+ "where rma.unidade = :unidade "
			+ "and rma.tenant_id = :tenantId")
})
public class RmaCreas implements Serializable {

	private static final long serialVersionUID = 82375949344894033L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;
	
	private Long tenant_id;
	
	@NotBlank(message="O mes e ano de referencia é obrigatório")
	private String mesAnoReferencia;
	
	private Integer bloco1a1 = 0;
	private Integer bloco1a2 = 0;
	private Integer bloco1b1 = 0;
	private Integer bloco1b2 = 0;
	private Integer bloco1b3 = 0;
	private Integer bloco1b4 = 0;
	private Integer bloco1b5 = 0;
	private Integer bloco1b6 = 0;
	private Integer bloco1b7 = 0;
	private Integer bloco1c1 = 0;
	private Integer bloco1c2 = 0;
	private Integer bloco1c3 = 0;
	private Integer bloco1c4 = 0;
	private Integer bloco1c5 = 0;
	private Integer bloco1c6 = 0;
	private Integer bloco1d1 = 0;
	private Integer bloco1d2 = 0;
	private Integer bloco1e1 = 0;
	private Integer bloco1e2 = 0;
	private Integer bloco1f1 = 0;
	private Integer bloco1g1 = 0;
	private Integer bloco1h1 = 0;
	private Integer bloco1i1 = 0;
	
	private Integer bloco2m1 = 0;
	private Integer bloco2m2 = 0;
	private Integer bloco2m3 = 0;
	private Integer bloco2m4 = 0;
	
	private Integer bloco3j1 = 0;
	private Integer bloco3j2 = 0;
	private Integer bloco3j3 = 0;
	private Integer bloco3j4 = 0;
	private Integer bloco3j5 = 0;
	private Integer bloco3j6 = 0;

	
	/* Vitimas */
	
	//b6
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_b6masc")
	private Vitima b6vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_b6fem")
	private Vitima b6vitimaFem;
	
	//c1 a c6
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c1masc")
	private Vitima c1vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c1fem")
	private Vitima c1vitimaFem;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c2masc")
	private Vitima c2vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c2fem")
	private Vitima c2vitimaFem;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c3masc")
	private Vitima c3vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c3fem")
	private Vitima c3vitimaFem;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c4masc")
	private Vitima c4vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c4fem")
	private Vitima c4vitimaFem;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c5masc")
	private Vitima c5vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c5fem")
	private Vitima c5vitimaFem;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c6masc")
	private Vitima c6vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_c6fem")
	private Vitima c6vitimaFem;
	
	//d1 a d2
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_d1masc")
	private Vitima d1vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_d1fem")
	private Vitima d1vitimaFem;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_d2masc")
	private Vitima d2vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_d2fem")
	private Vitima d2vitimaFem;
	
	//e1 a e2
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_e1masc")
	private Vitima e1vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_e1fem")
	private Vitima e1vitimaFem;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_e2masc")
	private Vitima e2vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_e2fem")
	private Vitima e2vitimaFem;
	
	//f1, g1, i1	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_f1fem")
	private Vitima f1vitimaFem;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_g1masc")
	private Vitima g1vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_g1fem")
	private Vitima g1vitimaFem;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_i1masc")
	private Vitima i1vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_i1fem")
	private Vitima i1vitimaFem;
	
	//j4 a j6
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_j4masc")
	private Vitima j4vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_j4fem")
	private Vitima j4vitimaFem;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_j5masc")
	private Vitima j5vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_j5fem")
	private Vitima j5vitimaFem;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_j6masc")
	private Vitima j6vitimaMasc;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="codigo_j6fem")
	private Vitima j6vitimaFem;
	
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
	
	public static RmaCreas createRmaCreas() {
		
		RmaCreas rma = new RmaCreas();
		
		rma.setB6vitimaFem(new Vitima());
		rma.setB6vitimaMasc(new Vitima());
		
		rma.setC1vitimaFem(new Vitima());
		rma.setC1vitimaMasc(new Vitima());
		rma.setC2vitimaFem(new Vitima());
		rma.setC2vitimaMasc(new Vitima());
		rma.setC3vitimaFem(new Vitima());
		rma.setC3vitimaMasc(new Vitima());
		rma.setC4vitimaFem(new Vitima());
		rma.setC4vitimaMasc(new Vitima());
		rma.setC5vitimaFem(new Vitima());
		rma.setC5vitimaMasc(new Vitima());
		rma.setC6vitimaFem(new Vitima());
		rma.setC6vitimaMasc(new Vitima());
		
		rma.setD1vitimaFem(new Vitima());
		rma.setD1vitimaMasc(new Vitima());
		rma.setD2vitimaFem(new Vitima());
		rma.setD2vitimaMasc(new Vitima());
		
		rma.setE1vitimaFem(new Vitima());
		rma.setE1vitimaMasc(new Vitima());
		rma.setE2vitimaFem(new Vitima());
		rma.setE2vitimaMasc(new Vitima());
		
		rma.setF1vitimaFem(new Vitima());
		
		rma.setG1vitimaFem(new Vitima());
		rma.setG1vitimaMasc(new Vitima());
		
		rma.setI1vitimaFem(new Vitima());
		rma.setI1vitimaMasc(new Vitima());
		
		rma.setJ4vitimaFem(new Vitima());
		rma.setJ4vitimaMasc(new Vitima());
		rma.setJ5vitimaFem(new Vitima());
		rma.setJ5vitimaMasc(new Vitima());
		rma.setJ6vitimaFem(new Vitima());
		rma.setJ6vitimaMasc(new Vitima());	
	
		return rma;
	}
}
