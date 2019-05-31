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

    @DELETE("category/{id}")
    Call<List<Category>> deleteCategory(@Path("id") int id);

    @PUT("exchange/{id}")
    Call<Exchange> updateExchange(@Path("id") int id, @Body Exchange exchange);

    @Multipart
    @POST("uploadimagecategory/image/{id}")
    Call<ResponseBody> uploadCategory(@Part MultipartBody.Part filePart, @Path("id") int id);

    @GET("newcategory")
    Call<List<Category>> getAllCategory();

    @GET("mycategorylist/{id}")
    Call<List<Category>> getMyCategories(@Path("id") int id);

    @POST("category/{id}")
    Call<Category> addCategoryName(@Body Category category, @Path("id") int id);

    @Multipart
    @POST("/imagecategory/{id}")
    Call<ResponseBody> updateImagedCategory(@Part MultipartBody.Part filePart, @Path("id") int id);

}
