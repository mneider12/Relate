package com.nydev.relate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

    /**
     * Create a new DatePickerFragment instance from a LocalDate.
     * Sets up a Bundle of arguments for onCreateDialog
     *
     * @param initialDate date to open the DatePickerDialog to when it is created.
     * @return new fragment to control the DatePickerDialog
     */
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

    /**
     * Create a DatePickerDialog based on the arguments setup in newInstance
     *
     * @param savedInstance Bundle can be used for restoring state if the activity is destroyed by the system. Currently not used.
     * @return DatePickerDialog set with year, month, day from arguments
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstance) {

        Bundle dateArgs = getArguments();

        // DatePickerDialog uses 0 based month ids (0-11). Need to subtract 1 from joda month id.
        return new DatePickerDialog(getActivity(), this,
                dateArgs.getInt(YEAR_KEY), dateArgs.getInt(MONTH_KEY) - 1, dateArgs.getInt(DAY_KEY));
    }

    /**
     * When this fragment is attached to the calling activity, save a reference to the caller
     * to use for communicating back the saved date.
     *
     * @param context calling context. Must implement DateSaveListener.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context); // needs inherited behavior

        mCallback = (DateSaveListener) context;
    }

    /**
     * When a date is selected in the DatePickerDialog, communicate that information through to the
     * calling context.
     *
     * @param datePicker {@inheritDoc}
     * @param year {@inheritDoc}
     * @param month {@inheritDoc}
     * @param day {@inheritDoc}
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // DatePickerDialog uses 0 based month ids (0-11). Need to add 1 to get joda month id.
        mCallback.onDateSave(new LocalDate(year, month + 1, day));
    }

    /**
     * Define an interface for sending a selected date back to the calling context.
     */
    public interface DateSaveListener {
        /**
         * Save the selected date
         *
         * @param date selected date
         */
        void onDateSave(LocalDate date);
    }

}
