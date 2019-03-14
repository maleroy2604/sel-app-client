package com.selclientapp.selapp.repositories;

import androidx.lifecycle.MutableLiveData;

import com.selclientapp.selapp.api.TokenWebService;
import com.selclientapp.selapp.api.UserWebService;
import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.utils.Tools;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserWebService userWebService;
    private final TokenWebService tokenWebService;
    private final Executor executor;

    @Inject
    public UserRepository(UserWebService userWebService, Executor executor, TokenWebService tokenWebService) {
        this.userWebService = userWebService;
        this.executor = executor;
        this.tokenWebService = tokenWebService;
    }


    public MutableLiveData<SelApiToken> login(TokenBody tokenBody) {
        final MutableLiveData<SelApiToken> data = new MutableLiveData<>();
        executor.execute(() -> {
            tokenWebService.getToken(tokenBody).enqueue(new Callback<SelApiToken>() {
                @Override
                public void onResponse(Call<SelApiToken> call, Response<SelApiToken> response) {
                    if(response.isSuccessful()){
                        ManagementTokenAndUSer.saveToken(response.body().getAccessToken());
                        ManagementTokenAndUSer.saveTokenBody(tokenBody);
                        data.postValue(response.body());
                    }else{
                        Tools.backgroundThreadShortToast("Wrong username or password !");
                    }

                }

                @Override
                public void onFailure(Call<SelApiToken> call, Throwable t) {

                }
            });
        });

        return data;
    }

    public MutableLiveData<User> getUser(TokenBody tokenBody) {
        final MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            userWebService.getUser(tokenBody.getUsername()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    ManagementTokenAndUSer.saveUser(response.body());
                    data.postValue(response.body());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        });
        return data;
    }

    public MutableLiveData<User> saveUser(User user) {
        final MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            userWebService.saveUser(user.getUsername(), user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.code() == 201 ){
                        ManagementTokenAndUSer.saveTokenBody(new TokenBody(user.getUsername(), user.getPassword()));
                        ManagementTokenAndUSer.saveUser(response.body());
                        data.postValue(response.body());
                    }else{
                        Tools.backgroundThreadShortToast("User already exists ! ");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        });

        return data;
    }

    public MutableLiveData<SelApiToken> getToken() {
        final MutableLiveData<SelApiToken> data = new MutableLiveData<>();
        executor.execute(() -> {
            tokenWebService.getToken(ManagementTokenAndUSer.getCurrentTokenBody()).enqueue(new Callback<SelApiToken>() {
                @Override
                public void onResponse(Call<SelApiToken> call, Response<SelApiToken> response) {
                    ManagementTokenAndUSer.saveToken(response.body().getAccessToken());
                    data.postValue(response.body());
                }

                @Override
                public void onFailure(Call<SelApiToken> call, Throwable t) {

                }
            });
        });
        return data;
    }

}


