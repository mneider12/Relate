package com.nydev.relate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;

/**
 * Created by markneider on 7/1/16.
 */
public class RelationshipDetailActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "RelationshipDetail";
    private Relationship relationship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_detail);
        Intent intent = getIntent();
        int relationshipId = intent.getIntExtra("com.nydev.relate.relationshipId", 0);
        if (PreferencesHelper.isValidRelationshipId(relationshipId))    // Activity should open existing relationship in view mode
        {
            loadRelationship(relationshipId);
        }
        else
        {
            relationship = new Relationship(this);
        }
    }

    private void loadRelationship(int relationshipId)
    {
        relationship = PreferencesHelper.getRelationship(this, relationshipId);
        TextView nameEntryEditText = (TextView) findViewById(R.id.name_entry_edit_text);

        if (relationship.getName() != null) // load name if set
        {
            nameEntryEditText.setText(relationship.getName());
            nameEntryEditText.setEnabled(false);
        }
        else    // if name is not set, hide name display
        {
            nameEntryEditText.setVisibility(View.GONE);
        }

        // TODO if (relationship.getBirthday() != null)
    }

    public void saveRelationship(View view)
    {
        PreferencesHelper.incrementNextRelationshipId(this);

        String name = getStringFromTextView(R.id.name_entry_edit_text);
        String birthday = getStringFromTextView(R.id.birthday_edit_text);
        String relationshipDescription = getStringFromTextView(R.id.relationship_edit_text);
        String note = getStringFromTextView(R.id.note_edit_text);

        RelationshipDbHelper relationshipDbHelper = new RelationshipDbHelper(this);
        relationshipDbHelper.insertRelationship(relationship.getId(), relationship.getName(), relationship.getName());
    }

    /**
     * Get the text from a text view and return it as a String
     * @param textViewId is the id of the TextView to be used in findViewById
     * @return text from the given TextView
     */
    private String getStringFromTextView(int textViewId)
    {
        TextView textView = (TextView) findViewById(textViewId);
        return textView.getText().toString();
    }

    public static class BirthdayPickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day)
        {

        }
    }

    public void showBirthdayPickerDialog(View view)
    {
        DialogFragment newFragment = new BirthdayPickerFragment();
        newFragment.show(getFragmentManager(), "birthdayPicker");
    }
}
