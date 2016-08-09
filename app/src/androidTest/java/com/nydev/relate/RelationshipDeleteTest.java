package com.nydev.relate;

import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static com.nydev.relate.UiTestHelper.withRelationship;

/**
 * Created by markneider on 8/8/16.
 * Test the delete relationship workflow
 */
public class RelationshipDeleteTest {

    // relationships before they are deleted from the database so state can be restored later
    private ArrayList<Relationship> savedRelationships;

    // Start test at dashboard activity
    @Rule
    public ActivityTestRule<RelationshipDashboardActivity> mDashboardRule =
            new ActivityTestRule<>(RelationshipDashboardActivity.class);

    // Initialize savedRelationships before tests are run
    @Before
    public void setupSavedRelationships() {
        savedRelationships = new ArrayList<>();
    }

    /**
     * Test the relationship delete workflow
     */
    @Test
    public void relationshipDeleteTest() {
        // get the last relationship from the database
        Relationship testRelationship = RelationshipDbTestHelper
                .getLastRelationshipFromDatabase(mDashboardRule.getActivity());
        savedRelationships.add(testRelationship); // save off the relationship so it can be restored later

        // click on the item in the dashboard ListView corresponding to testRelationship
        onData(allOf(instanceOf(Relationship.class), is(testRelationship))).perform(click());
        onView(withId(R.id.delete_relationship_button)).perform(click()); // click the delete button

        RelationshipDbHelper relationshipDbHelper =
                new RelationshipDbHelper(mDashboardRule.getActivity());
        // verify that the relationship does not exist in the database (won't be valid)
        assertFalse(
                relationshipDbHelper.isValidRelationshipId(testRelationship.getRelationshipId()));
        // check that the dashboard's ListView does not have an item for testRelationship
        onView(withId(R.id.thumbnail_container_layout))
                .check(matches(not(withRelationship(is(testRelationship)))));
    }

    /**
     * Restore relationships saved off in savedRelationships
     * Uses direct methods, not UI calls
     */
    @After
    public void restoreSavedRelationships() {
        RelationshipDbHelper relationshipDbHelper =
                new RelationshipDbHelper(mDashboardRule.getActivity());
        // since relationships were deleted and do not exist in database, directly insert them back
        for (Relationship relationship : savedRelationships) {
            relationshipDbHelper.insertRelationship(relationship);
        }
    }



}
