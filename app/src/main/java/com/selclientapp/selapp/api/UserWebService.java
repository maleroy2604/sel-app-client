package com.selclientapp.selapp.api;


import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.model.User;

import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserWebService {
    @POST("register")
    Call<SelApiToken> saveUser(@Body User user);

    @GET("user/{id}")
    Call<User> getUser(@Path("id") int id);

    @POST("userlogout")
    Call<User> logOut();

    @PUT("user/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);
}
