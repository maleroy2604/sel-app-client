package com.selclientapp.selapp.api;

import com.selclientapp.selapp.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageWebService {

    @POST("/message")
    Call<List<Message>> sendMessage(@Body Message message);

    @GET("/messages/{id}")
    Call<List<Message>> getMessagesExchange(@Path("id") int id);

}
