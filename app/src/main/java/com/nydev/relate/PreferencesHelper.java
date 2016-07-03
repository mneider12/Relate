package com.nydev.relate;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by markneider on 7/3/16.
 */
public class PreferencesHelper
{
    private static final String RELATIONSHIP_PREFS_NAME = "RelationshipPrefsFile";
    private static final String NEXT_RELATIONSHIP_ID = "next_relationship_id";
    private static final int INITIAL_ID = 1;

    private PreferencesHelper()
    {

    }

    public static int getNextRelationshipId(Context context)
    {
        SharedPreferences relationshipPreferences =
                context.getSharedPreferences(RELATIONSHIP_PREFS_NAME, Context.MODE_PRIVATE);
        return relationshipPreferences.getInt(NEXT_RELATIONSHIP_ID, INITIAL_ID);
    }

    public static void incrementNextRelationshipId(Context context)
    {
        SharedPreferences relationshipPreferences =
                context.getSharedPreferences(RELATIONSHIP_PREFS_NAME, Context.MODE_PRIVATE);
        int currentNextRelationshipId = relationshipPreferences.getInt(NEXT_RELATIONSHIP_ID, INITIAL_ID);
        SharedPreferences.Editor relationshipPreferencesEditor = relationshipPreferences.edit();
        relationshipPreferencesEditor.putInt(NEXT_RELATIONSHIP_ID, currentNextRelationshipId + 1);
        relationshipPreferencesEditor.apply();
    }

    public static void decrementNextRelationshipId(Context context)
    {
        SharedPreferences relationshipPreferences =
                context.getSharedPreferences(RELATIONSHIP_PREFS_NAME, Context.MODE_PRIVATE);
        int currentNextRelationshipId = relationshipPreferences.getInt(NEXT_RELATIONSHIP_ID, INITIAL_ID);
        SharedPreferences.Editor relationshipPreferencesEditor = relationshipPreferences.edit();
        relationshipPreferencesEditor.putInt(NEXT_RELATIONSHIP_ID, currentNextRelationshipId - 1);
        relationshipPreferencesEditor.apply();
    }
}
