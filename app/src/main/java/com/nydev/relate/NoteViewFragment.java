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

    private Note note; // note being displayed by this fragment

    /**
     * Setup arguments for creating a note view and return a new NoteViewFragment
     *
     * @param note note to display in this fragment
     * @return a new NoteViewFragment with a Bundle populated with arguments representing note
     */
    public static NoteViewFragment newInstance(Note note) {
        NoteViewFragment noteFragment = new NoteViewFragment();

        // setup arguments
        Bundle noteArgs = NoteFragmentHelper.saveNoteBundle(note);

        noteFragment.setArguments(noteArgs);

        return noteFragment;
    }

    /**
     * Create view for this fragment based on saved note values.
     *
     * @param layoutInflater {@inheritDoc}
     * @param container {@inheritDoc}
     * @param savedInstanceState {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // layout for note view is in note_view_fragment.xml
        View noteLayout =
                layoutInflater.inflate(R.layout.note_view_fragment, container, false);

        loadNote(noteLayout);

        return noteLayout;
    }

    /**
     * Set note based on arguments and populate fragment views for the note
     *
     * @param noteLayout layout root view
     */
    public void loadNote(View noteLayout) {
        // load note from arguments
        Bundle noteArgs = getArguments();
        note = NoteFragmentHelper.loadNote(noteArgs);

        // setup note TextView
        TextView noteTextView = (TextView) noteLayout.findViewById(R.id.note_text_view);
        noteTextView.setText(note.getNoteText());

        // setup note date button
        TextView noteDateButton = (TextView) noteLayout.findViewById(R.id.note_date_text_view);
        noteDateButton.setText(note.getNoteDate().toString("MMMM d yyyy"));
    }

    /**
     * Return the note displayed in this fragment
     *
     * @return note being displayed in this fragment
     */
    public Note getNote() {
        return note;
    }

}


