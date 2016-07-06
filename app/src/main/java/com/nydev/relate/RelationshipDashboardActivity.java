package com.nydev.relate;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class RelationshipDashboardActivity extends AppCompatActivity {

    private final String LOG_TAG = "nydev.Relate";  // Tag to use in android monitor log statements
    private final String PACKAGE_NAME = "com.nydev.relate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_dashboard);
        loadRelationshipThumbnails();
    }

    /**
     * This method will load a relationship detail activity
     */
    public void launchRelationshipDetail(int relationshipId)
    {
        Intent launchRelationshipDetail = new Intent(this, RelationshipDetailActivity.class);
        launchRelationshipDetail.putExtra("com.nydev.relate.relationshipId", relationshipId);
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
        int maxRelationshipId = PreferencesHelper.getNextRelationshipId(this);
        final Relationship[] relationships = new Relationship[maxRelationshipId - 1];
        for (int relationshipIdCounter = 0; relationshipIdCounter < maxRelationshipId - 1; relationshipIdCounter++)
        {
            int relationshipId = relationshipIdCounter + 1; // relationship IDs start at 1
            Relationship relationship = PreferencesHelper.getRelationship(this, relationshipId);
            relationships[relationshipIdCounter] = relationship;
        }
        ArrayAdapter<Relationship> adapter = new ArrayAdapter<>(this, R.layout.relationship_thumbnail, relationships);
        ListView listView = (ListView) findViewById(R.id.thumbnail_container_layout);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                launchRelationshipDetail(position + 1);
            }
        });
    }

}
