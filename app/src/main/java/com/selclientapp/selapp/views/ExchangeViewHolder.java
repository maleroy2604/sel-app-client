package com.selclientapp.selapp.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.database.entity.Exchange;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_exchange_item_name)
    TextView textView;

    public ExchangeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithExchange(Exchange exchange) {
        this.textView.setText(exchange.getName());
    }
}

