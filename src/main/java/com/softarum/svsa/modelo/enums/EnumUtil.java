package com.softarum.svsa.modelo.enums;

import java.util.Arrays;
import java.util.List;

public class EnumUtil {
	
	private static final List<CodigoAuxiliarAtendimento> CODIGOS_ATEND_INDIVIDUALIZADO;
	private static final List<CodigoAuxiliarAtendimento> CODIGOS_ATEND_CADUNICO;
	private static final List<CodigoAuxiliarAtendimento> CODIGOS_ABORDAGEM;
	private static final List<CodigoAuxiliarAtendimento> CODIGOS_ATEND_ABORDAGEM;
	private static final List<CodigoAuxiliarAtendimento> CODIGOS_ATEND_COLETIVO;
	
	private static final List<Parentesco> CODIGOS_PARENTESCO_MEMBROS;
	

	static {
		CODIGOS_ATEND_INDIVIDUALIZADO = Arrays.asList(
				CodigoAuxiliarAtendimento.ATENDIMENTO_SOCIAL,
				CodigoAuxiliarAtendimento.ATENDIMENTO_PSICOLOGICO,
				CodigoAuxiliarAtendimento.ATENDIMENTO_JURIDICO,
				CodigoAuxiliarAtendimento.ATENDIMENTO_PSICOSSOCIAL,					
				CodigoAuxiliarAtendimento.ATENDIMENTO_PSICOJURIDICO,
				CodigoAuxiliarAtendimento.ATENDIMENTO_JURIDICO_SOCIAL,	
				CodigoAuxiliarAtendimento.ATENDIMENTO_PSICO_JURIDICO_SOCIAL,
				//CodigoAuxiliarAtendimento.ATENDIMENTO_RECEPCAO,					
				CodigoAuxiliarAtendimento.CADASTRAMENTO_CADUNICO,
				CodigoAuxiliarAtendimento.CADASTRAMENTO_CADUNICO_BPC,
				CodigoAuxiliarAtendimento.ATUALIZACAO_CADUNICO,
				CodigoAuxiliarAtendimento.VISITA_DOMICILIAR,
				CodigoAuxiliarAtendimento.ACOMPANHAMENTO_MSE,					
				CodigoAuxiliarAtendimento.AUXILIO_NATALIDADE,
				CodigoAuxiliarAtendimento.AUXILIO_FUNERAL,
				CodigoAuxiliarAtendimento.OUTROS_BENEFICIOS,
				CodigoAuxiliarAtendimento.TRANSPORTE_MUNICIPAL_PESSOA_DEFICIENCIA,
				CodigoAuxiliarAtendimento.VULNERABILIDADE_TEMPORARIA_CESTA_BASICA,
				CodigoAuxiliarAtendimento.OUTROS);
		
		CODIGOS_ATEND_CADUNICO = Arrays.asList(
				CodigoAuxiliarAtendimento.CADASTRAMENTO_CADUNICO,
				CodigoAuxiliarAtendimento.CADASTRAMENTO_CADUNICO_BPC,
				CodigoAuxiliarAtendimento.ATUALIZACAO_CADUNICO,
				CodigoAuxiliarAtendimento.OUTROS_CADUNICO,
				CodigoAuxiliarAtendimento.VISITA_DOMICILIAR);
		
		CODIGOS_ATEND_ABORDAGEM = Arrays.asList(
				CodigoAuxiliarAtendimento.ATENDIMENTO_SOCIAL,
				CodigoAuxiliarAtendimento.ATENDIMENTO_PSICOLOGICO,
				CodigoAuxiliarAtendimento.ATENDIMENTO_JURIDICO,
				CodigoAuxiliarAtendimento.ATENDIMENTO_PSICOSSOCIAL,					
				CodigoAuxiliarAtendimento.ATENDIMENTO_PSICOJURIDICO,
				CodigoAuxiliarAtendimento.ATENDIMENTO_JURIDICO_SOCIAL,	
				CodigoAuxiliarAtendimento.ATENDIMENTO_PSICO_JURIDICO_SOCIAL,
				//CodigoAuxiliarAtendimento.ATENDIMENTO_RECEPCAO,					
				CodigoAuxiliarAtendimento.CADASTRAMENTO_CADUNICO,
				CodigoAuxiliarAtendimento.CADASTRAMENTO_CADUNICO_BPC,
				CodigoAuxiliarAtendimento.ATUALIZACAO_CADUNICO,
				CodigoAuxiliarAtendimento.VISITA_DOMICILIAR,
				CodigoAuxiliarAtendimento.ACOMPANHAMENTO_MSE,					
				CodigoAuxiliarAtendimento.AUXILIO_NATALIDADE,
				CodigoAuxiliarAtendimento.AUXILIO_FUNERAL,
				CodigoAuxiliarAtendimento.OUTROS_BENEFICIOS,
				CodigoAuxiliarAtendimento.TRANSPORTE_MUNICIPAL_PESSOA_DEFICIENCIA,
				CodigoAuxiliarAtendimento.VULNERABILIDADE_TEMPORARIA_CESTA_BASICA,
				CodigoAuxiliarAtendimento.OUTROS,
				CodigoAuxiliarAtendimento.TRABALHO_INFANTIL,
				CodigoAuxiliarAtendimento.EXPLORACAO_SEXUAL,
				CodigoAuxiliarAtendimento.USUARIO_CRACK_OU_DROGRAS_ILICITAS,
				CodigoAuxiliarAtendimento.USUARIO_ALCOOL,
				CodigoAuxiliarAtendimento.MIGRANTE,
				CodigoAuxiliarAtendimento.DOENCA_OU_TRANSTORNO_MENTAL);
		
		CODIGOS_ABORDAGEM = Arrays.asList(
				CodigoAuxiliarAtendimento.TRABALHO_INFANTIL,
				CodigoAuxiliarAtendimento.EXPLORACAO_SEXUAL,
				CodigoAuxiliarAtendimento.USUARIO_CRACK_OU_DROGRAS_ILICITAS,
				CodigoAuxiliarAtendimento.USUARIO_ALCOOL,
				CodigoAuxiliarAtendimento.MIGRANTE,
				CodigoAuxiliarAtendimento.DOENCA_OU_TRANSTORNO_MENTAL);
		
		CODIGOS_ATEND_COLETIVO = Arrays.asList(
				CodigoAuxiliarAtendimento.ATIVIDADE_COLETIVA_DE_CARATER_CONTINUADO,
				CodigoAuxiliarAtendimento.ATIVIDADE_COLETIVA_DE_CARATER_NAO_CONTINUADO,
				CodigoAuxiliarAtendimento.VULNERABILIDADE_TEMP_CESTA_BASICA_FAMILIA,
				CodigoAuxiliarAtendimento.REUNIAO_DE_REDE,
				CodigoAuxiliarAtendimento.REUNIAO_DE_FAMILIA,
				CodigoAuxiliarAtendimento.GRUPO_MSE,
				CodigoAuxiliarAtendimento.OUTROS_COLETIVO);
		
		CODIGOS_PARENTESCO_MEMBROS = Arrays.asList(
				Parentesco.RESPONSAVEL_FAMILIAR,
				Parentesco.CONJUGE_COMPANHEIRO,
				Parentesco.ENTEADO,
				Parentesco.FILHO,
				Parentesco.GENRO_NORA,
				Parentesco.IRMAO_IRMA,
				Parentesco.NAO_PARENTE,
				Parentesco.NETO_BISNETO,
				Parentesco.OUTRO_PARENTE,
				Parentesco.PAI_MAE,
				Parentesco.SOGRO_SOGRA);
		
	}


	public static List<CodigoAuxiliarAtendimento> getCodigosAtendIndividualizado() {
		return CODIGOS_ATEND_INDIVIDUALIZADO;
	}

	public static List<CodigoAuxiliarAtendimento> getCodigosAtendCadunico() {
		return CODIGOS_ATEND_CADUNICO;
	}
	
	public static List<CodigoAuxiliarAtendimento> getCodigosAtendColetivo() {
		return CODIGOS_ATEND_COLETIVO;
	}
	
	public static List<CodigoAuxiliarAtendimento> getCodigosAtendAbordagem() {
		return CODIGOS_ATEND_ABORDAGEM;
	}
	
	public static List<CodigoAuxiliarAtendimento> getCodigosAbordagem() {
		return CODIGOS_ABORDAGEM;
	}
	
	public static List<Parentesco> getCodigosParentescoMembro() {
		return CODIGOS_PARENTESCO_MEMBROS;
	}
}
