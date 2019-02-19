package com.selclientapp.selapp.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.selclientapp.selapp.api.TokenWebService;
import com.selclientapp.selapp.api.UserWebService;
import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.utils.Tools;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserWebService userWebService;
    TokenWebService tokenWebService;
    private final Executor executor;

    @Inject
    public UserRepository(UserWebService userWebService, Executor executor, TokenWebService tokenWebService) {
        this.userWebService = userWebService;
        this.executor = executor;
        this.tokenWebService = tokenWebService;
    }

    public MutableLiveData<SelApiToken> saveUser(User user) {
        final MutableLiveData<SelApiToken> data = new MutableLiveData<>();
        executor.execute(() -> {
            userWebService.saveUser(user.getUsername(), user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    tokenWebService.getToken(new TokenBody(user.getUsername(), user.getPassword())).enqueue(new Callback<SelApiToken>() {
                        @Override
                        public void onResponse(Call<SelApiToken> call, Response<SelApiToken> response) {
                            ManagementToken.saveToken(response.body().getAccessToken());
                            ManagementToken.savecurrentUsername(user.getUsername(), user.getPassword());
                            data.postValue(response.body());
                        }

                        @Override
                        public void onFailure(Call<SelApiToken> call, Throwable t) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Tools.backgroundThreadShortToast("Server not available");
                }
            });
        });
        return data;
    }

    public MutableLiveData<User> getUser() {
        final MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            userWebService.getUser(ManagementToken.getToken(), ManagementToken.getCurrentTokenBody().getUsername()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    data.postValue(response.body());
                    ManagementToken.saveCurrentId(response.body().getId());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Tools.backgroundThreadShortToast("Server not available");
                }
            });
        });
        return data;
    }
}
