package com.niveka_team_dev.prospectingapp.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.niveka_team_dev.prospectingapp.handlers.TokenManager;
import com.niveka_team_dev.prospectingapp.kernels.Session;
import com.niveka_team_dev.prospectingapp.models.AccessToken;
import com.niveka_team_dev.prospectingapp.services.api.AuthApiService;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

public class CustomAuthenticator implements Authenticator {

    private TokenManager tokenManager;
    private static CustomAuthenticator INSTANCE;
    private Session session;

    private CustomAuthenticator(TokenManager tokenManager){
        this.tokenManager = tokenManager;
        //session = new Session(this);
    }

    static synchronized CustomAuthenticator getInstance(TokenManager tokenManager){
        if(INSTANCE == null){
            INSTANCE = new CustomAuthenticator(tokenManager);
        }

        return INSTANCE;
    }


    @Nullable
    @Override
    public Request authenticate(@NonNull Route route, @NonNull Response response) throws IOException {

        if(responseCount(response) >= 3){
            return null;
        }

        AccessToken token = tokenManager.getToken();

        AuthApiService service = RetrofitBuilder.createService(AuthApiService.class);
        Call<AccessToken> call = service.refresh(token.getRefreshToken());
        retrofit2.Response<AccessToken> res = call.execute();

        if(res.isSuccessful()){
            AccessToken newToken = res.body();
            tokenManager.saveToken(newToken);

            return response.request().newBuilder().header("Authorization", "Bearer " + res.body().getAccessToken()).build();
        }else{
            return null;
        }
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}
