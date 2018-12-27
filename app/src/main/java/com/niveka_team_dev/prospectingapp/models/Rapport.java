package com.niveka_team_dev.prospectingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Rapport implements Parcelable{
    private long id;
    private String objet;
    private String contenu;
    private int type;
    private String key;
    private String copie;
    private String date;
    private String position;
    private String email;
    private String nomCom;

    public Rapport() {
    }

    protected Rapport(Parcel in) {
        id = in.readLong();
        objet = in.readString();
        contenu = in.readString();
        type = in.readInt();
        key = in.readString();
        copie = in.readString();
        date = in.readString();
        position = in.readString();
        email = in.readString();
        nomCom = in.readString();
    }

    public static final Creator<Rapport> CREATOR = new Creator<Rapport>() {
        @Override
        public Rapport createFromParcel(Parcel in) {
            return new Rapport(in);
        }

        @Override
        public Rapport[] newArray(int size) {
            return new Rapport[size];
        }
    };

    public String getNomCom() {
        return nomCom;
    }

    public void setNomCom(String nomCom) {
        this.nomCom = nomCom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCopie() {
        return copie;
    }

    public void setCopie(String copie) {
        this.copie = copie;
    }

    @Override
    public String toString() {
        return "Rapport{" +
                "id=" + id +
                ", objet='" + objet + '\'' +
                ", contenu='" + contenu + '\'' +
                ", type=" + type +
                ", key='" + key + '\'' +
                ", date='" + date + '\'' +
                ", psotion='" + position + '\'' +
                ", copie='" + copie + '\'' +
                '}';
    }

    public Map<String,Object> toMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("key",key);
        map.put("objet",objet);
        map.put("contenu",contenu);
        map.put("type",type);
        map.put("copie",copie);
        map.put("date",date);
        map.put("position",position);
        return map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(objet);
        parcel.writeString(contenu);
        parcel.writeInt(type);
        parcel.writeString(key);
        parcel.writeString(copie);
        parcel.writeString(date);
        parcel.writeString(position);
        parcel.writeString(email);
        parcel.writeString(nomCom);
    }
}
