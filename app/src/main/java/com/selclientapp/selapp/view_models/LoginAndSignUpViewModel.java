package com.selclientapp.selapp.view_models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.utils.TokenBody;
import com.selclientapp.selapp.repositories.UserRepository;

import java.io.File;

import javax.inject.Inject;


public class LoginAndSignUpViewModel extends ViewModel {
    private final UserRepository userRepo;
    private LiveData<User> userLiveData;
    private LiveData<File> fileLiveData;

    @Inject
    public LoginAndSignUpViewModel(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ----

    public void getUser(int id) {
        userLiveData = userRepo.getUser(id);
    }

    public void login(TokenBody tokenBody) {
        userLiveData = userRepo.login(tokenBody);
    }

    public void logout() {
        userLiveData = userRepo.logout();
    }

    public void saveUser(User user) {
        userLiveData = userRepo.saveUser(user);
    }

    public void uploadImage(File file, String imageProfileName) {
        userRepo.uploadImageTest(file, imageProfileName);
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
}