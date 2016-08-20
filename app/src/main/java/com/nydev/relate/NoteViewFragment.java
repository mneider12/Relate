package com.nydev.relate;

import android.app.Fragment;
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

    private static final String NOTE_TEXT_KEY = "note_text";
    private static final String NOTE_DATE_KEY = "note_date";

    public static NoteViewFragment newInstance(Note note) {
        NoteViewFragment noteFragment = new NoteViewFragment();

        // setup arguments
        Bundle noteArgs = new Bundle();
        noteArgs.putString(NOTE_TEXT_KEY, note.getNoteText());
        noteArgs.putString(NOTE_DATE_KEY, note.getNoteDate().toString("MMMM dd, yyyy"));

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

        String noteText = noteArgs.getString(NOTE_TEXT_KEY, "");
        TextView noteTextView = (TextView) noteLayout.findViewById(R.id.note_text_view);
        noteTextView.setText(noteText);

        String noteDateText = noteArgs.getString(NOTE_DATE_KEY); // default to today
        TextView noteDateButton = (TextView) noteLayout.findViewById(R.id.note_date_text_view);
        noteDateButton.setText(noteDateText);
    }
}
