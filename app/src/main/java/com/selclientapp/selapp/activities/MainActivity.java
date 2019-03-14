package com.selclientapp.selapp.activities;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.fragments.LoginFragment;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.configureDagger();
        if (ManagementTokenAndUSer.contains("TOKEN")) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            this.showLoginFragment(savedInstanceState);
        }

    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    // ---

    private void showLoginFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            LoginFragment fragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, null)
                    .commit();
        }
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }
}
