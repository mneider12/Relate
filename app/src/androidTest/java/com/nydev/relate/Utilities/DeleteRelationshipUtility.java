package com.nydev.relate.Utilities;

import android.support.test.rule.ActivityTestRule;

import com.nydev.relate.RelationshipDashboardActivity;
import com.nydev.relate.RelationshipTableHelper;

import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Created by markneider on 8/15/16.
 */
public class DeleteRelationshipUtility {

    private static final int relationshipId = 11; // set to relationship Id to delete

    // Start test at dashboard activity
    @Rule
    public ActivityTestRule<RelationshipDashboardActivity> mDashboardRule =
            new ActivityTestRule<>(RelationshipDashboardActivity.class);

    @Test
    public void deleteRelationship() {
        RelationshipTableHelper relationshipTableHelper =
                new RelationshipTableHelper(mDashboardRule.getActivity());
        relationshipTableHelper.deleteRelationship(relationshipId);
        assertFalse(relationshipTableHelper.isValidRelationshipId(relationshipId));
    }
}
