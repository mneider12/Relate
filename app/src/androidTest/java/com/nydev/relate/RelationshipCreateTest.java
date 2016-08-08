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
        MonthDay testBirthday = new MonthDay(testBirthMonth.getMonthOfYear(), testBirthDayOfMonth);

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

        Relationship testRelationship = getLastRelationshipFromDatabase();
        assertEquals(testName, testRelationship.getName().toString());
        assertEquals(testBirthday.toString(), testRelationship.getBirthdayString());
    }

    private Relationship getLastRelationshipFromDatabase() {
        SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper(mDashboardRule.getActivity(),
                RelationshipDbHelper.DATABASE_NAME, null, RelationshipDbHelper.DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };
        SQLiteDatabase relationshipDatabase = sqLiteOpenHelper.getReadableDatabase();

        Cursor lastRelationshipCursor = relationshipDatabase.rawQuery(
                "SELECT * FROM " + RelationshipEntry.TABLE_NAME +
                        " WHERE " + RelationshipEntry._ID + "= (SELECT MAX(" + RelationshipEntry._ID +
                        ")  FROM " + RelationshipEntry.TABLE_NAME + ")", null);

        Relationship relationship;
        if (lastRelationshipCursor.moveToFirst()) { // If a row was returned
            int relationshipId = lastRelationshipCursor.getInt(lastRelationshipCursor.getColumnIndex(
                    RelationshipEntry._ID));
            String lastName = lastRelationshipCursor.getString(lastRelationshipCursor.getColumnIndex(
                    RelationshipEntry.COLUMN_NAME_LAST_NAME));
            String firstName = lastRelationshipCursor.getString(lastRelationshipCursor.getColumnIndex(
                    RelationshipEntry.COLUMN_NAME_FIRST_NAME));
            String rawBirthday = lastRelationshipCursor.getString(lastRelationshipCursor.getColumnIndex(
                    RelationshipContract.RelationshipEntry.COLUMN_NAME_BIRTHDAY));
            relationship = new Relationship(relationshipId, lastName, firstName);
            if (rawBirthday != null) {
                MonthDay birthday = MonthDay.parse(rawBirthday);
                relationship.setBirthday(birthday);
            }
        } else { // no match found for relationshipId in _ID column
            relationship = new Relationship(); // blank relationship with ID -1. Not valid to save, no current use case.
        }

        lastRelationshipCursor.close();
        return relationship;
    }
}
