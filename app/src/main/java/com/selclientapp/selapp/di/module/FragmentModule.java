package com.selclientapp.selapp.di.module;

import com.selclientapp.selapp.fragments.ExchangeFragment;
import com.selclientapp.selapp.fragments.LoginFragment;
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
    abstract ExchangeFragment contributeExchange();

}
