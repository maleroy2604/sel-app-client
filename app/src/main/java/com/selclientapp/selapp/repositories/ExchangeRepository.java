package com.selclientapp.selapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.selclientapp.selapp.api.ExchangeWebService;
import com.selclientapp.selapp.model.Category;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.utils.NumberLimits;
import com.selclientapp.selapp.utils.Tools;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRepository {

    private final ExchangeWebService exchangeWebService;
    private final Executor executor;
    private ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();

    @Inject
    public ExchangeRepository(ExchangeWebService exchangeWebService, Executor executor) {
        this.exchangeWebService = exchangeWebService;
        this.executor = executor;
    }

    public LiveData<Exchange> saveExchange(Exchange exchange) {
        final MutableLiveData<Exchange> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.saveExchange(0, exchange).enqueue(new Callback<Exchange>() {
                @Override
                public void onResponse(Call<Exchange> call, Response<Exchange> response) {
                    if (response.isSuccessful()) {
                        data.postValue(response.body());
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<Exchange> call, Throwable t) {

                }
            });
        });
        return data;
    }

    public LiveData<List<Exchange>> getAllExchanges(NumberLimits numberLimit) {
        final MutableLiveData<List<Exchange>> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.getAllExchange(numberLimit).enqueue(new Callback<List<Exchange>>() {
                @Override
                public void onResponse(Call<List<Exchange>> call, Response<List<Exchange>> response) {
                    if (response.isSuccessful()) {
                        data.postValue(response.body());
                        System.out.println(response.body().toString());
                    } else {
                        data.postValue(null);
                        Tools.backgroundThreadShortToast("Unable to load the exchanges ! ");
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
        System.out.println("BEFORE REQUEST UPDATE " + exchange);
        final MutableLiveData<Exchange> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.updateExchange(exchange.getId(), exchange).enqueue(new Callback<Exchange>() {
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

    public void addCategory(File file,Category category) {
        System.out.println(file.getTotalSpace());
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        executor.execute(() -> {
            exchangeWebService.addCategoryName(category, managementTokenAndUSer.getCurrentUser().getId()).enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {
                    if (response.isSuccessful()) {
                        executor.execute(() -> {
                            exchangeWebService.uploadCategory(filePart, managementTokenAndUSer.getCurrentUser().getId()).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        });
                    } else {
                        Tools.backgroundThreadShortToast("This category already exists");
                    }

                }

                @Override
                public void onFailure(Call<Category> call, Throwable t) {

                }
            });
        });
    }

    public MutableLiveData<List<Category>> getAllCategory() {
        MutableLiveData<List<Category>> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.getAllCategory().enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                    data.postValue(response.body());
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {

                }
            });
        });
        return data;
    }

    public MutableLiveData<List<Category>> getMyCategories() {
        MutableLiveData<List<Category>> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.getMyCategories(managementTokenAndUSer.getCurrentUser().getId()).enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                    data.postValue(response.body());
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {

                }
            });
        });
        return data;
    }

    public MutableLiveData<List<Category>> deleteCategory() {
        MutableLiveData<List<Category>> data = new MutableLiveData<>();
        executor.execute(() -> {
            exchangeWebService.deleteCategory(managementTokenAndUSer.getCurrentUser().getId()).enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                    data.postValue(response.body());
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {

                }
            });
        });

        return data;

    }

}
