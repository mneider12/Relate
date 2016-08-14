package com.nydev.relate;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.MonthDay;

import java.util.ArrayList;

/**
 * Created by markneider on 8/6/16.
 * View or edit the details of a relationship
 */
public class RelationshipDetailActivity extends AppCompatActivity
    implements BirthdayPickerFragment.OnBirthdaySaveListener,
        DemographicsEditFragment.DemographicsSaveListener,
        DemographicsViewFragment.OnEditDemographicsButtonListener,
        NoteFragment.NoteSaveListener {

    private Relationship relationship;
    private RelationshipTableHelper relationshipTableHelper;
    private NoteTableHelper noteTableHelper;
    private Note note;

    /**
     * Load relationship information on activity creation
     * @param savedInstanceState key value pair information if the activity was last destroyed by the system automatically
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int relationshipId = intent.getIntExtra("com.nydev.relate.relationshipId", 0);

        relationshipTableHelper = new RelationshipTableHelper(this);
        if (relationshipTableHelper.isValidRelationshipId(relationshipId)) { // load in view mode if a valid existing relationship is passed in
            setContentView(R.layout.relationship_detail_view);

            relationship = relationshipTableHelper.getRelationship(relationshipId);

            noteTableHelper = new NoteTableHelper(this);
            ArrayList<Note> notes = noteTableHelper.getNotes(relationship);
            if (notes.isEmpty()) {
                note = new Note(this, relationship);
            } else {
                note = notes.get(0);
            }

            DemographicsViewFragment demographicsViewFragment =
                    DemographicsViewFragment.newInstance(relationship);
            NoteFragment noteFragment = NoteFragment.newInstance(note);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.note_container, noteFragment);
            fragmentTransaction.add(R.id.demographics_container, demographicsViewFragment);
            fragmentTransaction.commit();

        } else { // Create a new relationship in edit mode
            setContentView(R.layout.relationship_create);
            relationship = new Relationship(this); // reserves an ID for this Relationship
            note = new Note(this, relationship);
        }
        /*
        TextView noteView = (TextView) findViewById(R.id.note_edit_text);
        noteView.setText(note.getNoteText());
        // set watcher on note activity
        setNoteEditTextWatcher(noteView);*/
    }

    /**
     * Set a TextWatcher on the name edit text view to save the name afterTextChanged
     * @param noteEditTextView view to set watcher on
     */
    private void setNoteEditTextWatcher(final TextView noteEditTextView) {
        noteEditTextView.addTextChangedListener(new TextWatcher() {
            /**
             * Not used
             * {@inheritDoc}
             *
             * @param charSequence {@inheritDoc}
             * @param i            {@inheritDoc}
             * @param i1           {@inheritDoc}
             * @param i2           {@inheritDoc}
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * Not used
             * {@inheritDoc}
             *
             * @param charSequence {@inheritDoc}
             * @param i            {@inheritDoc}
             * @param i1           {@inheritDoc}
             * @param i2           {@inheritDoc}
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * After text is changed in the name entry text field, save the new name to the relationship
             *
             * @param noteEditable text from name edit text view
             */
            @Override
            public void afterTextChanged(Editable noteEditable) {
                note.setNoteText(noteEditable.toString());
            }
        });
    }

    /**
     * Save the relationship
     * @param saveButton not used - view calling this method, expected from onClick
     */
    public void saveRelationship(View saveButton)
    {
        if (relationshipTableHelper.isValidRelationshipId(relationship.getRelationshipId())) { // Relationship exists; update
            relationshipTableHelper.updateRelationship(relationship);
        } else { // relationship is new; insert
            relationshipTableHelper.insertRelationship(relationship);
        }
        if (!note.getNoteText().equals("")) {
            if (noteTableHelper.isValidNoteId(note.getNoteId())) {
                noteTableHelper.updateNote(note);
            } else {
                noteTableHelper.insertNote(note);
            }
        }
        finish();
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
        relationshipTableHelper.deleteRelationship(relationship.getRelationshipId());
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

    public void saveNoteText(String noteText) {
        note.setNoteText(noteText);
    }

    public void saveNoteDate(LocalDate noteDate) {
        note.setNoteDate(noteDate);
    }
}
