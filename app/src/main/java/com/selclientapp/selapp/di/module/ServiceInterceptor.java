package com.selclientapp.selapp.di.module;

import com.selclientapp.selapp.App;
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
        Request request = chain.request();
        ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();
        if (!(request.url().encodedPath().equals("/register") || request.url().encodedPath().equals("/authenticate"))) {
            request = request.newBuilder().addHeader("Authorization", managementTokenAndUSer.getAccessToken()).build();
            Response response = chain.proceed(request);
            if (response.code() == 401) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(App.URL_SERVER)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                TokenWebService tokenWebService = retrofit.create(TokenWebService.class);
                retrofit2.Response newToken = tokenWebService.refreshToken(managementTokenAndUSer.getRefreshToken()).execute();
                if (newToken.isSuccessful()) {
                    managementTokenAndUSer.saveAcessToken(newToken.body().toString());
                    Request newRequest = request.newBuilder()
                            .addHeader("Authorization", managementTokenAndUSer.getAccessToken())
                            .build();
                    return chain.proceed(newRequest);
                } else {
                    managementTokenAndUSer.logOut();
                }
            } else {
                return response;
            }
        }
        return chain.proceed(request);
    }
}
//"https://sel-app.herokuapp.com/"
//"http://10.0.2.2:5000/"