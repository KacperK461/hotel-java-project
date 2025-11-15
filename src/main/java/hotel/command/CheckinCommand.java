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
        try {
            System.out.print("Room number: ");
            String roomInput = sc.nextLine().trim();
            if (roomInput.isEmpty()) {
                System.out.println("Error: Room number cannot be empty.");
                return;
            }

            int roomNumber;
            try {
                roomNumber = Integer.parseInt(roomInput);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid room number format. Please enter a valid number.");
                return;
            }

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

            try {
                guests.add(parseGuest(mainGuestInput));
            } catch (Exception e) {
                System.out.println("Error: Invalid guest name format. Please enter first and last name separated by space.");
                return;
            }

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

                try {
                    guests.add(parseGuest(guestInput));
                } catch (Exception e) {
                    System.out.println("Warning: Invalid guest name format, skipping this guest.");
                }
            }

            System.out.print("Check-in date (YYYY-MM-DD, Enter for today): ");
            String checkinInput = sc.nextLine().trim();
            LocalDate checkinDate;

            if (checkinInput.isEmpty()) {
                checkinDate = LocalDate.now();
            } else {
                try {
                    checkinDate = LocalDate.parse(checkinInput);
                } catch (Exception e) {
                    System.out.println("Error: Invalid date format. Please use YYYY-MM-DD format.");
                    return;
                }
            }

            System.out.print("Planned checkout date (YYYY-MM-DD): ");
            String checkoutInput = sc.nextLine().trim();
            if (checkoutInput.isEmpty()) {
                System.out.println("Error: Checkout date is required.");
                return;
            }

            LocalDate plannedCheckout;
            try {
                plannedCheckout = LocalDate.parse(checkoutInput);
            } catch (Exception e) {
                System.out.println("Error: Invalid date format. Please use YYYY-MM-DD format.");
                return;
            }

            if (plannedCheckout.isBefore(checkinDate)) {
                System.out.println("Error: Checkout date cannot be before check-in date!");
                return;
            }

            service.checkin(roomNumber, guests, checkinDate, plannedCheckout);
            System.out.println("Successfully checked in!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format.");
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
}
