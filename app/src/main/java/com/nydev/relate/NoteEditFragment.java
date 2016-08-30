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

        noteTableHelper = new NoteTableHelper(context);
    }

    private void loadNote(View noteLayout) {

        Bundle noteArgs = getArguments();
        note = NoteFragmentHelper.loadNote(noteArgs);

        if (note.getNoteId() == -1) {
            note = new Note(getContext(), note.getRelationshipId());
        }

        String noteText = note.getNoteText();
        TextView noteTextView = (TextView) noteLayout.findViewById(R.id.note_edit_text);
        noteTextView.setText(noteText);
        setNoteEditTextWatcher(noteTextView);

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

    @Override
    public void onPause() {
        super.onPause();
        noteTableHelper.saveNote(note);
    }

    public void updateNoteDate(LocalDate noteDate) {
        note.setNoteDate(noteDate);
        View rootView = getView();
        if (rootView != null) {
            TextView noteDateTextView = (TextView) rootView.findViewById(R.id.note_date_button);
            noteDateTextView.setText(noteDate.toString());
        }
    }
}
