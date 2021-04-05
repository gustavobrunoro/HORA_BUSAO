package com.gustavobrunoro.horabusao.Model;

import java.io.Serializable;

public class Usuario implements Serializable {
        private String Nome;
        private String Email;
        private String FacebookID;
        private String FacebookFoto;

    public Usuario () {
    }

    public String getNome () {
        return Nome;
    }

    public void setNome (String nome) {
        Nome = nome;
    }

    public String getEmail () {
        return Email;
    }

    public void setEmail (String email) {
        Email = email;
    }

    public String getFacebookID () {
        return FacebookID;
    }

    public void setFacebookID (String facebookID) {
        FacebookID = facebookID;
    }

    public String getFacebookFoto () {
        return FacebookFoto;
    }

    public void setFacebookFoto (String facebookFoto) {
        FacebookFoto = facebookFoto;
    }
}
