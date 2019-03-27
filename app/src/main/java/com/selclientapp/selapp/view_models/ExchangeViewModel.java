package com.selclientapp.selapp.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.repositories.ExchangeRepository;

import java.util.List;

import javax.inject.Inject;

public class ExchangeViewModel extends ViewModel {
    private ExchangeRepository exchangeRepository;
    private LiveData<List<Exchange>> allExchanges;
    private LiveData<Exchange> exchangeLiveData;

    @Inject
    public ExchangeViewModel(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    public void init(int numberLimit) {
        allExchanges = exchangeRepository.getAllExchanges(numberLimit);
    }

    public void AddExchange(Exchange exchange) {
        exchangeLiveData = exchangeRepository.saveExchange(exchange);
    }

    public void deleteOneExchange(int id) {
        exchangeLiveData = exchangeRepository.deleteOneExchange(id);
    }

    public void updateExchange(Exchange exchange) {
        exchangeLiveData = exchangeRepository.updateExchange(exchange);
    }

    public LiveData<List<Exchange>> getAllExchanges() {
        return allExchanges;
    }

    public LiveData<Exchange> getExchangeLiveData() {
        return exchangeLiveData;
    }
}
