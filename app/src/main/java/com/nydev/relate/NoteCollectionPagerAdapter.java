package com.nydev.relate;

import android.content.Context;
import android.database.Cursor;
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
    // for example, noteIdMap[2]=9 would mean that the second note in the swipe-able list is ID 9 in the database
    private ArrayList<Integer> noteIdMap;
    private NoteTableHelper noteTableHelper; // helper for accessing note database
    private int relationshipId; // ID for the relationship under consideration
    private Fragment currentFragment; // save a reference to the currently displayed fragment
    private int editIndex; // set this so that the next time the fragment with position == editIndex is loaded, it will be in edit mode.
    private boolean needToRefresh;

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
        needToRefresh = false;
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
     * @return a fragment to display a note.
     *         If there are no notes to display, a new note edit fragment is returned.
     *         If the position == editIndex, then an existing note will be loaded in edit mode.
     *         Otherwise, an existing note is loaded in view mode.
     */
    @Override
    public Fragment getItem(int position) {
        if (noteIdMap.size() == 0) { // create an edit fragment for a new note
            return NoteEditFragment.newInstance(new Note(relationshipId));
        } else { // display an existing note in view mode
            return getNoteFragment(position);
        }
    }

    /**
     * Create a note fragment to display in a ViewPager.
     * position defines the order that the fragment will be displayed in.
     *
     * @param position position in the displayed list being retrieved.
     *                 Lines up to the positions in noteIdMap
     * @return an existing note in edit mode if position == editIndex,
     *         otherwise an existing note in view mode
     */
    private Fragment getNoteFragment(int position) {
        int noteId = noteIdMap.get(position);
        Note note;
        if (noteTableHelper.isValidNoteId(noteId)) {
            note = noteTableHelper.getNote(noteId);
        } else {
            note = new Note(noteId, relationshipId);
        }
        if (position == editIndex) { // indicates that this fragment should be the current note in edit mode.
            return NoteEditFragment.newInstance(note);
        } else {
            return NoteViewFragment.newInstance(note);
        }
    }

    /**
     * Return whether an object needs to change when the underlying data set changes
     *
     * @param object a note view or edit fragment to check for changes
     * @return POSITION_NONE indicates there is a change and this fragment has been replaced or removed
     *         POSITION_UNCHANGED indicates there is no change and this fragment should remain in the swipe-view
     */
    public int getItemPosition(Object object) {
        if (needToRefresh) { // removed a view, need to refresh all
            return PagerAdapter.POSITION_NONE;
        } else if (currentFragment.equals(object) && editIndex != -1) { //edit pressed for current view fragment. Reload as edit fragment
                return PagerAdapter.POSITION_NONE;
        } else if (object instanceof NoteEditFragment) { // Change edit fragments back to view fragments when the data set changes
            return PagerAdapter.POSITION_NONE;
        }
        return PagerAdapter.POSITION_UNCHANGED; // Default to no change
    }

    /**
     * Return the number of fragments that can be swiped to with this adapter.
     * When there are no saved notes in the adapter, 1 will be returned so the ViewPager will load a new note in edit mode.
     *
     * @return the number of stored notes if there are any, otherwise 1.
     */
    @Override
    public int getCount() {
        return Math.max(noteIdMap.size(), 1); // if there are no notes yet, still initialize pager. We will use getItem to display a blank note edit fragment
    }

    /**
     * Return the currently displayed fragment
     *
     * @return currently displayed fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    /**
     * Change the currently displayed view fragment into an edit fragment
     *
     * @param position current position being displayed in the UI
     */
    public void edit(int position) {
        editIndex = position; // this indicates that the view fragment should be reloaded as an edit fragment in getItem
        notifyDataSetChanged(); // kick off reloading fragments so the edit fragment will be displayed.
    }

    /**
     * Called when a new fragment is presented to the UI. Keep track of the current fragment and
     * notifyDataSetChanged when an edit fragment loses focus.
     *
     * @param container {@inheritDoc}
     * @param position {@inheritDoc}
     * @param object {@inheritDoc}
     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object); // maintain existing behavior
        currentFragment = (Fragment) object; // update the saved current fragment
        if (editIndex != -1 && position != editIndex) { // edit mode was turned on, but we have left the edit fragment. Turn off editing mode
            editIndex = -1; // no position will match this index
            notifyDataSetChanged(); // reload edit fragment as view fragment
        }
    }

    /**
     * Insert a new note at the given position, and set it to open in edit mode
     *
     * @param context calling context - used to reserve a new note id
     * @param position position to insert - usually the ViewPager current position
     */
    public void createNote(Context context, int position) {
        int noteId = PreferencesHelper.getNextNoteId(context);
        noteIdMap.add(position, noteId);
        editIndex = position;
        notifyDataSetChanged();
    }

    /**
     * Delete note at position.
     * Removes the note from the database and notifies the ViewPager that the dataset needs to be reread.
     *
     * @param position position in noteIdMap to delete
     */
    public void deleteNote(int position) {
        int noteId = noteIdMap.get(position);
        noteTableHelper.deleteNote(noteId);
        noteIdMap.remove(position);
        needToRefresh = true; // every view may shift due to delete. Refresh all.
        notifyDataSetChanged();
        needToRefresh = false; // reset so that we don't always refresh all.
    }
}
