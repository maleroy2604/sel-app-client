package com.selclientapp.selapp.view_models;

import android.arch.lifecycle.ViewModel;

import com.selclientapp.selapp.api.TokenBody;
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.repositories.TokenRepository;
import com.selclientapp.selapp.repositories.UserRepository;

import javax.inject.Inject;


public class LoginAndSignUpViewModel extends ViewModel {

    private TokenRepository tokenRepo;
    private UserRepository userRepo;

    @Inject
    public LoginAndSignUpViewModel(TokenRepository tokenRepo, UserRepository userRepo) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
    }

    // ----

    public void getTokenAndSaveIt(String username, String password) {
        tokenRepo.getTokenAndSaveIt(new TokenBody(username, password));
    }

    public void saveUser(User user) {
        userRepo.saveUser(user);
    }

}
