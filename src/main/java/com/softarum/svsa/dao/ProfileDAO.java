package com.softarum.svsa.dao;

import com.softarum.svsa.modelo.Profile;
import org.hibernate.Session;

import lombok.Setter;

@Setter
public class ProfileDAO {
    private Session session;

    public void save(Profile profile){
        this.session.saveOrUpdate(profile);//salva o objeto no banco de dados caso não possua um valor no id
    }
}
