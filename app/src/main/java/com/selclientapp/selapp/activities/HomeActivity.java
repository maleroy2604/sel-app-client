package com.selclientapp.selapp.activities;


import android.os.Bundle;

import com.bumptech.glide.Glide;


import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;


import com.google.android.material.navigation.NavigationView;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.fragments.AddExchangeFragment;

import com.selclientapp.selapp.fragments.EditImageProfileFragment;
import com.selclientapp.selapp.fragments.EditProfileFragment;
import com.selclientapp.selapp.fragments.ExchangeFragment;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.utils.ExchangeListener;
import com.selclientapp.selapp.view_models.LoginAndSignUpViewModel;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;


import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class HomeActivity extends AppCompatActivity implements HasSupportFragmentInjector, AddExchangeFragment.AddExchangeListener, NavigationView.OnNavigationItemSelectedListener, ExchangeListener {

    // FOR DESIGN
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawer;
    ImageView imageViewProfile;
    TextView counterHours;
    NavigationView navigationView;
    View headerLayout;
    ImageView imgHeaderDrawer;
    TextView profileName;
    TextView profileHours;

    //FOR DATA
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoginAndSignUpViewModel loginModel;
    private ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();


    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        configureElemToolbar(toolbar);
        configDrawer();
        //managementTokenAndUSer.logOut();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ButterKnife.bind(this);
        this.configureDagger();
        this.configureBottomNavBar(actionBar);
        this.showExchangeFragment(savedInstanceState);
        this.configViewModel();
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureBottomNavBar(ActionBar actionBar) {
        bottomNavigationView = findViewById(R.id.home_activity_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        showAddExchangeFragment();
                        break;

                    case R.id.action_log_out:
                        break;

                    case R.id.action_home:
                        getSupportFragmentManager().popBackStack();
                        actionBar.show();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animeSlideDown();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                bottomNavigationView.setVisibility(View.VISIBLE);
                return false;
            }
        });
        searchView.setImeOptions(searchView.getImeOptions() | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ExchangeFragment exchangeFragment = (ExchangeFragment) getSupportFragmentManager().findFragmentByTag("fragment_exchange");
                exchangeFragment.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void configureElemToolbar(Toolbar toolbar) {
        imageViewProfile = toolbar.findViewById(R.id.image_view_profile);
        counterHours = toolbar.findViewById(R.id.txt_counter_hours);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();
        switch (item.getItemId()) {
            case R.id.nav_logout:
                loginModel.logout();
                loginModel.getUserLiveData().observe(this, user -> {
                    managementTokenAndUSer.logOut();
                });
                break;
            case R.id.nav_edit_profile:
                showEditProfileFragment();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void configViewModel() {
        loginModel = ViewModelProviders.of(this, viewModelFactory).get(LoginAndSignUpViewModel.class);
        setElemProfile();
    }

    private void configDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerLayout = navigationView.getHeaderView(0);
        imgHeaderDrawer = headerLayout.findViewById(R.id.image_view_profile);
        configImgHeaderDrawer();
        profileName = headerLayout.findViewById(R.id.profile_name);
        profileHours = headerLayout.findViewById(R.id.hours_profile);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    public void configImgHeaderDrawer() {
        imgHeaderDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditImageFragment();
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    // -----------------
    // ACTION
    // -----------------

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count != 0) {
            getSupportFragmentManager().popBackStack();
            bottomNavigationView.setSelectedItemId(R.id.action_home);
            ActionBar actionBar = (this).getSupportActionBar();
            actionBar.show();
            setElemProfile();
            animeSlideUp();
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void showAddExchangeFragment() {
        AddExchangeFragment fragment = new AddExchangeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_home_container, fragment, "fragment_add_exchange").addToBackStack("fragment_add_exchange").commit();
    }

    private void showExchangeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            ExchangeFragment fragment = new ExchangeFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_home_container, fragment, "fragment_exchange")
                    .commit();
        }
    }

    private void showEditProfileFragment() {
        EditProfileFragment fragment = new EditProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_home_container, fragment, "fragment_edit_profile").addToBackStack("fragment_edit_profile").commit();
    }

    private void showEditImageFragment() {
        EditImageProfileFragment fragment = new EditImageProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_home_container, fragment, "fragment_edit_image_profile").addToBackStack("fragment_edit_image_profile").commit();
    }

    @Override
    public void restartLoader() {
        ExchangeFragment exchangeFragment = (ExchangeFragment) getSupportFragmentManager().findFragmentByTag("fragment_exchange");
        exchangeFragment.refreshExchanges();
    }

    @Override
    public void refreshExchange() {
        ExchangeFragment exchangeFragment = (ExchangeFragment) getSupportFragmentManager().findFragmentByTag("fragment_exchange");
        exchangeFragment.restartLoader();
    }

    @Override
    public void addExchange(Exchange exchange) {
        ExchangeFragment exchangeFragment = (ExchangeFragment) getSupportFragmentManager().findFragmentByTag("fragment_exchange");
        exchangeFragment.addExchangeToList(exchange);
    }

    @Override
    public void updateExchange(Exchange exchange) {
        ExchangeFragment exchangeFragment = (ExchangeFragment) getSupportFragmentManager().findFragmentByTag("fragment_exchange");
        exchangeFragment.updateExchangeToList(exchange);
    }

    private void animeSlideDown() {
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        bottomNavigationView.startAnimation(slideDown);
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void animeSlideUp() {
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        bottomNavigationView.startAnimation(slideUp);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void setElemProfile() {
        loginModel.getUser(managementTokenAndUSer.getCurrentUser().getId());
        loginModel.getUserLiveData().observe(this, user -> {
            String hours = getString(R.string.toolbar_txt_counter_hours, user.getCounterhours());
            Glide.with(this).load(user.getAvatarurl()).into(imgHeaderDrawer);
            Glide.with(this).load(user.getAvatarurl()).into(imageViewProfile);
            counterHours.setText(hours);
            profileName.setText(user.getUsername());
            profileHours.setText(hours);
        });
    }
}


