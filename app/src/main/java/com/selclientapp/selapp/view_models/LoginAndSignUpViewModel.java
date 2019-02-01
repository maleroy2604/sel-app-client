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

    private final TokenRepository tokenRepo;
    private final UserRepository userRepo;
    private LiveData<SelApiToken> selApiTokenLiveData;
    private LiveData<User> userLiveData;

    @Inject
    public LoginAndSignUpViewModel(TokenRepository tokenRepo, UserRepository userRepo) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
    }

    // ----

    public void getTokenAndSaveIt(String username, String password) {
        selApiTokenLiveData = tokenRepo.getTokenAndSaveIt(new TokenBody(username, password));
    }

    public void saveUser(User user) {
        userLiveData = userRepo.saveUser(user);
    }

    public LiveData<SelApiToken> getSelApiTokenLiveData() {
        return selApiTokenLiveData;
    }

   public LiveData<User> getUserLiveData(){return userLiveData;}

}
