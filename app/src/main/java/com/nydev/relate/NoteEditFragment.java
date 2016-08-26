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

    // calling activity must implement NoteSaveListener to save the note in this fragment
    private NoteSaveListener mCallback;

    // key values for onCreate Bundle
    private static final String NOTE_TEXT_KEY = "note_text";
    private static final String NOTE_DATE_KEY = "note_date";

    /**
     * Calling activity must implement NoteSaveListener so that note info entered into this fragement
     * can be communicated back to the activity and saved
     */
    public interface NoteSaveListener {
        /**
         * Save the information in this note
         * @param noteText text stored in this note
         */
        void saveNoteText(String noteText);
    }

    /**
     * Get a new NoteEditFragment
     *
     * @param note if note is null, then a blank edit fragment will be created to start a new note
     *             if note is passed in, then the fragment will be built to edit the existing note
     * @return new fragment to edit a note
     */
    public static NoteEditFragment newInstance(Note note) {
        NoteEditFragment noteFragment = new NoteEditFragment();

        // setup arguments
        Bundle noteArgs = new Bundle();
        if (note == null) { // creating a new note
            noteArgs.putString(NOTE_TEXT_KEY, "");
            noteArgs.putString(NOTE_DATE_KEY, new LocalDate().toString("MMM d yyyy"));
        } else { // editing existing note
            noteArgs.putString(NOTE_TEXT_KEY, note.getNoteText());
            noteArgs.putString(NOTE_DATE_KEY, note.getNoteDate().toString());
        }

        noteFragment.setArguments(noteArgs);

        return noteFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View noteLayout =
                layoutInflater.inflate(R.layout.note_edit_fragment, container, false);

        loadNote(noteLayout);

        return noteLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCallback = (NoteSaveListener) context;
    }

    private void loadNote(View noteLayout) {

        Bundle noteArgs = getArguments();

        String noteText = noteArgs.getString(NOTE_TEXT_KEY, "");
        TextView noteTextView = (TextView) noteLayout.findViewById(R.id.note_edit_text);
        noteTextView.setText(noteText);
        setNoteEditTextWatcher(noteTextView);

        String noteDateText = noteArgs.getString(NOTE_DATE_KEY, new LocalDate().toString()); // default to today
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
                mCallback.saveNoteText(noteEditable.toString());
            }
        });
    }
}
