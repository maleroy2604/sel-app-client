package com.selclientapp.selapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TokenWebService {
    @Headers({"Accept: application/json"})
    @POST("auth")
    Call<SelApiToken> getToken(@Body TokenBody tokenBody);
}
