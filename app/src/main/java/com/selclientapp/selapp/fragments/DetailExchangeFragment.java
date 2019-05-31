package com.selclientapp.selapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.selclientapp.selapp.App;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.model.ExchangeOcurence;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.utils.Tools;
import com.selclientapp.selapp.view_models.ExchangeOcurenceViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class DetailExchangeFragment extends Fragment {

    //FOR DESIGN
    @BindView(R.id.image_bck_detail)
    ImageView imageBck;
    @BindView(R.id.name_exchange)
    TextView mNameExchange;
    @BindView(R.id.description_exchange)
    TextView mDescriptionTxt;
    @BindView(R.id.back_arrow)
    ImageButton mBtnBackArrow;
    @BindView(R.id.bck_img_capacity)
    TextView mBckImgCapacity;
    @BindView(R.id.bck_img_date)
    TextView mBckImgDate;
    @BindView(R.id.category_bck_img)
    TextView mBckImgCategory;
    @BindView(R.id.owner_name_exchange)
    TextView mOwnerName;
    @BindView(R.id.btn_opt1)
    Button btnOtp1;
    @BindView(R.id.btn_opt2)
    Button btnOtp2;
    @BindView(R.id.btn_opt4)
    Button btnOtp4;


    //FOR DATA
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ExchangeOcurenceViewModel exchangeOcurenceViewModel;
    private Exchange exchange;
    ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();
    ExchangeFragment exchangeFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_exchange_participant, container, false);
        ButterKnife.bind(this, view);
        this.configureDagger();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.home_activity_bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        this.configureViewModel();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        exchange = bundle.getParcelable("exchange");
        if (managementTokenAndUSer.getCurrentUser().getId() == exchange.getOwner()) {
            configBtnManagement();
            configBtnEdit();
        } else {
            configBtnParticipant();
        }
        configDiscussion();
        configureElemView();
        configBackArrow();
    }

    private void configureViewModel() {
        exchangeOcurenceViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeOcurenceViewModel.class);
    }

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureElemView() {
        String dateExchange = exchange.getDate().substring(0, 10);
        String timeExchange = exchange.getDate().substring(10, 16);
        configImgBack();
        mNameExchange.setText(exchange.getName());
        mDescriptionTxt.setText(exchange.getDescription());
        setTextViexCapacity();
        mBckImgDate.setText(String.format(dateExchange + " at " + timeExchange));
        mBckImgCategory.setText(exchange.getCategory());
        mOwnerName.setText(exchange.getOwnerName());
    }

    private void configImgBack() {
        if (exchange.getCategory() == null) {
            imageBck.setImageResource(R.drawable.sel);
        } else {
            Glide.with(this).load(App.URL_SERVER + "imagecategory/" + exchange.getCategory() + ".jpeg").into((imageBck));

        }

    }

    private void configBackArrow() {
        mBtnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


    private void configBtnParticipant() {
        btnOtp4.setVisibility(View.GONE);
        if (findExchangeOcurence(exchange) == null) {
            configOptEnroll();
        } else {
            configOptWithdraw();
        }

    }

    private boolean isValid(Exchange exchange) {
        if (checkDate(exchange)) {
            Tools.backgroundThreadShortToast("The inscription are closed");
            return false;
        }
        return true;
    }

    private boolean checkDate(Exchange exchange) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date exchangeDate = null;
        try {
            exchangeDate = format.parse(exchange.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return exchangeDate.getTime() < new Date().getTime();
    }

    private void addExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        exchangeOcurenceViewModel.addExchangeOcurence(exchangeOcurence);
        exchangeOcurenceViewModel.getExchangeOcurenceLiveData().observe(getActivity(), exchangeOcur -> {
            exchangeOcurence.setId(exchangeOcur.getId());
            mBckImgCapacity.setText(String.format(exchange.getCurrentCapacity() + "/" + exchange.getCapacity()));
            btnOtp1.setText(R.string.withdraw);
            configOptWithdraw();
        });
    }

    private void deleteExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        exchangeOcurenceViewModel.deleteExchangeOcurence(exchangeOcurence);
    }

    private ExchangeOcurence findExchangeOcurence(Exchange exchange) {
        for (ExchangeOcurence exchangeOcurence : exchange.getExchangeocurence()) {
            if (exchangeOcurence.getParticipantId() == managementTokenAndUSer.getCurrentUser().getId()) {
                return exchangeOcurence;
            }
        }
        return null;
    }

    private void configOptEnroll() {
        btnOtp1.setText(R.string.enroll);
        btnOtp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeOcurence exchangeOcurence = new ExchangeOcurence(managementTokenAndUSer.getCurrentUser().getId(), exchange.getId(), managementTokenAndUSer.getCurrentUser().getUsername());
                if (isValid(exchange)) {
                    exchange.addExchangeOcurence(exchangeOcurence);
                    setTextViexCapacity();
                    addExchangeOcurence(exchangeOcurence);
                    updateExchangeFragment();
                    configOptWithdraw();
                }
            }
        });
    }

    private void configOptWithdraw() {
        btnOtp1.setText(R.string.withdraw);
        btnOtp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(exchange)) {
                    ExchangeOcurence exchangeOcurence = findExchangeOcurence(exchange);
                    exchange.removeExchangeOcurecne(exchangeOcurence);
                    setTextViexCapacity();
                    deleteExchangeOcurence(exchangeOcurence);
                    updateExchangeFragment();
                    configOptEnroll();
                }
            }
        });
    }

    private void setTextViexCapacity() {
        mBckImgCapacity.setText(String.format(exchange.getCurrentCapacity() + "/" + exchange.getCapacity()));
    }

    private void configBtnManagement() {
        btnOtp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exchange.getExchangeocurence().isEmpty()) {
                    Tools.backgroundThreadShortToast("Nobody is enroll to your exchange");
                } else {
                    showExchangeManagment();
                }
            }
        });
    }

    private void showExchangeManagment() {
        getFragmentManager().popBackStack();
        ExchangeManagementOcurence fragment = new ExchangeManagementOcurence();
        Bundle bundle = new Bundle();
        bundle.putParcelable("exchange", exchange);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_home_container, fragment, null).addToBackStack("fragment_management").commit();
    }

    private void configBtnEdit() {
        btnOtp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditExchangeFragment fragment = new EditExchangeFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("exchange", exchange);
                fragment.setArguments(bundle);
                showEditExchangeFragment(fragment);
            }
        });
    }

    private void configDiscussion() {
        btnOtp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiscussionFragment();
            }
        });
    }

    private void showEditExchangeFragment(EditExchangeFragment fragment) {
        getFragmentManager().popBackStack();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.home_activity_bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_home_container, fragment, null).addToBackStack("fragment_edit_exchange").commit();
    }

    private void updateExchangeFragment() {
        exchangeFragment = (ExchangeFragment) getFragmentManager().findFragmentByTag("fragment_exchange");
        exchangeFragment.updateExchangeToList(exchange);
    }

    private void showDiscussionFragment() {
        getFragmentManager().popBackStack();
        DiscussionFragment discussionFragment = new DiscussionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("exchange", exchange);
        discussionFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_home_container, discussionFragment, null).addToBackStack("fragment_discussion").commit();
    }
}
