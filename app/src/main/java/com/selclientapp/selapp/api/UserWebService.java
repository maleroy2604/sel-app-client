package com.selclientapp.selapp.api;


import com.selclientapp.selapp.model.User;

import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserWebService {
    @POST("user/{username}")
    Call<User> saveUser(@Path("username") String username, @Body User user);

    @GET("user/{username}")
    Call<User> getUser(@Path("username") String username);
}
