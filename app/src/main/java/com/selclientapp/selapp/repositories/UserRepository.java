package com.selclientapp.selapp.repositories;

import androidx.lifecycle.MutableLiveData;

import com.selclientapp.selapp.api.ImageWebService;
import com.selclientapp.selapp.api.TokenWebService;
import com.selclientapp.selapp.api.UserWebService;
import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.utils.TokenBody;
import com.selclientapp.selapp.utils.Tools;

import java.io.File;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserWebService userWebService;
    private final TokenWebService tokenWebService;
    private final Executor executor;
    private ManagementTokenAndUSer managementTokenAndUSer;
    private final ImageWebService imageWebService;

    @Inject
    public UserRepository(UserWebService userWebService, Executor executor, TokenWebService tokenWebService, ManagementTokenAndUSer managementTokenAndUSer, ImageWebService imageWebService) {
        this.userWebService = userWebService;
        this.executor = executor;
        this.tokenWebService = tokenWebService;
        this.managementTokenAndUSer = managementTokenAndUSer;
        this.imageWebService = imageWebService;
    }


    public MutableLiveData<User> login(TokenBody tokenBody) {
        final MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            tokenWebService.authenticate(tokenBody).enqueue(new Callback<SelApiToken>() {
                @Override
                public void onResponse(Call<SelApiToken> call, Response<SelApiToken> response) {
                    if (response.isSuccessful()) {
                        managementTokenAndUSer.saveSelApiToken(response.body());
                        data.postValue(response.body().getUser());
                    } else {
                        if (response.body() == null) {
                            data.postValue(null);
                        }

                    }

                }

                @Override
                public void onFailure(Call<SelApiToken> call, Throwable t) {

                }
            });
        });

        return data;
    }

    public MutableLiveData<User> logout() {
        MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            userWebService.logOut().enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    data.postValue(response.body());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        });
        return data;
    }

    public MutableLiveData<User> getUser(int id) {
        final MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            userWebService.getUser(id).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    data.postValue(response.body());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        });
        return data;
    }

    public MutableLiveData<User> saveUser(User user) {
        final MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            userWebService.saveUser(user).enqueue(new Callback<SelApiToken>() {
                @Override
                public void onResponse(Call<SelApiToken> call, Response<SelApiToken> response) {
                    if (response.code() == 201) {
                        managementTokenAndUSer.saveSelApiToken(response.body());
                        data.postValue(response.body().getUser());
                    } else {
                        Tools.backgroundThreadShortToast("User already exists ! ");
                    }
                }

                @Override
                public void onFailure(Call<SelApiToken> call, Throwable t) {

                }
            });
        });

        return data;
    }

    public MutableLiveData<User> uploadImageTest(File file, String avatarUrl) {
        final MutableLiveData<User> data = new MutableLiveData<>();
        if (avatarUrl != null) {
            int lastIndex = avatarUrl.lastIndexOf("/");
            String subString = avatarUrl.substring(lastIndex + 1);
            executor.execute(() -> {
                imageWebService.deleteImage(subString).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image",
                                file.getName(),
                                RequestBody.create(MediaType.parse("multipart/form-data"), file));
                        executor.execute(() -> {
                            imageWebService.uploadImage(filePart).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    data.postValue(response.body());
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                }
                            });
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });
            });

        } else {
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            executor.execute(() -> {
                imageWebService.uploadImage(filePart).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        data.postValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                    }
                });
            });
        }
        return data;
    }


    public MutableLiveData<User> updateUser(User user) {
        final MutableLiveData<User> data = new MutableLiveData<>();
        executor.execute(() -> {
            userWebService.updateUser(managementTokenAndUSer.getCurrentUser().getId(), user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    data.postValue(response.body());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        });
        return data;
    }
}


