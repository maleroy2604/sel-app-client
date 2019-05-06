package com.selclientapp.selapp.api;

import com.selclientapp.selapp.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageWebService {
    @Multipart
    @POST("uploadavatar/image")
    Call<RequestBody> uploadImage(@Part MultipartBody.Part filePart);

    @DELETE("imageavatar/{filename}")
    Call<ResponseBody> deleteImage(@Path("filename") String filename);
}
