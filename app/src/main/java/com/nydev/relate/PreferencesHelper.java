package com.nydev.relate;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by markneider on 7/3/16.
 * Utility class for interacting with settings stored in SharedPreferences
 */
public class PreferencesHelper
{
    private static final String RELATIONSHIP_PREFS_NAME = "RelationshipPrefsFile"; // name of the relationship preferences file
    private static final String NEXT_RELATIONSHIP_ID_KEY = "next_relationship_id"; // name of the index in RelationshipPrefsFile storing the next available ID
    private static final String NEXT_NOTE_ID_KEY = "next_note_id"; // name of the index in RelationshipPrefsFile storing the next available note id
    private static final int INITIAL_ID = 1; // first ID to use for a relationship when first loading the application
    @SuppressWarnings("unused") // LOG_TAG is only used for debugging currently
    private static final String LOG_TAG = "nydev.Relate";

    /**
     * This class should not be instantiated. All methods are static
     */
    private PreferencesHelper() {}

    /**
     * Always call this function to reserve the next available relationship ID.
     * This function will return the next available ID and then increment the next available ID in SharedPreferences
     * If NEXT_RELATIONSHIP_ID is not an existing key in RELATIONSHIP_PREFS_NAME SharedPreferences file,
     * then it will be initialized to INITIAL_ID.
     * @param context context of calling activity
     * @return next available ID
     */
    public static int getNextRelationshipId(Context context)
    {
        // fetch shared preferences
        SharedPreferences relationshipPreferences =
                context.getSharedPreferences(RELATIONSHIP_PREFS_NAME, Context.MODE_PRIVATE);
        // set ID from current next available ID
        int relationshipId = relationshipPreferences.getInt(NEXT_RELATIONSHIP_ID_KEY, INITIAL_ID);
        // increment the counter for next available ID
        incrementNextRelationshipId(relationshipPreferences);
        return relationshipId;
    }

    public static int getNextNoteId(Context context) {
        // fetch shared preferences
        SharedPreferences relationshipPreferences =
                context.getSharedPreferences(RELATIONSHIP_PREFS_NAME, Context.MODE_PRIVATE);
        // set ID from current next available ID
        int noteId = relationshipPreferences.getInt(NEXT_NOTE_ID_KEY, INITIAL_ID);
        // increment the counter for next available ID
        incrementNextNoteId(relationshipPreferences);
        return noteId;
    }

    /**
     * Increment the next available relationship ID in SharedPreferences.
     * If NEXT_RELATIONSHIP_ID_KEY is not an existing key in relationshipPreferences,
     * then it will be initialized to INITIAL_ID and then incremented.
     * @param relationshipPreferences relationship SharedPreferences.
     */
    private static void incrementNextRelationshipId(SharedPreferences relationshipPreferences)
    {
        incrementId(relationshipPreferences, NEXT_RELATIONSHIP_ID_KEY, INITIAL_ID);
    }

    /**
     * Increment the next available note ID in SharedPreferences.
     * If NEXT_NOTE_ID_KEY is not an existing key in relationshipPreferences,
     * then it will be initialized to INITIAL_ID and then increments.
     *
     * @param relationshipPreferences relationship SharedPreferences
     */
    private static void incrementNextNoteId(SharedPreferences relationshipPreferences) {
        incrementId(relationshipPreferences, NEXT_NOTE_ID_KEY, INITIAL_ID);
    }


    private static void incrementId(SharedPreferences relationshipPreferences, String idKey,
                                    int initialId) {
        SharedPreferences.Editor relationshipPreferencesEditor = relationshipPreferences.edit();
        int currentNextId = relationshipPreferences.getInt(idKey, initialId);
        relationshipPreferencesEditor.putInt(idKey, currentNextId + 1);
        relationshipPreferencesEditor.apply();
    }

    /**
     * Getter method for RELATIONSHIP_PREFS_NAME. See variable declaration for description.
     * @return RELATIONSHIP_PREFS_NAME
     */
    public static String getRelationshipPrefsName() {
        return RELATIONSHIP_PREFS_NAME;
    }

    /**
     * Getter method for NEXT_RELATIONSHIP_ID. See variable declaration for description.
     * @return NEXT_RELATIONSHIP_ID
     */
    public static String getNextRelationshipIdKey() {
        return NEXT_RELATIONSHIP_ID_KEY;
    }

    /**
     * Getter method for INITIAL_ID. See variable declaration for description.
     * @return initial ID when relationship SharedPreferences is created
     */
    public static int getInitialId() {
        return INITIAL_ID;
    }
}
