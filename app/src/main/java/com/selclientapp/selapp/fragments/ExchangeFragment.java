package com.selclientapp.selapp.fragments;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.view_models.ExchangeViewModel;
import com.selclientapp.selapp.views.ExchangeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ExchangeFragment extends Fragment {

    //FOR DESIGN
    @BindView(R.id.fragment_exchange_recycler_view)
    RecyclerView recyclerView;

    //FOR DATA
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ExchangeViewModel exchangeViewModel;
    private List<Exchange> exchanges;
    private ExchangeAdapter adapter;

    public ExchangeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        ButterKnife.bind(this, view);
        this.configureRecyclerView();
        this.configureDagger();
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
        exchangeViewModel.init();
        exchangeViewModel.getAllExchanges().observe(this, allExchanges -> updateUI(allExchanges));
    }

    private void configureRecyclerView() {
        this.exchanges = new ArrayList<>();
        this.adapter = new ExchangeAdapter(this.exchanges);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUI(List<Exchange> exchanges) {
        this.exchanges.addAll(exchanges);
        adapter.notifyDataSetChanged();
    }

}
