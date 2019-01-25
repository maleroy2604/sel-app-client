package com.selclientapp.selapp.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.selclientapp.selapp.database.entity.Exchange;
import com.selclientapp.selapp.repositories.ExchangeRepository;
import com.selclientapp.selapp.repositories.UserRepository;

import java.util.List;

import javax.inject.Inject;

public class ExchangeViewModel extends ViewModel {
    private UserRepository userRepository;
    private ExchangeRepository exchangeRepository;
    private LiveData<List<Exchange>> allExchanges;

    @Inject
    public ExchangeViewModel(UserRepository userRepository,ExchangeRepository exchangeRepository) {
        this.userRepository = userRepository;
        this.exchangeRepository = exchangeRepository;
    }

    public void init() {
        if (this.allExchanges != null) {
            return;
        }
        allExchanges = exchangeRepository.getAllExchanges();
    }

    public LiveData<List<Exchange>> getAllExchanges(){
        return allExchanges;
    }
}
