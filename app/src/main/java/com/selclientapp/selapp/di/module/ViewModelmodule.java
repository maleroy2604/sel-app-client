package com.selclientapp.selapp.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.selclientapp.selapp.di.key.ViewModelKey;
import com.selclientapp.selapp.view_models.ExchangeOcurenceViewModel;
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
    @IntoMap
    @ViewModelKey(ExchangeOcurenceViewModel.class)
    abstract ViewModel bindExchangeOcurenceViewModel(ExchangeOcurenceViewModel repoViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}
