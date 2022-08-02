package com.softarum.svsa.modelo.to;

import java.io.Serializable;
import java.util.Date;

import com.softarum.svsa.modelo.enums.Status;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gabriel
 *
 */
@Getter
@Setter
public class PessoaDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long codigo = 0L;
	private Long codigoProntuario = 0L;
	private String prontuarioFisico = "";
	private String nome = "";
	private String nomeSocial = "";
	private Date dataNascimento = null;
	private String unidade = "";
	private Status statusPessoa = null;
	private Status statusProntuario = null;
	private String nomeMae = "";
	
	public PessoaDTO() {}
	
	// selecionaPessoa
	public PessoaDTO(Long codigo, Long codigoProntuario, String prontuarioFisico, String nome, String nomeSocial, Date dataNascimento, 
			String unidade, Status statusPessoa, Status statusProntuario) {
		
		this.codigo = codigo;
		this.codigoProntuario = codigoProntuario;
		this.prontuarioFisico = prontuarioFisico;
		this.nome = nome;
		this.nomeSocial = nomeSocial;
		this.dataNascimento = dataNascimento;
		this.unidade = unidade;
		this.statusPessoa = statusPessoa;
		this.statusProntuario = statusProntuario;
	}
	
	// selecionaPessoaReferencia
	public PessoaDTO(Long codigo, Long codigoProntuario, String prontuarioFisico, String nome, String nomeSocial, Date dataNascimento, 
			String unidade, Status statusPessoa, Status statusProntuario, String nomeMae) {
		
		this.codigo = codigo;
		this.codigoProntuario = codigoProntuario;
		this.prontuarioFisico = prontuarioFisico;
		this.nome = nome;
		this.nomeSocial = nomeSocial;
		this.dataNascimento = dataNascimento;
		this.unidade = unidade;
		this.statusPessoa = statusPessoa;
		this.statusProntuario = statusProntuario;
		this.nomeMae = nomeMae;
	}

	
}
