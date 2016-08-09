package com.nydev.relate;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by markneider on 8/8/16.
 * Methods to help manage SharedPreferences during testing.
 * Helper methods can restore state before and after test cases.
 * If tests fail or are improperly implemented, then manual deletion or integrity updates may be
 * needed to restore data integrity.
 */
public class PreferencesTestHelper {

    private static final String RELATIONSHIP_PREFS_NAME = "RelationshipPrefsFile"; // name of the relationship preferences file
    private static final String NEXT_RELATIONSHIP_ID_KEY = "next_relationship_id"; // name of the index in RelationshipPrefsFile storing the next available ID
    private static final int INITIAL_ID = 1; // first ID to use for a relationship when first loading the application

    /**
     * This class should not be instantiated. All methods are static
     */
    private PreferencesTestHelper() {}

    /**
     * Get the current next relationshipId without incrementing the ID counter.
     * Use this to save off the current next relationshipId and then
     * call setNextRelationshipId to restore state before tests ran.
     * Caller must take care to delete entries in the relationship database separately
     * or risk corrupting the ID counter
     * @param context context from the calling activity
     * @return current next relationshipId in shared preferences
     */
    public static int getNextRelationshipIdNoIncrement(Context context) {
        SharedPreferences relationshipPreferences =
                context.getSharedPreferences(RELATIONSHIP_PREFS_NAME, Context.MODE_PRIVATE);
        return relationshipPreferences.getInt(NEXT_RELATIONSHIP_ID_KEY, INITIAL_ID);
    }

    /**
     * Set the SharedPreferences value that determines the next relationshipId to use.
     * If nextRelationshipId == INITIAL_ID, then the shared preference value will be blanked out,
     * because the counter in real usage would never get set to INITIAL_ID -
     * it would always be immediately incremented.
     * @param context context of the calling activity to get SharedPreferences
     * @param nextRelationshipId nextRelationshipId to set. This should be used in conjunction
     *                           with getNextRelationshipIdNoIncrement to restore the state of the
     *                           SharedPreferences before tests were run.
     */
    public static void setNextRelationshipId(Context context, int nextRelationshipId) {
        SharedPreferences relationshipPreferences =
                context.getSharedPreferences(RELATIONSHIP_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor relationshipPreferencesEditor = relationshipPreferences.edit();
        if (nextRelationshipId == INITIAL_ID) {
            relationshipPreferencesEditor.remove(NEXT_RELATIONSHIP_ID_KEY);
        } else {
            relationshipPreferencesEditor.putInt(NEXT_RELATIONSHIP_ID_KEY, nextRelationshipId);
        }
        relationshipPreferencesEditor.apply();
    }
}
