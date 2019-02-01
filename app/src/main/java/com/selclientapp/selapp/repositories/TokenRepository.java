package com.selclientapp.selapp.repositories;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.api.TokenWebService;
import com.selclientapp.selapp.utils.Tools;


import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TokenRepository {
    private  TokenWebService tokenWebService;

    @Inject
    public TokenRepository(TokenWebService webService) {
        tokenWebService = webService;
    }

    public LiveData<SelApiToken> getTokenAndSaveIt(TokenBody tokenBody) {
        final MutableLiveData<SelApiToken> data = new MutableLiveData<>();
        tokenWebService.getToken(tokenBody).enqueue(new Callback<SelApiToken>() {
            @Override
            public void onResponse(Call<SelApiToken> call, Response<SelApiToken> response) {
                if (response.isSuccessful()) {
                    ManagementToken.saveToken(response.body().getAccessToken());
                    ManagementToken.savecurrentUsername(tokenBody.getUsername(), tokenBody.getPassword());
                    data.postValue(response.body());
                } else {
                    Tools.backgroundThreadShortToast("Wrong Username or password");
                }
            }

            @Override
            public void onFailure(Call<SelApiToken> call, Throwable t) {
                Tools.backgroundThreadShortToast("Server not available");
            }
        });
        return data;
    }
}
