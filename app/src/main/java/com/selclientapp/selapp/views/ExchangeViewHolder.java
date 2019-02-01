package com.selclientapp.selapp.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Exchange;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeViewHolder extends RecyclerView.ViewHolder  {

    @BindView(R.id.fragment_exchange_item_name)
    TextView textViewName;

    @BindView(R.id.fragment_exchange_item_date)
    TextView textViewDate;

    @BindView(R.id.fragment_exchange_item_author)
    TextView textViewAuthor;

    @BindView(R.id.fragment_exchange_item_capacity)
    TextView textViewCapacity;

    @BindView(R.id.fragment_exchange_item_delete)
    ImageButton imageButton;

    private WeakReference<ExchangeAdapter.Listener> callbackWeakRef;

    public ExchangeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithExchange(Exchange exchange, ExchangeAdapter.Listener callback) {
        this.textViewName.setText(exchange.getName());
        this.textViewDate.setText(exchange.getDate());
        this.textViewAuthor.setText(exchange.getOwnerName());
        this.textViewCapacity.setText(exchange.getCurrentCapacity() + "/" + exchange.getCapacity());
        this.callbackWeakRef = new WeakReference<ExchangeAdapter.Listener>(callback);
        this.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeAdapter.Listener callback = callbackWeakRef.get();
                if (callback != null)
                    callback.onclickDeleteButton(getAdapterPosition());
            }
        });

    }
}

