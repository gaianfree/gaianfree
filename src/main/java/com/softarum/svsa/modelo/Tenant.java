package com.softarum.svsa.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import com.softarum.svsa.service.s3.AmazonS3Keys;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author murakamiadmin
 *
 */
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name="Tenant.buscarTodos", query="select t from Tenant t")
	
})
public class Tenant implements Serializable {

	private static final long serialVersionUID = -5526059262907035239L;
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@NotBlank(message="O nome do tenant/municipio é obrigatório.")
	private String tenant;	
	
	private String secretaria = null;
	
	private String s3Key = null;
	
	@Transient
	public String getUrlAnexo() {
		return AmazonS3Keys.URL_S3_BUCKET + getS3Key();
	}
}
