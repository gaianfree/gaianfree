package com.softarum.svsa.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.softarum.svsa.controller.LoginBean;

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
@Embeddable
public class Audit implements Serializable {
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = -3670549860268256037L;
	private LocalDateTime createdOn;
    private String createdBy;
    private LocalDateTime updatedOn;
    private String updatedBy;

    @Inject
	LoginBean loginBean;
    

	@PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
        createdBy = loginBean.getUserName();
    }
 
    @PreUpdate
    public void preUpdate() {
        updatedOn = LocalDateTime.now();
        updatedBy = loginBean.getUserName();
    }    
   
}