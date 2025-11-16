package hotel.service;

import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.repository.MyMap;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HotelService {

    private final MyMap<Integer, Room> rooms;

    public HotelService(MyMap<Integer, Room> rooms) {
        this.rooms = rooms;
    }

    public Room getRoom(int number) {
        return rooms.get(number);
    }

    public MyMap<Integer, Room> getRooms() {
        return rooms;
    }

    public void checkin(int roomNumber,
                        List<Guest> guests,
                        LocalDate checkin,
                        LocalDate checkout) {

        Room room = rooms.get(roomNumber);
        if (room == null)
            throw new IllegalArgumentException("Room not found");

        if (!room.isFree())
            throw new IllegalStateException("Room is already occupied");

        if (guests.size() > room.getCapacity())
            throw new IllegalStateException("Too many guests for this room");

        room.setBooking(new Booking(guests, checkin, checkout));
    }

    public double checkout(int roomNumber, LocalDate checkoutDate) {
        Room room = rooms.get(roomNumber);

        if (room == null)
            throw new IllegalArgumentException("Room not found");

        Booking booking = room.getBooking();
        if (booking == null)
            throw new IllegalStateException("Room is already free");

        if (checkoutDate.isBefore(booking.getCheckinDate())) {
            throw new IllegalArgumentException("Checkout date cannot be before check-in date!");
        }

        long days = ChronoUnit.DAYS.between(
                booking.getCheckinDate(),
                checkoutDate
        );

        if (days <= 0) days = 1;

        double cost = days * room.getPricePerNight();

        room.setBooking(null);

        return cost;
    }

    public void printAllRooms() {
        for (Integer key : rooms.keys()) {
            Room r = rooms.get(key);
            System.out.println(r + (r.isFree() ? " - FREE" : " - OCCUPIED"));
        }
    }
}
