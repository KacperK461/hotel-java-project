package hotel.service;

import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.repository.MyMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class HotelServiceTest {

    private HotelService hotelService;
    private MyMap<Integer, Room> rooms;
    private List<Guest> guests;

    @BeforeEach
    void setUp() {
        rooms = new MyMap<>();
        rooms.put(101, new Room(101, "Standard room", 2, 120.0));
        rooms.put(102, new Room(102, "Deluxe room", 4, 200.0));

        hotelService = new HotelService(rooms);

        guests = new ArrayList<>();
        guests.add(new Guest("John", "Doe"));
        guests.add(new Guest("Jane", "Smith"));
    }

    @Test
    void testGetRoom() {
        Room room = hotelService.getRoom(101);
        assertNotNull(room);
        assertEquals(101, room.getNumber());
        assertEquals("Standard room", room.getDescription());

        assertNull(hotelService.getRoom(999));
    }

    @Test
    void testCheckinSuccessful() {
        LocalDate checkin = LocalDate.of(2025, 11, 15);
        LocalDate checkout = LocalDate.of(2025, 11, 20);

        hotelService.checkin(101, guests, checkin, checkout);

        Room room = hotelService.getRoom(101);
        assertFalse(room.isFree());

        Booking booking = room.getBooking();
        assertNotNull(booking);
        assertEquals(2, booking.getGuests().size());
        assertEquals(checkin, booking.getCheckinDate());
        assertEquals(checkout, booking.getPlannedCheckoutDate());
    }

    @Test
    void testCheckinRoomNotFound() {
        LocalDate checkin = LocalDate.of(2025, 11, 15);
        LocalDate checkout = LocalDate.of(2025, 11, 20);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hotelService.checkin(999, guests, checkin, checkout);
        });

        assertEquals("Room not found", exception.getMessage());
    }

    @Test
    void testCheckinTooManyGuests() {
        LocalDate checkin = LocalDate.of(2025, 11, 15);
        LocalDate checkout = LocalDate.of(2025, 11, 20);

        List<Guest> tooManyGuests = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            tooManyGuests.add(new Guest("Guest" + i, "Test"));
        }

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            hotelService.checkin(101, tooManyGuests, checkin, checkout);
        });

        assertEquals("Too many guests for this room", exception.getMessage());
    }

    @Test
    void testCheckoutSuccessful() {
        LocalDate checkin = LocalDate.of(2025, 11, 15);
        LocalDate checkout = LocalDate.of(2025, 11, 20);

        hotelService.checkin(101, guests, checkin, checkout);

        double cost = hotelService.checkout(101, LocalDate.of(2025, 11, 17));

        assertEquals(240.0, cost, 0.01);
        assertTrue(hotelService.getRoom(101).isFree());
    }

    @Test
    void testCheckoutRoomNotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hotelService.checkout(999, LocalDate.of(2025, 11, 20));
        });

        assertEquals("Room not found", exception.getMessage());
    }

    @Test
    void testCheckoutDateValidation() {
        LocalDate checkin = LocalDate.of(2025, 11, 15);
        LocalDate checkout = LocalDate.of(2025, 11, 20);

        hotelService.checkin(101, guests, checkin, checkout);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hotelService.checkout(101, LocalDate.of(2025, 11, 10));
        });

        assertEquals("Checkout date cannot be before check-in date!", exception.getMessage());
    }

    @Test
    void testCheckoutSameDay() {
        LocalDate checkin = LocalDate.of(2025, 11, 15);
        LocalDate checkout = LocalDate.of(2025, 11, 20);

        hotelService.checkin(101, guests, checkin, checkout);

        double cost = hotelService.checkout(101, checkin);
        assertEquals(120.0, cost, 0.01);
        assertTrue(hotelService.getRoom(101).isFree());
    }

    @Test
    void testCompleteWorkflow() {
        LocalDate checkin = LocalDate.of(2025, 11, 15);
        LocalDate checkout = LocalDate.of(2025, 11, 20);
        LocalDate actualCheckout = LocalDate.of(2025, 11, 17);

        assertTrue(hotelService.getRoom(101).isFree());
        assertTrue(hotelService.getRoom(102).isFree());

        hotelService.checkin(101, guests, checkin, checkout);
        assertFalse(hotelService.getRoom(101).isFree());
        assertTrue(hotelService.getRoom(102).isFree());

        double cost = hotelService.checkout(101, actualCheckout);
        assertEquals(240.0, cost, 0.01);
        assertTrue(hotelService.getRoom(101).isFree());
        assertTrue(hotelService.getRoom(102).isFree());
    }

    @Test
    void testMultipleRoomsWorkflow() {
        LocalDate checkin = LocalDate.of(2025, 11, 15);
        LocalDate checkout = LocalDate.of(2025, 11, 20);

        List<Guest> fourGuests = new ArrayList<>();
        fourGuests.add(new Guest("Guest", "1"));
        fourGuests.add(new Guest("Guest", "2"));
        fourGuests.add(new Guest("Guest", "3"));
        fourGuests.add(new Guest("Guest", "4"));

        hotelService.checkin(101, guests, checkin, checkout);
        hotelService.checkin(102, fourGuests, checkin, checkout);

        assertFalse(hotelService.getRoom(101).isFree());
        assertFalse(hotelService.getRoom(102).isFree());

        double cost101 = hotelService.checkout(101, LocalDate.of(2025, 11, 17));
        assertEquals(240.0, cost101, 0.01);
        assertTrue(hotelService.getRoom(101).isFree());
        assertFalse(hotelService.getRoom(102).isFree());

        double cost102 = hotelService.checkout(102, LocalDate.of(2025, 11, 18));
        assertEquals(600.0, cost102, 0.01);
        assertTrue(hotelService.getRoom(101).isFree());
        assertTrue(hotelService.getRoom(102).isFree());
    }
}

