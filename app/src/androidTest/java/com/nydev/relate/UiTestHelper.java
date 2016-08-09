package com.nydev.relate;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.nydev.relate.DateHelper.Month;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
 * Helper functions to interact with the User Interface during Android Tests
 */
public class UiTestHelper {

    // Needs to have caller's test activity rule to perform actions
    @Rule
    public static ActivityTestRule<RelationshipDashboardActivity> mDashboardRule;

    /**
     * Set data in a DemographicsEditFragment
     * @param name name text to set in name_entry_view (replaces existing text)
     * @param birthMonth month to select in month_spinner
     * @param dayOfMonth day to select in day_spinner
     * @param dashboardRule caller's test activity rule
     */
    public static void editDemographics(String name, Month birthMonth, int dayOfMonth,
                                ActivityTestRule<RelationshipDashboardActivity> dashboardRule) {

        mDashboardRule = dashboardRule; // adopt caller's rule
        onView(withId(R.id.name_entry_view)).perform(clearText()); // clear text in name_entry_view
        onView(withId(R.id.name_entry_view)).perform(typeText(name), closeSoftKeyboard()); // Enter name on create form
        onView(withId(R.id.birthday_selection_button)).perform(click()); // open birthday picker

        // Select birth month
        onView(withId(R.id.month_spinner)).perform(click());
        onData(allOf(instanceOf(Month.class), is(birthMonth)))
                .inRoot(isPlatformPopup()).perform(click()); // inPlatformPopup needed to access dialog root view

        // select birth day of month
        onView(withId(R.id.day_spinner)).perform(click());
        onData(allOf(instanceOf(Integer.class), is(dayOfMonth)))
                .inRoot(isPlatformPopup()).perform(click()); // inPlatformPopup needed to access dialog root view

        // save birthday and relationship
        onView(withId(R.id.save_birthday)).perform(click());
    }

    /**
     * Matcher to find whether a Relationship exists in an AdapterView
     * @param relationshipMatcher matcher describing a relationship
     * @return Matcher with will match views backed by a Relationship matching relationshipMatcher
     */
    public static Matcher<View> withRelationship(final Matcher<Relationship> relationshipMatcher) {
        return new TypeSafeMatcher<View>() {
            /**
             * Check if relationshipMatcher matches a relationship in view
             * @param view an AdapterView to check for matches
             * @return true if relationshipMatcher matches a Relationship in view, otherwise false
             */
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (relationshipMatcher.matches(adapter.getItem(i))) {
                        return true;
                    }
                }
                return false;
            }

            /**
             * {@inheritDoc}
             * @param description {@inheritDoc}
             */
            @Override
            public void describeTo(Description description) {
                description.appendText("with Relationship: ");
                relationshipMatcher.describeTo(description);
            }
        };
    }
}
