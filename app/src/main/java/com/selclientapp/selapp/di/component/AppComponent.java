package com.selclientapp.selapp.di.component;

import android.app.Application;

import com.selclientapp.selapp.App;
import com.selclientapp.selapp.di.module.ActivityModule;
import com.selclientapp.selapp.di.module.AppModule;
import com.selclientapp.selapp.di.module.FragmentModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules={AndroidSupportInjectionModule.class, ActivityModule.class, FragmentModule.class, AppModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(App app);
}