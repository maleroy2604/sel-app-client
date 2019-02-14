package com.selclientapp.selapp.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.selclientapp.selapp.api.ExchangeWebService;
import com.selclientapp.selapp.api.TokenWebService;
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
            exchangeWebService.saveExchange(ManagementToken.getToken(), exchange.getId(), exchange).enqueue(new Callback<Exchange>() {
                @Override
                public void onResponse(Call<Exchange> call, Response<Exchange> response) {
                    data.setValue(response.body());;
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
            exchangeWebService.getAllExchange(ManagementToken.getToken()).enqueue(new Callback<List<Exchange>>() {
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

    public LiveData<List<Exchange>> deleteOneExchange(int id) {
        final MutableLiveData<List<Exchange>> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.deleteExchange(ManagementToken.getToken(), id).enqueue(new Callback<List<Exchange>>() {
                @Override
                public void onResponse(Call<List<Exchange>> call, Response<List<Exchange>> response) {
                    if (response.isSuccessful()) {
                        data.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Exchange>> call, Throwable t) {

                }
            });
        });
        return data;
    }

    public LiveData<Exchange> updateExchange(Exchange exchange) {
        final MutableLiveData<Exchange> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.updateExchange(ManagementToken.getToken(),exchange.getId(),exchange).enqueue(new Callback<Exchange>() {
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
