package com.selclientapp.selapp.di.module;

import com.selclientapp.selapp.activities.HomeActivity;
import com.selclientapp.selapp.activities.MainActivity;
import com.selclientapp.selapp.fragments.SortingBottomSheetFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract HomeActivity contributeHomeActivity();
}
