package hotel.model;

public class Guest {
    private final String firstName;
    private final String lastName;

    public Guest(String first, String last) {
        this.firstName = first;
        this.lastName = last;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
