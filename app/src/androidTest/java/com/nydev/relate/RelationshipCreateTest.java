package com.nydev.relate;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.rule.ActivityTestRule;

import org.joda.time.MonthDay;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import com.nydev.relate.DateHelper.Month;
import com.nydev.relate.RelationshipContract.RelationshipEntry;

/**
 * Created by markneider on 8/6/16.
 * Test the relationship creation workflow
 */
public class RelationshipCreateTest {

    @Rule
    public ActivityTestRule<RelationshipDashboardActivity> mDashboardRule =
            new ActivityTestRule<>(RelationshipDashboardActivity.class);

    /**
     * Test the relationship creation workflow.
     * SIDE EFFECT: will add a relationship to the database
     */
    @Test
    public void relationshipCreateTest() {
        @SuppressWarnings("SpellCheckingInspection")
        final String testName = "Anakin Skywalker";
        final Month testBirthMonth = Month.APRIL;
        final int testBirthDayOfMonth = 19;
        MonthDay testBirthday = new MonthDay(testBirthMonth.getMonthOfYear(), testBirthDayOfMonth);

        onView(withId(R.id.create_relationship_button)).perform(click()); // open create activity
        DemographicsEditTestHelper.editDemographics(testName, testBirthMonth, testBirthDayOfMonth,
                mDashboardRule);
        onView(withId(R.id.save_relationship)).perform(click());

        Relationship testRelationship =
                RelationshipDbTestHelper.getLastRelationshipFromDatabase(mDashboardRule.getActivity());
        assertEquals(testName, testRelationship.getName().toString());
        assertEquals(testBirthday.toString(), testRelationship.getBirthdayString());
    }
}