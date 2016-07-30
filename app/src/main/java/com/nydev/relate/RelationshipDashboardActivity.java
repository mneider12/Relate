package com.nydev.relate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.nydev.relate.RelationshipContract.RelationshipEntry;

import java.util.ArrayList;

/**
 * First activity when opening the application. Displays saved relationship records and allows
 * drilling down into detail activities for each. Also can launch into creating a new relationship.
 */
public class RelationshipDashboardActivity extends AppCompatActivity {

    @SuppressWarnings("unused") // currently only being used when necessary for debugging
    private final String LOG_TAG = "nydev.Relate";  // Tag to use in android monitor log statements

    /**
     * Load relationships when the activity is created
     * @param savedInstanceState saved key value pairs if the system previously destroyed the activity automatically
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_dashboard);
        loadRelationshipThumbnails();
    }

    private void loadRelationshipThumbnails()
    {
        RelationshipDbHelper relationshipDbHelper = new RelationshipDbHelper(this); // Helper class to access Relationship database
        Cursor relationshipsCursor = relationshipDbHelper.getAllRelationships(); // load all relationships right away - may need more performance aware process later
        ArrayAdapter<Relationship> relationshipArrayAdapter = getRelationshipArrayAdapter(relationshipsCursor); // load relationships into an ArrayAdapter
        ListView relationshipListView = (ListView) findViewById(R.id.thumbnail_container_layout); // ListView to display list of relationships
        relationshipListView.setAdapter(relationshipArrayAdapter); // bind relationshipArrayAdapter to relationshipListView

        // When a relationship item is clicked, launch the detail view activity for that relationship
        relationshipListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Relationship relationship = (Relationship) adapterView.getItemAtPosition(position);
                launchRelationshipDetailView(relationship.getRelationshipId());
            }
        });
    }

    /**
     * This method will load a relationship detail activity
     */
    public void launchRelationshipDetailView(int relationshipId)
    {
        // build an intent identifying the relationship to load
        final String PACKAGE_NAME = "com.nydev.relate"; // identifier to use for sending intent extras from this application
        final String DOT_SEPARATOR = ".";
        final String RELATIONSHIP_ID_INTENT_EXTRA = "relationshipId";
        Intent launchRelationshipDetail = new Intent(this, RelationshipDetailViewActivity.class);
        launchRelationshipDetail.putExtra(
                PACKAGE_NAME + DOT_SEPARATOR + RELATIONSHIP_ID_INTENT_EXTRA, relationshipId);
        startActivity(launchRelationshipDetail);
    }

    /**
     * Create a new relationship
     */
    public void createRelationship(View view)
    {
        // launch detail activity to edit new relationship
        Intent launchRelationshipDetailEdit = new Intent(this, RelationshipDetailActivity.class);
        startActivity(launchRelationshipDetailEdit);
    }

    /**
     * Load Relationships into an ArrayAdapter. There will be one Relationship for each row in the Cursor
     * @param relationshipsCursor Cursor with one row for each Relationship
     * @return ArrayAdapter with one Relationship for each row in relationshipsCursor
     */
    @NonNull
    private ArrayAdapter<Relationship> getRelationshipArrayAdapter(Cursor relationshipsCursor) {
        int relationshipId;
        String lastName, firstName;
        ArrayList<Relationship> relationships = new ArrayList<>();
        // Probably should update to not create a relationship object, instead just load info needed for thumbnail
        if (relationshipsCursor.moveToFirst()) {    // checks if Cursor is empty and moves to first row
            do {
                relationshipId = relationshipsCursor.getInt(
                        relationshipsCursor.getColumnIndex(RelationshipEntry._ID));
                lastName = relationshipsCursor.getString(
                        relationshipsCursor.getColumnIndex(RelationshipEntry.COLUMN_NAME_LAST_NAME));
                firstName = relationshipsCursor.getString(
                        relationshipsCursor.getColumnIndex(RelationshipEntry.COLUMN_NAME_FIRST_NAME));
                relationships.add(new Relationship(relationshipId, lastName, firstName));
            } while (relationshipsCursor.moveToNext()); // will return false after last row
        }
        // return ArrayAdapter from list of relationships and with a thumbnail view for each
        return new ArrayAdapter<>(this, R.layout.relationship_thumbnail, relationships);
    }

}
