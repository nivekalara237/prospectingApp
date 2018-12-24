package com.niveka_team_dev.prospectingapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message implements Serializable {
    private long id;
    private String body;
    private String time;
    private String channelID;
    private String key;
    @JsonIgnore
    private User sender;
    private long senderId;
    private long receiverId;
    private String senderName;
    private boolean seen;

    public Message() {
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", time='" + time + '\'' +
                ", channelID='" + channelID + '\'' +
                ", key='" + key + '\'' +
                ", sender=" + sender +
                ", senderId=" + senderId +
                ", senderName=" + senderName +
                ", receiverId=" + receiverId +
                ", seedn=" + seen +
                '}';
    }


    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("body",body);
        map.put("time",time);
        map.put("channelID",channelID);
        map.put("key",key);
        map.put("sender",sender);
        map.put("senderName",senderName);
        map.put("senderId",senderId);
        map.put("receiverId",receiverId);
        map.put("seen",seen);
        return map;
    }
}
