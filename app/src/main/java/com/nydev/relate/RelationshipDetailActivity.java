package com.nydev.relate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by markneider on 7/1/16.
 */
public class RelationshipDetailActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "RelationshipDetail";
    private static final String PREFS_NAME = "RelationshipPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relationship_detail);
    }

    public void saveRelationship(View view)
    {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();

        int id = preferences.getInt("next_id", 1);

        preferencesEditor.putInt("next_id", id + 1);    // increment next id
        preferencesEditor.apply();

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
            preferencesEditor.putInt("next_id", id);    // reset ID counter
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
