package com.selclientapp.selapp.fragments;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.repositories.ManagementToken;
import com.selclientapp.selapp.utils.ItemClickSupport;
import com.selclientapp.selapp.view_models.ExchangeViewModel;
import com.selclientapp.selapp.views.ExchangeAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ExchangeFragment extends Fragment implements ExchangeAdapter.Listener {

    //FOR DESIGN
    @BindView(R.id.fragment_exchange_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_exchange_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

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
        this.configureRefreshLayout();
        this.configOnclickRecyclerView();
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
        if (ManagementToken.hasToRefreshToken(new Date())) {
            exchangeViewModel.getTokenAndSaveIt(ManagementToken.getCurrentTokenBody());
            exchangeViewModel.getSelApiTokenLiveData().observe(this, token -> {
                getAllExchanges();
            });
        } else {
            getAllExchanges();
        }

    }

    private void configureRecyclerView() {
        this.exchanges = new ArrayList<>();
        this.adapter = new ExchangeAdapter(this.exchanges, this);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                exchangeViewModel.init();
                exchangeViewModel.getAllExchanges().observe(Objects.requireNonNull(getActivity()), allExchanges -> updateUI(allExchanges));
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

    @Override
    public void onclickDeleteButton(int position) {
        Exchange exchange = adapter.getExchange(position);
        exchangeViewModel.deleteOneExchange(exchange.getId());
        exchangeViewModel.getAllExchanges().observe(getActivity(), allExchanges -> updateUI(allExchanges));
    }

    @Override
    public void onclickEditButton(int position) {
        Exchange exchange = adapter.getExchange(position);
        EditExchangeFragment fragment = new EditExchangeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("exchange",exchange);
        fragment.setArguments(bundle);
        showEditExchangeFragment(fragment);
    }

    // -----------------
    // ACTION
    // -----------------

    private void getAllExchanges() {
        exchangeViewModel.init();
        exchangeViewModel.getAllExchanges().observe(this, allExchanges -> updateUI(allExchanges));
    }

    private void showEditExchangeFragment(EditExchangeFragment fragment) {

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_home_container, fragment, null).addToBackStack("fragment_edit_exchange").commit();
    }
}
