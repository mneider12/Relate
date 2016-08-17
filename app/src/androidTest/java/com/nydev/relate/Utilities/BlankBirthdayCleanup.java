package com.nydev.relate.Utilities;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.rule.ActivityTestRule;

import com.nydev.relate.RelationshipContract.RelationshipEntry;
import com.nydev.relate.RelationshipDashboardActivity;
import com.nydev.relate.RelationshipDbHelper;
import com.nydev.relate.RelationshipDbTestHelper;

import org.junit.Rule;
import org.junit.Test;

/**
 * Created by markneider on 8/16/16.
 */
public class BlankBirthdayCleanup {

    private static final String replaceBlanksWithNullSql =
            "UPDATE " + RelationshipEntry.TABLE_NAME +
            " SET " + RelationshipEntry.COLUMN_NAME_BIRTHDAY + "=NULL " +
            "WHERE " + RelationshipEntry.COLUMN_NAME_BIRTHDAY + "=\"\"";

    @Rule
    public ActivityTestRule<RelationshipDashboardActivity> mDashboardRule =
            new ActivityTestRule<>(RelationshipDashboardActivity.class);

    @Test
    public void setReplaceBlanksWithNull() {
        RelationshipDbHelper relationshipDbHelper = new RelationshipDbHelper(mDashboardRule.getActivity());
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getWritableDatabase();
        relationshipDatabase.execSQL(replaceBlanksWithNullSql);
    }
}
