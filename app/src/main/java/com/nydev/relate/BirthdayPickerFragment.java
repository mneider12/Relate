package com.nydev.relate;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.MonthDay;

import com.nydev.relate.DateHelper.Month;

import java.util.ArrayList;

/**
 * Created by markneider on 8/2/16
 * Reusable fragment to select a month and day birthday
 */
public class BirthdayPickerFragment extends DialogFragment {

    @SuppressWarnings("unused") // only needed during debugging
    private static final String LOG_TAG = "nydev.Relate"; // identifier for debug messages from this class

    private static final String MONTH_KEY = "month"; // key for an argument with a month value
    private static final String DAY_KEY = "day"; // key for an argument with a day value

    OnBirthdaySaveListener mCallback; // calling context - needs to implement OnBirthdaySaveListener

    /**
     * Methods needed for communication between calling activity and this Fragment.
     * The caller must implement this interface to avoid crashes.
     */
    public interface OnBirthdaySaveListener {
        /**
         * Save birthday information from BirthdayPickerFragment
         * This method will be called by this Fragment to send birthday information back to the
         * calling activity once it is ready to be saved.
         * @param birthday birthday selected by this dialog
         */
        void saveBirthday(MonthDay birthday);

        /**
         * Call saveBirthday method in BirthdayPickerFragment.
         * Called directly by BirthdayPickerFragment UI.
         * @param saveButton Button in Dialog that called this method from onClick
         */
        void saveBirthday(View saveButton);
    }

    /**
     * Create a new BirthdayPickerFragment.
     * @param initialDate Initial date to be displayed in the fragment UI.
     *                    If null, then initialize to January 1.
     * @return new BirthdayPickerFragment
     */
    public static BirthdayPickerFragment newInstance(@Nullable MonthDay initialDate) {
        BirthdayPickerFragment birthdayPickerFragment = new BirthdayPickerFragment();

        if (initialDate == null) {
            initialDate = new MonthDay(1, 1); // Initializes to January 1 by default
        }
        // Set up arguments for use in onCreateView
        Bundle birthdayArgs = new Bundle(); // key-value pairs
        birthdayArgs.putInt(MONTH_KEY, initialDate.getMonthOfYear());
        birthdayArgs.putInt(DAY_KEY, initialDate.getDayOfMonth());
        birthdayPickerFragment.setArguments(birthdayArgs);

        return birthdayPickerFragment;
    }

    /**
     * Inflate dialog view, setup and initialize date spinners
     * @param layoutInflater {@inheritDoc}
     * @param container {@inheritDoc}
     * @param savedInstance {@inheritDoc}
     * @return initialized dialog view
     */
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstance) {
        View birthdayPickerDialogView = layoutInflater.inflate(R.layout.birthday_picker_dialog, container,
                false);

        Bundle birthdayArgs = getArguments(); // month and day should be specified

        Spinner monthSpinner = (Spinner) birthdayPickerDialogView.findViewById(R.id.month_spinner);

        // Override necessary methods to tweak UI behavior
        ArrayAdapter<Month> monthAdapter = new ArrayAdapter<Month>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, Month.values()) {

            /**
             * Set text to Month abbreviation for selected item instead of toString
             * @param position {@inheritDoc}
             * @param convertView {@inheritDoc}
             * @param parent {@inheritDoc}
             * @return view for selected item with month abbreviation as text
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);

                view.setText(getItem(position).getAbbreviation());
                return view;
            }
        };

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        int monthOfYear = birthdayArgs.getInt(MONTH_KEY);
        int monthIndex = monthOfYear - 1; // monthOfYear values start at 1, need to subtract 1 for array index
        monthSpinner.setSelection(monthIndex); // set initial month selection

        Spinner dayInMonthSpinner = (Spinner) birthdayPickerDialogView.findViewById(R.id.day_spinner);

        // Populate daysInMonthArray based on maxDays in monthOfYear
        Month month = Month.getMonth(monthOfYear);
        ArrayList<Integer> daysInMonthArray = new ArrayList<>();
        for (int day = 1; day <= month.getMaxDays(); day++) {
            daysInMonthArray.add(day);
        }

        // Create day spinner
        ArrayAdapter<Integer> dayInMonthAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, daysInMonthArray);
        dayInMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayInMonthSpinner.setAdapter(dayInMonthAdapter);
        // set initial day selection
        dayInMonthSpinner.setSelection(birthdayArgs.getInt(DAY_KEY) - 1); // day values start at 1, subtract 1 for 0-based index

        return birthdayPickerDialogView;
    }

    /**
     * Capture context as an OnBirthdaySaveListener to use for communication between calling
     * activity and this Fragment
     * @param context {@inheritDoc}
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCallback = (OnBirthdaySaveListener) context; // calling activity must implement OnBirthdaySaveListener
    }

    /**
     * Communicate birthday information back to calling activity.
     * Calls saveBirthday(MonthDay) method in calling activity.
     */
    public void saveBirthday() {
        // Check that we can find the root view of this Fragment
        View rootView = getView();
        if (rootView == null) {
            Log.e(LOG_TAG, "Could not find root view in BirthdayPickerFragment. Failed to save birthday");
            return;
        }

        // get the selected month from the month spinner
        Spinner monthSpinner = (Spinner) rootView.findViewById(R.id.month_spinner);
        Month birthMonth = (Month) monthSpinner.getSelectedItem();

        // get the selected day from the day spinner
        Spinner dayOfMonthSpinner = (Spinner) rootView.findViewById(R.id.day_spinner);
        int dayOfMonth = (Integer) dayOfMonthSpinner.getSelectedItem();

        MonthDay birthday = new MonthDay(birthMonth.getMonthOfYear(), dayOfMonth);

        mCallback.saveBirthday(birthday); // send birthday to calling activity

        this.dismiss(); // close dialog now that we're done with it
    }
}