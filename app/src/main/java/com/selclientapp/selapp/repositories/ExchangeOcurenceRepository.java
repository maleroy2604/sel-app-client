package com.selclientapp.selapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
            exchangeOcurenceWebService.addExchangeOcurence(0, exchangeOcurence).enqueue(new Callback<ExchangeOcurence>() {
                @Override
                public void onResponse(Call<ExchangeOcurence> call, Response<ExchangeOcurence> response) {
                    if (response.isSuccessful()) {
                        data.postValue(response.body());
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
            exchangeOcurenceWebService.deleteExchangeOcurence(exchangeOcurence.getId()).enqueue(new Callback<ExchangeOcurence>() {
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
            exchangeOcurenceWebService.updateExchangeOcurence(exchangeOcurence.getId(), exchangeOcurence).enqueue(new Callback<ExchangeOcurence>() {
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
            exchangeOcurenceWebService.getAllExchangeOcurence(exchangeId).enqueue(new Callback<List<ExchangeOcurence>>() {
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
