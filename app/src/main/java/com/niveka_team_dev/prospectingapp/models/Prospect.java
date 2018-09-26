package com.niveka_team_dev.prospectingapp.models;

import java.util.HashMap;
import java.util.Map;

public class Prospect {
    private long id;
    private String email;
    private String telephone;
    private String nom;
    private String localisation;
    private String impression;
    private String emailUser;

    public Prospect(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("nom",nom);
        map.put("telephone",telephone);
        map.put("localisation",localisation);
        map.put("impression",impression);
        map.put("email",email);
        map.put("emailUser",emailUser);

        return map;
    }
}
