package com.niveka_team_dev.prospectingapp.handlers;

import android.content.Context;
import android.content.SharedPreferences;

import com.niveka_team_dev.prospectingapp.kernels.Session;
import com.niveka_team_dev.prospectingapp.models.AccessToken;

public class TokenManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Session session;

    private static TokenManager INSTANCE = null;

    private TokenManager(Context prefs){
        session = new Session(prefs);
    }

    public static synchronized TokenManager getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new TokenManager(context);
        }
        return INSTANCE;
    }

    public void saveToken(AccessToken token){
        session.saveDataString("ACCESS_TOKEN",token.getAccessToken());
        session.saveDataString("REFRESH_TOKEN",token.getRefreshToken());
    }

    public void deleteToken(){
        session.retrieveDataString("ACCESS_TOKEN");
        session.retrieveDataString("REFRESH_TOKEN");
    }

    public AccessToken getToken(){
        AccessToken token = new AccessToken();
        token.setAccessToken(session.retrieveDataString("ACCESS_TOKEN"));
        token.setRefreshToken(session.retrieveDataString("REFRESH_TOKEN"));
        return token;
    }

}
