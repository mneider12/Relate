package com.nydev.relate;

import android.content.Context;
import android.database.Cursor;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.nydev.relate.NoteContract.NoteEntry;

/**
 * Created by markneider on 8/21/16.
 * Adapter to support swiping note view
 */
public class NoteCollectionPagerAdapter extends FragmentStatePagerAdapter {

    // map positions in the fragment list to note IDs in the database
    // for example, noteIdMap[2]=9 would mean that the second note in the swipable list is ID 9 in the database
    private ArrayList<Integer> noteIdMap;
    private NoteTableHelper noteTableHelper; // helper for accessing note database
    private int relationshipId; // ID for the relationship under consideration
    private Fragment currentFragment;
    private int currentPosition;
    private int editIndex;


    /**
     * Create a pager adapter to provider a list of note fragments
     *
     * @param fragmentManager fragment manager responsible for managing fragment life cycles
     * @param relationshipId relationshipId for the relationship under consideration
     * @param context calling activity context, used to interact with database
     */
    public NoteCollectionPagerAdapter(FragmentManager fragmentManager, int relationshipId,
                                      Context context) {
        super(fragmentManager); // deliver fragment manager to super class
        this.relationshipId = relationshipId;
        noteTableHelper = new NoteTableHelper(context); // use this helper to load notes
        createNoteIdMap();
        editIndex = -1;
    }

    /**
     * Retrieve a list of note IDs for this relationship from the database.
     * Create a mapping representing order in the swipe-able list to note IDs
     */
    private void createNoteIdMap() {
        Cursor noteIdCursor = noteTableHelper.getNoteIds(relationshipId);
        noteIdMap = new ArrayList<>();
        if (noteIdCursor.moveToFirst()) {
            do {
                noteIdMap.add(noteIdCursor.getInt(noteIdCursor.getColumnIndex(
                        NoteEntry._ID)));
            } while (noteIdCursor.moveToNext());
        }
        noteIdCursor.close();
    }

    /**
     * Return a fragment for the note at position. Record the current fragment
     * If there are no notes that already exist for the relationship under consideration,
     * then a blank note edit fragment will be returned.
     *
     * @param position position in the note page swipe-able list (noteIdMap)
     * @return fragment to view note at position in the swipe-able list if a note exists,
     *         otherwise, a blank edit note fragment
     */
    @Override
    public Fragment getItem(int position) {
        if (noteIdMap.size() == 0) { // create an edit fragment for a new note
            return NoteEditFragment.newInstance(new Note(relationshipId));
        } else { // display an existing note in view mode
            return getNoteFragment(position);
        }
    }

    private Fragment getNoteFragment(int position) {
        int noteId = noteIdMap.get(position);
        Note note = noteTableHelper.getNote(noteId);
        if (position == editIndex) {
            return NoteEditFragment.newInstance(note);
        } else {
            return NoteViewFragment.newInstance(note);
        }
    }

    public int getItemPosition(Object object) {
        if (currentFragment.equals(object) && editIndex != -1) {
            return PagerAdapter.POSITION_NONE;
        } else if (object instanceof NoteEditFragment) {
            return PagerAdapter.POSITION_NONE;
        }
        return PagerAdapter.POSITION_UNCHANGED;
    }

    @Override
    public int getCount() {
        return Math.max(noteIdMap.size(), 1); // if there are no notes yet, still initialize pager. We will use getItem to display a blank note edit fragment
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void edit(int position) {
        editIndex = position;
        notifyDataSetChanged();
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentPosition = position;
        currentFragment = (Fragment) object;
        if (editIndex != -1 && position != editIndex) {
            editIndex = -1;
            notifyDataSetChanged();
        }
    }
}
