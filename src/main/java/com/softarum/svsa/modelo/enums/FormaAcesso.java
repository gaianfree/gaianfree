package com.softarum.svsa.modelo.enums;

/**
 * @author murakamiadmin
 *
 */
public enum FormaAcesso {
	
	// Está sendo gravado no banco com código sequencial, portanto não alterar a ordem das formas. 
	// para adicionar novas formas continue a sequencia.
	
    DE("DEMANDA ESPONTÂNEA"),
    BEU("BUSCA ATIVA PELA EQUIPE DA UNIDADE"),
    EPSB("ENCAMINHAMENTO POR UNIDADES DE PROTEÇÃO SOCIAL BÁSICA"),
    EPSE("ENCAMINHAMENTO POR UNIDADES DE PROTEÇÃO SOCIAL ESPECIAL"),
    EAS("ENCAMINHAMENTO PELA ÁREA DE SAÚDE"),
    EAE("ENCAMINHAMENTO PELA ÁREA DE EDUCAÇÃO"),
    EAH("ENCAMINHAMENTO PELA ÁREA DA HABITAÇÃO"),
    EOPS("ENCAMINHAMENTO POR OUTRAS POLÍTICAS SETORIAIS"),
    ECT("ENCAMINHAMENTO PELO CONSELHO TUTELAR"),
    EPJ("ENCAMINHAMENTO PELO PODER JUDICIÁRIO"),
    EDP("ENCAMINHAMENTO PELA DEFENSORIA PÚBLICA"),
    EMP("ENCAMINHAMENTO PELO MINISTÉRIO PÚBLICO"),
    ED("ENCAMINHAMENTO POR DELEGACIAS"),
    EFC("ENCAMINHAMENTO PELA FUNDAÇÃO CASA"),    
    ESP("ENCAMINHAMENTO PELO SISTEMA PRISIONAL"),
    VD("VIOLAÇÃO DE DIREITOS"),
    OE("OUTROS ENCAMINHAMENTOS");

    private String descricao;

    FormaAcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
