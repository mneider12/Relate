package com.nydev.relate;

import android.app.DialogFragment;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.nydev.relate.RelationshipEditHelper.saveMode;

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
        RelationshipEditHelper.saveRelationship(
                relationship.getRelationshipId(), saveMode.CREATE, this);
    }

    /**
     * Don't save relationship - destroy activity and return to dashboard
     * @param cancelButton the view calling this function (not used)
     */
    public void cancelRelationship(View cancelButton) {
        RelationshipEditHelper.fallBackToDashboard(this);
    }

    public void showBirthdayPickerDialog(View birthdayPickerButton) {
        DialogFragment birthdayPickerFragment = new BirthdayPickerFragment();
        FragmentManager fragmentManager = getFragmentManager();
        birthdayPickerFragment.show(fragmentManager, "birthdayPicker");
    }
}
