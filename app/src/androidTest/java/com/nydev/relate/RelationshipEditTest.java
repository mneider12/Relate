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
 */
public class RelationshipEditTest {

    private ArrayList<Relationship> preEditRelationships;

    @Rule
    public ActivityTestRule<RelationshipDashboardActivity> mDashboardRule =
            new ActivityTestRule<>(RelationshipDashboardActivity.class);

    @Before
    public void createPreEditRelationships() {
        preEditRelationships = new ArrayList<>();
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

        Relationship testRelationship = RelationshipDbTestHelper
                .getLastRelationshipFromDatabase(mDashboardRule.getActivity());
        preEditRelationships.add(testRelationship);

        onData(allOf(instanceOf(Relationship.class), is(testRelationship))).perform(click());
        onView(withId(R.id.edit_demographics_button)).perform(click());

        UiTestHelper.editDemographics(testName, testBirthMonth, testBirthDayOfMonth,
                mDashboardRule);
        onView(withId(R.id.save_relationship)).perform(click());

        testRelationship = RelationshipDbTestHelper
                .getLastRelationshipFromDatabase(mDashboardRule.getActivity());
        assertEquals(testName, testRelationship.getName().toString());
        assertEquals(testBirthday.toString(), testRelationship.getBirthdayString());

    }

    @After
    public void restorePreEditRelationships() {
        RelationshipDbHelper relationshipDbHelper =
                new RelationshipDbHelper(mDashboardRule.getActivity());
        for (Relationship relationship : preEditRelationships) {
            relationshipDbHelper.updateRelationship(relationship);
        }
    }
}
