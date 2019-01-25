package com.selclientapp.selapp.repositories;

import com.selclientapp.selapp.api.UserWebService;
import com.selclientapp.selapp.database.dao.UserDao;
import com.selclientapp.selapp.database.entity.User;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserWebService userWebService;
    private final UserDao userDao;
    private final Executor executor;

    @Inject
    public UserRepository(UserWebService userWebService, Executor executor, UserDao userDao) {
        this.userWebService = userWebService;
        this.userDao = userDao;
        this.executor = executor;
    }

    public void saveUser(User user) {
        executor.execute(() -> {
            userWebService.saveUser(user.getUsername(),user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    executor.execute(() -> {
                        userDao.saveUser(user);
                    });
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        });

    }


}
