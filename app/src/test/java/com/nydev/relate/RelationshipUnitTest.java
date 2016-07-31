package com.nydev.relate;

import android.content.Context;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by markneider on 7/31/16.
 * Unit test suite for Relationship class
 */
public class RelationshipUnitTest {

    @Test
    public void defaultConstructorTest() {
        // Expectation variables
        final int expectedRelationshipId = -1;
        final String expectedLastName = "";
        final String expectedFirstName = "";

        Relationship relationship = new Relationship();
        assertEquals(expectedRelationshipId, relationship.getRelationshipId());
        assertEquals(expectedLastName, relationship.getLastName());
        assertEquals(expectedFirstName, relationship.getFirstName());
    }

    @Test
    public void contextConstructorTest() {
        // Expectation variables
        final int expectedRelationshipId = 12;
        final String expectedLastName = "";
        final String expectedFirstName = "";

        // create mock objects
        Context mockedContext = MockHelper.getContext();


        Relationship relationship = new Relationship(mockedContext);
    }
}
