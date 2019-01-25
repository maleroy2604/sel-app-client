package com.selclientapp.selapp.api;

import com.selclientapp.selapp.database.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserWebService {
    @POST("user/{username}")
    Call<User> saveUser(@Path("username") String username, @Body User user);

}
