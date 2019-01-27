package com.selclientapp.selapp.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.selclientapp.selapp.api.ExchangeWebService;
import com.selclientapp.selapp.api.TokenWebService;
import com.selclientapp.selapp.api.UserWebService;
import com.selclientapp.selapp.repositories.ExchangeRepository;
import com.selclientapp.selapp.repositories.TokenRepository;
import com.selclientapp.selapp.repositories.UserRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
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
    TokenRepository provideTokenRepository(TokenWebService webservice) {
        return new TokenRepository(webservice);
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository(UserWebService webservice, Executor executor) {
        return new UserRepository(webservice, executor);
    }

    @Provides
    @Singleton
    ExchangeRepository provideExchangeRepository(ExchangeWebService webservice, Executor executor) {
        return new ExchangeRepository(webservice, executor);
    }


    // --- NETWORK INJECTION ---

    private static String BASE_URL = "http://10.0.2.2:5000/";

    @Provides
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    Retrofit provideRetrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build();
        return retrofit;
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

}
