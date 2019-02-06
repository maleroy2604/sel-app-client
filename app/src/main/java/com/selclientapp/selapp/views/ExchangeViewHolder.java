package com.selclientapp.selapp.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.repositories.ManagementToken;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_exchange_item_name)
    TextView textViewName;

    @BindView(R.id.fragment_exchange_item_date)
    TextView textViewDate;

    @BindView(R.id.fragment_exchange_item_author)
    TextView textViewAuthor;

    @BindView(R.id.fragment_exchange_item_capacity)
    TextView textViewCapacity;

    @BindView(R.id.fragment_exchange_item_delete)
    ImageButton imageButtonDelete;

    @BindView(R.id.fragment_exchange_item_update)
    ImageButton imageButtonUpdate;

    private WeakReference<ExchangeAdapter.Listener> callbackWeakRef;

    public ExchangeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithExchange(Exchange exchange, ExchangeAdapter.Listener callback) {
        this.textViewName.setText(String.format("Name: " + exchange.getName()));
        this.textViewDate.setText(String.format("Date & hour: " + exchange.getDate()));
        this.textViewAuthor.setText(String.format("Owner: " + exchange.getOwnerName()));
        this.textViewCapacity.setText(String.format("Capacity: " + exchange.getCurrentCapacity() + "/" + exchange.getCapacity()));
        configImageButton(exchange);
        this.callbackWeakRef = new WeakReference<ExchangeAdapter.Listener>(callback);


    }

    public void configImageButton(Exchange exchange) {
        if (!(exchange.getOwner() == ManagementToken.getCurrentId())) {
            imageButtonUpdate.setVisibility(View.GONE);
            imageButtonDelete.setVisibility(View.GONE);
        }

        this.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeAdapter.Listener callback = callbackWeakRef.get();
                if (callback != null)
                    callback.onclickDeleteButton(getAdapterPosition());
            }
        });

        this.imageButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeAdapter.Listener callback = callbackWeakRef.get();
                if (callback != null)
                    callback.onclickEditButton(getAdapterPosition());
            }
        });
    }
}

