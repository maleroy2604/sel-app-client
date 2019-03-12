package com.selclientapp.selapp.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.selclientapp.selapp.model.ExchangeOcurence;
import com.selclientapp.selapp.repositories.ExchangeOcurenceRepository;


import java.util.List;

import javax.inject.Inject;

public class ExchangeOcurenceViewModel extends ViewModel {
    private final ExchangeOcurenceRepository exchangeOcurenceRepository;
    private LiveData<ExchangeOcurence> exchangeOcurenceLiveData;
    private LiveData<List<ExchangeOcurence>> listExchangeOcurence = null;

    @Inject
    public ExchangeOcurenceViewModel(ExchangeOcurenceRepository exchangeOcurenceRepository) {
        this.exchangeOcurenceRepository = exchangeOcurenceRepository;
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

    public LiveData<ExchangeOcurence> getExchangeOcurenceLiveData() {
        return exchangeOcurenceLiveData;
    }

    public LiveData<List<ExchangeOcurence>> getListExchangeOcurence() {
        return listExchangeOcurence;
    }
}
