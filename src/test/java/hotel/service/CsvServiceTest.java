package hotel.service;

import hotel.model.Guest;
import hotel.model.Room;
import hotel.repository.MyMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class CsvServiceTest {

    private CsvService csvService;

    @BeforeEach
    void setUp() {
        csvService = new CsvService();
    }

    @Test
    void testCsvServiceCreation() {
        assertNotNull(csvService);
    }

    @Test
    void testLoadFromCsvFileNotFound() {
        assertThrows(IOException.class, () -> {
            csvService.loadFromCsv("nonexistent_file.csv");
        });
    }

    @Test
    void testSaveToCsvNullRooms() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            csvService.saveToCsv(null, "test.csv");
        });

        assertEquals("Rooms map cannot be null", exception.getMessage());
    }

    @Test
    void testBasicFunctionality() {
        // Test basic service creation and null checks
        MyMap<Integer, Room> rooms = new MyMap<>();
        rooms.put(101, new Room(101, "Test room", 2, 100.0));

        // This should not throw an exception
        assertDoesNotThrow(() -> {
            assertTrue(rooms.keys().size() > 0);
        });
    }
}

