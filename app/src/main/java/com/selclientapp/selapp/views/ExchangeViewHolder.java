package com.selclientapp.selapp.views;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.selclientapp.selapp.App;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;

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

    @BindView(R.id.fragment_exchange_item_horiz)
    ImageButton imageButtonHoriz;

    @BindView(R.id.image_view_profile)
    ImageView imageProfile;

    @BindView(R.id.image_bck_exchange)
    ImageView imageBck;


    private WeakReference<ExchangeAdapter.Listener> callbackWeakRef;


    public ExchangeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithExchange(Exchange exchange, ExchangeAdapter.Listener callback) {
        String dateExchange = exchange.getDate().substring(0, 10);
        String timeExchange = exchange.getDate().substring(10, 16);

        this.textViewName.setText(String.format(exchange.getName()).toUpperCase());
        this.textViewAuthor.setText(String.format(exchange.getOwnerName()));
        this.textViewDate.setText(String.format(dateExchange + " at " + timeExchange));
        this.textViewCapacity.setText(String.format(exchange.getCurrentCapacity() + "/" + exchange.getCapacity()));
        this.callbackWeakRef = new WeakReference<>(callback);
        if (exchange.getAvatarUrl() == null) {
            imageBck.setImageResource(R.drawable.sel);
        } else {
            Glide.with(App.context).load(App.URL_SERVER + "imageavatar/" + exchange.getAvatarUrl()).into(imageProfile);
        }

        if (exchange.getCategory() == null) {
            imageBck.setImageResource(R.drawable.sel);
        } else {
            Glide.with(App.context).load(App.URL_SERVER + "imagecategory/" + exchange.getCategory().toLowerCase() + ".jpeg").into(imageBck);
        }
        configHoriz();
    }

    private void configHoriz() {
        imageButtonHoriz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeAdapter.Listener callback = callbackWeakRef.get();
                if (callback != null)
                    callback.onClickDialog(getAdapterPosition());
            }
        });
    }
}