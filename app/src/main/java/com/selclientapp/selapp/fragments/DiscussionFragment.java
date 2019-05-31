package com.selclientapp.selapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.model.Message;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.view_models.MessageViewModel;
import com.selclientapp.selapp.views.DiscussionAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class DiscussionFragment extends Fragment {

    @BindView(R.id.fragment_discussion_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_discussion_btn_send)
    Button btnSend;
    @BindView(R.id.fragment_discussion_message)
    EditText editTextMessage;
    @BindView(R.id.fragment_arrow)
    ImageButton imgArrowBack;
    @BindView(R.id.fragment_title_header)
    TextView titleHeader;


    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private MessageViewModel messageViewModel;
    private List<Message> messages = new ArrayList<>();
    private DiscussionAdapter discussionAdapter;
    private Exchange exchange;
    private ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discussion, container, false);
        ButterKnife.bind(this, view);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.home_activity_bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        this.configureDagger();
        this.configureRecyclerView();
        this.configBtnSend();
        this.configureArrowBack();
        this.configElemView();
        Bundle bundle = getArguments();
        exchange = bundle.getParcelable("exchange");
        discussionAdapter.notifyDataSetChanged();
        this.configureViewmodel();
        return view;
    }

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    public DiscussionFragment() {

    }

    private void configureRecyclerView() {
        this.messages = new ArrayList<>();
        this.discussionAdapter = new DiscussionAdapter(this.messages);
        this.recyclerView.setAdapter(this.discussionAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureViewmodel() {
        messageViewModel = ViewModelProviders.of(this, viewModelFactory).get(MessageViewModel.class);
        messageViewModel.getMessagesExchange(exchange.getId());
        messageViewModel.getListMessageLiveData().observe(this, messages -> {
            updateUi(messages);
        });
    }

    private void updateUi(List<Message> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
        discussionAdapter.notifyDataSetChanged();
    }

    private void configBtnSend() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String avatarUrl = managementTokenAndUSer.getCurrentUser().getFileName();
                Message message = new Message(editTextMessage.getText().toString(), exchange.getId(), managementTokenAndUSer.getCurrentUser().getId(), avatarUrl);
                //exchange.addMessage(message);
                messageViewModel.sendMessage(message);
                messageViewModel.getListMessageLiveData().observe(getActivity(), messagesList -> {
                    messages.clear();
                    messages.addAll(messagesList);
                    discussionAdapter.notifyDataSetChanged();
                    editTextMessage.setText("");
                });
            }
        });
    }


    private void configureArrowBack() {
        imgArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        });

    }

    private void configElemView() {
        titleHeader.setText("Discussion");
    }

}
