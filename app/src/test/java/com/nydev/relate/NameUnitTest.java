package com.nydev.relate;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by markneider on 7/31/16.
 * Unit test suite for Name class
 */
public class NameUnitTest {

    /**
     * Test the Name(String rawString) constructor
     */
    @Test
    public void rawStringConstructorTest() {
        final String rawName = " mark von Nyder  ";
        final String lastName = "Nyder";
        final String firstName = "mark von";
        Name name = new Name(rawName); // target of this test
        assertEquals(lastName, name.getLastName());
        assertEquals(firstName, name.getFirstName());
    }

    /**
     * Test the toString method
     */
    @Test
    public void toStringTest() {
        final String lastName = "McTestFace";
        final String firstName = "Testy";
        final String expectedToString = "Testy McTestFace";
        Name name = new Name(lastName, firstName);
        assertEquals(expectedToString, name.toString()); // target of this test
    }
}
