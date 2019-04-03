package com.selclientapp.selapp.repositories;

import androidx.lifecycle.MutableLiveData;

import com.selclientapp.selapp.api.TokenWebService;
import com.selclientapp.selapp.api.UserWebService;
import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.utils.TokenBody;
import com.selclientapp.selapp.utils.Tools;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserWebService userWebService;
    private final TokenWebService tokenWebService;
    private final Executor executor;
    private ManagementTokenAndUSer managementTokenAndUSer;

    @Inject
    public UserRepository(UserWebService userWebService, Executor executor, TokenWebService tokenWebService, ManagementTokenAndUSer managementTokenAndUSer) {
        this.userWebService = userWebService;
        this.executor = executor;
        this.tokenWebService = tokenWebService;
        this.managementTokenAndUSer = managementTokenAndUSer;
    }


    public MutableLiveData<User> login(TokenBody tokenBody) {
        final MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            tokenWebService.authenticate(tokenBody).enqueue(new Callback<SelApiToken>() {
                @Override
                public void onResponse(Call<SelApiToken> call, Response<SelApiToken> response) {
                    if (response.isSuccessful()) {
                        managementTokenAndUSer.saveSelApiToken(response.body());
                        data.postValue(response.body().getUser());
                    } else {
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

    public MutableLiveData<User> logout() {
        MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            userWebService.logOut().enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    data.postValue(response.body());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        });
        return data;
    }

    public MutableLiveData<User> getUser(int id) {
        final MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            userWebService.getUser(id).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
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
            userWebService.saveUser(user).enqueue(new Callback<SelApiToken>() {
                @Override
                public void onResponse(Call<SelApiToken> call, Response<SelApiToken> response) {
                    if (response.code() == 201) {
                        managementTokenAndUSer.saveSelApiToken(response.body());
                        System.out.println("user " + response.body().getUser().toString());
                        data.postValue(response.body().getUser());
                    } else {
                        Tools.backgroundThreadShortToast("User already exists ! ");
                    }
                }

                @Override
                public void onFailure(Call<SelApiToken> call, Throwable t) {

                }
            });
        });

        return data;
    }
}


