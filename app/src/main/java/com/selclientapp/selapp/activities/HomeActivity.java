package com.selclientapp.selapp.activities;

import android.app.Dialog;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import androidx.appcompat.widget.SearchView;

import android.widget.TextView;

import com.selclientapp.selapp.App;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.fragments.AddExchangeFragment;
import com.selclientapp.selapp.fragments.EditExchangeFragment;

import com.selclientapp.selapp.fragments.ExchangeManagementOcurence;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.model.ExchangeOcurence;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.utils.ItemClickSupport;
import com.selclientapp.selapp.utils.Tools;
import com.selclientapp.selapp.view_models.ExchangeOcurenceViewModel;
import com.selclientapp.selapp.view_models.ExchangeViewModel;
import com.selclientapp.selapp.view_models.LoginAndSignUpViewModel;
import com.selclientapp.selapp.views.ExchangeAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class HomeActivity extends AppCompatActivity implements HasSupportFragmentInjector, ExchangeAdapter.Listener {


    //FOR DESIGN
    @BindView(R.id.fragment_exchange_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_exchange_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    BottomNavigationView bottomNavigationView;
    TextView counterHours;


    //FOR DATA
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ExchangeViewModel exchangeViewModel;
    private ExchangeOcurenceViewModel exchangeOcurenceViewModel;
    private LoginAndSignUpViewModel loginAndSignUpViewModel;
    private ArrayList<Exchange> exchanges = new ArrayList<>();
    private ExchangeAdapter adapter;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        ButterKnife.bind(this);
        this.configureDagger();
        this.configureBottomNavBar(savedInstanceState, actionBar);
        counterHours = toolbar.findViewById(R.id.toolbar_hours);
        configOnclickRecyclerView();
        configureViewmodel();
        configureRefreshLayout();
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

    private void configureBottomNavBar(Bundle savedInstanceState, ActionBar actionBar) {
        bottomNavigationView = findViewById(R.id.home_activity_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        showAddExchangeFragment(savedInstanceState);
                        break;

                    case R.id.action_log_out:
                        ManagementTokenAndUSer.logOut();
                        Intent intent = new Intent(App.context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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

    private void configureViewmodel() {
        loginAndSignUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginAndSignUpViewModel.class);
        exchangeViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeViewModel.class);
        exchangeOcurenceViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeOcurenceViewModel.class);
        refreshExchanges();
        if (ManagementTokenAndUSer.contains("HOURS")) {
            counterHours.setText("Hours : " + ManagementTokenAndUSer.getHours());
        } else {
            refreshHours();
        }
    }

    private void configureRecyclerView() {
        this.adapter = new ExchangeAdapter(this.exchanges, this);
        recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
    }

    private void configureRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshExchanges();
            }
        });

        swipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (recyclerView.getScrollY() == 0)
                            swipeRefreshLayout.setEnabled(true);
                        else
                            swipeRefreshLayout.setEnabled(false);
                    }
                });
    }

    private void configOnclickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_exchange_item).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Exchange exchange = adapter.getExchange(position);
            }
        });
    }

    @Override
    public void onClickDialog(int position) {
        Dialog dialog = new Dialog(this);
        Exchange exchange = adapter.getExchange(position);
        dialog.setContentView(R.layout.dialog_options);
        TextView textDelete;
        TextView textEdit;
        TextView textManagement;
        if (exchange.getOwner() == ManagementTokenAndUSer.getCurrentId()) {
            textDelete = dialog.findViewById(R.id.dialog_option_delete);
            textEdit = dialog.findViewById(R.id.dialog_option_edit);
            textManagement = dialog.findViewById(R.id.dialog_option_management);

            textDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteExchange(exchange);
                    dialog.dismiss();
                }
            });

            textEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditExchangeFragment fragment = new EditExchangeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("exchange", exchange);
                    fragment.setArguments(bundle);
                    showEditExchangeFragment(fragment);
                    dialog.dismiss();
                }
            });

            textManagement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (exchange.getExchangeocurence().isEmpty()) {
                        Tools.backgroundThreadShortToast("Nobody is enroll to your exchange");
                    } else {
                        showExchangeManagment(position);
                        dialog.dismiss();
                    }
                }
            });

        } else {
            dialog.setContentView(R.layout.dialo_options_user);
            TextView textEnroll = dialog.findViewById(R.id.dialog_option_enroll);
            TextView textWithDraw = dialog.findViewById(R.id.dialog_option_withdraw);

            textEnroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExchangeOcurence exchangeOcurence = new ExchangeOcurence(0, ManagementTokenAndUSer.getCurrentId(), exchange.getId());
                    if (isValidEnroll(exchange)) {
                        exchange.getExchangeocurence().add(exchangeOcurence);
                        updateUI(exchanges);
                        addExchangeOcurence(exchangeOcurence);
                        dialog.dismiss();
                    }
                }
            });

            textWithDraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isValidWithDraw(exchange)) {
                        deleteExchangeOcurence(findExchangeOcurence(exchange));
                        dialog.dismiss();
                    }
                }
            });
        }
        TextView discussion = dialog.findViewById(R.id.dialog_option_discussion);
        ImageButton imageButtonClose = dialog.findViewById(R.id.dialog_option_close);
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }


    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUI(List<Exchange> exchanges) {
        if (exchanges != null) {
            swipeRefreshLayout.setRefreshing(false);
            this.exchanges.clear();
            Collections.reverse(exchanges);
            this.exchanges.addAll(exchanges);
            adapter.notifyDataSetChanged();
        } else {
            Tools.backgroundThreadShortToast("Invalid credentials");
        }

    }


    // -----------------
    // ACTION
    // -----------------

    private void showEditExchangeFragment(EditExchangeFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_home_container, fragment, null).addToBackStack("fragment_edit_exchange").commit();
    }

    private void deleteExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        exchangeOcurenceViewModel.deleteExchangeOcurence(exchangeOcurence);
        exchangeOcurenceViewModel.getExchangeOcurenceLiveData().observe(this, ocurence -> {
            refreshExchanges();
        });
    }


    private void deleteExchange(Exchange exchange) {
        this.exchanges.remove(exchange);
        exchangeViewModel.deleteOneExchange(exchange.getId());
        exchangeViewModel.getExchangeLiveData().observe(this, ex -> {
            refreshExchanges();
        });


    }

    private void addExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        exchangeOcurenceViewModel.addExchangeOcurence(exchangeOcurence);
        exchangeOcurenceViewModel.getExchangeOcurenceLiveData().observe(this, ocurence -> {
            refreshExchanges();
        });
    }

    private void showExchangeManagment(int position) {
        Exchange exchange = adapter.getExchange(position);
        ExchangeManagementOcurence fragment = new ExchangeManagementOcurence();
        Bundle bundle = new Bundle();
        bundle.putParcelable("exchange", exchange);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_home_container, fragment, null).addToBackStack("fragment_management").commit();
    }


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count != 0) {
            getSupportFragmentManager().popBackStack();
            bottomNavigationView.setSelectedItemId(R.id.action_home);
            ActionBar actionBar = (this).getSupportActionBar();
            actionBar.show();
            // swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void showAddExchangeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            AddExchangeFragment fragment = new AddExchangeFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_home_container, fragment, null).addToBackStack("fragment_exchange").commit();
        }

    }

    // -----------------
    // UTILS
    // -----------------

    private ExchangeOcurence findExchangeOcurence(Exchange exchange) {
        for (ExchangeOcurence exchangeOcurence : exchange.getExchangeocurence()) {
            if (exchangeOcurence.getParticipantId() == ManagementTokenAndUSer.getCurrentId()) {
                return exchangeOcurence;
            }
        }
        return null;
    }

    private boolean isValidEnroll(Exchange exchange) {
        if (findExchangeOcurence(exchange) != null) {
            Tools.backgroundThreadShortToast("You are already enroll to this exchange");
            return false;
        } else if (checkDate(exchange)) {
            Tools.backgroundThreadShortToast("The inscription are closed");
            return false;
        }
        return true;
    }

    private boolean isValidWithDraw(Exchange exchange) {
        if (findExchangeOcurence(exchange) == null) {
            Tools.backgroundThreadShortToast("You have to enroll first");
            return false;
        } else if (checkDate(exchange)) {
            Tools.backgroundThreadShortToast("The inscription are closed");
            return false;
        }
        return true;
    }


    private boolean checkDate(Exchange exchange) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date exchangeDate = null;
        try {
            exchangeDate = format.parse(exchange.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return exchangeDate.getTime() < new Date().getTime();
    }

    private void refreshHours() {
        loginAndSignUpViewModel.getUser(ManagementTokenAndUSer.getCurrentTokenBody());
        loginAndSignUpViewModel.getUserLiveData().observe(this, user -> {
            counterHours.setText("Hours : " + user.getCounterhours());
        });
    }

    private void refreshExchanges() {
        exchangeViewModel.init();
        exchangeViewModel.getAllExchanges().observe(this, exchanges -> {
            this.adapter = new ExchangeAdapter(exchanges, this);
            recyclerView.setHasFixedSize(true);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            this.recyclerView.setAdapter(this.adapter);
            updateUI(exchanges);

        });
    }
}

