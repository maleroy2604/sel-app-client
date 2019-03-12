package com.selclientapp.selapp.repositories;

import com.selclientapp.selapp.api.TokenWebService;
import com.selclientapp.selapp.model.SelApiToken;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenRepository {

    private TokenWebService tokenWebService;
    private Executor executor;

    @Inject
    public TokenRepository(TokenWebService tokenWebService, Executor executor) {
        this.tokenWebService = tokenWebService;
        this.executor = executor;
    }

    public void getTokenAndSaveIt() {
        executor.execute(() -> {
            tokenWebService.getToken(ManagementTokenAndUSer.getCurrentTokenBody()).enqueue(new Callback<SelApiToken>() {
                @Override
                public void onResponse(Call<SelApiToken> call, Response<SelApiToken> response) {
                    ManagementTokenAndUSer.saveToken(response.body().getAccessToken());
                }

                @Override
                public void onFailure(Call<SelApiToken> call, Throwable t) {

                }
            });
        });
    }
}
