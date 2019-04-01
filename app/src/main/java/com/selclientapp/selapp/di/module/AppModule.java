package com.selclientapp.selapp.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.selclientapp.selapp.api.ExchangeOcurenceWebService;
import com.selclientapp.selapp.api.ExchangeWebService;
import com.selclientapp.selapp.api.TokenWebService;
import com.selclientapp.selapp.api.UserWebService;
import com.selclientapp.selapp.repositories.ExchangeOcurenceRepository;
import com.selclientapp.selapp.repositories.ExchangeRepository;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.repositories.UserRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Module(includes = ViewModelmodule.class)
public class AppModule {
    // --- REPOSITORY INJECTION ---

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository(UserWebService userWebService, Executor executor, TokenWebService tokenWebService, ManagementTokenAndUSer managementTokenAndUSer) {
        return new UserRepository(userWebService, executor, tokenWebService, managementTokenAndUSer);
    }

    @Provides
    @Singleton
    ExchangeRepository provideExchangeRepository(ExchangeWebService webservice, Executor executor) {
        return new ExchangeRepository(webservice, executor);
    }

    @Provides
    @Singleton
    ExchangeOcurenceRepository provideExchangeOcurenceRepository(ExchangeOcurenceWebService webservice, Executor executor) {
        return new ExchangeOcurenceRepository(webservice, executor);
    }


    // --- NETWORK INJECTION ---

    @Provides
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    Retrofit provideRetrofit(Gson gson) {
        String BASE_URL = "http://10.0.2.2:5000/";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new ServiceInterceptor()).build();
        //"https://sel-app.herokuapp.com/"
        //"http://10.0.2.2:5000/"
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build();
    }

    @Provides
    @Singleton
        ManagementTokenAndUSer provideManagerTokenAndUser() {
        return new ManagementTokenAndUSer();
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Provides
    @Singleton
    TokenWebService provideTokenWebservice(Retrofit restAdapter) {
        return restAdapter.create(TokenWebService.class);
    }

    @Provides
    @Singleton
    UserWebService provideUserWebservice(Retrofit restAdapter) {
        return restAdapter.create(UserWebService.class);
    }

    @Provides
    @Singleton
    ExchangeWebService provideExchangeWebservice(Retrofit restAdapter) {
        return restAdapter.create(ExchangeWebService.class);
    }

    @Provides
    @Singleton
    ExchangeOcurenceWebService provideExchangeOcurenceWebservice(Retrofit restAdapter) {
        return restAdapter.create(ExchangeOcurenceWebService.class);
    }


}
