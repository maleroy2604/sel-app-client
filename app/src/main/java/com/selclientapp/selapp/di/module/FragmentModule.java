package com.selclientapp.selapp.di.module;

import com.selclientapp.selapp.fragments.AddExchangeFragment;
import com.selclientapp.selapp.fragments.DetailExchangeFragment;
import com.selclientapp.selapp.fragments.EditExchangeFragment;
import com.selclientapp.selapp.fragments.EditProfileFragment;
import com.selclientapp.selapp.fragments.ExchangeFragment;
import com.selclientapp.selapp.fragments.ExchangeManagementOcurence;
import com.selclientapp.selapp.fragments.LoginFragment;
import com.selclientapp.selapp.fragments.NoConnexionFragment;
import com.selclientapp.selapp.fragments.SignUpFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract LoginFragment contributeLogin();

    @ContributesAndroidInjector
    abstract SignUpFragment contributeSingUp();

    @ContributesAndroidInjector
    abstract EditProfileFragment contributeEditProfile();

    @ContributesAndroidInjector
    abstract ExchangeFragment contributeExchangeFragment();

    @ContributesAndroidInjector
    abstract ExchangeManagementOcurence contributeExchangeManagementOcurence();

    @ContributesAndroidInjector
    abstract AddExchangeFragment contributeAddExchange();

    @ContributesAndroidInjector
    abstract EditExchangeFragment contributeEditExchange();

    @ContributesAndroidInjector
    abstract NoConnexionFragment contributeNoConnexionFragment();

    @ContributesAndroidInjector
    abstract DetailExchangeFragment contributeDetailExchangeFragment();
}
