package com.softarum.svsa.modelo.to.rma;

import java.io.Serializable;
import java.util.Date;

import com.softarum.svsa.util.CalculoUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * Classe para geração do relatorio RMA Creas
 * @author murakamiadmin
 *
 */
@Log4j
@Getter
@Setter
public class PessoaTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Date dataNascimento;
	
	public PessoaTO(Date dataNascimento) {
		this.setDataNascimento(dataNascimento);
	}
	
	
	public int getIdade() {
		int idade = 0;
		if(this.getDataNascimento() != null)
			idade = CalculoUtil.calcularIdade(this.getDataNascimento());
		else
			log.info("getDataNascimento: " + this.getDataNascimento());
		return idade;			
	}
}
