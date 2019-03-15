package com.selclientapp.selapp.di.module;

import com.selclientapp.selapp.api.TokenWebService;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;

import java.io.IOException;


import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //"https://sel-app.herokuapp.com/"
        //"http://10.0.2.2:5000/"
        Request request = chain.request();
        if (!(request.url().encodedPath().equals("/user/{username}") || request.url().encodedPath().equals("/auth"))) {
            request = request.newBuilder().addHeader("authorization", ManagementTokenAndUSer.getToken()).build();
            Response response = chain.proceed(request);
            if (response.code() == 401) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:5000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                TokenWebService tokenWebService = retrofit.create(TokenWebService.class);
                retrofit2.Response newToken = tokenWebService.getToken(ManagementTokenAndUSer.getCurrentTokenBody()).execute();
                if (newToken.isSuccessful()) {
                    ManagementTokenAndUSer.saveToken(newToken.body().toString());
                    Request newRequest = request.newBuilder()
                            .addHeader("authorization", ManagementTokenAndUSer.getToken())
                            .build();
                    return chain.proceed(newRequest);
                } else {
                    ManagementTokenAndUSer.logOut();
                }
            } else {
                return response;
            }
        }
        return chain.proceed(request);
    }
}
