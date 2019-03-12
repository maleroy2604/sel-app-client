package com.selclientapp.selapp.api;

import com.selclientapp.selapp.model.Exchange;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ExchangeWebService {
    @POST("exchange/{id}")
    Call<Exchange> saveExchange(@Path("id") int id, @Body Exchange exchange);

    @GET("exchanges")
    Call<List<Exchange>> getAllExchange();

    @DELETE("exchange/{id}")
    Call<Exchange> deleteExchange(@Path("id") int id);

    @PUT("exchange/{id}")
    Call<Exchange> updateExchange(@Path("id") int id, @Body Exchange exchange);
}
