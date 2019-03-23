package com.niveka_team_dev.prospectingapp.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

public class Entreprise implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;
    private String designation;
    private String logo;
    private String range_utilisateur;
    private int nombre_utilisteur;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public Entreprise() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRange_utilisateur() {
        return range_utilisateur;
    }

    public void setRange_utilisateur(String range_utilisateur) {
        this.range_utilisateur = range_utilisateur;
    }

    public int getNombre_utilisteur() {
        return nombre_utilisteur;
    }

    public void setNombre_utilisteur(int nombre_utilisteur) {
        this.nombre_utilisteur = nombre_utilisteur;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "Entreprise{" +
                "id='" + id + '\'' +
                ", designation='" + designation + '\'' +
                ", logo='" + logo + '\'' +
                ", range_utilisateur='" + range_utilisateur + '\'' +
                ", nombre_utilisteur=" + nombre_utilisteur +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                '}';
    }

    public Map<String, Object> toMap(){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this,Map.class);
    }

    public JSONObject toJson(){
        return new JSONObject(this.toMap());
    }

}
