package com.selclientapp.selapp.api;

import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.utils.TokenBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TokenWebService {
    @POST("authenticate")
    Call<SelApiToken> authenticate(@Body TokenBody tokenBody);

    @POST("tokenrefresh")
    Call<SelApiToken> refreshToken(@Header("Authorization")String tokenRefresh);
}
