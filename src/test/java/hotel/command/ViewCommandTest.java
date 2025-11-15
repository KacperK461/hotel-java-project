package hotel.command;

import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.repository.MyMap;
import hotel.service.HotelService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ViewCommandTest {

    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void viewOccupiedRoom_printsBookingDetails() {
        MyMap<Integer, Room> map = new MyMap<>();
        Room room = new Room(301, "View room", 2, 120.0);
        LocalDate checkin = LocalDate.now().minusDays(1);
        LocalDate planned = LocalDate.now().plusDays(1);
        room.setBooking(new Booking(List.of(new Guest("Foo","Bar")), checkin, planned));
        map.put(301, room);
        HotelService service = new HotelService(map);

        String input = "301\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ViewCommand cmd = new ViewCommand(service);
        cmd.execute();

        String printed = out.toString();
        assertTrue(printed.contains("Guests:"));
        assertTrue(printed.contains("Check-in date:"));
        assertTrue(printed.contains("Planned checkout date:"));
    }

    @Test
    void viewFreeRoom_printsFreeMessage() {
        MyMap<Integer, Room> map = new MyMap<>();
        Room room = new Room(302, "Free room", 1, 80.0);
        map.put(302, room);
        HotelService service = new HotelService(map);

        String input = "302\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ViewCommand cmd = new ViewCommand(service);
        cmd.execute();

        String printed = out.toString();
        assertTrue(printed.contains("Room is currently free."));
    }
}
