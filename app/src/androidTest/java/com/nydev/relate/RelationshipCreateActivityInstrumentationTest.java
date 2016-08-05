package com.nydev.relate;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by markneider on 8/4/16.
 * Android test suite for the RelationshipCreateActivity class
 */

@RunWith(AndroidJUnit4.class)
public class RelationshipCreateActivityInstrumentationTest {

    @Rule
    public ActivityTestRule<RelationshipCreateActivity> mRelationshipCreateActivityRule =
            new ActivityTestRule<>(RelationshipCreateActivity.class);

    @Test
    public void setName() {
        onView(withId(R.id.name_text_view));
    }
}
