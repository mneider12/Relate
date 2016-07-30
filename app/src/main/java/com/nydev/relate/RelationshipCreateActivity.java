package com.nydev.relate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by markneider on 7/1/16.
 * Create a Relationship
 */
public class RelationshipCreateActivity extends AppCompatActivity
{
    @SuppressWarnings("unused") // only used currently when needed for debugging.
    private static final String LOG_TAG = "nydev.Relate";
    private Relationship relationship; // Relationship being edited

    /**
     * Load relationship information on activity creation
     * @param savedInstanceState key value pair information if the activity was last destroyed by the system automatically
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_create);
        relationship = new Relationship(this); // reserves an ID for this Relationship
    }

    /**
     * Save the relationship being created
     * @param saveButton not used - view calling this method, expected from onClick
     */
    public void saveRelationship(View saveButton)
    {
        String rawName = getStringFromTextView(R.id.name_entry_edit_text);
        Name relationName = new Name(rawName); // parse name into last name and first name

        RelationshipDbHelper relationshipDbHelper = new RelationshipDbHelper(this);
        relationshipDbHelper.insertRelationship(relationship.getRelationshipId(),
                relationName.getLastName(), relationName.getFirstName());
    }

    public void cancelRelationship(View cancelButton) {
        // re-launch Dashboard Activity
        Intent launchDashboardActivity = new Intent(this, RelationshipDashboardActivity.class);
        launchDashboardActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // back shouldn't return to this activity
        startActivity(launchDashboardActivity);
    }

    /**
     * Get the text from a text view and return it as a String
     * @param textViewId is the id of the TextView to be used in findViewById
     * @return text from the given TextView
     */
    @NonNull
    private String getStringFromTextView(int textViewId)
    {
        TextView textView = (TextView) findViewById(textViewId);
        return textView.getText().toString();
    }
}
