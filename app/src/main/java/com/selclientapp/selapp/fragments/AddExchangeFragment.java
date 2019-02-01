package com.selclientapp.selapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.selclientapp.selapp.R;


import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class AddExchangeFragment extends Fragment {

    //FOR DESIGN
    @BindView(R.id.fragment_exchange_add_input_username)
    TextInputLayout usernameInput;
    @BindView(R.id.fragment_exchange_add_username)
    EditText username;
    @BindView(R.id.fragment_exchange_add_description)
    EditText description;
    @BindView(R.id.fragment_exchange_add_date)
    EditText datePicker;
    @BindView(R.id.fragment_exchange_add_time)
    EditText timePicker;
    @BindView(R.id.fragment_exchange_add_image_btn_date)
    ImageButton imgDate;
    @BindView(R.id.fragment_exchange_add_image_btn_time)
    ImageButton imgTime;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    //FOR DATA

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_edit, container, false);
        ButterKnife.bind(this, view);
        this.configureDagger();
        this.configureDateListener();
        this.configureTimeListener();
        return view;
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureDateListener() {
        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Widget_Holo, mDateSetListener, year, month, day);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = year + "-" + month + "-" + dayOfMonth;
                datePicker.setText(date);
            }
        };
    }

    private void configureTimeListener() {
        imgTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int minute = c.get(Calendar.MINUTE);
                int hour = c.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog dialog = new TimePickerDialog(getActivity(),android.R.style.Widget_Holo,mTimeSetListener,hour,minute,true);
                dialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
                timePicker.setText(time);
            }
        };
    }

    // -----------------
    // ACTION
    // -----------------
}
