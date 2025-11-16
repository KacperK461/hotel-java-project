package hotel.command;

import hotel.model.Guest;
import hotel.service.HotelService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CheckinCommand implements Command {

    private final HotelService service;

    public CheckinCommand(HotelService service) {
        this.service = service;
    }

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Room number: ");
        String roomInput = sc.nextLine().trim();
        if (roomInput.isEmpty()) {
            System.out.println("Error: Room number cannot be empty.");
            return;
        }

        Integer roomNumberObj = tryParseRoomNumber(roomInput);
        if (roomNumberObj == null) {
            System.out.println("Error: Invalid room number format. Please enter a valid number.");
            return;
        }
        int roomNumber = roomNumberObj;

        var room = service.getRoom(roomNumber);
        if (room == null) {
            System.out.println("Error: Room not found!");
            return;
        }
        if (!room.isFree()) {
            System.out.println("Error: Room is already occupied!");
            return;
        }

        List<Guest> guests = new ArrayList<>();
        System.out.print("Main guest name (First Last): ");
        String mainGuestInput = sc.nextLine().trim();
        if (mainGuestInput.isEmpty()) {
            System.out.println("Error: Main guest name cannot be empty.");
            return;
        }

        Guest mainGuest = tryParseGuest(mainGuestInput);
        if (mainGuest == null) {
            System.out.println("Error: Invalid guest name format. Please enter first and last name separated by space.");
            return;
        }
        guests.add(mainGuest);

        for (int i = 1; i < room.getCapacity(); i++) {
            System.out.print("Add additional guest? (y/n): ");
            String addGuest = sc.nextLine().trim();
            if (!addGuest.equalsIgnoreCase("y") && !addGuest.equalsIgnoreCase("yes")) break;

            System.out.print("Guest name (First Last): ");
            String guestInput = sc.nextLine().trim();
            if (guestInput.isEmpty()) {
                System.out.println("Warning: Empty guest name, skipping.");
                continue;
            }

            Guest extra = tryParseGuest(guestInput);
            if (extra == null) {
                System.out.println("Warning: Invalid guest name format, skipping this guest.");
                continue;
            }
            guests.add(extra);
        }

        System.out.print("Check-in date (YYYY-MM-DD, Enter for today): ");
        String checkinInput = sc.nextLine().trim();
        LocalDate checkinDate = tryParseDateOrToday(checkinInput);
        if (checkinDate == null) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD format.");
            return;
        }

        System.out.print("Planned checkout date (YYYY-MM-DD): ");
        String checkoutInput = sc.nextLine().trim();
        if (checkoutInput.isEmpty()) {
            System.out.println("Error: Checkout date is required.");
            return;
        }

        LocalDate plannedCheckout = tryParseDate(checkoutInput);
        if (plannedCheckout == null) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD format.");
            return;
        }

        if (plannedCheckout.isBefore(checkinDate)) {
            System.out.println("Error: Checkout date cannot be before check-in date!");
            return;
        }

        try {
            service.checkin(roomNumber, guests, checkinDate, plannedCheckout);
            System.out.println("Successfully checked in!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
        }
    }

    private Guest parseGuest(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty");
        }

        String[] parts = line.trim().split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Please provide both first and last name");
        }

        return new Guest(parts[0], parts[1]);
    }

    private Integer tryParseRoomNumber(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Guest tryParseGuest(String line) {
        try {
            return parseGuest(line);
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDate tryParseDateOrToday(String input) {
        if (input == null || input.isEmpty()) {
            return LocalDate.now();
        }
        return tryParseDate(input);
    }

    private LocalDate tryParseDate(String input) {
        try {
            return LocalDate.parse(input);
        } catch (Exception e) {
            return null;
        }
    }
}
