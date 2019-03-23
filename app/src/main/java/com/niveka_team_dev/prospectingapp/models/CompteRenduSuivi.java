package com.niveka_team_dev.prospectingapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompteRenduSuivi implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String contenu;
    private String dateProchaineRdv;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private String suiviId;
    private String prospectId;
    private Suivi suivi;
    private Prospect prospect;

    public CompteRenduSuivi() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDateProchaineRdv() {
        return dateProchaineRdv;
    }

    public void setDateProchaineRdv(String dateProchaineRdv) {
        this.dateProchaineRdv = dateProchaineRdv;
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

    public String getSuiviId() {
        return suiviId;
    }

    public void setSuiviId(String suiviId) {
        this.suiviId = suiviId;
    }

    public String getProspectId() {
        return prospectId;
    }

    public void setProspectId(String prospectId) {
        this.prospectId = prospectId;
    }

    public Suivi getSuivi() {
        return suivi;
    }

    public void setSuivi(Suivi suivi) {
        this.suivi = suivi;
    }

    public Prospect getProspect() {
        return prospect;
    }

    public void setProspect(Prospect prospect) {
        this.prospect = prospect;
    }

    @Override
    public String toString() {
        return "CompteRenduSuivi{" +
                "id='" + id + '\'' +
                ", contenu='" + contenu + '\'' +
                ", dateProchaineRdv='" + dateProchaineRdv + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                ", suiviId='" + suiviId + '\'' +
                ", prospectId='" + prospectId + '\'' +
                ", suivi=" + suivi +
                ", prospect=" + prospect +
                '}';
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        map = mapper.convertValue(this,Map.class);
        return map;
    }

    public JSONObject toJSON(){
        return new JSONObject(this.toMap());
    }
}
