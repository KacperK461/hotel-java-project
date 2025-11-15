package hotel.command;

import hotel.model.Booking;
import hotel.model.Room;
import hotel.service.HotelService;

import java.util.Scanner;

public class ViewCommand implements Command {

    private final HotelService service;

    public ViewCommand(HotelService service) {
        this.service = service;
    }

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter room number: ");
            String roomInput = sc.nextLine().trim();

            if (roomInput.isEmpty()) {
                System.out.println("Error: Room number cannot be empty.");
                return;
            }

            int num;
            try {
                num = Integer.parseInt(roomInput);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid room number format. Please enter a valid number.");
                return;
            }

            Room room = service.getRoom(num);
            if (room == null) {
                System.out.println("Error: Room not found.");
                return;
            }

            System.out.println(room);
            Booking b = room.getBooking();
            if (b != null) {
                System.out.println("Guests: " + b.getGuests());
                System.out.println("Check-in date: " + b.getCheckinDate());
                System.out.println("Planned checkout date: " + b.getPlannedCheckoutDate());
            } else {
                System.out.println("Room is currently free.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format.");
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
        }
    }
}
