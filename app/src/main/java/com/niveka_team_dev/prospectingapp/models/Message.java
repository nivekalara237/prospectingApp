package com.niveka_team_dev.prospectingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message implements Serializable,Parcelable {
    private String id;
    private String contenu;
    private String time;
    private String channelId;
    private String key;
    private User user;
    @JsonIgnore
    private String userId;
    @JsonIgnore
    private String receiverId;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private boolean vu;
    private Attachment attachment;

    @JsonIgnore
    private String[] listSeen;

    public Message() {
    }

    protected Message(Parcel in) {
        id = in.readString();
        contenu = in.readString();
        time = in.readString();
        channelId = in.readString();
        key = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        userId = in.readString();
        receiverId = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
        vu = in.readByte() != 0;
        attachment = in.readParcelable(Attachment.class.getClassLoader());
        listSeen = in.createStringArray();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public boolean isVu() {
        return vu;
    }

    public void setVu(boolean seen) {
        this.vu = seen;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User sender) {
        this.user = sender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String senderId) {
        this.userId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public String[] getListSeen() {
        return listSeen;
    }

    public void setListSeen(String[] listSeen) {
        this.listSeen = listSeen;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", body='" + contenu + '\'' +
                ", time='" + time + '\'' +
                ", channelID='" + channelId + '\'' +
                ", key='" + key + '\'' +
                ", sender=" + user +
                ", senderId=" + userId +
                ", receiverId=" + receiverId +
                ", seedn=" + vu +
                ", Attachment=" +attachment+
                '}';
    }


    public Map<String, Object> toMap(){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this,Map.class);
    }

    public JSONObject toJSON(){
        return new JSONObject(toMap());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(contenu);
        parcel.writeString(time);
        parcel.writeString(channelId);
        parcel.writeString(key);
        parcel.writeParcelable(user, i);
        parcel.writeString(userId);
        parcel.writeString(receiverId);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeString(deletedAt);
        parcel.writeByte((byte) (vu ? 1 : 0));
        parcel.writeParcelable(attachment,i);
        parcel.writeStringArray(listSeen);
    }
}
