package hotel.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class BookingTest {

    private Booking booking;
    private List<Guest> guests;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;

    @BeforeEach
    void setUp() {
        guests = new ArrayList<>();
        guests.add(new Guest("John", "Doe"));
        guests.add(new Guest("Jane", "Smith"));

        checkinDate = LocalDate.of(2025, 11, 15);
        checkoutDate = LocalDate.of(2025, 11, 20);

        booking = new Booking(guests, checkinDate, checkoutDate);
    }

    @Test
    void testConstructor() {
        assertEquals(guests, booking.getGuests());
        assertEquals(checkinDate, booking.getCheckinDate());
        assertEquals(checkoutDate, booking.getPlannedCheckoutDate());
    }

    @Test
    void testConstructorWithEmptyGuestList() {
        List<Guest> emptyGuests = new ArrayList<>();
        Booking emptyBooking = new Booking(emptyGuests, checkinDate, checkoutDate);

        assertEquals(emptyGuests, emptyBooking.getGuests());
        assertTrue(emptyBooking.getGuests().isEmpty());
        assertEquals(checkinDate, emptyBooking.getCheckinDate());
        assertEquals(checkoutDate, emptyBooking.getPlannedCheckoutDate());
    }

    @Test
    void testConstructorWithNullGuestList() {
        Booking nullGuestsBooking = new Booking(null, checkinDate, checkoutDate);

        assertNull(nullGuestsBooking.getGuests());
        assertEquals(checkinDate, nullGuestsBooking.getCheckinDate());
        assertEquals(checkoutDate, nullGuestsBooking.getPlannedCheckoutDate());
    }

    @Test
    void testConstructorWithNullDates() {
        Booking nullDatesBooking = new Booking(guests, null, null);

        assertEquals(guests, nullDatesBooking.getGuests());
        assertNull(nullDatesBooking.getCheckinDate());
        assertNull(nullDatesBooking.getPlannedCheckoutDate());
    }

    @Test
    void testConstructorWithSameDates() {
        LocalDate sameDate = LocalDate.of(2025, 11, 15);
        Booking sameDateBooking = new Booking(guests, sameDate, sameDate);

        assertEquals(guests, sameDateBooking.getGuests());
        assertEquals(sameDate, sameDateBooking.getCheckinDate());
        assertEquals(sameDate, sameDateBooking.getPlannedCheckoutDate());
    }

    @Test
    void testGetGuests() {
        List<Guest> retrievedGuests = booking.getGuests();
        assertEquals(2, retrievedGuests.size());
        assertEquals("John Doe", retrievedGuests.get(0).toString());
        assertEquals("Jane Smith", retrievedGuests.get(1).toString());
    }

    @Test
    void testSingleGuestBooking() {
        List<Guest> singleGuest = new ArrayList<>();
        singleGuest.add(new Guest("Alice", "Johnson"));

        Booking singleGuestBooking = new Booking(singleGuest, checkinDate, checkoutDate);

        assertEquals(1, singleGuestBooking.getGuests().size());
        assertEquals("Alice Johnson", singleGuestBooking.getGuests().get(0).toString());
    }

    @Test
    void testMultipleGuestsBooking() {
        List<Guest> multipleGuests = new ArrayList<>();
        multipleGuests.add(new Guest("Alice", "Johnson"));
        multipleGuests.add(new Guest("Bob", "Williams"));
        multipleGuests.add(new Guest("Carol", "Brown"));

        Booking multipleGuestsBooking = new Booking(multipleGuests, checkinDate, checkoutDate);

        assertEquals(3, multipleGuestsBooking.getGuests().size());
        assertEquals("Alice Johnson", multipleGuestsBooking.getGuests().get(0).toString());
        assertEquals("Bob Williams", multipleGuestsBooking.getGuests().get(1).toString());
        assertEquals("Carol Brown", multipleGuestsBooking.getGuests().get(2).toString());
    }

    @Test
    void testDateEdgeCases() {
        LocalDate minDate = LocalDate.MIN;
        LocalDate maxDate = LocalDate.MAX;

        Booking edgeCaseBooking = new Booking(guests, minDate, maxDate);

        assertEquals(minDate, edgeCaseBooking.getCheckinDate());
        assertEquals(maxDate, edgeCaseBooking.getPlannedCheckoutDate());
    }
}

