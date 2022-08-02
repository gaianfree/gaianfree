package com.softarum.svsa.service.s3;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;

import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.softarum.svsa.modelo.Oficio;
import com.softarum.svsa.modelo.Prontuario;
import com.softarum.svsa.util.NegocioException;

/**
 * @author murakamiadmin
 * 
 * Classe que utiliza o Amazon S3 para manilulação de arquivos
 *
 */
public class AmazonS3Service implements Serializable {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(AmazonS3Service.class);
		
	/*
	 * ----------------------------------------------------------------
	 * Recebimento de Ofício
	 * Oficios Anexados ao recebimento
	 * 
	 * Salva o arquivo (uploaded) no bucket do Amazon S3 
	 * ----------------------------------------------------------------
     */
    public Oficio gravaAnexoOficio(Oficio oficio, Part uploadedFile) throws IOException, NegocioException{		
    	    		
    		/*
    		 *  Define caminho e nome do pdf para armazenamento na pasta do município
    		 *  svsa/<municipio>/oficios/<nomeArquivo>
    		 *  onde:
    		 *  	 nomeArquivo = <codigo_unidade>-<codigo_oficio>-<ano>
    		 */
    		LocalDate localDate = LocalDate.now();
    		String nomeArquivo = "svsa/" + oficio.getUnidade().getEndereco().getMunicipio() + "/oficios/" 
				    			+ oficio.getUnidade().getCodigo()
				    			+ "-" + oficio.getCodigo().toString()
				    			+ "-" + localDate.getYear()
				    			+ ".pdf";    			
			
			// guarda a chave para acesso ao arquivo no S3 em caixa baixa
			oficio.setS3Key(uploadObject(uploadedFile, nomeArquivo));
			
			return oficio;    	
	}
	public InputStream leAnexoOficio(Oficio oficio) throws NegocioException {
		
    		log.info("Lendo anexo de oficio... " + oficio.getNrOficio()); 		
			
			return downloadObject(oficio.getS3Key());   	
	}
	
	
	
	/*
	 * ----------------------------------------------------------------
	 * CapaProntuario
	 * Folha de Rosto CadÚnico Anexado ao Prontuario
	 * 
	 * Salva o arquivo (uploaded) no bucket do Amazon S3
	 * ---------------------------------------------------------------- 
     */
	public Prontuario gravaPdfCadUnico(Prontuario prontuario, Part uploadedFile) throws NegocioException{		
    		
    		log.info("Gravando pdf cadUnico... ");
		    		
    		/*  Define caminho e nome do pdf para armazenamento na pasta do município */
    		String nomeArquivo = "svsa/" 
    							+ prontuario.getUnidade().getEndereco().getMunicipio() 
    							+ "/cadunico/" 
    							+ prontuario.getCodigo() + ".pdf";	
    	
    		// guarda a chave para acesso ao arquivo no S3 em caixa baixa
			prontuario.setS3Key(uploadObject(uploadedFile, nomeArquivo));
			
			return prontuario;    		
	}	
	public InputStream lePdfCadUnico(Prontuario prontuario) throws NegocioException {
	
    		log.info("Lendo pdf cadUnico... " + prontuario.getCodigo());
    		
			return downloadObject(prontuario.getS3Key());	
	}
	
	
	
	/* retorna a chave S3Key para acesso ao objeto */
	public String gravaImagem(Part uploadedFile, String municipio) throws NegocioException {

    		log.info("Gravando imagem... ");    			
    		
    		/*  Define caminho e nome do arquivo para armazenamento na pasta de imagens  */
    		String nomeArquivo = AmazonS3Keys.IMAGENS + municipio.toLowerCase()  + "." + uploadedFile.getContentType();	
    						
			return uploadObject(uploadedFile, nomeArquivo);    	   	
	}
	public InputStream leImagem(String s3Key) throws NegocioException {

    		log.info("Lendo imagem... " + s3Key);    		
    						
			return downloadObject(s3Key);    	   	
	}
	
	/* retorna a chave S3Key para acesso ao objeto */
	private String uploadObject(Part uploadedFile, String nomeObjeto) throws NegocioException {
		
		try {		
    		
    		log.info("Gravando no bucket: " + nomeObjeto);
    		
    		AmazonS3 s3Client =  getAmazonS3();    		
    		
			InputStream conteudoArquivo = uploadedFile.getInputStream();
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(uploadedFile.getContentType());
			metadata.setContentLength(uploadedFile.getSize());
			PutObjectRequest por = new PutObjectRequest(AmazonS3Keys.NOME_BUCKET, nomeObjeto.toLowerCase(), conteudoArquivo, metadata);
			PutObjectResult result = s3Client.putObject(por.withCannedAcl(CannedAccessControlList.PublicRead));
			
			log.info("Arquivo salvo em: " + nomeObjeto.toLowerCase() + ". Resultado = " + result);		
					
			return nomeObjeto.toLowerCase();
    		
    	} catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            throw new NegocioException("O arquivo foi enviado, porém não foi possível gravá-lo.");
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            throw new NegocioException("Upload: Problema com o servidor de arquivos. Contacte o administrador.");
        } catch (IOException e) {
        	throw new NegocioException("O arquivo pode estar corrompido.");			
		}    	
	}
	
	/* retorna o imputstream do objeto */
	public InputStream downloadObject(String nomeArquivo) throws NegocioException {		
		
		S3Object s3Object = null;
		
		try {    				
    		
    		log.info("Lendo no bucket... " + nomeArquivo);
    		
    		AmazonS3 s3Client =  getAmazonS3();
    		
    		if(nomeArquivo != null) {    			
    			s3Object = s3Client.getObject(new GetObjectRequest(AmazonS3Keys.NOME_BUCKET, nomeArquivo));  
    		}   		
    		
			log.info("Arquivo lido de: " + nomeArquivo.toLowerCase());
			
			return (InputStream) s3Object.getObjectContent();    		
		
    	} catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            throw new NegocioException("O arquivo não foi encontrado ou não foi anexado.");
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            throw new NegocioException("Download: Problema com o servidor de arquivos. Contacte o administrador.");
        
        } finally {
	        // To ensure that the network connection doesn't remain open, close any open input streams.
	        if (s3Object != null) {
	        	try {
	        		s3Object.close();
	        	}catch(IOException io) {
	                throw new NegocioException("Problema para fechar o arquivo. Contacte o administrador.");
	        	}
	        }	       
        }
	}
	
	private AmazonS3 getAmazonS3() {
		
		/*
		 * desenvolvedor
		 */
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withCredentials(new ProfileCredentialsProvider(AmazonS3Keys.PROFILE_CREDENTIALS))
                .withRegion(AmazonS3Keys.REGION)
                .build(); 

		
		/*
		 *  default		 
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion(AmazonS3Keys.REGION)
				.build();
		 */
		return s3Client;
	}
}