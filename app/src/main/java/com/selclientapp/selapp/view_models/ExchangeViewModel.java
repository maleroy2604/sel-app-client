package com.selclientapp.selapp.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.repositories.ExchangeRepository;
import com.selclientapp.selapp.repositories.UserRepository;

import java.util.List;

import javax.inject.Inject;

public class ExchangeViewModel extends ViewModel {
    private UserRepository userRepository;
    private ExchangeRepository exchangeRepository;
    private LiveData<List<Exchange>> allExchanges = null;

    @Inject
    public ExchangeViewModel(UserRepository userRepository, ExchangeRepository exchangeRepository) {
        this.userRepository = userRepository;
        this.exchangeRepository = exchangeRepository;
    }

    public void init() {
        allExchanges = exchangeRepository.getAllExchanges();
        System.out.println("allExchanges" + allExchanges.getValue());
    }

    public LiveData<List<Exchange>> getAllExchanges() {
        return allExchanges;
    }
}
