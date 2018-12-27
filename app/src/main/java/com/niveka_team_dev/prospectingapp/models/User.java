package com.niveka_team_dev.prospectingapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable{
    private long id;
    private String username;
    private String email;
    private String sexe;
    private String password;
    private String uid;
    private String channelID;
    private String createdAt;
    private String photo;

    public User() {
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String token) {
        this.uid = token;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String fbID) {
        this.channelID = fbID;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", sexe='" + sexe + '\'' +
                ", password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                ", channelID='" + channelID + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",getId());
        map.put("uid",getUid());
        map.put("sexe",getSexe());
        map.put("email",getEmail());
        map.put("username",getUsername());
        map.put("password",getPassword());
        map.put("channelID",getChannelID());
        map.put("createdAt",getCreatedAt());
        map.put("photo",getPhoto());
        return map;
    }

    public JSONObject toJson(){
        return new JSONObject(this.toMap());
    }
}
