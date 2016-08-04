package com.nydev.relate;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
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

    private static final String LOG_TAG = "nydev.Relate";

    private static final String MONTH_KEY = "month";
    private static final String DAY_KEY = "day";

    OnBirthdaySaveListener mCallback;

    public interface OnBirthdaySaveListener {
        void saveBirthday(Month birthMonth, int birthDayOfMonth);
        void saveBirthday(View saveButton);
    }

    public static BirthdayPickerFragment newInstance(MonthDay initialDate) {
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

        Bundle birthdayArgs = getArguments();

        Spinner monthSpinner = (Spinner) birthdayPickerDialogView.findViewById(R.id.month_spinner);
        ArrayAdapter<Month> monthAdapter = new ArrayAdapter<Month>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, Month.values()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);

                view.setText(getItem(position).getAbbreviation());
                return view;
            }
        };
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setSelection(birthdayArgs.getInt(MONTH_KEY) - 1);

        Spinner dayInMonthSpinner = (Spinner) birthdayPickerDialogView.findViewById(R.id.day_spinner);
        ArrayList<Integer> daysInMonthArray = new ArrayList<>();
        for (int day = 1; day <= 31; day++) {
            daysInMonthArray.add(day);
        }
        ArrayAdapter<Integer> dayInMonthAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, daysInMonthArray);
        dayInMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayInMonthSpinner.setAdapter(dayInMonthAdapter);
        dayInMonthSpinner.setSelection(birthdayArgs.getInt(DAY_KEY) - 1);

        return birthdayPickerDialogView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCallback = (OnBirthdaySaveListener) context;
    }

    public void saveBirthday(View saveButton) {
        Spinner monthSpinner = (Spinner) getView().findViewById(R.id.month_spinner);
        Month birthMonth = (Month) monthSpinner.getSelectedItem();

        Spinner dayOfMonthSpinner = (Spinner) getView().findViewById(R.id.day_spinner);
        int dayOfMonth = (Integer) dayOfMonthSpinner.getSelectedItem();

        mCallback.saveBirthday(birthMonth, dayOfMonth);


    }
}