package com.selclientapp.selapp.api;

import com.selclientapp.selapp.model.Category;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.utils.NumberLimits;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ExchangeWebService {
    @POST("exchange/{id}")
    Call<Exchange> saveExchange(@Path("id") int id, @Body Exchange exchange);

    @POST("exchanges")
    Call<List<Exchange>> getAllExchange(@Body NumberLimits numberLimit);

    @DELETE("exchange/{id}")
    Call<Exchange> deleteExchange(@Path("id") int id);

    @PUT("exchange/{id}")
    Call<Exchange> updateExchange(@Path("id") int id, @Body Exchange exchange);

    @Multipart
    @POST("uploadimagecategory")
    Call<ResponseBody>uploadCategory(@Part MultipartBody.Part filePart);

    @GET("categories")
    Call<List<Category>>getAllCategory();

    @POST("categories")
    Call<Category>addCategoryName(@Body Category category);
}
