package com.niveka_team_dev.prospectingapp.handlers;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niveka_team_dev.prospectingapp.kernels.Session;
import com.niveka_team_dev.prospectingapp.activities.LoginActivity;
import com.niveka_team_dev.prospectingapp.models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UserHelpers {
    private Session session;
    public final static String CURRENT_USER_ID = "user";
    public static User currentUser;

    public static User fromJSON(JSONObject json) throws JSONException {
        return (User) JsonHelper.fromJson(json);
    }


    public  static User jsonToPOJO(JSONObject json) throws JSONException {
        ObjectMapper mapper = new ObjectMapper();
        User pojo = mapper.convertValue(JsonHelper.fromJson(json), User.class);
        return pojo;
    }

    public static User getCurrentUser(Context c){
        User user =null;
        Session sessions = new Session(c);
        String in_session = sessions.retrieveDataString(CURRENT_USER_ID);

        Log.e("LOG",in_session);
        if (UserHelpers.currentUser!=null){
            user = UserHelpers.currentUser;
        }else if (in_session!=null || !TextUtils.isEmpty(in_session)){
            try {
                JSONObject obj = new JSONObject(in_session);
                user = jsonToPOJO(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (user==null){
            Intent in = new Intent(c, LoginActivity.class);
            c.startActivity(in);
        }
        return user;
    }

    public static void putUserToSession(String userStr,Context c) throws JSONException {
        Session sessions = new Session(c);
        sessions.saveDataString(CURRENT_USER_ID,userStr);
        JSONObject o = new JSONObject(userStr);
        UserHelpers.currentUser = jsonToPOJO(o);
    }
}
