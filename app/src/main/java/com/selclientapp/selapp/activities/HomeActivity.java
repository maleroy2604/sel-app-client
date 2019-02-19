package com.selclientapp.selapp.activities;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.selclientapp.selapp.App;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.fragments.AddExchangeFragment;
import com.selclientapp.selapp.fragments.EditExchangeFragment;

import com.selclientapp.selapp.fragments.ExchangeManagementOcurence;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.model.ExchangeOcurence;
import com.selclientapp.selapp.repositories.ManagementToken;
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

public class HomeActivity extends AppCompatActivity implements HasSupportFragmentInjector, ExchangeAdapter.Listener, SearchView.OnQueryTextListener {


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
    private List<Exchange> exchanges;
    private ExchangeAdapter adapter;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        ButterKnife.bind(this);
        this.configureDagger();
        this.configureBottomNavBar(savedInstanceState, actionBar);
        counterHours = toolbar.findViewById(R.id.toolbar_hours);
        configureViewmodel();
        configOnclickRecyclerView();
        configureRefreshLayout();
        configureRecyclerView();
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
                        ManagementToken.logOut();
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
    // -----------------
    // CONFIGURATION
    // -----------------


    private void configureViewmodel() {
        loginAndSignUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginAndSignUpViewModel.class);
        exchangeViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeViewModel.class);
        exchangeOcurenceViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeOcurenceViewModel.class);

        refreshHours();
        refreshExchanges();

    }

    private void configureRecyclerView() {
        this.exchanges = new ArrayList<>();
        this.adapter = new ExchangeAdapter(this.exchanges, this);
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
        if (exchange.getOwner() == ManagementToken.getCurrentId()) {
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
                    ExchangeOcurence exchangeOcurence = new ExchangeOcurence(0, ManagementToken.getCurrentId(), exchange.getId());

                    if (isValidEnroll(exchange)) {
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

        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText.toLowerCase();
        List<Exchange> newList = new ArrayList<>();

        for (Exchange exchange : exchanges) {
            if ((exchange.getName().toLowerCase().contains(newText))) {
                newList.add(exchange);
            }
        }
        swipeRefreshLayout.setRefreshing(false);
        adapter.updateList(newList);

        return true;
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUI(List<Exchange> exchanges) {
        swipeRefreshLayout.setRefreshing(false);
        this.exchanges.clear();
        Collections.reverse(exchanges);
        this.exchanges.addAll(exchanges);
        adapter.notifyDataSetChanged();
    }

    // -----------------
    // ACTION
    // -----------------

    private void getAllExchanges() {
        exchangeViewModel.init();
        exchangeViewModel.getAllExchanges().observe(this, allExchanges -> updateUI(allExchanges));
    }

    private void showEditExchangeFragment(EditExchangeFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_home_container, fragment, null).addToBackStack("fragment_edit_exchange").commit();
    }

    private void deleteExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        if (ManagementToken.hasToRefreshToken(new Date())) {
            exchangeViewModel.getTokenAndSaveIt(ManagementToken.getCurrentTokenBody());
            exchangeViewModel.getSelApiTokenLiveData().observe(this, token -> {
                exchangeOcurenceViewModel.deleteExchangeOcurence(exchangeOcurence);
                exchangeOcurenceViewModel.getExchangeOcurenceLiveData().observe(this, ocurence -> {
                    refreshExchanges();
                });
            });
        } else {
            exchangeOcurenceViewModel.deleteExchangeOcurence(exchangeOcurence);
            exchangeOcurenceViewModel.getExchangeOcurenceLiveData().observe(this, ocurence -> {
                refreshExchanges();
            });
        }
    }

    private void deleteExchange(Exchange exchange) {
        exchangeViewModel.deleteOneExchange(exchange.getId());
        exchangeViewModel.getAllExchanges().observe(this, exchanges -> updateUI(exchanges));
    }

    private void addExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        if (ManagementToken.hasToRefreshToken(new Date())) {
            exchangeViewModel.getTokenAndSaveIt(ManagementToken.getCurrentTokenBody());
            exchangeViewModel.getSelApiTokenLiveData().observe(this, token -> {
                exchangeOcurenceViewModel.addExchangeOcurence(exchangeOcurence);
                exchangeOcurenceViewModel.getExchangeOcurenceLiveData().observe(this, ocurence -> {
                    refreshExchanges();
                });
            });
        } else {
            exchangeOcurenceViewModel.addExchangeOcurence(exchangeOcurence);
            exchangeOcurenceViewModel.getExchangeOcurenceLiveData().observe(this, ocurence -> {
                refreshExchanges();
            });
        }

    }

    private void refreshExchanges() {
        exchangeViewModel.init();
        exchangeViewModel.getAllExchanges().observe(this,exchanges -> {
            updateUI(exchanges);
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
            refreshExchanges();
            refreshHours();
            bottomNavigationView.setSelectedItemId(R.id.action_home);
            ActionBar actionBar = (this).getSupportActionBar();
            actionBar.show();
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    private void showAddExchangeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            AddExchangeFragment fragment = new AddExchangeFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_home_container, fragment, null).addToBackStack("fragment_exchange").commit();
        }

    }

    private void refreshHours() {
        loginAndSignUpViewModel.getUser();
        loginAndSignUpViewModel.getUserLiveData().observe(this, user -> {
            counterHours.setText("Hours : " + user.getCounterhours());
        });

    }

    // -----------------
    // UTILS
    // -----------------

    private ExchangeOcurence findExchangeOcurence(Exchange exchange) {
        for (ExchangeOcurence exchangeOcurence : exchange.getExchangeocurence()) {
            if (exchangeOcurence.getParticipantId() == ManagementToken.getCurrentId()) {
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

}

