package com.selclientapp.selapp.views;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.ExchangeOcurence;

import java.util.List;

public class OcurenceAdapter extends RecyclerView.Adapter<OcurenceViewHolder> {

    public interface Listener {
        void onClickSendHours(int position);
        void onClickRemoveParticipant(int position);
    }

    //FOR DATA
    private List<ExchangeOcurence> exchangeOcurences;

    //FOR CALLBACK
    private final Listener callback;

    public OcurenceAdapter(List<ExchangeOcurence> exchangeOcurences, Listener callback) {
        this.callback = callback;
        this.exchangeOcurences = exchangeOcurences;
    }

    @Override
    public OcurenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_exchange_management_ocurence, parent, false);

        return new OcurenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OcurenceViewHolder holder, int position) {
        holder.updateWithOcurence(this.exchangeOcurences.get(position), this.callback);
    }

    @Override
    public int getItemCount() {
        return this.exchangeOcurences.size();
    }

    public ExchangeOcurence getExchangeOcurence(int possition) {
        return this.exchangeOcurences.get(possition);
    }


}
