package com.nydev.relate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.w3c.dom.Text;

/**
 * Created by markneider on 8/14/16.
 */
public class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

    DateSaveListener mCallback;

    public interface DateSaveListener {
        void onDateSave(LocalDate date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        LocalDate initialDate = new LocalDate(); // default to today

        // DatePickerDialog uses 0 based month ids (0-11). Need to subtract 1 from joda month id.
        return new DatePickerDialog(getActivity(), this, initialDate.getYear(),
                initialDate.getMonthOfYear() - 1, initialDate.getDayOfMonth());
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
