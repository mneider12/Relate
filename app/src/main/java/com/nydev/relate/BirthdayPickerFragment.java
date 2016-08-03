package com.nydev.relate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import org.joda.time.MonthDay;

import java.util.Calendar;

public class BirthdayPickerFragment extends DialogFragment {



    public static BirthdayPickerFragment newInstance(MonthDay initialDate) {
        final String MONTH_KEY = "month";
        final String DAY_KEY = "day";

        BirthdayPickerFragment birthdayPickerFragment = new BirthdayPickerFragment();

        if (initialDate == null) {
            initialDate = new MonthDay();
        }
        Bundle birthdayArgs = new Bundle();
        birthdayArgs.putInt(MONTH_KEY, initialDate.getMonthOfYear());
        birthdayArgs.putInt(DAY_KEY, initialDate.getDayOfMonth());
        birthdayPickerFragment.setArguments(birthdayArgs);

        return birthdayPickerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View birthdayPickerDialogView = inflater.inflate(R.layout.birthday_picker_dialog, container,
                false);

        return birthdayPickerDialogView;
    }
}

/*public class BirthdayPickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog.OnDateSetListener mOnDatePickedListener;

    public BirthdayPickerFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mOnDatePickedListener = (DatePickerDialog.OnDateSetListener) context;
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mOnDatePickedListener.onDateSet(datePicker, year, month, day);
    }
}
*/