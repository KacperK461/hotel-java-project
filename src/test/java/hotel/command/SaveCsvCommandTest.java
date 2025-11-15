package hotel.command;

import hotel.model.Room;
import hotel.repository.MyMap;
import hotel.service.CsvService;
import hotel.service.HotelService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class SaveCsvCommandTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream out;
    private final File csvFile = new File("hotel_config.csv");

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        if (csvFile.exists()) csvFile.delete();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        if (csvFile.exists()) csvFile.delete();
    }

    @Test
    void saveCsv_createsFileWithContents() throws Exception {
        MyMap<Integer, Room> map = new MyMap<>();
        map.put(1, new Room(1, "Room A", 1, 10.0));
        HotelService service = new HotelService(map);
        CsvService csvService = new CsvService();

        SaveCsvCommand cmd = new SaveCsvCommand(service, csvService);
        cmd.execute();

        assertTrue(csvFile.exists());
        String printed = out.toString();
        assertTrue(printed.contains("Saving hotel state to:"));
    }
}
