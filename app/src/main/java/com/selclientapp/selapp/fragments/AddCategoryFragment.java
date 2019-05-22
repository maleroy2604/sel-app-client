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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.utils.Tools;
import com.selclientapp.selapp.utils.WriteIntoFile;
import com.selclientapp.selapp.view_models.ExchangeViewModel;


import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class AddCategoryFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 2;

    @BindView(R.id.fragment_add_category_input)
    TextInputLayout addCategoryInput;
    @BindView(R.id.fragment_add_category_editText)
    EditText addCategoryEditText;
    @BindView(R.id.btn_add_category)
    Button btnAddCategory;
    @BindView(R.id.fragment_title_header)
    TextView titleHeader;
    @BindView(R.id.fragment_arrow)
    ImageButton imgArrowBack;
    @BindView(R.id.image_category)
    ImageView imageCategory;
    @BindView(R.id.foto_hint)
    TextView fotoHint;

    private Uri mImageUri;
    private File image;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    ExchangeViewModel exchangeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_category, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        configureDagger();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.home_activity_bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        configureArrowBack();
        configureViewModel();
        configElemView();
        return view;
    }

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel() {
        this.exchangeViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeViewModel.class);
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

    private void configElemView() {
        imageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        titleHeader.setText("Add Category");
        configBtnAddCategory();
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
                Glide.with(this).load(mImageUri).into(imageCategory);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void configBtnAddCategory() {
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageUri != null && !addCategoryEditText.getText().toString().trim().isEmpty()) {
                    image = new File("data/data/com.selclientapp.selapp/" + addCategoryEditText.getText().toString().trim() + ".JPEG");
                    WriteIntoFile writeIntoFile = new WriteIntoFile(image, mImageUri);
                    executorService.execute(writeIntoFile);
                    exchangeViewModel.addCategoryExchange(image, addCategoryEditText.getText().toString().trim());
                    backToActivity();
                } else {
                    Tools.backgroundThreadShortToast("Choose an image or enter a category name");
                }

            }
        });
    }


}
