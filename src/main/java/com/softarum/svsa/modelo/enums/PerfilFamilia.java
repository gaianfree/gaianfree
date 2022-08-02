package com.softarum.svsa.modelo.enums;

/**
 * @author murakamiadmin
 *
 */
public enum PerfilFamilia {
	
	

	SITUACAO_EXTREMA_POBREZA("Situação de extrema pobreza"),
	BENEFICIARIA_BOLSA_FAMILIA("Beneficiária do bolsa família"),
	BENEFICIARIA_BOLSA_FAMILIA_EM_DESCUMPRIMENTO_COND("Beneficiária do bolsa família, em descumprimento de condicionalidades"),
	BENEFICIARIA_BPC("Beneficiária do BPC"),
	COM_CRIANCA_ADOL_EM_TRABALHO_INFANTIL("Com crianças ou adolescentes em situação de trabalho infantil"),
	COM_CRIANCA_ADOL_GRAVIDA("Com crianças ou adolescentes grávidas"),
	COM_CRIANCA_ADOL_EM_SV_ACOLHIMENTO("Com crianças ou adolescentes em serviço de acolhimento"),
	// CREAS
	SITUACAO_VIOLENCIA_PSICOATIVAS("Com situação de violência associada a abusivo de substâncias psicoativas"),
	ADOLECENTE_MEDIDAS_SOCIOEDUCATIVAS("Com adolescente em cumprimento de Medidas Socioeducativas em meio aberto"),	
	//
	OUTROS("Outros"),
	NAO_CONTEMPLADAS_PTR("Atende critérios dos PTR mas não foi contemplada"),
	EM_SITUACAO_VULNERABILIDADE("Em situação de vulnerabilidade"),
	COM_DEFICIENTES_OU_IDOSOS("Com deficientes ou idosos");
	
    
	private String descricao;

    public String getDescricao() {
        return descricao;
    }

    private PerfilFamilia(String descricao) {
        this.descricao = descricao;
    }

}
