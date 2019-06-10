package com.selclientapp.selapp.fragments;

import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.selclientapp.selapp.App;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Category;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.utils.Tools;
import com.selclientapp.selapp.view_models.ExchangeViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class SortingBottomSheetFragment extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {

    //FOR DESIGN
    @BindView(R.id.fragment_sorting_spinner)
    AppCompatSpinner spinnerCategory;
    @BindView(R.id.fragment_sorting_btn_end_date)
    Button btnEndDate;
    @BindView(R.id.fragment_sorting_btn_start_date)
    Button btnStartDate;
    @BindView(R.id.fragment_sorting_btn_search)
    Button btnSearch;

    //FOR DATA
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    protected ExchangeViewModel exchangeViewModel;
    private DatePickerDialog.OnDateSetListener mDateStartSetListener, mDateEndSetListener;
    private String dateEndSorting, dateStartSorting, category;
    private Date dateStart, dateEnd;
    private List<Exchange> exchangesIsFull;
    private ExchangeFragment exchangeFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sorting, container, false);
        ButterKnife.bind(this, view);
        configureDagger();
        configElemview();
        getExchangeList();
        return view;
    }

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configViewModel() {
        exchangeViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeViewModel.class);
    }

    private void configElemview() {
        configBtnSearch();
        configBtnEndDate();
        btnEndDate.setEnabled(false);
        configViewModel();
        configBtnStartDate();
        configureSpinner();
    }


    private void configBtnSearch() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeFragment.getAdapter().updateList(sortingListExchange());
                exchangeFragment.getAdapter().notifyDataSetChanged();
                dismiss();
            }
        });
    }

    private void configBtnStartDate() {
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), mDateStartSetListener, year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        mDateStartSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = "";

                if (month < 10) {
                    date += year + "-0" + month;
                } else {
                    date += year + "-" + month;
                }

                if (dayOfMonth < 10) {
                    date += "-0" + dayOfMonth;
                } else {
                    date += "-" + dayOfMonth;
                }

                dateStartSorting = date;
                dateStart = Tools.getDate(date);
                if (dateStart != null) {
                    btnEndDate.setEnabled(true);
                }
                btnStartDate.setText(dateStartSorting);
            }
        };
    }


    private void configBtnEndDate() {

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), mDateEndSetListener, year, month, day);
                dialog.getDatePicker().setMinDate(dateStart.getTime());
                dialog.show();

            }
        });

        mDateEndSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = "";

                if (month < 10) {
                    date += year + "-0" + month;
                } else {
                    date += year + "-" + month;
                }

                if (dayOfMonth < 10) {
                    date += "-0" + dayOfMonth;
                } else {
                    date += "-" + dayOfMonth;
                }
                dateEndSorting = date;
                dateEnd = Tools.getDate(dateEndSorting);
                btnEndDate.setText(dateEndSorting);
            }
        };
    }

    private void configureSpinner() {
        exchangeViewModel.getAllCategory();
        exchangeViewModel.getCategoryList().observe(this, categories -> {
            categories.add(0, new Category("no category"));
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(getActivity(), R.layout.cunstom_spinner, categories);
            spinnerCategory.getBackground().setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_ATOP);
            spinnerCategory.setAdapter(adapter);
            spinnerCategory.setOnItemSelectedListener(this);
        });

    }

    private void getExchangeList() {
        exchangeFragment = (ExchangeFragment) getFragmentManager().findFragmentByTag("fragment_exchange");
        exchangesIsFull = exchangeFragment.getExchanges();
    }

    private List<Exchange> sortingListExchange() {
        List<Exchange> filteredList = new ArrayList<>();
        for (Exchange item : exchangesIsFull) {
            Date exchangeDate = Tools.getDate(item.getDate());
            if (dateStart == null) {
                if (item.getCategory() != null && item.getCategory().equals(category)) {
                    filteredList.add(item);
                }
            } else {
                if (dateStart.compareTo(exchangeDate) <= 0 && dateEnd == null) {
                    filteredList.add(item);
                } else {
                    if (dateEnd == null && dateStart.compareTo(exchangeDate) <= 0 && item.getCategory().equals(category)) {
                        filteredList.add(item);
                    } else if (dateEnd != null && dateEnd.compareTo(exchangeDate) >= 0 && dateStart.compareTo(exchangeDate) <= 0 && category.equals("no category")) {
                        filteredList.add(item);
                    } else if (dateEnd != null && dateEnd.compareTo(exchangeDate) >= 0 && dateStart.compareTo(exchangeDate) <= 0 && item.getCategory().equals(category)) {
                        filteredList.add(item);
                    }
                }

            }
        }
        configBtnCloseSorting();

        return filteredList;

    }

    private void configBtnCloseSorting() {
        ImageButton imgBtnCloseSorting = exchangeFragment.getBtnCloseSorting();
        imgBtnCloseSorting.setVisibility(View.VISIBLE);
        imgBtnCloseSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeFragment.getAdapter().updateList(exchangesIsFull);
                imgBtnCloseSorting.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        category = "no category";
    }
}
