package com.nydev.relate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

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
        Intent launchRelationshipDetailEdit = new Intent(this, RelationshipDetailEditActivity.class);
        startActivity(launchRelationshipDetailEdit);
    }

    private void loadRelationshipThumbnails()
    {
        // figure out the largest possible ID
        int maxRelationshipId = PreferencesHelper.getNextRelationshipId(this);
        ArrayList<Relationship> relationships = new ArrayList<>();
        for (int relationshipIdCounter = 0; relationshipIdCounter < maxRelationshipId - 1; relationshipIdCounter++)
        {
            int relationshipId = relationshipIdCounter + 1; // relationship IDs start at 1
            Relationship relationship = PreferencesHelper.getRelationship(this, relationshipId);
            if (relationship != null)
            {
                relationships.add(relationship);
            }
        }
        ArrayAdapter<Relationship> adapter = new ArrayAdapter<>(this, R.layout.relationship_thumbnail, relationships);
        ListView listView = (ListView) findViewById(R.id.thumbnail_container_layout);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                launchRelationshipDetailView(position + 1);
            }
        });
    }

}
