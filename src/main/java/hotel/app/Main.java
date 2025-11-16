package hotel.app;

import hotel.command.*;
import hotel.model.Room;
import hotel.repository.MyMap;
import hotel.service.CsvService;
import hotel.service.HotelService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MyMap<Integer, Room> rooms = new MyMap<>();
        CsvService csvService = new CsvService();

    String csvFilePath = args.length > 0 ? args[0] : "hotel_config.csv";
        try {
            System.out.println("Loading hotel configuration from: " + csvFilePath);
            rooms = csvService.loadFromCsv(csvFilePath);
            System.out.println("Successfully loaded " + rooms.keys().size() + " room(s) from CSV file.");
        } catch (Exception e) {
            System.out.println("Error loading CSV file: " + e.getMessage());
            System.out.println("Using default hardcoded configuration.");

            rooms.put(101, new Room(101, "Standard room", 2, 120));
            rooms.put(102, new Room(102, "Apartment", 4, 350));
            rooms.put(103, new Room(103, "Single room", 1, 80));
        }

        HotelService hotelService = new HotelService(rooms);

        CommandRegistry registry = new CommandRegistry();
        registry.register("prices", new PricesCommand(hotelService));
        registry.register("view", new ViewCommand(hotelService));
        registry.register("checkin", new CheckinCommand(hotelService));
        registry.register("checkout", new CheckoutCommand(hotelService));
        registry.register("list", new ListCommand(hotelService));
        registry.register("save", new SaveCsvCommand(hotelService, csvService));

        Scanner sc = new Scanner(System.in);
        System.out.println("Hotel Management System - Enter a command:");
        System.out.println("Available commands: prices, view, checkin, checkout, list, save, exit");

        boolean running = true;
        while (running) {
            try {
                System.out.print("> ");
                String cmd = sc.nextLine().trim().toLowerCase();
                if (cmd.equals("exit")) {
                    System.out.println("Shutting down the system...");
                    running = false;
                } else if (cmd.isEmpty()) {
                    System.out.println("Please enter a command. Available commands: prices, view, checkin, checkout, list, save, exit");
                } else {
                    Command command = registry.get(cmd);
                    if (command == null) {
                        System.out.println("Unknown command: '" + cmd + "'. Available commands: prices, view, checkin, checkout, list, save, exit");
                    } else {
                        command.execute();
                    }
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }
}
