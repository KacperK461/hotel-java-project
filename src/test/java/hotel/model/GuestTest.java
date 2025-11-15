package hotel.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GuestTest {

    @Test
    void testConstructorAndToString() {
        Guest guest = new Guest("John", "Doe");
        assertEquals("John Doe", guest.toString());
    }

    @Test
    void testConstructorWithNullValues() {
        Guest nullGuest = new Guest(null, null);
        assertEquals("null null", nullGuest.toString());
    }

    @Test
    void testConstructorWithEmptyValues() {
        Guest emptyGuest = new Guest("", "");
        assertEquals(" ", emptyGuest.toString());
    }

    @Test
    void testToStringWithSpecialCharacters() {
        Guest specialGuest = new Guest("José", "O'Connor");
        assertEquals("José O'Connor", specialGuest.toString());
    }

    @Test
    void testToStringWithOnlyFirstName() {
        Guest firstNameOnlyGuest = new Guest("John", "");
        assertEquals("John ", firstNameOnlyGuest.toString());
    }

    @Test
    void testToStringWithOnlyLastName() {
        Guest lastNameOnlyGuest = new Guest("", "Doe");
        assertEquals(" Doe", lastNameOnlyGuest.toString());
    }

    @Test
    void testToStringWithWhitespace() {
        Guest whitespaceGuest = new Guest("  John  ", "  Doe  ");
        assertEquals("  John     Doe  ", whitespaceGuest.toString());
    }
}

