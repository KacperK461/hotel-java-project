package hotel.service;

import hotel.model.Guest;
import hotel.model.Room;
import hotel.repository.MyMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvServiceExtraTest {

    private final File tmp = new File("tmp_hotel.csv");

    @BeforeEach
    void before() {
        if (tmp.exists()) tmp.delete();
    }

    @AfterEach
    void after() {
        if (tmp.exists()) tmp.delete();
    }

    @Test
    void loadFromCsv_parsesBookingAndGuests_withSingleAndMultiNames() throws Exception {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(tmp))) {
            w.write("# comment"); w.newLine();
            w.write("10,Room X,99.50,2,,,"); w.newLine();
            w.write("20,Room Y,120.00,3,Single;John Doe,2025-11-10,2025-11-12"); w.newLine();
        }

        CsvService csv = new CsvService();
        MyMap<Integer, Room> rooms = csv.loadFromCsv(tmp.getPath());

        assertNotNull(rooms);
        assertTrue(rooms.keys().contains(10));
        assertTrue(rooms.keys().contains(20));

        Room r20 = rooms.get(20);
        assertNotNull(r20.getBooking());
        List<Guest> guests = r20.getBooking().getGuests();
        assertEquals(2, guests.size());
        assertEquals("Single", guests.get(0).toString().split(" ")[0]);
        assertEquals("John Doe", guests.get(1).toString());
    }

    @Test
    void saveToCsv_and_load_roundtrip_preservesRoomAndBooking() throws Exception {
        MyMap<Integer, Room> rooms = new MyMap<>();
    Room r = new Room(99, "Desc with comma", 2, 77.77);
        r.setBooking(new hotel.model.Booking(List.of(new Guest("A","B")), LocalDate.of(2025,11,1), LocalDate.of(2025,11,3)));
        rooms.put(99, r);

        CsvService csv = new CsvService();
        csv.saveToCsv(rooms, tmp.getPath());

        assertTrue(tmp.exists());

        MyMap<Integer, Room> loaded = csv.loadFromCsv(tmp.getPath());
        assertTrue(loaded.keys().contains(99));
        Room lr = loaded.get(99);
        assertNotNull(lr.getBooking());
        assertEquals(1, lr.getBooking().getGuests().size());
        assertEquals("A B", lr.getBooking().getGuests().get(0).toString());
    }

    @Test
    void loadFromCsv_ignoresMalformedLines_and_continues() throws Exception {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(tmp))) {
            w.write("1,OK,10.0,1,,,"); w.newLine();
            w.write("2,BAD,,2,,,"); w.newLine();
            w.write("3,SHORT"); w.newLine();
        }

        CsvService csv = new CsvService();
        MyMap<Integer, Room> rooms = csv.loadFromCsv(tmp.getPath());

        assertTrue(rooms.keys().contains(1));
        assertFalse(rooms.keys().contains(2));
        assertFalse(rooms.keys().contains(3));
    }
}
