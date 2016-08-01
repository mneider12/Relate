package com.nydev.relate;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by markneider on 7/31/16.
 * Unit test suite for Relationship class
 */
public class RelationshipUnitTest {

    /**
     * Test the default Relationship constructor. Expect relationshipId = -1 and data is null or default.
     * Relationship does not need to be ready for actual use. It is invalid at construction time.
     */
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

    /**
     * Test the Relationship constructor that assigns an ID based on SharedPreferences from the Context
     */
    @Test
    public void contextConstructorTest() {
        // Expectation variables
        final int expectedRelationshipId = MockHelper.getNextRelationshipId();
        final String expectedLastName = "";
        final String expectedFirstName = "";

        // create mock objects
        Context mockedContext = MockHelper.getContext();

        Relationship relationship = new Relationship(mockedContext);

        assertEquals(expectedRelationshipId, relationship.getRelationshipId());
        assertEquals(expectedLastName, relationship.getLastName());
        assertEquals(expectedFirstName, relationship.getFirstName());
    }

    /**
     * Test fully specified constructor
     */
    @Test
    public void fullySpecifiedConstructorTest() {
        // Expectation variables
        final int expectedRelationshipId = 17;
        final String expectedLastName = "Knight";
        final String expectedFirstName = "Dark";

        // target of test
        Relationship relationship = new Relationship(expectedRelationshipId, expectedLastName,
                expectedFirstName);

        // assertions
        assertEquals(expectedRelationshipId, relationship.getRelationshipId());
        assertEquals(expectedLastName, relationship.getLastName());
        assertEquals(expectedFirstName, relationship.getFirstName());
    }
}
