package com.selclientapp.selapp.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.selclientapp.selapp.di.key.ViewModelKey;
import com.selclientapp.selapp.view_models.ExchangeViewModel;
import com.selclientapp.selapp.view_models.FactoryViewModel;
import com.selclientapp.selapp.view_models.LoginAndSignUpViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelmodule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginAndSignUpViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginAndSignUpViewModel repoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ExchangeViewModel.class)
    abstract ViewModel bindExchangeViewModel(ExchangeViewModel repoViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}
