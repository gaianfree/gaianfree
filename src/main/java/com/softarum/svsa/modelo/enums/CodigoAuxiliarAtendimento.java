package com.softarum.svsa.modelo.enums;

/**
 * @author murakamiadmin
 *
 */
public enum CodigoAuxiliarAtendimento {
	
	/* ao alterar algum  código verificar aderência as consultas em: 
	 * CapaProntuarioDAO metodo qdeProntuariosNovos() 
	 * AgendamentoIndividualDAO dashboardQdesCodAuxIndiv()
	 * RmaDAO buscarC1()  
	 * 
	 * se alterar esta lista (Beneficio Eventual) verificar o metodo verificaBeneficio do RealizarAtendimento
	 * 
	 */
	
	/* ---------------------------------*/
	/* para atendimentos individualizados  (16) */
	/* ---------------------------------*/	
	ATENDIMENTO_SOCIAL,	
	ATENDIMENTO_PSICOLOGICO,
	ATENDIMENTO_JURIDICO,
	ATENDIMENTO_PSICOSSOCIAL,
	ATENDIMENTO_PSICOJURIDICO,
	ATENDIMENTO_JURIDICO_SOCIAL,
	ATENDIMENTO_PSICO_JURIDICO_SOCIAL,
	ATENDIMENTO_RECEPCAO,	
	VISITA_DOMICILIAR,
	ACOMPANHAMENTO_MSE,		
	AUXILIO_NATALIDADE,/* ----- benefícios lançados automaticamente em Codição Familiar (3) */
	AUXILIO_FUNERAL,
	OUTROS_BENEFICIOS,
	TRANSPORTE_MUNICIPAL_PESSOA_DEFICIENCIA, /* ----- especificos  (3)*/
	VULNERABILIDADE_TEMPORARIA_CESTA_BASICA,
	DEMANDA_NAO_OFERTADA,
	OUTROS,
	/* ---------------------------------------*/
	/* para atendimentos individualizados CadUnico  (4)*/
	/* ---------------------------------------*/
	CADASTRAMENTO_CADUNICO,
	CADASTRAMENTO_CADUNICO_BPC,
	ATUALIZACAO_CADUNICO,
	OUTROS_CADUNICO,	
	/* ---------------------------------------*/
	/* para atendimentos individualizados POP  (5)*/
	/* ---------------------------------------*/
	TRABALHO_INFANTIL, 
	EXPLORACAO_SEXUAL, 
	USUARIO_CRACK_OU_DROGRAS_ILICITAS, 
	USUARIO_ALCOOL, MIGRANTE,
	DOENCA_OU_TRANSTORNO_MENTAL,
	/* ---------------------------------*/
	/* para atendimentos coletivos  (7) */
	/* ---------------------------------*/
	VULNERABILIDADE_TEMP_CESTA_BASICA_FAMILIA,
	ATIVIDADE_COLETIVA_DE_CARATER_CONTINUADO,
	ATIVIDADE_COLETIVA_DE_CARATER_NAO_CONTINUADO,
	GRUPO_MSE,
	REUNIAO_DE_REDE,
	REUNIAO_DE_FAMILIA,
	OUTROS_COLETIVO
}
