package com.nydev.relate;

import android.app.DialogFragment;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.MonthDay;

/**
 * Created by markneider on 7/1/16.
 * Create a Relationship
 */
public class RelationshipCreateActivity extends AppCompatActivity
        implements BirthdayPickerFragment.OnBirthdaySaveListener
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
        nameTextView.addTextChangedListener(new TextWatcher() {
            /**
             * Not used
             * {@inheritDoc}
             * @param charSequence {@inheritDoc}
             * @param i {@inheritDoc}
             * @param i1 {@inheritDoc}
             * @param i2 {@inheritDoc}
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * Not used
             * {@inheritDoc}
             * @param charSequence {@inheritDoc}
             * @param i {@inheritDoc}
             * @param i1 {@inheritDoc}
             * @param i2 {@inheritDoc}
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * After text is changed in the name entry text field, save the new name to the relationship
             * @param nameEditable text from name edit text view
             */
            @Override
            public void afterTextChanged(Editable nameEditable) {
                String rawName = nameEditable.toString();
                Name relationshipName = new Name(rawName);
                relationship.setName(relationshipName);
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

    /**
     * Create a Dialog for the user to enter birthday information
     * @param birthdayPickerButton Button calling this method from onClick
     */
    public void showBirthdayPickerDialog(View birthdayPickerButton) {
        DialogFragment birthdayPickerFragment = BirthdayPickerFragment.newInstance(null); // Initialize to January 1
        FragmentManager fragmentManager = getFragmentManager();
        birthdayPickerFragment.show(fragmentManager, "birthdayPicker");
    }

    /**
     * Call the saveBirthday() method in the birthdayPicker Fragment.
     * @param saveButton Button in Dialog that called this method from onClick
     */
    public void saveBirthday(View saveButton) {
        BirthdayPickerFragment birthdayPickerFragment =
                (BirthdayPickerFragment) getFragmentManager().findFragmentByTag("birthdayPicker");
        birthdayPickerFragment.saveBirthday();
    }

    /**
     * Save the relation's birthday.
     * Called by BirthdayPickerFragment.
     * @param birthday birthday selected by this dialog
     */
    public void saveBirthday(MonthDay birthday) {
        relationship.setBirthday(birthday);
        Button birthdayButton = (Button) findViewById(R.id.birthday_selection_button);
        birthdayButton.setText(birthday.toString());
    }
}
