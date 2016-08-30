package com.nydev.relate;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by markneider on 8/14/16.
 * Fragment to control display of existing notes
 */
public class NoteViewFragment extends Fragment {

    private static final String NOTE_ID_KEY = "note_id";
    private static final String RELATIONSHIP_ID_KEY = "relationship_id";
    private static final String NOTE_CREATED_DATE_KEY = "note_created_date";
    private static final String NOTE_DATE_KEY = "note_date";
    private static final String NOTE_TEXT_KEY = "note_text";
    private Note note;

    public static NoteViewFragment newInstance(Note note) {
        NoteViewFragment noteFragment = new NoteViewFragment();

        // setup arguments
        Bundle noteArgs = NoteFragmentHelper.saveNoteBundle(note);

        noteFragment.setArguments(noteArgs);

        return noteFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View noteLayout =
                layoutInflater.inflate(R.layout.note_view_fragment, container, false);

        loadNote(noteLayout);

        return noteLayout;
    }

    public void loadNote(View noteLayout) {
        Bundle noteArgs = getArguments();
        note = NoteFragmentHelper.loadNote(noteArgs);

        TextView noteTextView = (TextView) noteLayout.findViewById(R.id.note_text_view);
        noteTextView.setText(note.getNoteText());

        TextView noteDateButton = (TextView) noteLayout.findViewById(R.id.note_date_text_view);
        noteDateButton.setText(note.getNoteDate().toString("MMMM d yyyy"));
    }

    public Note getNote() {
        return note;
    }

}


