package com.selclientapp.selapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

import com.selclientapp.selapp.di.component.DaggerAppComponent;


public class App extends Application implements HasActivityInjector {

    public static final String URL_SERVER ="https://sel-app.herokuapp.com/";
    //"https://sel-app.herokuapp.com/"
    //"http://10.0.2.2:5000/"

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        this.initDagger();
        context = getApplicationContext();
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    // ---

    private void initDagger() {
        DaggerAppComponent.builder().application(this).build().inject(this);
    }
}
