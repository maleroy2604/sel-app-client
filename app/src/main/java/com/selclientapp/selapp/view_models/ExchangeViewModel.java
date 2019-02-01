package com.selclientapp.selapp.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.repositories.ExchangeRepository;
import com.selclientapp.selapp.repositories.ManagementToken;
import com.selclientapp.selapp.repositories.TokenRepository;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class ExchangeViewModel extends ViewModel {
    private final TokenRepository tokenRepository;
    private ExchangeRepository exchangeRepository;
    private LiveData<List<Exchange>> allExchanges = null;

    @Inject
    public ExchangeViewModel( ExchangeRepository exchangeRepository,TokenRepository tokenRepository) {
        this.exchangeRepository = exchangeRepository;
        this.tokenRepository = tokenRepository;
    }

    public void init() {
        if(ManagementToken.hasToRefreshToken(new Date())){
            tokenRepository.getTokenAndSaveIt(ManagementToken.getCurrentTokenBody());
        }
        allExchanges = exchangeRepository.getAllExchanges();
    }

    public void deleteOneExchange(int id) {
        allExchanges = exchangeRepository.deleteOneExchange(id);
    }

    public LiveData<List<Exchange>> getAllExchanges() {
        return allExchanges;
    }
}
