package com.nydev.relate;

import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Rule;
import org.junit.Test;

import static com.nydev.relate.UiTestHelper.withAdaptedData;

/**
 * Created by markneider on 8/8/16.
 */
public class RelationshipDeleteTest {


    @Rule
    public ActivityTestRule<RelationshipDashboardActivity> mDashboardRule =
            new ActivityTestRule<>(RelationshipDashboardActivity.class);

    /**
     * Test the relationship delete workflow
     * SIDE EFFECT: Deletes the last relationship from the database (if at least one exists)
     */
    @Test
    public void relationshipDeleteTest() {
        Relationship testRelationship = RelationshipDbTestHelper
                .getLastRelationshipFromDatabase(mDashboardRule.getActivity());

        onData(allOf(instanceOf(Relationship.class), is(testRelationship))).perform(click());
        onView(withId(R.id.delete_relationship_button)).perform(click());

        RelationshipDbHelper relationshipDbHelper =
                new RelationshipDbHelper(mDashboardRule.getActivity());
        assertFalse(
                relationshipDbHelper.isValidRelationshipId(testRelationship.getRelationshipId()));
        onView(withId(R.id.thumbnail_container_layout))
                .check(matches(not(withAdaptedData(is(testRelationship)))));
    }



}
