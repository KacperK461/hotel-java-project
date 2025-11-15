package hotel.command;

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

import static org.junit.jupiter.api.Assertions.*;

public class CheckinCommandTest {

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
    void successfulCheckin_setsBookingAndPrintsSuccess() {
        MyMap<Integer, Room> map = new MyMap<>();
        Room room = new Room(101, "Test room", 2, 100.0);
        map.put(101, room);
        HotelService service = new HotelService(map);

        LocalDate checkin = LocalDate.now();
        LocalDate checkout = checkin.plusDays(2);

        String input = String.join("\n",
                "101",
                "John Doe",
                "n",
                checkin.toString(),
                checkout.toString()
        ) + "\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CheckinCommand cmd = new CheckinCommand(service);
        cmd.execute();

    String printed = out.toString();
    assertTrue(printed.contains("Successfully checked in!"));
    assertNotNull(room.getBooking());
    assertTrue(room.getBooking().getGuests().stream().anyMatch(g -> g.toString().equals("John Doe")));
    }
}
