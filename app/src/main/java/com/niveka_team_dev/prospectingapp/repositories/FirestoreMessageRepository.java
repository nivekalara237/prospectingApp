package com.niveka_team_dev.prospectingapp.repositories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.niveka_team_dev.prospectingapp.models.Channel;
import com.niveka_team_dev.prospectingapp.models.Message;

import java.util.Map;

import es.dmoral.toasty.Toasty;

public class FirestoreMessageRepository {
    public FirebaseFirestore firestoreDB;
    private CollectionReference messagesRef;
    private Context context;
    public FirestoreMessageRepository(Channel channel, Context context){
        this.firestoreDB = FirebaseFirestore.getInstance();
        this.messagesRef = this.firestoreDB
                .collection("enterprise_"+channel.getEntrepriseId())
                .document("channel_"+channel.getId())
                .collection("messages");
        this.context = context;
    }


    public void add(Message message){
        DocumentReference doc = messagesRef.document();
        String key = doc.getId();
        message.setKey(key);
        Map<String,Object> map = message.toMap();
        doc.set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Message added",Toast.LENGTH_SHORT).show();
                        //new_message.setText("");
                        //scrolldToBottom();
                        //emojIcon.closeEmojIcon();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toasty.error(context,"Message non envoyé", Toast.LENGTH_SHORT,false).show();
                    }
                });
    }

    public void getMessages(){

    }
}
