package com.selclientapp.selapp.fragments;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.model.ExchangeOcurence;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.utils.ItemClickSupport;
import com.selclientapp.selapp.utils.Tools;
import com.selclientapp.selapp.view_models.ExchangeOcurenceViewModel;
import com.selclientapp.selapp.view_models.ExchangeViewModel;
import com.selclientapp.selapp.views.ExchangeAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ExchangeFragment extends Fragment implements ExchangeAdapter.Listener {


    // FOR DESIGN
    @BindView(R.id.fragment_exchange_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_exchange_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    // FOR DATA
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ExchangeViewModel exchangeViewModel;
    private ExchangeOcurenceViewModel exchangeOcurenceViewModel;
    private ArrayList<Exchange> exchanges = new ArrayList<>();
    private ExchangeAdapter adapter;
    private int numberLimit = 15;
    private static final int LIMIT = 15;

    public ExchangeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        ButterKnife.bind(this, view);
        this.configureDagger();
        this.configureRefreshLayout();
        this.configureViewmodel();
        return view;
    }


    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureViewmodel() {
        exchangeViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeViewModel.class);
        exchangeOcurenceViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeOcurenceViewModel.class);
        initRecyclerView();
    }

    private void configureRecyclerView(List<Exchange> exchanges) {
        this.exchanges.addAll(exchanges);
        this.adapter = new ExchangeAdapter(this.exchanges, this);
        recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(this.adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (numberLimit <= exchanges.size() + LIMIT) {
                    numberLimit += LIMIT;
                    refreshExchanges();
                }
            }
        });
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
        Dialog dialog = new Dialog(getActivity());
        Exchange exchange = adapter.getExchange(position);
        dialog.setContentView(R.layout.dialog_options);
        TextView textDelete;
        TextView textEdit;
        TextView textManagement;

        if (exchange.getOwner() == ManagementTokenAndUSer.getCurrentId()) {
            textDelete = dialog.findViewById(R.id.dialog_option_delete);
            textEdit = dialog.findViewById(R.id.dialog_option_edit);
            textManagement = dialog.findViewById(R.id.dialog_option_management);

            configTextDelete(textDelete, exchange, dialog);
            configTextEdit(textEdit, exchange, dialog);
            configTextManagement(textManagement, exchange, dialog, position);

        } else {
            dialog.setContentView(R.layout.dialog_options_user);
            TextView textInscription = dialog.findViewById(R.id.dialog_option_inscription);
            configTextInscription(textInscription, exchange, dialog);
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

    private void configTextDelete(TextView textDel, Exchange ex, Dialog dialog) {
        textDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteExchange(ex);
                dialog.dismiss();
            }
        });
    }

    private void configTextEdit(TextView textEdit, Exchange ex, Dialog dialog) {
        textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditExchangeFragment fragment = new EditExchangeFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("exchange", ex);
                fragment.setArguments(bundle);
                showEditExchangeFragment(fragment);
                dialog.dismiss();
            }
        });
    }

    private void configTextManagement(TextView textManagement, Exchange ex, Dialog dialog, int position) {
        textManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ex.getExchangeocurence().isEmpty()) {
                    Tools.backgroundThreadShortToast("Nobody is enroll to your exchange");
                } else {
                    showExchangeManagment(position);
                    dialog.dismiss();
                }
            }
        });
    }

    private void configTextInscription(TextView textInscription, Exchange ex, Dialog dialog) {
        if (findExchangeOcurence(ex) == null) {
            configEnroll(textInscription, ex, dialog);
        } else {
            configWithdraw(textInscription, ex, dialog);
        }
    }

    private void configEnroll(TextView textInscription, Exchange ex, Dialog dialog) {
        textInscription.setText("Enroll");
        textInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeOcurence exchangeOcurence = new ExchangeOcurence(0, ManagementTokenAndUSer.getCurrentId(), ex.getId());
                if (isValid(ex)) {
                    ex.addExchangeOcurence(exchangeOcurence);
                    adapter.notifyDataSetChanged();
                    addExchangeOcurence(exchangeOcurence);
                    dialog.dismiss();
                }
            }
        });
    }

    private void configWithdraw(TextView textInscription, Exchange ex, Dialog dialog) {
        textInscription.setText("Withdraw");
        textInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(ex)) {
                    ExchangeOcurence exchangeOcurence = findExchangeOcurence(ex);
                    ex.removeExchangeOcurecne(exchangeOcurence);
                    adapter.notifyDataSetChanged();
                    deleteExchangeOcurence(exchangeOcurence);
                    dialog.dismiss();
                }
            }
        });
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUI(List<Exchange> exchanges) {
        if (exchanges != null) {
            swipeRefreshLayout.setRefreshing(false);
            this.exchanges.clear();
            this.exchanges.addAll(exchanges);
            adapter.updateList(exchanges);
        } else {
            Tools.backgroundThreadShortToast("Invalid credentials");
        }

    }
    // -----------------
    // ACTION
    // -----------------

    private void showEditExchangeFragment(EditExchangeFragment fragment) {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.home_activity_bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        getFragmentManager().beginTransaction()
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
        adapter.notifyDataSetChanged();
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
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_home_container, fragment, null).addToBackStack("fragment_management").commit();
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

    private boolean isValid(Exchange exchange) {
        if (checkDate(exchange)) {
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

    public void refreshExchanges() {
        exchangeViewModel.init(numberLimit);
        exchangeViewModel.getAllExchanges().observe(this, exchanges -> {
            updateUI(exchanges);
        });
    }

    private void initRecyclerView() {
        exchangeViewModel.init(numberLimit);
        exchangeViewModel.getAllExchanges().observe(this, exchanges -> {
            configureRecyclerView(exchanges);
        });
    }

    public Filter getFilter() {
        return adapter.getFilter();
    }

    public void addExchangeToList(Exchange exchange) {
        this.exchanges.add(0, exchange);
        this.adapter.notifyDataSetChanged();
    }

    public void updateExchangeToList(Exchange exchange) {
        for (Exchange ex : this.exchanges) {
            if (ex.getOwner() == ManagementTokenAndUSer.getCurrentId()) {
                ex = exchange;
            }
        }
        this.adapter.notifyDataSetChanged();
    }

    public void setNumberLimit(int numberLimit) {
        if (numberLimit == LIMIT) {
            this.numberLimit = numberLimit;
        }
    }
}
