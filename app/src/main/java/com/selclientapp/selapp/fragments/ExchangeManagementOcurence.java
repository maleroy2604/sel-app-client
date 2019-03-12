package com.selclientapp.selapp.fragments;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.model.ExchangeOcurence;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.view_models.ExchangeOcurenceViewModel;
import com.selclientapp.selapp.views.OcurenceAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ExchangeManagementOcurence extends Fragment implements OcurenceAdapter.Listener {

    //FOR DESIGN
    @BindView(R.id.fragment_exchange_management_recycler_view)
    RecyclerView recyclerView;


    //FOR DATA
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ExchangeOcurenceViewModel exchangeOcurenceViewModel;
    private List<ExchangeOcurence> exchangeOcurences;
    private OcurenceAdapter ocurenceAdapter;

    public ExchangeManagementOcurence() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_management, container, false);
        ButterKnife.bind(this, view);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        this.configureRecyclerView();
        this.configureDagger();
        configureViewmodel();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        Exchange exchange = bundle.getParcelable("exchange");
        this.exchangeOcurences.clear();
        this.exchangeOcurences.addAll(exchange.getExchangeocurence());
        ocurenceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickSendHours(int position) {
        ExchangeOcurence exchangeOcurence = ocurenceAdapter.getExchangeOcurence(position);
        updateExchangeOcurence(exchangeOcurence);
    }

    @Override
    public void onClickRemoveParticipant(int position) {
        ExchangeOcurence exchangeOcurence = ocurenceAdapter.getExchangeOcurence(position);
        deleteExchangeOcurence(exchangeOcurence);
    }

    // -----------------
    // CONFIGURATION
    // -----------------
    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureRecyclerView() {
        this.exchangeOcurences = new ArrayList<>();
        this.ocurenceAdapter = new OcurenceAdapter(this.exchangeOcurences, this);
        this.recyclerView.setAdapter(this.ocurenceAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureViewmodel() {
        exchangeOcurenceViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeOcurenceViewModel.class);
    }

    private void updateExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        int id = exchangeOcurence.getExchangeId();
        exchangeOcurenceViewModel.updateExchangeOcurence(exchangeOcurence);
        exchangeOcurenceViewModel.getExchangeOcurenceLiveData().observe(this, ocurence -> {
            refreshExchangeOcurence(id);
        });
    }

    private void deleteExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        exchangeOcurenceViewModel.deleteExchangeOcurence(exchangeOcurence);
        exchangeOcurenceViewModel.getExchangeOcurenceLiveData().observe(this, ocurence -> {
            refreshExchangeOcurence(exchangeOcurence.getExchangeId());
        });
    }

    private void refreshExchangeOcurence(Integer exchangeId) {
        exchangeOcurenceViewModel.getAllExchangeOcurence(exchangeId);
        exchangeOcurenceViewModel.getListExchangeOcurence().observe(this, ocurences -> {
            updateUi(ocurences);
        });
    }

    private void updateUi(List<ExchangeOcurence> exchangeOcurences) {
        this.exchangeOcurences.clear();
        this.exchangeOcurences.addAll(exchangeOcurences);
        ocurenceAdapter.notifyDataSetChanged();
    }


}
