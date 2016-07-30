package com.nydev.relate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by markneider on 7/30/16.
 * Utility functions shared by RelationshipCreateActivity and RelationshipDetailViewActivity
 */
public class RelationshipEditHelper {

    /**
     * static helper class should not be constructed
     */
    private RelationshipEditHelper() {

    }

    /**
     * Save the relationship being created
     * @param relationshipId unique ID to use when saving to the database
     * @param activity calling activity
     */
    public static void saveRelationship(int relationshipId, saveMode mode, AppCompatActivity activity)
    {
        String rawName = getStringFromTextView(R.id.name_text_view, activity);
        Name relationName = new Name(rawName); // parse name into last name and first name

        RelationshipDbHelper relationshipDbHelper = new RelationshipDbHelper(activity);
        if (mode == saveMode.CREATE) {
            relationshipDbHelper.insertRelationship(relationshipId,
                    relationName.getLastName(), relationName.getFirstName());
        } else { // saveMode is UPDATE
            relationshipDbHelper.updateRelationship(relationshipId,
                    relationName.getLastName(), relationName.getFirstName());
        }
    }

    /**
     * Get the text from a text view and return it as a String
     * @param textViewId is the id of the TextView to be used in findViewById
     * @param activity calling activity
     * @return text from the given TextView
     */
    @NonNull
    private static String getStringFromTextView(int textViewId, AppCompatActivity activity)
    {
        TextView textView = (TextView) activity.findViewById(textViewId);
        return textView.getText().toString();
    }

    /**
     * Destroys calling activity and loads the dashboard
     * @param activity calling activity
     */
    public static void fallBackToDashboard(AppCompatActivity activity) {
        Intent launchDashboardActivity = new Intent(activity, RelationshipDashboardActivity.class);
        launchDashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // back shouldn't return to this activity
        activity.startActivity(launchDashboardActivity);
    }

    public enum saveMode {
        CREATE, UPDATE
    }
}
