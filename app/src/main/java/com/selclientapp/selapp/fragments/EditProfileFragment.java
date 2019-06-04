package com.selclientapp.selapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.utils.ExchangeListener;
import com.selclientapp.selapp.utils.TokenBody;
import com.selclientapp.selapp.utils.Tools;
import com.selclientapp.selapp.utils.WriteIntoFile;
import com.selclientapp.selapp.view_models.LoginAndSignUpViewModel;


import javax.inject.Inject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;


import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;


public class EditProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

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

    // FOR DESIGN
    @BindView(R.id.fragment_title_header)
    TextView titleHeader;
    @BindView(R.id.fragment_arrow)
    ImageButton imgArrowBack;
    @BindView(R.id.image_view_profile)
    ImageView imageProfile;
    @BindView(R.id.foto_hint)
    TextView fotoHint;
    @BindView(R.id.fragment_edit_profile_input_username)
    TextInputLayout usernameEditInput;
    @BindView(R.id.fragment_edit_profile_username)
    EditText editProfileUsername;
    @BindView(R.id.fragment_edit_profile_input_password)
    TextInputLayout passwordEditInput;
    @BindView(R.id.fragment_edit_profile_password)
    EditText passwordEdit;
    @BindView(R.id.fragment_edit_profile_input_email)
    TextInputLayout emailInput;
    @BindView(R.id.fragment_edit_profile_email)
    EditText emailEdit;
    @BindView(R.id.fragment_edit_profile_input_old_password)
    TextInputLayout oldPasswordInput;
    @BindView(R.id.fragment_edit_profile_old_password)
    EditText oldPasswordEdit;
    @BindView(R.id.btn_update_profile)
    Button btnUpdateProfile;

    //FOR DATA
    private Uri mImageUri;
    private ExchangeListener callback;
    private File image;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();
    private User userEdited;
    private static final int MAX_LENGHT = 29;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    LoginAndSignUpViewModel loginAndSignUpViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        this.configureDagger();
        this.configureImgChooseFile();
        this.configureViewModel();
        this.configureArrowBack();
        this.configureElemView();
        this.configureBtnUpdateProfile();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.home_activity_bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        return view;
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel() {
        this.loginAndSignUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginAndSignUpViewModel.class);
    }

    private void configureBtnUpdateProfile() {
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEdited = new User(editProfileUsername.getText().toString(),
                        passwordEdit.getText().toString(),
                        oldPasswordEdit.getText().toString(),
                        emailEdit.getText().toString()
                );
                if (!(image == null)) {
                    updateImageOnly();
                } else {
                    updateUserInfo();
                }
            }
        });

    }

    private void updateImageOnly() {
        loginAndSignUpViewModel.uploadImage(image, managementTokenAndUSer.getCurrentUser().getFileName());
        loginAndSignUpViewModel.getUserLiveData().observe(getActivity(), user -> {
            updateUserInfo();
        });
    }

    private void updateUserInfo() {
        String newPassword = passwordEdit.getText().toString();
        String oldPassword = oldPasswordEdit.getText().toString();
        if (!(newPassword.isEmpty() && oldPassword.isEmpty())) {
            callViewModelUserInfo();
        } else {
            userEdited = new User(editProfileUsername.getText().toString(), emailEdit.getText().toString());
            callViewModelUserInfo();
        }
    }

    private void callViewModelUserInfo() {
        loginAndSignUpViewModel.updateUser(userEdited);
        loginAndSignUpViewModel.getUserLiveData().observe(getActivity(), user1 -> {
            managementTokenAndUSer.saveUser(user1);
            backToActivity();
        });
    }


    private void configureImgChooseFile() {
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void configureArrowBack() {
        imgArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToActivity();
            }
        });
    }

    private void backToActivity() {
        getActivity().onBackPressed();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private final TextWatcher watcherUsername = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validField(editProfileUsername, usernameEditInput, USERNAME_PATTERN, "Username hasn't 4 character or contains white space or specail character.");
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
            validConfirmPassword();
            validField(passwordEdit, passwordEditInput, PASSWORD_PATTERN, "Password must contains : 4 character, one digit, one lower and one  upper case letter and  one special character.");
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
            validField(emailEdit, emailInput, Patterns.EMAIL_ADDRESS, "Please enter a valid email address");
            validBtn();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    // -----------------
    // ACTION
    // -----------------

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {


            if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                mImageUri = data.getData();
                fotoHint.setText("");
                this.image = new File("data/data/com.selclientapp.selapp/" + getSingleName() + ".JPEG");
                WriteIntoFile writeIntoFile = new WriteIntoFile(image, mImageUri);
                executorService.execute(writeIntoFile);
                Glide.with(this).load(mImageUri).into(imageProfile);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private void configureElemView() {
        titleHeader.setText("Edit Profile.");
        editProfileUsername.setText(managementTokenAndUSer.getCurrentUser().getUsername());
        emailEdit.setText(managementTokenAndUSer.getCurrentUser().getEmail());
        Glide.with(this).load(managementTokenAndUSer.getCurrentUser().getFileName()).into(imageProfile);
        editProfileUsername.addTextChangedListener(watcherUsername);
        passwordEdit.addTextChangedListener(watcherPassword);
        emailEdit.addTextChangedListener(watcherEmail);
        configOldPasswordEdit();
    }

    private void configOldPasswordEdit() {
        oldPasswordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TokenBody tokenBody = new TokenBody(managementTokenAndUSer.getCurrentUser().getUsername(), oldPasswordEdit.getText().toString());
                if (!hasFocus) {
                    loginAndSignUpViewModel.login(tokenBody);
                    loginAndSignUpViewModel.getUserLiveData().observe(getActivity(), user -> {
                        if (user == null && !oldPasswordEdit.getText().toString().isEmpty()) {
                            oldPasswordInput.setError("Old password is wrong !");
                            btnUpdateProfile.setEnabled(false);
                        } else {
                            oldPasswordInput.setError("");
                            btnUpdateProfile.setEnabled(true);
                        }
                    });
                }
            }
        });
    }

    // -----------------
    // UTILS
    // -----------------
    public String getSingleName() {
        String randomString = Tools.randomAlphaNumeric(6);
        if (managementTokenAndUSer.getCurrentUser().getFileName() == null) {
            return randomString;
        } else {
            String fileName = managementTokenAndUSer.getCurrentUser().getFileName();
            String singleName = fileName;

            while (singleName.equals(randomString)) {
                randomString = Tools.randomAlphaNumeric(6);
            }
            return randomString;
        }
    }

    private boolean validField(EditText editText, TextInputLayout input, Pattern PTN, String msg) {
        String password = editText.getText().toString().trim();
        String oldPassword = oldPasswordEdit.getText().toString();
        if (password.equals(oldPassword) && !oldPassword.isEmpty()) {
            input.setError("New password has to be different then old password !");
            return false;
        } else if (!PTN.matcher(password).matches()) {
            input.setError(msg);
            return false;
        } else if (password.length() > MAX_LENGHT) {
            input.setError("Field can't be so long");
            return false;
        } else if (password.isEmpty() && oldPassword.isEmpty()) {
            input.setError("");
            return true;
        } else {
            input.setError("");
            return true;
        }
    }

    private boolean validConfirmPassword() {
        String confirmPassword = oldPasswordEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if (!password.isEmpty() && confirmPassword.isEmpty()) {
            oldPasswordInput.setError("Old password can't be empty.");
            return false;
        } else {
            oldPasswordInput.setError("");
            return true;
        }
    }

    private void validBtn() {
        boolean usernameIsValid = validField(editProfileUsername, usernameEditInput, USERNAME_PATTERN, "Username hasn't 4 character or contains white space or specail character.");
        boolean passwordIsValid = validField(passwordEdit, passwordEditInput, PASSWORD_PATTERN, "Password must contains : 4 character, one digit, one lower and one  upper case letter and  one special character.");
        boolean emailIsValid = validField(emailEdit, emailInput, Patterns.EMAIL_ADDRESS, "Please enter a valid email address");

        btnUpdateProfile.setEnabled(usernameIsValid && passwordIsValid && validConfirmPassword() && emailIsValid);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (ExchangeListener) context;
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }
}
