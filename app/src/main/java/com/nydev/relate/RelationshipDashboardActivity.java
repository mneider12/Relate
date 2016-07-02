package com.nydev.relate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RelationshipDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_dashboard);
    }

    /**
     * This method will load a relationship detail activity
     */
    public void launchRelationshipDetail(View view)
    {
        Intent launchRelationshipDetail = new Intent(this, RelationshipDetailActivity.class);
        startActivity(launchRelationshipDetail);
    }
}
