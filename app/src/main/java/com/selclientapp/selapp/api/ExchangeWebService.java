package com.selclientapp.selapp.api;

import com.selclientapp.selapp.database.entity.Exchange;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ExchangeWebService {
    @POST("{id}")
    Call<Exchange> saveExchange(@Header("authorization") String token,@Path("id") int id, @Body Exchange exchange);

    @GET("exchanges")
    Call<List<Exchange>> getAllExchange(@Header("authorization") String token);
}
