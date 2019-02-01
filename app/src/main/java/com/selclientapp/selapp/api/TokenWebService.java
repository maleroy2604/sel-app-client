package com.selclientapp.selapp.api;

import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.repositories.TokenBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TokenWebService {
    @POST("auth")
    Call<SelApiToken> getToken(@Body TokenBody tokenBody);
}
