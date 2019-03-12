package com.selclientapp.selapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.selclientapp.selapp.api.ExchangeWebService;
import com.selclientapp.selapp.model.Exchange;

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

    public LiveData<Exchange> saveExchange(Exchange exchange) {
        final MutableLiveData<Exchange> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.saveExchange( exchange.getId(), exchange).enqueue(new Callback<Exchange>() {
                @Override
                public void onResponse(Call<Exchange> call, Response<Exchange> response) {
                    if (response.isSuccessful()) {
                        data.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Exchange> call, Throwable t) {

                }
            });
        });
        return data;
    }

    public LiveData<List<Exchange>> getAllExchanges() {
        final MutableLiveData<List<Exchange>> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.getAllExchange().enqueue(new Callback<List<Exchange>>() {
                @Override
                public void onResponse(Call<List<Exchange>> call, Response<List<Exchange>> response) {
                    if (response.isSuccessful()) {
                        data.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Exchange>> call, Throwable t) {

                }
            });
        });
        return data;
    }

    public LiveData<Exchange> deleteOneExchange(int id) {
        final MutableLiveData<Exchange> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.deleteExchange(id).enqueue(new Callback<Exchange>() {
                @Override
                public void onResponse(Call<Exchange> call, Response<Exchange> response) {
                    if (response.isSuccessful()) {
                        data.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Exchange> call, Throwable t) {

                }
            });

        });

        return data;
    }

    public LiveData<Exchange> updateExchange(Exchange exchange) {
        final MutableLiveData<Exchange> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.updateExchange( exchange.getId(), exchange).enqueue(new Callback<Exchange>() {
                @Override
                public void onResponse(Call<Exchange> call, Response<Exchange> response) {
                    if (response.isSuccessful()) {
                        data.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Exchange> call, Throwable t) {

                }
            });
        });
        return data;
    }

}
