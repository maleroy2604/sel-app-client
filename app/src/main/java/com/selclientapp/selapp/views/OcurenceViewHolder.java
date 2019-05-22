package com.selclientapp.selapp.views;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.ExchangeOcurence;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.utils.Tools;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OcurenceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_management_username)
    TextView userName;

    @BindView(R.id.fragment_management_hours)
    EditText hours;

    @BindView(R.id.fragment_management_send_hours)
    Button sendHours;

    private WeakReference<OcurenceAdapter.Listener> callbackWeakRef;
    private ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();


    public OcurenceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithOcurence(ExchangeOcurence exchangeOcurence, OcurenceAdapter.Listener callback) {
        userName.setText(exchangeOcurence.getParticipantName());
        this.callbackWeakRef = new WeakReference<>(callback);
        configElemView(exchangeOcurence);
    }

    private void configElemView(ExchangeOcurence exchangeOcurence) {
        //sendHours.setEnabled(false);
        hours.addTextChangedListener(watchHours);
        sendHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OcurenceAdapter.Listener callback = callbackWeakRef.get();
                if( managementTokenAndUSer.getCurrentUser().getCounterhours() == 0 ||  managementTokenAndUSer.getCurrentUser().getCounterhours() < Integer.parseInt(hours.getText().toString().trim())){
                    Tools.backgroundThreadShortToast("Not enough hours !");
                }else{
                    exchangeOcurence.setHours(Integer.parseInt(hours.getText().toString()));
                    if (callback != null) {
                        callback.onClickSendHours(getAdapterPosition());
                    }
                }
            }
        });

    }

    TextWatcher watchHours = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
           // String hour = hours.getText().toString().trim();
            //sendHours.setEnabled(!(hour.isEmpty()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}

