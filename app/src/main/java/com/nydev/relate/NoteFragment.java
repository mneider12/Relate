package com.nydev.relate;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {

    private NoteSaveListener mCallback;

    private static final String NOTE_TEXT_KEY = "note_text";

    public interface NoteSaveListener {
        void saveNoteText(String noteText);
        void saveNoteDate(LocalDate noteDate);
    }

    public static NoteFragment newInstance(Note note) {
        NoteFragment noteFragment = new NoteFragment();

        // setup arguments
        Bundle demographicArgs = new Bundle();
        demographicArgs.putString(NOTE_TEXT_KEY, note.getNoteText());

        noteFragment.setArguments(demographicArgs);

        return noteFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View noteLayout =
                layoutInflater.inflate(R.layout.note_fragment, container, false);

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
