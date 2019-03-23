package com.niveka_team_dev.prospectingapp.services.api;

import com.niveka_team_dev.prospectingapp.models.AccessToken;
import com.niveka_team_dev.prospectingapp.models.User;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthApiService {
    @POST("register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("username") String username, @Field("password") String password);

    @POST("social_auth")
    @FormUrlEncoded
    Call<AccessToken> socialAuth(@Field("name") String name,
                                 @Field("email") String email,
                                 @Field("provider") String provider,
                                 @Field("provider_user_id") String providerUserId);

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);

    @POST("authenticate")
    Call<AccessToken> authenticate(@Body Map<String, Object> map);

    @GET("account")
    Call<User> account();

    @POST("users/fcm/change_token/{id}")
    Call<ResponseBody> updateToken(@Body Map<String,String> map, @Path("id") String uid);

    @POST("register/{entreprise}")
    Call<User> createAccount(@Body Map<String,Object> map,@Path("entreprise") String entreprise);

    @POST("account/reset-password/init")
    Call<ResponseBody> requestResetPassword(@Body String email);

}
