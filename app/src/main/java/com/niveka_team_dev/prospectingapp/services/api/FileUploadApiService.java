package com.niveka_team_dev.prospectingapp.services.api;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FileUploadApiService {

    @Multipart
    @FormUrlEncoded
    @POST("")
    Call<ResponseBody> uploadMessageAttachment();

    @Multipart
    @FormUrlEncoded
    @POST("{id}/files")
    Call<ResponseBody> uploadRepportAttachment(@Part MultipartBody.Part files, @Path("id") String rapportID);

    @Multipart
    @FormUrlEncoded
    @POST("")
    Call<ResponseBody> uploadCRSAttachment();
}
