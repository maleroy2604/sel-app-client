package com.selclientapp.selapp.repositories;

import android.arch.lifecycle.LiveData;

import com.selclientapp.selapp.api.ExchangeWebService;
import com.selclientapp.selapp.database.dao.ExchangeDao;
import com.selclientapp.selapp.database.entity.Exchange;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRepository {

    private final ExchangeWebService exchangeWebService;
    private final ExchangeDao exchangeDao;
    private final Executor executor;

    @Inject
    public ExchangeRepository(ExchangeWebService exchangeWebService, Executor executor, ExchangeDao exchangeDao) {
        this.exchangeWebService = exchangeWebService;
        this.exchangeDao = exchangeDao;
        this.executor = executor;
    }

    public void saveExchange(Exchange exchange) {
        executor.execute(() -> {
            exchangeWebService.saveExchange(SaveSharedpreferences.getToken(), exchange.getExchangeId(), exchange).enqueue(new Callback<Exchange>() {
                @Override
                public void onResponse(Call<Exchange> call, Response<Exchange> response) {
                    executor.execute(() -> {
                        Exchange exchange = response.body();
                        exchangeDao.saveExchange(exchange);
                    });
                }

                @Override
                public void onFailure(Call<Exchange> call, Throwable t) {

                }
            });
        });
    }

    public LiveData<List<Exchange>> getAllExchanges() {
        exchangeWebService.getAllExchange(SaveSharedpreferences.getToken()).enqueue(new Callback<List<Exchange>>() {
            @Override
            public void onResponse(Call<List<Exchange>> call, Response<List<Exchange>> response) {
                executor.execute(() -> {
                    System.out.println(response.body());
                    List<Exchange> allExchange = response.body();
                    for (Exchange exchange : allExchange) {
                        exchangeDao.saveExchange(exchange);
                    }

                });
            }

            @Override
            public void onFailure(Call<List<Exchange>> call, Throwable t) {

            }
        });
        return exchangeDao.getAllExchange();
    }


}
