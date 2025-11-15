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

            double cost = service.checkout(roomNumber, LocalDate.now());
            System.out.printf("Amount to pay: %.2f PLN%n", cost);
            System.out.println("Successfully checked out!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
        }
    }
}
