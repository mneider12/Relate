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

    /**
     * Test the getNextRelationship method.
     * Confirm that the correct next available ID is returned and the next available ID is incremented.
     */
    @Test
    public void getNextRelationshipIdTest() {
        // Call to mockSharedPreference will return this value
        final int expectedNextRelationshipId = MockHelper.getNextRelationshipId();

        // Create mock objects
        Context mockedContext = MockHelper.getContext();
        SharedPreferences mockedSharedPreferences = mockedContext.getSharedPreferences(
                PreferencesHelper.getRelationshipPrefsName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor mockedSharedPreferencesEditor = mockedSharedPreferences.edit();

        // target of test - verify that the PreferencesHelper returns the next relationship ID from the mock Context's SharedPreferences
        assertEquals(expectedNextRelationshipId, PreferencesHelper.getNextRelationshipId(mockedContext));
        // verify we tried to increment the value of NEXT_RELATIONSHIP_ID
        verify(mockedSharedPreferencesEditor).putInt(PreferencesHelper.getNextRelationshipIdKey(),
                expectedNextRelationshipId + 1); // verify we tried to increment nextRelationshipId
    }
}
