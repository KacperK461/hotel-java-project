package hotel.model;

import java.time.LocalDate;
import java.util.List;

public class Booking {

    private final List<Guest> guests;
    private final LocalDate checkinDate;
    private final LocalDate plannedCheckoutDate;

    public Booking(List<Guest> guests, LocalDate checkin, LocalDate checkout) {
        this.guests = guests;
        this.checkinDate = checkin;
        this.plannedCheckoutDate = checkout;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public LocalDate getPlannedCheckoutDate() {
        return plannedCheckoutDate;
    }
}
