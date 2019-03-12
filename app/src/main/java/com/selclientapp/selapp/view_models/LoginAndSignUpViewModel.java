package com.selclientapp.selapp.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.repositories.TokenBody;
import com.selclientapp.selapp.repositories.UserRepository;

import javax.inject.Inject;


public class LoginAndSignUpViewModel extends ViewModel {
    private final UserRepository userRepo;
    private LiveData<User> userLiveData;
    private LiveData<SelApiToken> selApiTokenLiveData;


    @Inject
    public LoginAndSignUpViewModel(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ----

    public void login(TokenBody tokenBody) {
       selApiTokenLiveData = userRepo.login(tokenBody);
    }

    public void getUser(TokenBody tokenBody){
        userLiveData =userRepo.getUser(tokenBody);
    }

    public void saveUser(User user) {
        userLiveData = userRepo.saveUser(user);
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<SelApiToken> getSelApiTokenLiveData() {
        return selApiTokenLiveData;
    }
}
