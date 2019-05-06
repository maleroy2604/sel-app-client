package com.selclientapp.selapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.selclientapp.selapp.App;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.User;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.utils.ExchangeListener;
import com.selclientapp.selapp.view_models.LoginAndSignUpViewModel;


import javax.inject.Inject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;


public class EditProfileFragment extends Fragment implements Runnable {

    private static final int PICK_IMAGE_REQUEST = 1;

    // FOR DESIGN

    @BindView(R.id.fragment_title_header)
    TextView titleHeader;
    @BindView(R.id.fragment_arrow)
    ImageButton imgArrowBack;
    @BindView(R.id.image_view_profile)
    ImageView imageProfile;
    @BindView(R.id.foto_hint)
    TextView fotoHint;

    private Uri mImageUri;
    private ExchangeListener callback;
    private File image ;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

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
                callback.refreshExchange();
                callback.restartLoader();
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
    public void run() {
        writeIntoFile(image, mImageUri);
        loginAndSignUpViewModel.uploadImage(image, managementTokenAndUSer.getCurrentUser().getAvatarurl());
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
                System.out.println(getActivity().getExternalCacheDir());
                this.image = new File("data/data/com.selclientapp.selapp/" + getSingleName(mImageUri) + ".JPEG");
                executorService.execute(this);
                //this.run();
                imageProfile.setImageURI(mImageUri);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        }

    }

    private void configureElemView() {
        titleHeader.setText("Edit Profile.");
    }

    public String getSingleName(Uri uri) {
        int lastindex = uri.toString().lastIndexOf("/");
        String singleName = uri.toString().substring(lastindex);
        return singleName;
    }

    private OutputStream configFileOutput(File image) {
        try {
            return new FileOutputStream(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getByteArray(Uri mImageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(App.context.getContentResolver(), mImageUri);
            Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 175, 158, false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapResized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeIntoFile(File image, Uri imageUri) {
        OutputStream fos = configFileOutput(image);
        try {
            fos.write(getByteArray(imageUri));
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
