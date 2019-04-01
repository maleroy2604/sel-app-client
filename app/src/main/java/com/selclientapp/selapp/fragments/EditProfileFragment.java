package com.selclientapp.selapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.view_models.LoginAndSignUpViewModel;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class EditProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    // FOR DESIGN
    @BindView(R.id.fragment_arrow)
    ImageButton imgArrowBack;
    @BindView(R.id.fragment_title_header)
    TextView titleHeader;
    @BindView(R.id.image_view_profile)
    ImageView imageProfile;
    @BindView(R.id.foto_hint)
    TextView fotoHint;

    private Uri mImageUri;

    //FOR DATA
    private User user;
    private ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    LoginAndSignUpViewModel loginAndSignUpViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        this.configureDagger();
        this.configureBtnChooseFile();
        this.configureViewModel();
        this.configureArrowBack();
        this.configureElemView();
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
        loginAndSignUpViewModel.getUser(managementTokenAndUSer.getCurrentUser().getId());
        loginAndSignUpViewModel.getUserLiveData().observe(getActivity(), user1 -> {
            this.user = user1;
        });
    }

    private void configureBtnChooseFile() {
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
                getActivity().onBackPressed();
            }
        });
    }

    // -----------------
    // ACTION
    // -----------------

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            mImageUri = data.getData();
            fotoHint.setText("");
            imageProfile.setImageURI(mImageUri);
        }
    }

    private void configureElemView(){
        titleHeader.setText("Edit Profile.");
    }

}
