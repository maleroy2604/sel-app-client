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
import android.util.Patterns;
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
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.utils.Tools;
import com.selclientapp.selapp.view_models.LoginAndSignUpViewModel;


import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class SignUpFragment extends Fragment {

    // FOR DESIGN
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=?])" +    //at least 1 special character
                    "(?=\\S+$)" +
                    ".{4,}" +               //at least 4 characters
                    "$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^" +
                    "[a-z0-9]{4,}" +
                    "$");

    @BindView(R.id.fragment_sign_up_input_username)
    TextInputLayout usernameInput;
    @BindView(R.id.fragment_sign_up_username)
    EditText usernameEditText;
    @BindView(R.id.fragment_sign_up_input_password)
    TextInputLayout passwordInput;
    @BindView(R.id.fragment_sign_up_password)
    EditText passwordEditText;
    @BindView(R.id.fragment_sign_up_input_confirm_password)
    TextInputLayout passwordConfirmInput;
    @BindView(R.id.fragment_sign_up_confirm_password)
    EditText passwordConfirmEditText;
    @BindView(R.id.fragment_sign_up_input_email)
    TextInputLayout emailInput;
    @BindView(R.id.fragment_sign_up_email)
    EditText emailEditText;
    @BindView(R.id.fragment_btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.fragment_link_login)
    TextView linkLogin;

    //FOR DATA
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoginAndSignUpViewModel loginModel;

    public SignUpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        configElemView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.configureViewmodel();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.hasInternetConnection()) {
                    User user = new User(usernameEditText.getText().toString(), passwordEditText.getText().toString(), passwordConfirmEditText.getText().toString(), emailEditText.getText().toString());
                    System.out.println("user " + user.toString());
                    loginModel.saveUser(user);
                    loginModel.getUserLiveData().observe(getActivity(), userLiveData -> {
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    });
                } else {
                    Toast.makeText(App.context, "No internet connetion available !", Toast.LENGTH_LONG).show();
                }
            }
        });
        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginFragment();
            }
        });

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
            validField(usernameEditText, usernameInput, USERNAME_PATTERN, "Username hasn't 4 character or contains white space or specail character.");
            validBtn();
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
            validField(passwordEditText, passwordInput, PASSWORD_PATTERN, "Password must contains : 4 character, one digit, one lower and one  upper case letter and  one special character.");
            validBtn();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final TextWatcher watcherEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validField(emailEditText, emailInput, Patterns.EMAIL_ADDRESS, "Please enter a valid email address");
            validBtn();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final TextWatcher watcherPasswordConfirm = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validConfirmPassword();
            validBtn();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void configElemView() {
        btnSignUp.setEnabled(false);
        passwordInput.setError("Field can't be empty");
        passwordEditText.addTextChangedListener(watcherPassword);
        usernameInput.setError("Field can't be empty");
        usernameEditText.addTextChangedListener(watcherUsername);
        passwordConfirmInput.setError("Field can't be empty");
        passwordConfirmEditText.addTextChangedListener(watcherPasswordConfirm);
        emailInput.setError("Field can't be empty");
        emailEditText.addTextChangedListener(watcherEmail);
    }

    // -----------------
    // ACTION
    // -----------------

    private void showLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, null).addToBackStack("fragment_sign_up").commit();
    }
    // -----------------
    // UTILS
    // -----------------

    private boolean validField(EditText editText, TextInputLayout input, Pattern PTN, String msg) {
        String password = editText.getText().toString().trim();
        if (password.isEmpty()) {
            input.setError("Field can't be empty.");
            return false;
        } else if (!PTN.matcher(password).matches()) {
            input.setError(msg);
            return false;
        } else {
            input.setError("");
            return true;
        }
    }

    private boolean validConfirmPassword() {
        String confirmPassword = passwordConfirmEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (password.isEmpty()) {
            passwordConfirmInput.setError("Field can't be empty.");
            return false;
        } else if (confirmPassword.equals(password)) {
            passwordConfirmInput.setError("");
            return true;
        } else {
            passwordConfirmInput.setError("Has to be equals to password.");
            return false;
        }

    }

    private void validBtn() {
        boolean usernameIsValid = validField(usernameEditText, usernameInput, USERNAME_PATTERN, "Username hasn't 4 character or contains white space or specail character.");
        boolean passwordIsValid = validField(passwordEditText, passwordInput, PASSWORD_PATTERN, "Password must contains : 4 character, one digit, one lower and one  upper case letter and  one special character.");
        boolean emailIsValid = validField(emailEditText, emailInput, Patterns.EMAIL_ADDRESS, "Please enter a valid email address");

        btnSignUp.setEnabled(usernameIsValid && passwordIsValid && validConfirmPassword() && emailIsValid);
    }

}
