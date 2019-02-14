package com.selclientapp.selapp.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.selclientapp.selapp.api.ExchangeOcurenceWebService;
import com.selclientapp.selapp.model.ExchangeOcurence;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeOcurenceRepository {

    private final ExchangeOcurenceWebService exchangeOcurenceWebService;
    private final Executor executor;

    @Inject
    public ExchangeOcurenceRepository(ExchangeOcurenceWebService exchangeOcurenceRepository, Executor executor) {
        this.exchangeOcurenceWebService = exchangeOcurenceRepository;
        this.executor = executor;
    }

    public LiveData<ExchangeOcurence> addExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        final MutableLiveData<ExchangeOcurence> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeOcurenceWebService.addExchangeOcurence(ManagementToken.getToken(), exchangeOcurence.getId(), exchangeOcurence).enqueue(new Callback<ExchangeOcurence>() {
                @Override
                public void onResponse(Call<ExchangeOcurence> call, Response<ExchangeOcurence> response) {
                    if (response.isSuccessful()) {
                        data.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ExchangeOcurence> call, Throwable t) {

                }
            });
        });
        return data;
    }

    public LiveData<ExchangeOcurence> deleteExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        final MutableLiveData<ExchangeOcurence> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeOcurenceWebService.deleteExchangeOcurence(ManagementToken.getToken(), exchangeOcurence.getId()).enqueue(new Callback<ExchangeOcurence>() {
                @Override
                public void onResponse(Call<ExchangeOcurence> call, Response<ExchangeOcurence> response) {
                    if (response.isSuccessful()) {
                        data.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ExchangeOcurence> call, Throwable t) {

                }
            });
        });
        return data;
    }

    public LiveData<ExchangeOcurence> updateExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        final MutableLiveData<ExchangeOcurence> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeOcurenceWebService.updateExchangeOcurence(ManagementToken.getToken(), exchangeOcurence.getId(), exchangeOcurence).enqueue(new Callback<ExchangeOcurence>() {
                @Override
                public void onResponse(Call<ExchangeOcurence> call, Response<ExchangeOcurence> response) {
                    if (response.isSuccessful()) {
                        data.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ExchangeOcurence> call, Throwable t) {

                }
            });
        });
        return data;
    }

    public LiveData<List<ExchangeOcurence>> getAllExchangeOcurence(Integer exchangeId) {
        final MutableLiveData<List<ExchangeOcurence>> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeOcurenceWebService.getAllExchangeOcurence(ManagementToken.getToken(), exchangeId).enqueue(new Callback<List<ExchangeOcurence>>() {
                @Override
                public void onResponse(Call<List<ExchangeOcurence>> call, Response<List<ExchangeOcurence>> response) {
                    if (response.isSuccessful()) {
                        data.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<ExchangeOcurence>> call, Throwable t) {

                }
            });
        });
        return data;
    }


}
