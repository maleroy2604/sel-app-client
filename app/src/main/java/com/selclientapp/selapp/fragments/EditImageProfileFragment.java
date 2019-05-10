package com.selclientapp.selapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.utils.ExchangeListener;
import com.selclientapp.selapp.utils.Tools;
import com.selclientapp.selapp.utils.WriteIntoFile;
import com.selclientapp.selapp.view_models.LoginAndSignUpViewModel;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class EditImageProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 2;
    //DESIGN
    @BindView(R.id.image_view_profile)
    ImageView imageProfile;
    @BindView(R.id.foto_hint)
    TextView fotoHint;
    @BindView(R.id.btn_update_image)
    Button btnUpdateImage;
    @BindView(R.id.fragment_arrow)
    ImageButton imgArrowBack;
    @BindView(R.id.fragment_title_header)
    TextView titleHeader;


    @Inject
    ViewModelProvider.Factory viewModelFactory;
    LoginAndSignUpViewModel loginAndSignUpViewModel;

    //DATA
    private Uri mImageUri;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private File image;
    ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();
    private ExchangeListener callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_image_only, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.home_activity_bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        this.configureDagger();
        this.configureImgChooseFile();
        this.configViewModel();
        this.configElemView();
        this.configureArrowBack();
        return view;
    }


    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configViewModel() {
        this.loginAndSignUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginAndSignUpViewModel.class);
    }


    private void configureImgChooseFile() {
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                mImageUri = data.getData();
                fotoHint.setText("");
                image = new File("data/data/com.selclientapp.selapp/" + getSingleName() + ".JPEG");
                WriteIntoFile writeIntoFile = new WriteIntoFile(image, mImageUri);
                executorService.execute(writeIntoFile);
                Glide.with(this).load(mImageUri).into(imageProfile);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
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
        callback.refreshExchange();
        callback.restartLoader();
    }

    public String getSingleName() {
        String randomString = Tools.randomAlphaNumeric(6);
        if (managementTokenAndUSer.getCurrentUser().getAvatarurl() == null) {
            return randomString;
        } else {
            String avatarUrl = managementTokenAndUSer.getCurrentUser().getAvatarurl();
            int lastindex = avatarUrl.lastIndexOf("/");
            String singleName = avatarUrl.substring(lastindex);
            while (singleName.equals(randomString)) {
                randomString = Tools.randomAlphaNumeric(6);
            }
            return randomString;
        }
    }

    public void configElemView() {
        titleHeader.setText(getString(R.string.image_profile));
        btnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image != null) {
                    loginAndSignUpViewModel.uploadImage(image);
                    loginAndSignUpViewModel.getUserLiveData().observe(getActivity(), user -> {
                        managementTokenAndUSer.saveUser(user);
                    });
                } else {
                    Tools.backgroundThreadShortToast("You have to choose an image");
                }
                backToActivity();
            }
        });
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
