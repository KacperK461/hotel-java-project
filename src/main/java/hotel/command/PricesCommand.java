package hotel.command;

import hotel.model.Room;
import hotel.repository.MyMap;
import hotel.service.HotelService;

public class PricesCommand implements Command {

    private final HotelService service;

    public PricesCommand(HotelService service) {
        this.service = service;
    }

    @Override
    public void execute() {
        try {
            MyMap<Integer, Room> rooms = service.getRooms();
            if (rooms == null || rooms.keys().isEmpty()) {
                System.out.println("No rooms available.");
                return;
            }

            System.out.println("Room prices:");
            for(Integer key: rooms.keys()) {
                Room room = rooms.get(key);
                if (room != null) {
                    System.out.printf("Room %d: %.2f PLN/night%n", key, room.getPricePerNight());
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving room prices: " + e.getMessage());
        }
    }
}
