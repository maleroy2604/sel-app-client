package com.selclientapp.selapp.views;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Exchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeViewHolder> implements Filterable {

    public interface Listener {
        void onClickDialog(int position);
    }

    //FOR DATA
    private List<Exchange> exchanges;
    private List<Exchange> exchangesIsFull;

    //FOR CALLBACK
    private final Listener callback;

    public ExchangeAdapter(List<Exchange> exchanges, Listener callback) {
        this.exchanges = exchanges;
        this.callback = callback;
        this.exchangesIsFull = new ArrayList<>(exchanges);
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

    public Exchange getExchange(int position) {
        return this.exchanges.get(position);
    }

    public List<Exchange> getExchanges() {
        return this.exchangesIsFull;
    }

    @Override
    public Filter getFilter() {
        return exchangeFilter;
    }


    private Filter exchangeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Exchange> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exchangesIsFull);
            } else {
                String filterPattern = constraint.toString().toUpperCase().trim();
                for (Exchange item : exchangesIsFull) {
                    if (item.getName().toUpperCase().contains(filterPattern) || item.getOwnerName().toUpperCase().contains(filterPattern) || item.getDate().toUpperCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exchanges.clear();
            exchanges.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void updateList(List<Exchange> exchanges) {
        this.exchanges.clear();
        this.exchanges.addAll(exchanges);
        notifyDataSetChanged();
    }

    // method test
    public void testResetList() {
        updateList(exchangesIsFull);
    }

}
