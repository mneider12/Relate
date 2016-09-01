package com.nydev.relate;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import org.joda.time.LocalDate;
import org.joda.time.MonthDay;

/**
 * Created by markneider on 8/6/16.
 * View or edit the details of a relationship
 */
public class RelationshipDetailActivity extends AppCompatActivity
    implements BirthdayPickerFragment.OnBirthdaySaveListener,
        DemographicsEditFragment.DemographicsSaveListener,
        DatePickerFragment.DateSaveListener {

    private Relationship relationship;  // relationship being considered
    private RelationshipTableHelper relationshipTableHelper; // helper class for database operations on the relationship table
    private NoteCollectionPagerAdapter noteAdapter; // adapter to display note for this relationship


    /**
     * Load relationship information on activity creation
     * @param savedInstanceState key value pair information if the activity was last destroyed by the system automatically
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int relationshipId = intent.getIntExtra("com.nydev.relate.relationshipId", -1); // -1 indicates an invalid relationshipId

        relationshipTableHelper = new RelationshipTableHelper(this);
        if (relationshipTableHelper.isValidRelationshipId(relationshipId)) { // load in view mode if a valid existing relationship is passed in
            onCreateExistingRelationship(relationshipId);
        } else { // Create a new relationship in edit mode
            setContentView(R.layout.relationship_create);
            relationship = new Relationship(this); // reserves an ID for this Relationship
            saveRelationship();

            // Setup Toolbar title
            setTitle("NEW");
        }
        // create Toolbar
        Toolbar relationshipToolbar = (Toolbar) findViewById(R.id.relationship_toolbar);
        setSupportActionBar(relationshipToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void onCreateExistingRelationship(int relationshipId) {
        // set layout for viewing an existing relationship
        setContentView(R.layout.relationship_detail_view);
        //load an existing relationship
        relationship = relationshipTableHelper.getRelationship(relationshipId);
        // setup noteAdapter as the backbone for a swipe-able page view of notes.
        noteAdapter = new NoteCollectionPagerAdapter(getSupportFragmentManager(),
                relationshipId, this);
        ViewPager notePager = (ViewPager) findViewById(R.id.note_container); // Widget that swipes along a list provider by an adapter
        notePager.setAdapter(noteAdapter);
        // Demographics fragment in view mode initially when loading an existing relationship
        DemographicsViewFragment demographicsViewFragment =
                DemographicsViewFragment.newInstance(relationship);
        // Commit demographics fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.demographics_container, demographicsViewFragment);
        fragmentTransaction.commit();

        // Setup Toolbar title
        String name = relationship.getName().toString();
        if (name.equals("")) {
            setTitle("<unnamed>");
        } else {
            setTitle(relationship.getName().toString());
        }
    }

    /**
     * Save the relationship
     * @param saveButton not used - view calling this method, expected from onClick
     */
    public void saveRelationship(View saveButton)
    {
        saveRelationship();
        finish();
    }

    public void saveRelationship() {
        if (relationshipTableHelper.isValidRelationshipId(relationship.getRelationshipId())) { // Relationship exists; update
            relationshipTableHelper.updateRelationship(relationship);
        } else { // Relationship is new; insert
            relationshipTableHelper.insertRelationship(relationship);
        }
    }

    /**
     * Don't save relationship - revert changes and return to previous activity
     * @param cancelButton the view calling this function (not used)
     */
    public void cancelRelationship(View cancelButton) {
        if (relationshipTableHelper.isValidRelationshipId(relationship.getRelationshipId())) { // need to restore data
            relationship = relationshipTableHelper.getRelationship(relationship.getRelationshipId());
        }
        finish();
    }

    /**
     * Create a Dialog for the user to enter birthday information
     * @param birthdayPickerButton Button calling this method from onClick
     */
    public void showBirthdayPickerDialog(View birthdayPickerButton) {
        DialogFragment birthdayPickerFragment = BirthdayPickerFragment.newInstance(null); // Initialize to January 1
        FragmentManager fragmentManager = getSupportFragmentManager();
        birthdayPickerFragment.show(fragmentManager, "birthdayPicker");
    }

    /**
     * Call the saveBirthday() method in the birthdayPicker Fragment.
     * @param saveButton Button in Dialog that called this method from onClick
     */
    public void saveBirthday(View saveButton) {
        BirthdayPickerFragment birthdayPickerFragment =
                (BirthdayPickerFragment) getSupportFragmentManager().findFragmentByTag("birthdayPicker");
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
     * Delete current relationship from the relationship database and return to the dashboard
     */
    public void deleteRelationship() {
        relationshipTableHelper.deleteRelationship(relationship.getRelationshipId());
        finish();
    }

    public void editDemographics() {
        DemographicsEditFragment demographicsEditFragment =
                DemographicsEditFragment.newInstance(relationship);

        FragmentTransaction replaceViewWithEdit = getSupportFragmentManager().beginTransaction();

        replaceViewWithEdit.replace(R.id.demographics_container, demographicsEditFragment);
        replaceViewWithEdit.addToBackStack(null);
        replaceViewWithEdit.commit();
    }

    public void showDatePickerDialog(View noteDateSelectButton) {
        LocalDate noteDate = new LocalDate(((TextView) noteDateSelectButton).getText());
        DialogFragment newFragment = DatePickerFragment.newInstance(noteDate);
        newFragment.show(getSupportFragmentManager(), "noteDatePicker");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_demographics:
                editDemographics();
                return true;
            case R.id.action_delete_relationship:
                deleteRelationship();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_activity_app_bar, menu);
        return true;
    }

    public void createNote(View createNoteButton) {
        Note note = new Note(relationship.getRelationshipId());
        Fragment noteFragment = NoteEditFragment.newInstance(note); // open in edit mode for a new note
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.note_container, noteFragment);
        fragmentTransaction.commit();
    }

    public void editNote(View editButton) {
        noteAdapter.edit(((ViewPager) findViewById(R.id.note_container)).getCurrentItem());
    }

    @Override
    public void onStop() {
        super.onStop();
        saveRelationship();
    }

    @Override
    public void onBackPressed() {
        saveRelationship();
        super.onBackPressed();
    }

    @Override
    public void onDateSave(LocalDate noteDate) {
        NoteEditFragment noteEditFragment = (NoteEditFragment) noteAdapter.getCurrentFragment();
        noteEditFragment.updateNoteDate(noteDate);
    }
}
