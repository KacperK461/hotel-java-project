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
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutCommandTest {

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
    void successfulCheckout_freesRoomAndPrintsAmount() {
        MyMap<Integer, Room> map = new MyMap<>();
        Room room = new Room(201, "Checkout room", 2, 50.0);
        LocalDate checkin = LocalDate.now().minusDays(2);
        LocalDate planned = LocalDate.now().plusDays(1);
        room.setBooking(new Booking(List.of(new Guest("A","B")), checkin, planned));
        map.put(201, room);
        HotelService service = new HotelService(map);

        String input = "201\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CheckoutCommand cmd = new CheckoutCommand(service);
        cmd.execute();

        String printed = out.toString();
        assertTrue(printed.contains("Successfully checked out!"));
        assertTrue(printed.contains("Amount to pay"));
        assertNull(room.getBooking());
    }
}
