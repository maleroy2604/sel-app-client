package com.selclientapp.selapp.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.database.entity.Exchange;

import java.util.List;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeViewHolder> {

    //FOR DATA
    private List<Exchange> exchanges;

    public ExchangeAdapter(List<Exchange> exchanges) {
        this.exchanges = exchanges;
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
        holder.updateWithExchange(this.exchanges.get(position));
    }

    @Override
    public int getItemCount() {
        return this.exchanges.size();
    }
}
