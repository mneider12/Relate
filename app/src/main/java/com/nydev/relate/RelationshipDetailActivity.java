package com.nydev.relate;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.joda.time.MonthDay;

/**
 * Created by markneider on 8/6/16.
 * View or edit the details of a relationship
 */
public class RelationshipDetailActivity extends AppCompatActivity
    implements BirthdayPickerFragment.OnBirthdaySaveListener,
        DemographicsEditFragment.DemographicsSaveListener,
        DemographicsViewFragment.OnEditDemographicsButtonListener {

    private Relationship relationship;
    private RelationshipDbHelper relationshipDbHelper;

    /**
     * Load relationship information on activity creation
     * @param savedInstanceState key value pair information if the activity was last destroyed by the system automatically
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int relationshipId = intent.getIntExtra("com.nydev.relate.relationshipId", 0);

        relationshipDbHelper = new RelationshipDbHelper(this);
        if (relationshipDbHelper.isValidRelationshipId(relationshipId)) { // load in view mode if a valid existing relationship is passed in
            setContentView(R.layout.relationship_detail_view);

            relationship = relationshipDbHelper.getRelationship(relationshipId);

            DemographicsViewFragment demographicsViewFragment =
                    DemographicsViewFragment.newInstance(relationship);

            getFragmentManager().beginTransaction().add(
                    R.id.demographics_container, demographicsViewFragment).commit();

        } else { // Create a new relationship in edit mode
            setContentView(R.layout.relationship_create);
            relationship = new Relationship(this); // reserves an ID for this Relationship
        }
    }

    /**
     * Save the relationship being created
     * @param saveButton not used - view calling this method, expected from onClick
     */
    public void saveRelationship(View saveButton)
    {
        if (relationshipDbHelper.isValidRelationshipId(relationship.getRelationshipId())) { // Relationship exists; update
            relationshipDbHelper.updateRelationship(relationship);
        } else { // relationship is new; insert
            relationshipDbHelper.insertRelationship(relationship);
        }
        finish();
    }

    /**
     * Don't save relationship - revert changes and return to previous activity
     * @param cancelButton the view calling this function (not used)
     */
    public void cancelRelationship(View cancelButton) {
        if (relationshipDbHelper.isValidRelationshipId(relationship.getRelationshipId())) { // need to restore data
            relationship = relationshipDbHelper.getRelationship(relationship.getRelationshipId());
        }
        finish();
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

    public void saveName(Name name) {
        relationship.setName(name);
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

    /**
     * Delete current relationship from the relationship database and return to the dashboard.
     * @param deleteButton Button that called this method from onClick
     */
    public void deleteRelationship(View deleteButton) {
        relationshipDbHelper.deleteRelationship(relationship.getRelationshipId());
        finish();
    }

    public void editDemographics(View editButton) {
        DemographicsEditFragment demographicsEditFragment =
                DemographicsEditFragment.newInstance(relationship);

        FragmentTransaction replaceViewWithEdit = getFragmentManager().beginTransaction();

        replaceViewWithEdit.replace(R.id.demographics_container, demographicsEditFragment);
        replaceViewWithEdit.addToBackStack(null);
        replaceViewWithEdit.commit();
    }
}
