package com.selclientapp.selapp.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.repositories.TokenBody;
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.repositories.TokenRepository;
import com.selclientapp.selapp.repositories.UserRepository;

import javax.inject.Inject;


public class LoginAndSignUpViewModel extends ViewModel {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepo;
    private LiveData<SelApiToken> selApiTokenLiveData;
    private LiveData<User> userLiveData;


    @Inject
    public LoginAndSignUpViewModel(UserRepository userRepo, TokenRepository tokenRepository) {
        this.userRepo = userRepo;
        this.tokenRepository = tokenRepository;
    }

    // ----

    public void getTokenAndSaveIt(String username, String password) {
        selApiTokenLiveData = tokenRepository.getTokenAndSaveIt(new TokenBody(username, password));
    }

    public void saveUser(User user) {
        userLiveData = userRepo.saveUser(user);
    }

    public void getUser() {
        userLiveData = userRepo.getUser();
    }

    public LiveData<SelApiToken> getSelApiTokenLiveData() {
        return selApiTokenLiveData;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

}
