package com.selclientapp.selapp.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.selclientapp.selapp.model.Message;
import com.selclientapp.selapp.repositories.MessageRepository;

import java.util.List;

import javax.inject.Inject;

public class MessageViewModel extends ViewModel {
    private final MessageRepository messageRepository;
    private LiveData<List<Message>> listMessageLiveData;

    @Inject
    public MessageViewModel(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void sendMessage(Message message) {
        listMessageLiveData = messageRepository.sendMessage(message);
    }

    public void getMessagesExchange(int exchangeId) {
        listMessageLiveData = messageRepository.getMessagesExchange(exchangeId);
    }

    public LiveData<List<Message>> getListMessageLiveData() {
        return listMessageLiveData;
    }
}
