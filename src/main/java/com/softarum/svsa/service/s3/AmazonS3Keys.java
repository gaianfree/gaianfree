package com.softarum.svsa.service.s3;

import com.amazonaws.regions.Regions;

public final class AmazonS3Keys {
	
	public static final String NOME_BUCKET = "svsaweb";
	public static final String PROFILE_CREDENTIALS = "desenvolvedor"; // role do Amazon IAM
	public static final Regions REGION = Regions.US_EAST_1; 
	
	public static final String URL_S3_BUCKET = "https://svsaweb.s3.amazonaws.com/";
	
	public static final String IMAGENS = "svsa/imagens/";

}
