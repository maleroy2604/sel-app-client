package com.selclientapp.selapp.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.selclientapp.selapp.model.ExchangeOcurence;
import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.repositories.ExchangeOcurenceRepository;
import com.selclientapp.selapp.repositories.TokenBody;
import com.selclientapp.selapp.repositories.TokenRepository;


import java.util.List;

import javax.inject.Inject;

public class ExchangeOcurenceViewModel extends ViewModel {

    private final ExchangeOcurenceRepository exchangeOcurenceRepository;
    private final TokenRepository tokenRepository;
    private LiveData<SelApiToken> selApiTokenLiveData;
    private LiveData<ExchangeOcurence> exchangeOcurenceLiveData;
    private LiveData<List<ExchangeOcurence>> listExchangeOcurence = null;

    @Inject
    public ExchangeOcurenceViewModel(ExchangeOcurenceRepository exchangeOcurenceRepository, TokenRepository tokenRepository) {
        this.exchangeOcurenceRepository = exchangeOcurenceRepository;
        this.tokenRepository = tokenRepository;
    }

    public void addExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        exchangeOcurenceLiveData = exchangeOcurenceRepository.addExchangeOcurence(exchangeOcurence);
    }

    public void deleteExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        exchangeOcurenceLiveData = exchangeOcurenceRepository.deleteExchangeOcurence(exchangeOcurence);
    }

    public void updateExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        exchangeOcurenceLiveData = exchangeOcurenceRepository.updateExchangeOcurence(exchangeOcurence);
    }

    public void getAllExchangeOcurence(Integer exchangeId) {
        listExchangeOcurence = exchangeOcurenceRepository.getAllExchangeOcurence(exchangeId);
    }

    public void getTokenAndSaveIt(TokenBody tokenBody) {
        selApiTokenLiveData = tokenRepository.getTokenAndSaveIt(tokenBody);
    }

    public LiveData<ExchangeOcurence> getExchangeOcurenceLiveData() {
        return exchangeOcurenceLiveData;
    }

    public LiveData<SelApiToken> getSelApiTokenLiveData() {
        return selApiTokenLiveData;
    }

    public LiveData<List<ExchangeOcurence>> getListExchangeOcurence() {
        return listExchangeOcurence;
    }
}
