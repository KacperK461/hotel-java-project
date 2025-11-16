package hotel.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainAppTest {

    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;
    private ByteArrayOutputStream out;
    private File csv;

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        csv = new File(System.getProperty("java.io.tmpdir"), "hotel_main_test_" + System.nanoTime() + ".csv");
        if (csv.exists()) csv.delete();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        if (csv != null && csv.exists()) csv.delete();
    }

    @Test
    void main_whenCsvMissing_usesDefaultConfiguration_and_exits() {
        String input = "exit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String nonExistentPath = System.getProperty("java.io.tmpdir") + "nonexistent_" + System.nanoTime() + ".csv";
        Main.main(new String[]{nonExistentPath});

        String printed = out.toString();
        assertTrue(printed.contains("Using default hardcoded configuration."));
        assertTrue(printed.contains("Hotel Management System - Enter a command:"));
    }

    @Test
    void main_whenCsvPresent_loadsAndExits() throws Exception {
        try (java.io.FileWriter fw = new java.io.FileWriter(csv)) {
            fw.write("1,Room CSV,50.00,1,,,");
        }

        String input = "exit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

    Main.main(new String[]{csv.getPath()});

        String printed = out.toString();
        assertTrue(printed.contains("Successfully loaded") || printed.contains("Loading hotel configuration"));
    }
}
