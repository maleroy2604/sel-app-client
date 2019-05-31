package com.selclientapp.selapp.repositories;

import androidx.lifecycle.MutableLiveData;

import com.selclientapp.selapp.api.MessageWebService;
import com.selclientapp.selapp.model.Message;
import com.selclientapp.selapp.utils.Tools;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageRepository {

    private final MessageWebService messageWebService;
    private final Executor executor;

    @Inject
    public MessageRepository(MessageWebService messageWebService, Executor executor) {
        this.messageWebService = messageWebService;
        this.executor = executor;
    }

    public MutableLiveData<List<Message>> sendMessage(Message message) {
        MutableLiveData<List<Message>> data = new MutableLiveData<>();
        executor.execute(() -> {
            messageWebService.sendMessage(message).enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    if (response.isSuccessful()) {
                        data.postValue(response.body());
                    } else {
                        Tools.backgroundThreadShortToast("Error server can't send your message");
                    }

                }

                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {
                    Tools.backgroundThreadShortToast("Request failed");
                }
            });
        });
        return data;
    }

    public MutableLiveData<List<Message>> getMessagesExchange(int exchangeId) {
        MutableLiveData<List<Message>> data = new MutableLiveData<>();
        executor.execute(() -> {
            messageWebService.getMessagesExchange(exchangeId).enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    if (response.isSuccessful()) {
                        data.postValue(response.body());
                    } else {
                        Tools.backgroundThreadShortToast("Can't get message from the server");
                    }
                }

                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {
                    Tools.backgroundThreadShortToast("Request failed");
                }
            });
        });
        return data;
    }
}
