package com.selclientapp.selapp.views;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Exchange;

import java.util.ArrayList;
import java.util.List;


public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeViewHolder> {

    public interface Listener {
        void onClickDialog(int position);
    }

    //FOR DATA
    private List<Exchange> exchanges;


    //FOR CALLBACK
    private final Listener callback;

    public ExchangeAdapter(List<Exchange> exchanges, Listener callback) {
        this.exchanges = exchanges;
        this.callback = callback;
    }

    @Override
    public ExchangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_exchange_item, parent, false);

        return new ExchangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExchangeViewHolder holder, int position) {
        holder.updateWithExchange(this.exchanges.get(position), this.callback);
    }

    @Override
    public int getItemCount() {
        return this.exchanges.size();
    }

    public Exchange getExchange(int possition) {
        return this.exchanges.get(possition);
    }

    public void updateList(List<Exchange> filteredListExchanges) {
        this.exchanges = new ArrayList<>();
        this.exchanges.addAll(filteredListExchanges);
        notifyDataSetChanged();
    }

    public void setExchange(Exchange exchange) {
        for (Exchange elem : this.exchanges) {
            if (elem.getId() == exchange.getId()) {
                elem = exchange;
            }
        }
    }

    public void removeExchange(Exchange exchange) {
        this.exchanges.remove(exchange);
    }

    public List<Exchange> getExchanges() {
        return exchanges;
    }
}
