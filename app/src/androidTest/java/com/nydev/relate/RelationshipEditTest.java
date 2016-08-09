package com.nydev.relate;

import android.support.test.rule.ActivityTestRule;

import org.joda.time.MonthDay;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import com.nydev.relate.DateHelper.Month;

import java.util.ArrayList;

/**
 * Created by markneider on 8/8/16.
 * Test the relationship edit workflow
 */
public class RelationshipEditTest {

    private ArrayList<Relationship> savedRelationships; // save relationships before editing so they can be restored later

    // start test from dashboard activity
    @Rule
    public ActivityTestRule<RelationshipDashboardActivity> mDashboardRule =
            new ActivityTestRule<>(RelationshipDashboardActivity.class);

    /**
     * initialize savedRelationships
     */
    @Before
    public void createPreEditRelationships() {
        savedRelationships = new ArrayList<>();
    }

    /**
     * Test the relationship edit workflow
     */
    @Test
    public void editRelationship() {
        final String testName = "Darth Vader";
        final Month testBirthMonth = Month.JANUARY;
        final int testBirthDayOfMonth = 17;
        MonthDay testBirthday = new MonthDay(testBirthMonth.getMonthOfYear(), testBirthDayOfMonth);

        // get the last relationship in the database to test with
        Relationship testRelationship = RelationshipDbTestHelper
                .getLastRelationshipFromDatabase(mDashboardRule.getActivity());
        savedRelationships.add(testRelationship); // save off relationship before editing

        onData(allOf(instanceOf(Relationship.class), is(testRelationship))).perform(click()); // open testRelationship detail
        onView(withId(R.id.edit_demographics_button)).perform(click()); // edit demographics

        // fill in test demographic data
        UiTestHelper.editDemographics(testName, testBirthMonth, testBirthDayOfMonth,
                mDashboardRule);
        onView(withId(R.id.save_relationship)).perform(click()); // save changes

        // reload relationship from database, to test save was successful
        testRelationship = RelationshipDbTestHelper
                .getLastRelationshipFromDatabase(mDashboardRule.getActivity());
        assertEquals(testName, testRelationship.getName().toString()); // check name
        assertEquals(testBirthday.toString(), testRelationship.getBirthdayString()); // check birthday
    }

    /**
     * After edits are confirmed, revert changes from savedRelationships/
     * Uses direct methods, not UI calls.
     */
    @After
    public void restorePreEditRelationships() {
        RelationshipDbHelper relationshipDbHelper =
                new RelationshipDbHelper(mDashboardRule.getActivity());
        for (Relationship relationship : savedRelationships) {
            relationshipDbHelper.updateRelationship(relationship);
        }
    }
}
