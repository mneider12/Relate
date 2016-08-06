package com.nydev.relate;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by markneider on 8/6/16.
 */
public class RelationshipCreateTest {

    @Rule
    public ActivityTestRule<RelationshipDashboardActivity> mDashboardRule =
            new ActivityTestRule<>(RelationshipDashboardActivity.class);

    @Test
    public void createRelationshipTest() {
        onView(withId(R.id.create_relationship_button)).perform(click());
    }
}
