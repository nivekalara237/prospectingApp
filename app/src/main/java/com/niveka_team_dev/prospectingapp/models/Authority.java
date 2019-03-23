package com.niveka_team_dev.prospectingapp.models;

import java.io.Serializable;

public class Authority implements Serializable{
    private String name;
    private String entrepriseId;

    public Authority() {
    }
    public Authority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntrepriseId() {
        return entrepriseId;
    }

    public void setEntrepriseId(String entrepriseId) {
        this.entrepriseId = entrepriseId;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "name='" + name + '\'' +
                ", entrepriseId='" + entrepriseId + '\'' +
                '}';
    }
}
