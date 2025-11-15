package hotel.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class RoomTest {

    private Room room;
    private Booking booking;
    private List<Guest> guests;

    @BeforeEach
    void setUp() {
        room = new Room(101, "Standard room", 2, 120.50);

        guests = new ArrayList<>();
        guests.add(new Guest("John", "Doe"));
        guests.add(new Guest("Jane", "Smith"));

        booking = new Booking(guests, LocalDate.of(2025, 11, 15), LocalDate.of(2025, 11, 20));
    }

    @Test
    void testConstructor() {
        assertEquals(101, room.getNumber());
        assertEquals("Standard room", room.getDescription());
        assertEquals(2, room.getCapacity());
        assertEquals(120.50, room.getPricePerNight(), 0.01);
        assertTrue(room.isFree());
        assertNull(room.getBooking());
    }

    @Test
    void testIsFreeWhenNoBooking() {
        assertTrue(room.isFree());
    }

    @Test
    void testIsFreeWhenHasBooking() {
        room.setBooking(booking);
        assertFalse(room.isFree());
    }

    @Test
    void testSetAndGetBooking() {
        assertNull(room.getBooking());

        room.setBooking(booking);
        assertEquals(booking, room.getBooking());
        assertFalse(room.isFree());
    }

    @Test
    void testSetBookingToNull() {
        room.setBooking(booking);
        assertFalse(room.isFree());

        room.setBooking(null);
        assertTrue(room.isFree());
        assertNull(room.getBooking());
    }

    @Test
    void testReplaceBooking() {
        List<Guest> newGuests = new ArrayList<>();
        newGuests.add(new Guest("Alice", "Johnson"));
        Booking newBooking = new Booking(newGuests, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 5));

        room.setBooking(booking);
        assertEquals(booking, room.getBooking());

        room.setBooking(newBooking);
        assertEquals(newBooking, room.getBooking());
        assertNotEquals(booking, room.getBooking());
    }

    @Test
    void testToString() {
        String expected = "#101 (Standard room) — 120.5 PLN, max 2 guests";
        assertEquals(expected, room.toString());
    }

    @Test
    void testToStringWithDifferentValues() {
        Room testRoom = new Room(205, "Deluxe Suite", 4, 350.75);
        String expected = "#205 (Deluxe Suite) — 350.75 PLN, max 4 guests";
        assertEquals(expected, testRoom.toString());
    }

    @Test
    void testToStringWithZeroPrice() {
        Room freeRoom = new Room(999, "Free room", 1, 0.0);
        String expected = "#999 (Free room) — 0.0 PLN, max 1 guests";
        assertEquals(expected, freeRoom.toString());
    }

    @Test
    void testToStringWithSpecialCharacters() {
        Room specialRoom = new Room(303, "Luxury & Premium Suite", 3, 299.99);
        String expected = "#303 (Luxury & Premium Suite) — 299.99 PLN, max 3 guests";
        assertEquals(expected, specialRoom.toString());
    }

    @Test
    void testGettersAreConsistent() {
        Room testRoom = new Room(505, "Test Room", 6, 199.99);

        assertEquals(505, testRoom.getNumber());
        assertEquals("Test Room", testRoom.getDescription());
        assertEquals(6, testRoom.getCapacity());
        assertEquals(199.99, testRoom.getPricePerNight(), 0.01);
    }

    @Test
    void testConstructorWithZeroValues() {
        Room zeroRoom = new Room(0, "", 0, 0.0);
        assertEquals(0, zeroRoom.getNumber());
        assertEquals("", zeroRoom.getDescription());
        assertEquals(0, zeroRoom.getCapacity());
        assertEquals(0.0, zeroRoom.getPricePerNight(), 0.01);
    }

    @Test
    void testConstructorWithNegativeValues() {
        Room negativeRoom = new Room(-1, "Negative room", -5, -100.0);
        assertEquals(-1, negativeRoom.getNumber());
        assertEquals("Negative room", negativeRoom.getDescription());
        assertEquals(-5, negativeRoom.getCapacity());
        assertEquals(-100.0, negativeRoom.getPricePerNight(), 0.01);
    }
}

