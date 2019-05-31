package com.selclientapp.selapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.selclientapp.selapp.App;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Category;
import com.selclientapp.selapp.model.Exchange;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;
import com.selclientapp.selapp.utils.ExchangeListener;
import com.selclientapp.selapp.view_models.ExchangeViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class AddExchangeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //FOR DESIGN
    @BindView(R.id.fragment_exchange_add_input_username)
    TextInputLayout usernameInput;
    @BindView(R.id.fragment_exchange_add_username)
    TextInputEditText username;
    @BindView(R.id.fragment_exchange_add_input_description)
    TextInputLayout descriptionInput;
    @BindView(R.id.fragment_exchange_add_description)
    TextInputEditText description;
    @BindView(R.id.fragment_exchange_add_date)
    TextView datePicker;
    @BindView(R.id.fragment_exchange_add_input_date)
    TextInputLayout dateInput;
    @BindView(R.id.fragment_exchange_add_input_time)
    TextInputLayout timeInput;
    @BindView(R.id.fragment_exchange_add_time)
    TextView timePicker;
    @BindView(R.id.fragment_title_header)
    TextView titleHeader;
    @BindView(R.id.fragment_exchange_add_image_btn_date)
    ImageButton imgDate;
    @BindView(R.id.fragment_exchange_add_image_btn_time)
    ImageButton imgTime;
    @BindView(R.id.fragment_exchange_add_btn_create)
    Button btnCreate;
    @BindView(R.id.fragment_exchange_add_input_capacity)
    TextInputLayout capacityInput;
    @BindView(R.id.fragment_exchange_add_capacity)
    TextInputEditText capacity;
    @BindView(R.id.fragment_arrow)
    ImageButton imgArrowBack;
    @BindView(R.id.spinner_ex_edit)
    protected Spinner spinner;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    protected static String dateExchange;
    protected static String timeExchange;
    protected String category = null;

    //FOR DATA
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    protected ExchangeViewModel exchangeViewModel;
    protected AddExchangeListener callbackAddListener;
    protected ExchangeListener callbackExchangeListener;
    protected ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();


    public interface AddExchangeListener {
        void addExchange(Exchange exchange);

        void updateExchange(Exchange exchange);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_edit, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        this.configureDagger();
        this.configureViewmodel();
        this.configureDateListener();
        this.configureTimeListener();
        this.configureBtnCreate();
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

    private void configureViewmodel() {
        exchangeViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExchangeViewModel.class);
    }

    private void configureDateListener() {
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
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

                dateExchange = date;
                datePicker.setText(date);
            }
        };
    }

    private void configureTimeListener() {
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int minute = c.get(Calendar.MINUTE);
                int hour = c.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog dialog = new TimePickerDialog(getActivity(), mTimeSetListener, hour, minute, true);
                dialog.show();
            }
        });

        imgTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int minute = c.get(Calendar.MINUTE);
                int hour = c.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog dialog = new TimePickerDialog(getActivity(), mTimeSetListener, hour, minute, true);
                dialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = "";
                if (hourOfDay < 10) {
                    time += "0" + hourOfDay;
                } else {
                    time += hourOfDay;
                }

                if (minute < 10) {
                    time += ":0" + minute;
                } else {
                    time += ":" + minute;
                }

                timeExchange = " " + time;
                timePicker.setText(time);
            }
        };
    }

    private void configureBtnCreate() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int capa = Integer.parseInt(capacity.getText().toString());
                Exchange exchange = new Exchange(
                        username.getText().toString(),
                        description.getText().toString(),
                        managementTokenAndUSer.getCurrentUser().getUsername(),
                        dateExchange + timeExchange + ":00",
                        capa,
                        managementTokenAndUSer.getCurrentUser().getId(),
                        managementTokenAndUSer.getCurrentUser().getFileName(),
                        category);
                callbackAddListener.addExchange(exchange);
                exchangeViewModel.AddExchange(exchange);
                exchangeViewModel.getExchangeLiveData().observe(getActivity(), ex -> {
                    getActivity().onBackPressed();
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                });
            }
        });
    }

    private void configureElemView() {
        btnCreate.setEnabled(false);
        titleHeader.setText("New Exchange.");
        usernameInput.setError("Field can't be empty.");
        username.addTextChangedListener(usernameWatcher);
        descriptionInput.setError("Field can't be empty.");
        description.addTextChangedListener(descriptionWatcher);
        dateInput.setError("Field can't be empty.");
        datePicker.addTextChangedListener(dateWatcher);
        timeInput.setError("Field can't be empty.");
        timePicker.addTextChangedListener(timeWatcher);
        capacityInput.setError("Field can't be empty.");
        capacity.addTextChangedListener(capacityWatcher);
        this.configureArrowBack();
        this.configureSpinner();
    }

    private final TextWatcher usernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name = username.getText().toString().trim();
            hasToSetError(name, usernameInput);
            btnCreate.setEnabled(validBtnCreate());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final TextWatcher descriptionWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String desc = description.getText().toString().trim();
            hasToSetError(desc, descriptionInput);
            btnCreate.setEnabled(validBtnCreate());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final TextWatcher dateWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String date = datePicker.getText().toString().trim();
            hasToSetError(date, dateInput);
            btnCreate.setEnabled(validBtnCreate());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final TextWatcher timeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String time = timePicker.getText().toString().trim();
            hasToSetErrorTime(time, timeInput);
            btnCreate.setEnabled(validBtnCreate());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private final TextWatcher capacityWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String capa = capacity.getText().toString().trim();
            hasToSetErrorCapacity(capa, capacityInput);
            btnCreate.setEnabled(validBtnCreate());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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

    protected void configureSpinner() {
        exchangeViewModel.getAllCategory();
        exchangeViewModel.getCategoryList().observe(this, categories -> {
            if (categories != null) {
                categories.add(0, new Category(null));
            } else {
                categories = new ArrayList<>();
            }
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(getActivity(), R.layout.cunstom_spinner, categories);
            spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_ATOP);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

        });
    }
    // -----------------
    // UTILS
    // -----------------

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        category = null;
    }

    private boolean hasToSetError(String editText, TextInputLayout textInputLayout) {
        if (editText.isEmpty()) {
            textInputLayout.setError("Field can't be empty");
            return false;
        } else {
            textInputLayout.setError("");
            return true;
        }
    }

    private boolean hasToSetErrorCapacity(String editText, TextInputLayout textInputLayout) {
        if (editText.isEmpty()) {
            textInputLayout.setError("Field can't be empty");
            return false;
        } else if (Integer.parseInt(editText) < 2) {
            textInputLayout.setError("Must be superior to one");
            return false;
        } else if (Integer.parseInt(editText) > 300) {
            textInputLayout.setError("Capacity is limited to 300");
            return false;
        } else {
            textInputLayout.setError("");
            return true;
        }
    }

    private boolean hasToSetErrorTime(String editText, TextInputLayout textInputLayout) {
        if (dateExchange != null && timeExchange != null) {
            String dateTime = dateExchange + timeExchange;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date currentDate = null;
            try {
                currentDate = format.parse(dateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (editText.isEmpty()) {
                textInputLayout.setError("Field can't be empty");
                return false;
            } else if (currentDate.getTime() < new Date().getTime()) {
                textInputLayout.setError("Can't choose an hour from the past");
                return false;

            } else {
                textInputLayout.setError("");
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean validBtnCreate() {
        String desc = description.getText().toString().trim();
        String name = username.getText().toString().trim();
        String capa = capacity.getText().toString().trim();
        String date = datePicker.getText().toString().trim();
        String time = timePicker.getText().toString().trim();
        return (hasToSetError(name, usernameInput) && hasToSetError(desc, descriptionInput) && hasToSetError(date, dateInput) && hasToSetErrorTime(time, timeInput) && hasToSetErrorCapacity(capa, capacityInput));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbackAddListener = (AddExchangeListener) context;
        callbackExchangeListener = (ExchangeListener) context;
    }

    @Override
    public void onDetach() {
        callbackAddListener = null;
        callbackExchangeListener = null;
        super.onDetach();
    }
}
