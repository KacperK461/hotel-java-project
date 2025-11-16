package hotel.command;

import hotel.service.HotelService;

import java.time.LocalDate;
import java.util.Scanner;

public class CheckoutCommand implements Command {

    private final HotelService service;

    public CheckoutCommand(HotelService service) {
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

        try {
            double cost = service.checkout(roomNumberObj, LocalDate.now());
            System.out.printf("Amount to pay: %.2f PLN%n", cost);
            System.out.println("Successfully checked out!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
        }
    }

    private Integer tryParseRoomNumber(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
