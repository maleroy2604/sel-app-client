package com.selclientapp.selapp.api;

import com.selclientapp.selapp.model.ExchangeOcurence;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ExchangeOcurenceWebService {
    @POST("exchangeocurence/{id}")
    Call<ExchangeOcurence> addExchangeOcurence(@Path("id") int id, @Body ExchangeOcurence exchangeOcurence);

    @DELETE("exchangeocurence/{id}")
    Call<ExchangeOcurence> deleteExchangeOcurence(@Path("id") int id);

    @PUT("exchangeocurence/{id}")
    Call<ExchangeOcurence> updateExchangeOcurence(@Path("id") int id, @Body ExchangeOcurence exchangeOcurence);

    @GET("exchangeocurences/{id}")
    Call<List<ExchangeOcurence>> getAllExchangeOcurence(@Path("id") int id);
}
