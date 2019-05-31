package com.selclientapp.selapp.api;

import com.selclientapp.selapp.model.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageWebService {
    @Multipart
    @POST("uploadavatar/image/{id}")
    Call<User> uploadImage(@Part MultipartBody.Part filePart, @Path("id") int userId);

    @DELETE("uploadavatar/image/{id}")
    Call<ResponseBody> deleteImage(@Path("id") int userId);
}
