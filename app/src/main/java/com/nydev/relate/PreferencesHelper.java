package com.nydev.relate;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by markneider on 7/3/16.
 */
public class PreferencesHelper
{
    private static final String RELATIONSHIP_PREFS_NAME = "RelationshipPrefsFile";
    private static final String NEXT_RELATIONSHIP_ID = "next_relationship_id";
    private static final int INITIAL_ID = 1;
    private static final String LOG_TAG = "nydev.Relate";

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

    public static Relationship getRelationship(Context context, int relationshipId)
    {
        String filename = (relationshipId) + ".ser";
        try (FileInputStream fileInputStream = context.openFileInput(filename);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream))
        {
            Relationship relationship = (Relationship) objectInputStream.readObject();
            return relationship;
        }
        catch (IOException|ClassNotFoundException exception)
        {
            Log.e(LOG_TAG, "Failed to load relationship: " + relationshipId);
            return null;
        }
    }
}
