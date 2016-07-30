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

public class RelationshipDashboardActivity extends AppCompatActivity {


    private final String LOG_TAG = "nydev.Relate";  // Tag to use in android monitor log statements
    private final String PACKAGE_NAME = "com.nydev.relate";

    private RelationshipDbHelper relationshipDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_dashboard);
        loadRelationshipThumbnails();
    }

    /**
     * This method will load a relationship detail activity
     */
    public void launchRelationshipDetailView(int relationshipId)
    {
        Intent launchRelationshipDetail = new Intent(this, RelationshipDetailViewActivity.class);
        launchRelationshipDetail.putExtra(PACKAGE_NAME + ".relationshipId", relationshipId);
        startActivity(launchRelationshipDetail);
    }

    /**
     * Create a new relationship
     */
    public void createRelationship(View view)
    {
        Intent launchRelationshipDetailEdit = new Intent(this, RelationshipDetailActivity.class);
        startActivity(launchRelationshipDetailEdit);
    }

    private void loadRelationshipThumbnails()
    {
        relationshipDbHelper = new RelationshipDbHelper(this);
        Cursor relationshipsCursor = relationshipDbHelper.getAllRelationships();
        int relationshipId;
        String lastName, firstName;
        ArrayAdapter<Relationship> adapter = getRelationshipArrayAdapter(relationshipsCursor);
        ListView listView = (ListView) findViewById(R.id.thumbnail_container_layout);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Relationship relationship = (Relationship) adapterView.getItemAtPosition(position);
                launchRelationshipDetailView(relationship.getId());
            }
        });
    }

    @NonNull
    private ArrayAdapter<Relationship> getRelationshipArrayAdapter(Cursor relationshipsCursor) {
        int relationshipId;
        String lastName;
        String firstName;ArrayList<Relationship> relationships = new ArrayList<>();
        if (relationshipsCursor.moveToFirst()) {    // checks if Cursor is empty
            do {
                relationshipId = relationshipsCursor.getInt(
                        relationshipsCursor.getColumnIndex(RelationshipEntry._ID));
                lastName = relationshipsCursor.getString(
                        relationshipsCursor.getColumnIndex(RelationshipEntry.COLUMN_NAME_LAST_NAME));
                firstName = relationshipsCursor.getString(
                        relationshipsCursor.getColumnIndex(RelationshipEntry.COLUMN_NAME_FIRST_NAME));
                relationships.add(new Relationship(relationshipId, firstName + " " + lastName, null, null, null));
            } while (relationshipsCursor.moveToNext());
        }
        return new ArrayAdapter<>(this, R.layout.relationship_thumbnail, relationships);
    }

}
