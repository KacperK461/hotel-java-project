package hotel.command;

import hotel.model.Room;
import hotel.repository.MyMap;
import hotel.service.HotelService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ListAndPricesCommandTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void listCommand_printsAllRooms() {
        MyMap<Integer, Room> map = new MyMap<>();
        map.put(1, new Room(1, "A", 1, 10.0));
        map.put(2, new Room(2, "B", 2, 20.0));
        HotelService service = new HotelService(map);

        ListCommand cmd = new ListCommand(service);
        cmd.execute();

        String printed = out.toString();
        assertTrue(printed.contains("All rooms:"));
        assertTrue(printed.contains("#1"));
        assertTrue(printed.contains("#2"));
    }

    @Test
    void pricesCommand_printsPrices() {
        MyMap<Integer, Room> map = new MyMap<>();
        map.put(10, new Room(10, "X", 1, 99.5));
        map.put(20, new Room(20, "Y", 1, 150.0));
        HotelService service = new HotelService(map);

        PricesCommand cmd = new PricesCommand(service);
        cmd.execute();

        String printed = out.toString();
        assertTrue(printed.contains("Room prices:"));
        assertTrue(printed.contains("Room 10"));
        assertTrue(printed.contains("Room 20"));
    }
}
