package com.nydev.relate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

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
     * Destroys calling activity and loads the dashboard.
     * After calls to this method, the back button will not return to the calling activity.
     * @param activity calling activity
     */
    public static void fallBackToDashboard(AppCompatActivity activity) {
        Intent launchDashboardActivity = new Intent(activity, RelationshipDashboardActivity.class);
        launchDashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // back shouldn't return to this activity
        activity.startActivity(launchDashboardActivity);
    }
}
