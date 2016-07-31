package com.nydev.relate;

import android.content.Context;
import android.content.SharedPreferences;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

/**
 * Created by markneider on 7/31/16.
 * Utility class to provide mock objects for unit tests
 */
public class MockHelper {

    private final static int INIT_NEXT_RELATIONSHIP_ID = 15; // new scenarios will start at this ID

    /**
     * All methods should be static and this class should never be constructed
     */
    private MockHelper() {

    }

    /**
     * Get a mocked Context. Mocked features list:
     *  getSharedPreferences(PreferencesHelper.getRelationshipPrefsName(),Context.MODE_PRIVATE)
     * @return mocked Context
     */
    public static Context getContext() {
        Context mockedContext = mock(Context.class);

        // Fetch mockSharedPreference from mockContext
        // cannot mock with the thenReturn method, so the line below needs to stay separated
        SharedPreferences mockedSharedPreferences = getSharedPreferences();
        when(mockedContext.getSharedPreferences(PreferencesHelper.getRelationshipPrefsName(),
                Context.MODE_PRIVATE)).thenReturn(mockedSharedPreferences);

        return mockedContext;
    }

    /**
     * Get a mocked SharedPreferences. Mocked features list:
     *  edit()
     *  getInt(PreferencesHelper.getNextRelationshipIdKey(),PreferencesHelper.getInitialId())
     * @return mocked SharedPreferences
     */
    public static SharedPreferences getSharedPreferences() {
        SharedPreferences mockedSharedPreferences = mock(SharedPreferences.class);

        // Fetch SharedPreferences.Editor when edit() is called
        // cannot mock with the thenReturn method, so the line below needs to stay separated
        SharedPreferences.Editor mockedSharedPreferencesEditor = getSharedPreferencesEditor();
        when(mockedSharedPreferences.edit()).thenReturn(mockedSharedPreferencesEditor);
        getSharedPreferencesEditor().apply(); // just here because of lint warning for calling edit without apply. No effect.
        // return 15 as the nextRelationshipId
        when(mockedSharedPreferences.getInt(PreferencesHelper.getNextRelationshipIdKey(),
                PreferencesHelper.getInitialId())).thenReturn(INIT_NEXT_RELATIONSHIP_ID);

        return mockedSharedPreferences;
    }

    /**
     * Get a mocked SharedPreferences.Editor. No features currently mocked.
     * @return mocked SharedPreferences.Editor
     */
    public static SharedPreferences.Editor getSharedPreferencesEditor() {
        return mock(SharedPreferences.Editor.class);
    }

    /**
     * Get the nextRelationshipId that will be used in mocks
     * @return
     */
    public static int getNextRelationshipId() {
        return INIT_NEXT_RELATIONSHIP_ID;
    }
}
