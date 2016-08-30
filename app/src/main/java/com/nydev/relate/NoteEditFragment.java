package com.nydev.relate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    private NoteTableHelper noteTableHelper;

    private Note note;

    /**
     * Get a new NoteEditFragment
     *
     * @param note note to edit
     * @return new fragment to edit a note
     */
    public static NoteEditFragment newInstance(Note note) {
        NoteEditFragment noteFragment = new NoteEditFragment();

        Bundle noteArgs = NoteFragmentHelper.saveNoteBundle(note);
        noteFragment.setArguments(noteArgs);

        return noteFragment;
    }

    /**
     * Load information from existing note, or create a new one if needed.
     *
     * @param layoutInflater {@inheritDoc}
     * @param container {@inheritDoc}
     * @param savedInstanceState {@inheritDoc}
     * @return layout with note information in child views
     */
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // layout is defined in note_edit_fragment.xml
        View noteLayout =
                layoutInflater.inflate(R.layout.note_edit_fragment, container, false);

        // load note into views attached to noteLayout root view.
        loadNote(noteLayout);

        return noteLayout;
    }

    /**
     * When this fragment is attached to a calling context, open a noteTableHelper so the fragment
     * can manage saving changes to a note.
     *
     * @param context calling context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        noteTableHelper = new NoteTableHelper(context);
    }

    /**
     * Load elements of note in to child views of noteLayout
     *
     * @param noteLayout root view containing child view for note aspects to populate.
     */
    private void loadNote(View noteLayout) {

        // load note from arguments
        Bundle noteArgs = getArguments();
        note = NoteFragmentHelper.loadNote(noteArgs);

        if (note.getNoteId() == -1) { // shell note passed in. Use relationshipId to create a new permanent note
            note = new Note(getContext(), note.getRelationshipId());
        }

        // Setup note TextView
        String noteText = note.getNoteText();
        TextView noteTextView = (TextView) noteLayout.findViewById(R.id.note_edit_text);
        noteTextView.setText(noteText);
        setNoteEditTextWatcher(noteTextView);

        // Set note date within the date edit button.
        String noteDateText = note.getNoteDate().toString();
        TextView noteDateButton = (Button) noteLayout.findViewById(R.id.note_date_button);
        noteDateButton.setText(noteDateText);
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
     * When the fragment is paused, save the note so that edits are not lost.
     */
    @Override
    public void onPause() {
        super.onPause();
        noteTableHelper.saveNote(note);
    }

    /**
     * Update note to reflect a new note date, and set the date in the date edit button.
     *
     * @param noteDate new date for this note
     */
    public void updateNoteDate(LocalDate noteDate) {
        note.setNoteDate(noteDate);
        View rootView = getView();
        if (rootView != null) { // this should always pass, as this should not be called before onAttach. This block is included to pass lint warning.
            TextView noteDateTextView = (TextView) rootView.findViewById(R.id.note_date_button);
            noteDateTextView.setText(noteDate.toString());
        }
    }
}
