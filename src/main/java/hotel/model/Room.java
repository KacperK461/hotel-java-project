package hotel.model;

public class Room {
    private final int number;
    private final String description;
    private final int capacity;
    private final double pricePerNight;

    private Booking booking;

    public Room(int number, String description, int capacity, double pricePerNight) {
        this.number = number;
        this.description = description;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
    }

    public boolean isFree() {
        return booking == null;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking b) {
        this.booking = b;
    }

    public int getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "#" + number + " (" + description + ") - " +
                pricePerNight + " PLN, max " + capacity + " guests";
    }
}
