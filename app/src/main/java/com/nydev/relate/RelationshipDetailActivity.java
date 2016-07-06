package com.nydev.relate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by markneider on 7/1/16.
 */
public class RelationshipDetailActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "RelationshipDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_detail);
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
        TextView nameEntryEditText = (TextView) findViewById(R.id.name_entry_edit_text);
        nameEntryEditText.setText(relationship.getName());
    }

    public void saveRelationship(View view)
    {
        int id = PreferencesHelper.getNextRelationshipId(this);
        PreferencesHelper.incrementNextRelationshipId(this);

        String name = getStringFromTextView(R.id.name_entry_edit_text);
        String birthday = getStringFromTextView(R.id.birthday_edit_text);
        String relationshipDescription = getStringFromTextView(R.id.relationship_edit_text);
        String note = getStringFromTextView(R.id.note_edit_text);

        Relationship relationship = new Relationship(id, name, birthday, relationshipDescription, note);

        String filename = id + ".ser";

        try
        {
            FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(relationship);
            objectOutputStream.close();
            fileOutputStream.close();
            Log.d(LOG_TAG, "Successfully saved id: " + id);
        }
        catch (IOException IOException)
        {
            Log.e(LOG_TAG, "Failed to save relationship ID: " + id);
            PreferencesHelper.decrementNextRelationshipId(this);    // reset ID counter
        }
    }

    /**
     * Get the text from a text view and return it as a String
     * @param textViewId is the id of the TextView to be used in findViewById
     * @return text from the given TextView
     */
    private String getStringFromTextView(int textViewId)
    {
        TextView textView = (TextView) findViewById(textViewId);
        return textView.getText().toString();
    }
}
