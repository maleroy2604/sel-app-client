package com.selclientapp.selapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.selclientapp.selapp.App;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.fragments.AddExchangeFragment;
import com.selclientapp.selapp.fragments.ExchangeFragment;
import com.selclientapp.selapp.repositories.ManagementToken;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class HomeActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        this.configureDagger();
        this.showExchangeFragment(savedInstanceState);
        this.configureBottomNavBar(savedInstanceState);
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureBottomNavBar(Bundle savedInstanceState){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.home_activity_bottom_navigation);
        bottomNavigationView.canScrollVertically(1);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        showAddExchangeFragment(savedInstanceState);
                        break;

                    case R.id.action_log_out:
                        ManagementToken.logOut();
                        Intent intent = new Intent(App.context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    case R.id.action_home:
                        showExchangeFragment(savedInstanceState);
                        break;
                }
                return true;
            }
        });
    }








    private void showExchangeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            ExchangeFragment fragment = new ExchangeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_home_container, fragment, null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if(count != 0){
            getSupportFragmentManager().popBackStack();
        }

    }

    private void showAddExchangeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            AddExchangeFragment fragment = new AddExchangeFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_home_container, fragment, null).addToBackStack("fragment_exchange").commit();
        }

    }


}

