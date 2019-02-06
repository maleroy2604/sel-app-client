package com.selclientapp.selapp.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.selclientapp.selapp.App;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.repositories.ExchangeRepository;
import com.selclientapp.selapp.repositories.ManagementToken;
import com.selclientapp.selapp.repositories.TokenBody;
import com.selclientapp.selapp.repositories.TokenRepository;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class ExchangeViewModel extends ViewModel {
    private final TokenRepository tokenRepository;
    private ExchangeRepository exchangeRepository;
    private LiveData<List<Exchange>> allExchanges = null;
    private LiveData<SelApiToken> selApiTokenLiveData;
    private LiveData<Exchange> exchangeLiveData;

    @Inject
    public ExchangeViewModel(ExchangeRepository exchangeRepository, TokenRepository tokenRepository) {
        this.exchangeRepository = exchangeRepository;
        this.tokenRepository = tokenRepository;
    }

    public void init() {
        allExchanges = exchangeRepository.getAllExchanges();
    }

    public void AddExchange( Exchange exchange) {
        exchangeLiveData = exchangeRepository.saveExchange(exchange);
    }

    public void deleteOneExchange(int id) {
        allExchanges = exchangeRepository.deleteOneExchange(id);
    }

    public void getTokenAndSaveIt(TokenBody tokenBody) {
        selApiTokenLiveData = tokenRepository.getTokenAndSaveIt(tokenBody);
    }

    public void updateExchange(Exchange exchange){
        exchangeLiveData = exchangeRepository.updateExchange(exchange);
    }

    public LiveData<List<Exchange>> getAllExchanges() {
        return allExchanges;
    }

    public LiveData<SelApiToken> getSelApiTokenLiveData() {
        return selApiTokenLiveData;
    }

    public LiveData<Exchange> getExchangeLiveData() {
        return exchangeLiveData;
    }


}
