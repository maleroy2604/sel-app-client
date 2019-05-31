package com.selclientapp.selapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Category;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.view_models.ExchangeViewModel;
import com.selclientapp.selapp.views.CategoriesAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class FragmentCategory extends Fragment implements CategoriesAdapter.Listener {

    //FOR DESIGN
    @BindView(R.id.fragment_my_categories_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_arrow)
    ImageButton imgArrowBack;


    @BindView(R.id.fragment_title_header)
    TextView titleHeader;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ExchangeViewModel exchangeViewModel;
    private List<Category> categories = new ArrayList<>();
    private CategoriesAdapter categoriesAdapter;
    private ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();

    public FragmentCategory() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_categories, container, false);
        ButterKnife.bind(this, view);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        this.configureRecyclerView();
        this.configureDagger();
        this.configureArrowBack();
        this.configElemView();
        this.configureViewmodel();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.home_activity_bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        return view;
    }

    private void configureRecyclerView() {
        this.categories = new ArrayList<>();
        this.categoriesAdapter = new CategoriesAdapter(this.categories, this);
        this.recyclerView.setAdapter(this.categoriesAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
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
        titleHeader.setText(getString(R.string.my_categories)
        );
    }


    private void configureViewmodel() {
        exchangeViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeViewModel.class);
        exchangeViewModel.getMyCategories();
        exchangeViewModel.getCategoryList().observe(this, categories1 -> {
           updateUi(categories1);
        });
    }

    private void updateUi(List<Category> categoriesList) {
        this.categories.clear();
        if(categoriesList == null){
            categoriesList = new ArrayList<>();
        }
        this.categories.addAll(categoriesList);
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteCategory(int position) {
        exchangeViewModel.deleteCategory();
        categories.remove(position);
        exchangeViewModel.getCategoryList().observe(this, categories->{
            updateUi(categories);
        });
    }
}

