package com.selclientapp.selapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selclientapp.selapp.model.Exchange;

public class EditExchangeFragment extends AddExchangeFragment {

    private Exchange exchange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        exchange = bundle.getParcelable("exchange");
        username.setText(exchange.getName());
        description.setText(exchange.getDescription());
        dateExchange = exchange.getDate().substring(0, 10);
        timeExchange = exchange.getDate().substring(10, 16);
        datePicker.setText(dateExchange);
        timePicker.setText(timeExchange);
        capacity.setText(Integer.toString(exchange.getCapacity()));
        btnCreate.setText("Update");
        configurBtnUpdate();
    }

    private void configurBtnUpdate() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateExchange();
                exchangeViewModel.updateExchange(exchange);
                exchangeViewModel.getExchangeLiveData().observe(getActivity(), ex -> {
                    getActivity().onBackPressed();
                });
            }
        });

    }

    private void updateExchange() {
        int capa = Integer.parseInt(capacity.getText().toString());
        exchange.setCapacity(capa);
        exchange.setDate(dateExchange + timeExchange);
        exchange.setName(username.getText().toString());
        exchange.setDescription(description.getText().toString());
    }

}
