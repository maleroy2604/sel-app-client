package com.selclientapp.selapp.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.selclientapp.selapp.api.ExchangeWebService;
import com.selclientapp.selapp.model.Exchange;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRepository {

    private final ExchangeWebService exchangeWebService;
    private final Executor executor;

    @Inject
    public ExchangeRepository(ExchangeWebService exchangeWebService, Executor executor) {
        this.exchangeWebService = exchangeWebService;

        this.executor = executor;
    }

    public void saveExchange(Exchange exchange) {
        executor.execute(() -> {
            exchangeWebService.saveExchange(SaveSharedpreferences.getToken(), exchange.getId(), exchange).enqueue(new Callback<Exchange>() {
                @Override
                public void onResponse(Call<Exchange> call, Response<Exchange> response) {
                }

                @Override
                public void onFailure(Call<Exchange> call, Throwable t) {

                }
            });
        });
    }

    public LiveData<List<Exchange>> getAllExchanges() {
        final MutableLiveData<List<Exchange>> data = new MutableLiveData<>();

        exchangeWebService.getAllExchange(SaveSharedpreferences.getToken()).enqueue(new Callback<List<Exchange>>() {
            @Override
            public void onResponse(Call<List<Exchange>> call, Response<List<Exchange>> response) {
                if (response.isSuccessful()) {
                    System.out.println("response" + response.body());
                    data.setValue(response.body());
                    System.out.println("data response " + data.getValue());
                }
            }

            @Override
            public void onFailure(Call<List<Exchange>> call, Throwable t) {

            }
        });
        System.out.println("data " + data.getValue());
        return data;
    }

}
