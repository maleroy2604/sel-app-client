package com.selclientapp.selapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class NoConnexionFragment extends Fragment {

    @BindView(R.id.btn_no_connexion)
    Button btnNoConnexion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connexion_unable, container, false);
        ButterKnife.bind(this, view);
        configureDagger();
        configElemView();
        return view;
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configElemView() {
        btnNoConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.hasInternetConnection()) {
                    showExchangeFragment();
                } else {
                    Tools.backgroundThreadShortToast("No internet connexion available !");
                }
            }
        });
    }

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void showExchangeFragment() {
            ExchangeFragment fragment = new ExchangeFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_home_container, fragment, "fragment_exchange")
                    .commit();
    }


}
