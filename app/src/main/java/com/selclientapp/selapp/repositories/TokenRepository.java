package com.selclientapp.selapp.repositories;



import com.selclientapp.selapp.api.SelApiToken;
import com.selclientapp.selapp.api.TokenBody;
import com.selclientapp.selapp.api.TokenWebService;
import com.selclientapp.selapp.utils.Tools;


import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TokenRepository {
    private final TokenWebService tokenWebService;

    @Inject
    public TokenRepository(TokenWebService webService) {
        this.tokenWebService = webService;
    }

    public void getTokenAndSaveIt(TokenBody tokenBody) {

        tokenWebService.getToken(tokenBody).enqueue(new Callback<SelApiToken>() {
            @Override
            public void onResponse(Call<SelApiToken> call, Response<SelApiToken> response) {
                if (response.isSuccessful()) {
                    SaveSharedpreferences.saveToken(response.body().getAccessToken());
                    SaveSharedpreferences.savecurrentUsername(tokenBody.getUsername());
                } else {
                    Tools.backgroundThreadShortToast("Wrong Username or password");
                }
            }

            @Override
            public void onFailure(Call<SelApiToken> call, Throwable t) {
                Tools.backgroundThreadShortToast("Server not available");
            }
        });
    }

}
