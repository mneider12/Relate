package com.nydev.relate;

import android.support.test.rule.ActivityTestRule;

import org.joda.time.MonthDay;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import com.nydev.relate.DateHelper.Month;

import java.util.ArrayList;

/**
 * Created by markneider on 8/6/16.
 * Test the relationship creation workflow
 */
public class RelationshipCreateTest {

    private ArrayList<Relationship> createdRelationships; // save any created relationships to delete later
    private int savedNextRelationshipId; // save off nextRelationshipId before tests and restore after.

    // start test from dashboard activity
    @Rule
    public ActivityTestRule<RelationshipDashboardActivity> mDashboardRule =
            new ActivityTestRule<>(RelationshipDashboardActivity.class);

    // Create the createdRelationships ArrayList and save off the current value of nextRelationshipId
    @Before
    public void setupRelationshipCreated() {
        createdRelationships = new ArrayList<>();
        savedNextRelationshipId = PreferencesTestHelper
                .getNextRelationshipIdNoIncrement(mDashboardRule.getActivity());
    }

    /**
     * Test the relationship creation workflow.
     */
    @Test
    public void relationshipCreateTest() {
        @SuppressWarnings("SpellCheckingInspection") // I double checked
        final String testName = "Anakin Skywalker";
        final Month testBirthMonth = Month.APRIL;
        final int testBirthDayOfMonth = 19;
        MonthDay testBirthday = new MonthDay(testBirthMonth.getMonthOfYear(), testBirthDayOfMonth);

        onView(withId(R.id.create_relationship_button)).perform(click()); // open create activity
        // Fill out demographics with test data
        UiTestHelper.editDemographics(testName, testBirthMonth, testBirthDayOfMonth,
                mDashboardRule);
        onView(withId(R.id.save_relationship)).perform(click()); // save relationship

        // Retrieve the last saved relationship to verify it is the one just created
        Relationship testRelationship =
                RelationshipDbTestHelper.getLastRelationshipFromDatabase(mDashboardRule.getActivity());
        assertEquals(testName, testRelationship.getName().toString()); // verify name
        assertEquals(testBirthday.toString(), testRelationship.getBirthdayString()); // verify birthday
        createdRelationships.add(testRelationship); // save relationship to delete after test
    }

    /**
     * Delete created relationships and restore previous value of nextRelationshipId.
     * Uses direct methods, not UI calls
     */
    @After
    public void deleteCreatedRelationships() {
        RelationshipTableHelper relationshipTableHelper =
                new RelationshipTableHelper(mDashboardRule.getActivity());
        for (Relationship relationship : createdRelationships) {
            relationshipTableHelper.deleteRelationship(relationship.getRelationshipId());
        }
        PreferencesTestHelper.setNextRelationshipId(
                mDashboardRule.getActivity(), savedNextRelationshipId);
    }
}
