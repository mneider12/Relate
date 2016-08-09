package com.nydev.relate;

import android.support.test.rule.ActivityTestRule;

import com.nydev.relate.DateHelper.Month;

import org.junit.Rule;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by markneider on 8/8/16.
 */
public class DemographicsEditTestHelper {

    @Rule
    public static ActivityTestRule<RelationshipDashboardActivity> mDashboardRule;

    public static void editDemographics(String name, Month birthMonth, int dayOfMonth,
                                ActivityTestRule<RelationshipDashboardActivity> dashboardRule) {

        mDashboardRule = dashboardRule;
        onView(withId(R.id.name_entry_view)).perform(clearText());
        onView(withId(R.id.name_entry_view)).perform(typeText(name), closeSoftKeyboard()); // Enter name on create form
        onView(withId(R.id.birthday_selection_button)).perform(click()); // open birthday picker

        // Select birth month
        onView(withId(R.id.month_spinner)).perform(click());
        onData(allOf(instanceOf(Month.class), is(birthMonth)))
                .inRoot(isPlatformPopup()).perform(click());

        // select birth day of month
        onView(withId(R.id.day_spinner)).perform(click());
        onData(allOf(instanceOf(Integer.class), is(dayOfMonth)))
                .inRoot(isPlatformPopup()).perform(click());

        // save birthday and relationship
        onView(withId(R.id.save_birthday)).perform(click());
    }
}
