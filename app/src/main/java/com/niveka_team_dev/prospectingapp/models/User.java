package com.niveka_team_dev.prospectingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.Exclude;

import org.json.JSONObject;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable,Parcelable{
    private String id;
    private String channelID;
    @Exclude
    private String photo;
    @Exclude
    private String iid;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private boolean activated = false;
    private String langKey;
    private String imageUrl;
    @Exclude
    private String activationKey;
    @Exclude
    private String resetKey;
    //@JsonIgnore
    //private transient Instant resetDate = null;
    private String createdAt=null;
    private String updatedAt=null;
    @Exclude
    private String activatedAt=null;
    private String entrepriseId;
    private String androidFcmToken;
    private String iosFcmToken;
    private String webFcmToken;
    private Entreprise entreprise = new Entreprise();
    @Exclude
    private List<String> authorities = new ArrayList<>();

    @JsonIgnore
    @Exclude
    private Set<Suivi> suivis = new HashSet<>();


    public User() {
    }

    protected User(Parcel in) {
        id = in.readString();
        channelID = in.readString();
        photo = in.readString();
        iid = in.readString();
        login = in.readString();
        password = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        activated = in.readByte() != 0;
        langKey = in.readString();
        imageUrl = in.readString();
        activationKey = in.readString();
        resetKey = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        activatedAt = in.readString();
        entrepriseId = in.readString();
        androidFcmToken = in.readString();
        iosFcmToken = in.readString();
        webFcmToken = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String fbID) {
        this.channelID = fbID;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
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

    public String getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(String activatedAt) {
        this.activatedAt = activatedAt;
    }

    public String getEntrepriseId() {
        return entrepriseId;
    }

    public void setEntrepriseId(String entrepriseId) {
        this.entrepriseId = entrepriseId;
    }

    public String getAndroidFcmToken() {
        return androidFcmToken;
    }

    public void setAndroidFcmToken(String androidFcmToken) {
        this.androidFcmToken = androidFcmToken;
    }

    public String getIosFcmToken() {
        return iosFcmToken;
    }

    public void setIosFcmToken(String iosFcmToken) {
        this.iosFcmToken = iosFcmToken;
    }

    public String getWebFcmToken() {
        return webFcmToken;
    }

    public void setWebFcmToken(String webFcmToken) {
        this.webFcmToken = webFcmToken;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public Set<Suivi> getSuivis() {
        return suivis;
    }

    public void setSuivis(Set<Suivi> suivis) {
        this.suivis = suivis;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", channelID='" + channelID + '\'' +
                ", photo='" + photo + '\'' +
                ", iid='" + iid + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", activated=" + activated +
                ", langKey='" + langKey + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", activationKey='" + activationKey + '\'' +
                ", resetKey='" + resetKey + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", activatedAt='" + activatedAt + '\'' +
                ", entrepriseId='" + entrepriseId + '\'' +
                ", androidFcmToken='" + androidFcmToken + '\'' +
                ", iosFcmToken='" + iosFcmToken + '\'' +
                ", webFcmToken='" + webFcmToken + '\'' +
                ", entreprise=" + entreprise +
                ", authorities=" + authorities +
                ", suivis=" + suivis +
                '}';
    }

    //
    public Map<String,Object> toMap(){
        ObjectMapper oMapper = new ObjectMapper();
        return oMapper.convertValue(this, Map.class);
    }

    public JSONObject toJson(){
        return new JSONObject(this.toMap());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(channelID);
        parcel.writeString(photo);
        parcel.writeString(iid);
        parcel.writeString(login);
        parcel.writeString(password);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeByte((byte) (activated ? 1 : 0));
        parcel.writeString(langKey);
        parcel.writeString(imageUrl);
        parcel.writeString(activationKey);
        parcel.writeString(resetKey);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeString(activatedAt);
        parcel.writeString(entrepriseId);
        parcel.writeString(androidFcmToken);
        parcel.writeString(iosFcmToken);
        parcel.writeString(webFcmToken);
    }
}
