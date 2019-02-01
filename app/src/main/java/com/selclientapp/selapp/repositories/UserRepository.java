package com.selclientapp.selapp.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.selclientapp.selapp.api.UserWebService;
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.utils.Tools;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserWebService userWebService;

    private final Executor executor;

    @Inject
    public UserRepository(UserWebService userWebService, Executor executor) {
        this.userWebService = userWebService;
        this.executor = executor;
    }

    public MutableLiveData<User> saveUser(User user) {
        final MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            userWebService.saveUser(user.getUsername(), user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    data.postValue(response.body());
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
