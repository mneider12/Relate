package com.nydev.relate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

/**
 * Created by markneider on 8/14/16.
 * Picker dialog for a full date (day, month and year)
 */
public class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

    private static final String YEAR_KEY = "year";
    private static final String MONTH_KEY = "month";
    private static final String DAY_KEY = "day";

    DateSaveListener mCallback;

    public interface DateSaveListener {
        void onDateSave(LocalDate date);
    }

    public static DatePickerFragment newInstance(@Nullable LocalDate initialDate) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();

        if (initialDate == null) {
            initialDate = new LocalDate(); // default to today
        }

        // Set up arguments for onCreateDialog
        Bundle dateArgs = new Bundle();
        dateArgs.putInt(YEAR_KEY, initialDate.getYear());
        dateArgs.putInt(MONTH_KEY, initialDate.getMonthOfYear());
        dateArgs.putInt(DAY_KEY, initialDate.getDayOfMonth());
        datePickerFragment.setArguments(dateArgs);

        return datePickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {

        Bundle dateArgs = getArguments();

        // DatePickerDialog uses 0 based month ids (0-11). Need to subtract 1 from joda month id.
        return new DatePickerDialog(getActivity(), this,
                dateArgs.getInt(YEAR_KEY), dateArgs.getInt(MONTH_KEY), dateArgs.getInt(DAY_KEY));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCallback = (DateSaveListener) context;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // DatePickerDialog uses 0 based month ids (0-11). Need to subtract 1 from joda month id.
        mCallback.onDateSave(new LocalDate(year, month + 1,day));
    }

}
