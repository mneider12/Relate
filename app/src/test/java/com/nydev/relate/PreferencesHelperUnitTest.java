package com.nydev.relate;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Created by markneider on 7/31/16.
 * Unit test suite for PreferencesHelper class
 */
public class PreferencesHelperUnitTest {

    private static final String RELATIONSHIP_PREFS_NAME = "RelationshipPrefsFile"; // name of the relationship preferences file
    private static final String NEXT_RELATIONSHIP_ID = "next_relationship_id"; // name of the index in RelationshipPrefsFile storing the next available ID
    private static final int INITIAL_ID = 1; // first ID to use for a relationship when first loading the application

    /**
     * Test the getNextRelationship method.
     * Confirm that the correct next available ID is returned and the next available ID is incremented.
     */
    @Test
    public void getNextRelationshipIdTest() {
        // Call to mockSharedPreference will return this value
        final int expectedNextRelationshipId = 27;

        // Create mock objects
        Context mockedContext = mock(Context.class);
        SharedPreferences mockedSharedPreference = mock(SharedPreferences.class);
        SharedPreferences.Editor mockedSharedPreferencesEditor = mock(SharedPreferences.Editor.class);

        // Define mock behavior
        // Fetch mockSharedPreference from mockContext
        when(mockedContext.getSharedPreferences(RELATIONSHIP_PREFS_NAME, Context.MODE_PRIVATE))
                .thenReturn(mockedSharedPreference);
        // Return expectedNextRelationshipId from mockedSharedPreference
        when(mockedSharedPreference.getInt(NEXT_RELATIONSHIP_ID, INITIAL_ID)).thenReturn(expectedNextRelationshipId);
        // Get mockedSharedPreferencesEditor from mockedSharedPreference
        when(mockedSharedPreference.edit()).thenReturn(mockedSharedPreferencesEditor);
        mockedSharedPreferencesEditor.apply(); // just here because of lint warning for calling edit without apply. No effect.

        // target of test - verify that the PreferencesHelper returns the next relationship ID from the mock Context's SharedPreferences
        assertEquals(expectedNextRelationshipId, PreferencesHelper.getNextRelationshipId(mockedContext));
        // verify we tried to increment the value of NEXT_RELATIONSHIP_ID
        verify(mockedSharedPreferencesEditor).putInt(NEXT_RELATIONSHIP_ID, expectedNextRelationshipId + 1); // verify we tried to
    }
}
