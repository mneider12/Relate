package com.nydev.relate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by markneider on 7/5/16.
 */
public class RelationshipDetailViewActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_detail_view);
        Intent intent = getIntent();
        int relationshipId = intent.getIntExtra("com.nydev.relate.relationshipId", 0);
        if (relationshipId != 0)
        {
            loadRelationship(relationshipId);
        }
    }

    private void loadRelationship(int relationshipId)
    {
        Relationship relationship = PreferencesHelper.getRelationship(this, relationshipId);
        TextView nameEntryEditText = (TextView) findViewById(R.id.name_text_view);
        nameEntryEditText.setText(relationship.getName());
    }
}
