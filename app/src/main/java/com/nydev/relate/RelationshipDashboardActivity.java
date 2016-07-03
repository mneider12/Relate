package com.nydev.relate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RelationshipDashboardActivity extends AppCompatActivity {

    int nextId;
    private static final String LOG_TAG = "RelationshipDashboard";
    private static final String PREFS_NAME = "RelationshipPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_dashboard);
        loadRelationshipThumbnails();
    }

    /**
     * This method will load a relationship detail activity
     */
    public void launchRelationshipDetail(View view)
    {
        Intent launchRelationshipDetail = new Intent(this, RelationshipDetailActivity.class);
        startActivity(launchRelationshipDetail);
    }

    /**
     * Create a new relationship
     */
    public void createRelationship(View view)
    {
        Intent launchRelationshipDetail = new Intent(this, RelationshipDetailActivity.class);
        startActivity(launchRelationshipDetail);
    }

    private void loadRelationshipThumbnails()
    {
        // figure out the largest possible ID
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int maxRelationshipId = preferences.getInt("next_id", 1);
        for (int relationshipId = 1; relationshipId < maxRelationshipId; relationshipId++)
        {
            String filename = relationshipId + ".ser";
            try
            {
                FileInputStream fileInputStream = openFileInput(filename);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Relationship relationship = (Relationship) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            }
            catch (IOException|ClassNotFoundException exception)
            {
                Log.e(LOG_TAG, "Failed to load relationship: " + relationshipId);
            }


        }
    }

}
