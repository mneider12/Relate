package com.nydev.relate;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;

import org.joda.time.MonthDay;

/**
 * Created by markneider on 7/1/16.
 * Create a Relationship
 */
public class RelationshipCreateActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener
{
    @SuppressWarnings("unused") // only used currently when needed for debugging.
    private static final String LOG_TAG = "nydev.Relate";
    private Relationship relationship; // Relationship being edited

    /**
     * Load relationship information on activity creation
     * @param savedInstanceState key value pair information if the activity was last destroyed by the system automatically
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_create);
        relationship = new Relationship(this); // reserves an ID for this Relationship

        TextView nameTextView = (TextView) findViewById(R.id.name_text_view);
        nameTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View nameTextView, boolean hasFocus) {
                if (!hasFocus) {
                    String rawName = ((TextView) nameTextView).getText().toString();
                    Name relationshipName = new Name(rawName);
                    relationship.setName(relationshipName);
                }
            }
        });
    }

    /**
     * Save the relationship being created
     * @param saveButton not used - view calling this method, expected from onClick
     */
    public void saveRelationship(View saveButton)
    {
        RelationshipDbHelper relationshipDbHelper = new RelationshipDbHelper(this);
        relationshipDbHelper.insertRelationship(relationship);
        RelationshipEditHelper.fallBackToDashboard(this);
    }

    /**
     * Don't save relationship - destroy activity and return to dashboard
     * @param cancelButton the view calling this function (not used)
     */
    public void cancelRelationship(View cancelButton) {
        RelationshipEditHelper.fallBackToDashboard(this);
    }

    public void showBirthdayPickerDialog(View birthdayPickerButton) {
        DialogFragment birthdayPickerFragment = BirthdayPickerFragment.newInstance(
                new MonthDay(1, 1)); // default to January 1
        FragmentManager fragmentManager = getFragmentManager();
        birthdayPickerFragment.show(fragmentManager, "birthdayPicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        MonthDay birthday = new MonthDay(month, day);
        relationship.setBirthday(birthday);
    }
}
