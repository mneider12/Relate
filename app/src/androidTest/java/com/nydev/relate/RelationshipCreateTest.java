package com.nydev.relate;

import android.support.test.espresso.DataInteraction;
import android.support.test.rule.ActivityTestRule;
import android.util.MonthDisplayHelper;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import com.nydev.relate.DateHelper.Month;

/**
 * Created by markneider on 8/6/16.
 * Test the Relationship creation workflow
 */
public class RelationshipCreateTest {

    @Rule
    public ActivityTestRule<RelationshipDashboardActivity> mDashboardRule =
            new ActivityTestRule<>(RelationshipDashboardActivity.class);

    @Test
    public void createRelationshipTest() {
        @SuppressWarnings("SpellCheckingInspection")
        final String testName = "Anakin Skywalker";
        final Month testBirthMonth = Month.APRIL;
        final int testBirthDayOfMonth = 19;

        onView(withId(R.id.create_relationship_button)).perform(click()); // open create activity
        onView(withId(R.id.name_entry_view)).perform(typeText(testName), closeSoftKeyboard()); // Enter name on create form
        onView(withId(R.id.birthday_selection_button)).perform(click()); // open birthday picker

        // Select birth month
        onView(withId(R.id.month_spinner)).perform(click());
        onData(allOf(instanceOf(Month.class), is(testBirthMonth)))
                .inRoot(isPlatformPopup()).perform(click());

        // select birth day of month
        onView(withId(R.id.day_spinner)).perform(click());
        onData(allOf(instanceOf(Integer.class), is(testBirthDayOfMonth)))
                .inRoot(isPlatformPopup()).perform(click());

        // save birthday and relationship
        onView(withId(R.id.save_birthday)).perform(click());
        onView(withId(R.id.save_relationship)).perform(click());

        
    }
}
