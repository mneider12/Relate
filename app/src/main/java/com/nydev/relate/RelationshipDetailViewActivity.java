package com.nydev.relate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.MonthDay;

/**
 * Created by markneider on 7/5/16.
 * Load an existing Relationship and display info
 */
public class RelationshipDetailViewActivity extends AppCompatActivity
{

    private RelationshipDbHelper relationshipDbHelper;
    private Relationship relationship;

    /**
     * Receive Intent and load an existing Relationship.
     * If Relationship is not passed by Intent or cannot be loaded,
     * will fallback to RelationshipDashboardActivity
     * @param savedInstanceState key value pairs of data saved if the system automatically closed this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_detail_view);
        Intent intent = getIntent();
        int relationshipId = intent.getIntExtra("com.nydev.relate.relationshipId", 0);
        relationshipDbHelper = new RelationshipDbHelper(this);
        if (relationshipDbHelper.isValidRelationshipId(relationshipId))
        {
            loadRelationship(relationshipId);
        } else {
            failbackToDashboard("Invalid Relationship ID, cannot load.");
        }
    }

    /**
     * Fallback to the Dashboard if there is a problem loading the relationship.
     * Optionally display a Toast explaining why
     * @param message message to be displayed via Toast while falling back. Pass in null to not
     *                create a Toast.
     */
    private void failbackToDashboard(String message) {
        if (message != null) {
            // display failure message
            Toast invalidRelationship = Toast.makeText(
                    this, message, Toast.LENGTH_SHORT);
            invalidRelationship.show();
        }

        RelationshipEditHelper.fallBackToDashboard(this);
    }

    /**
     * Retrieve Relationship data from database and load into activity views.
     * @param relationshipId unique ID for Relationship record to lookup
     */
    private void loadRelationship(int relationshipId)
    {
        relationship = relationshipDbHelper.getRelationship(relationshipId);
        TextView nameEntryEditText = (TextView) findViewById(R.id.name_text_view);
        nameEntryEditText.setText(relationship.getName().toString());

        MonthDay birthday = relationship.getBirthday();
        if (birthday != null) {
            TextView birthDayTextView = (TextView) findViewById(R.id.birthday_text_view);
            birthDayTextView.setText(birthday.toString());
        }
    }

    public void saveRelationship(View saveButton) {
        RelationshipDbHelper relationshipDbHelper = new RelationshipDbHelper(this);
        relationshipDbHelper.updateRelationship(relationship);
    }

    public void deleteRelationship(View deleteButton) {
        relationshipDbHelper.deleteRelationship(relationship.getRelationshipId());
        RelationshipEditHelper.fallBackToDashboard(this);
    }
}
