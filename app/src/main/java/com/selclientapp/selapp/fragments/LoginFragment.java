package com.selclientapp.selapp.fragments;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.selclientapp.selapp.App;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.activities.HomeActivity;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.repositories.TokenBody;
import com.selclientapp.selapp.utils.Tools;
import com.selclientapp.selapp.view_models.LoginAndSignUpViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;


public class LoginFragment extends Fragment {

    // FOR DATA
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoginAndSignUpViewModel loginModel;
    //private ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();

    // FOR DESIGN
    @BindView(R.id.fragment_login_input_username)
    TextInputLayout usernameInput;
    @BindView(R.id.fragment_login_username)
    EditText usernameEditText;
    @BindView(R.id.fragment_login_input_password)
    TextInputLayout passwordInput;
    @BindView(R.id.fragment_login_password)
    EditText passwordEditText;
    @BindView(R.id.fragment_btn_login)
    Button btnLogin;
    @BindView(R.id.fragment_link_signup)
    TextView linkSignUp;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        configElemView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.configureViewmodel();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Tools.hasInternetConnection()) {
                    TokenBody tokenBody = new TokenBody(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                    loginModel.login(tokenBody);
                    loginModel.getUserLiveData().observe(getActivity(), user -> {
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    });

                } else {
                    Toast.makeText(App.context, "No internet connetion available !", Toast.LENGTH_LONG).show();
                }
            }
        });

        linkSignUp.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              showSignUpFragment();
                                          }
                                      }
        );
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureViewmodel() {
        loginModel = ViewModelProviders.of(this, viewModelFactory).get(LoginAndSignUpViewModel.class);
    }

    private final TextWatcher watcherUsername = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            hasToSetError(usernameEditText, usernameInput);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final TextWatcher watcherPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            hasToSetError(passwordEditText, passwordInput);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void configElemView() {
        btnLogin.setEnabled(false);
        usernameInput.setError("Field can't be empty");
        passwordInput.setError("Field can't be empty");
        usernameEditText.addTextChangedListener(watcherUsername);
        passwordEditText.addTextChangedListener(watcherPassword);
    }
    // -----------------
    // ACTION
    // -----------------

    private void showSignUpFragment() {
        SignUpFragment fragment = new SignUpFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, null).addToBackStack("fragment_login").commit();

    }
    // -----------------
    // UTILS
    // -----------------

    private void hasToSetError(EditText editText, TextInputLayout input) {
        if (editText.getText().toString().trim().isEmpty()) {
            input.setError("Field can't be empty");
            hasToSetEnableBtn();
        } else {
            input.setError("");
            hasToSetEnableBtn();
        }
    }

    private void hasToSetEnableBtn() {
        btnLogin.setEnabled(!usernameEditText.getText().toString().trim().isEmpty() && !passwordEditText.getText().toString().trim().isEmpty());
    }


}
