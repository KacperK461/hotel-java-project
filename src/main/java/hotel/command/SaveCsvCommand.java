package hotel.command;

import hotel.service.CsvService;
import hotel.service.HotelService;

public class SaveCsvCommand implements Command {

    private final HotelService service;
    private final CsvService csvService;
    private final String filePath;

    public SaveCsvCommand(HotelService service, CsvService csvService) {
        this(service, csvService, "hotel_config.csv");
    }

    public SaveCsvCommand(HotelService service, CsvService csvService, String filePath) {
        this.service = service;
        this.csvService = csvService;
        this.filePath = filePath;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Saving hotel state to: " + filePath);
            csvService.saveToCsv(service.getRooms(), filePath);
            System.out.println("Successfully saved hotel state to CSV file: " + filePath);

        } catch (java.io.IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
        }
    }
}

