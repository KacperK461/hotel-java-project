package hotel.command;

import hotel.service.HotelService;

public class ListCommand implements Command {

    private final HotelService service;

    public ListCommand(HotelService service) {
        this.service = service;
    }

    @Override
    public void execute() {
        try {
            System.out.println("All rooms:");
            service.printAllRooms();
        } catch (Exception e) {
            System.out.println("Error listing rooms: " + e.getMessage());
        }
    }
}
