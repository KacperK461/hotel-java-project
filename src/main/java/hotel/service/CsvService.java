package hotel.service;

import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.repository.MyMap;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CsvService {

    private static final String CSV_SEPARATOR = ",";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MyMap<Integer, Room> loadFromCsv(String filePath) throws IOException {
        MyMap<Integer, Room> rooms = new MyMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue;
                }

                try {
                    Room room = parseCsvLine(line);
                    rooms.put(room.getNumber(), room);
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + e.getMessage());
                    System.err.println("Line content: " + line);
                }
            }
        }

        return rooms;
    }

    private Room parseCsvLine(String line) {
        String[] parts = line.split(CSV_SEPARATOR, -1);

        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid CSV format. Expected at least 4 columns: room_number,description,price,capacity");
        }

        try {
            int roomNumber = Integer.parseInt(parts[0].trim());
            String description = parts[1].trim();
            double price = Double.parseDouble(parts[2].trim().replace(",", "."));
            int capacity = Integer.parseInt(parts[3].trim());

            Room room = new Room(roomNumber, description, capacity, price);

            if (parts.length >= 7) {
                String guestNames = parts[4].trim();
                String checkinDateStr = parts[5].trim();
                String checkoutDateStr = parts[6].trim();

                if (!guestNames.isEmpty() && !checkinDateStr.isEmpty() && !checkoutDateStr.isEmpty()) {
                    try {
                        List<Guest> guests = parseGuests(guestNames);
                        LocalDate checkinDate = LocalDate.parse(checkinDateStr, DATE_FORMATTER);
                        LocalDate checkoutDate = LocalDate.parse(checkoutDateStr, DATE_FORMATTER);

                        room.setBooking(new Booking(guests, checkinDate, checkoutDate));
                    } catch (Exception e) {
                        System.err.println("Warning: Could not parse booking information for room " + roomNumber + ": " + e.getMessage());
                        System.err.println("  Guests: '" + guestNames + "', Check-in: '" + checkinDateStr + "', Check-out: '" + checkoutDateStr + "'");
                    }
                }
            }

            return room;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in CSV line: " + e.getMessage());
        }
    }

    private List<Guest> parseGuests(String guestNames) {
        List<Guest> guests = new ArrayList<>();
        String[] guestArray = guestNames.split(";");

        for (String guestName : guestArray) {
            guestName = guestName.trim();
            if (!guestName.isEmpty()) {
                String[] nameParts = guestName.split("\\s+", 2);
                if (nameParts.length >= 2) {
                    guests.add(new Guest(nameParts[0], nameParts[1]));
                } else if (nameParts.length == 1) {
                    guests.add(new Guest(nameParts[0], ""));
                }
            }
        }

        return guests;
    }

    public void saveToCsv(MyMap<Integer, Room> rooms, String filePath) throws IOException {
        if (rooms == null) {
            throw new IllegalArgumentException("Rooms map cannot be null");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("# Hotel configuration file");
            writer.newLine();
            writer.write("# Format: room_number,description,price,capacity,guest_names(semicolon separated),checkin_date,checkout_date");
            writer.newLine();

            for (Integer roomNumber : rooms.keys()) {
                Room room = rooms.get(roomNumber);
                if (room != null) {
                    String csvLine = roomToCsvLine(room);
                    writer.write(csvLine);
                    writer.newLine();
                }
            }
        }
    }

    private String roomToCsvLine(Room room) {
        StringBuilder sb = new StringBuilder();

        sb.append(room.getNumber()).append(CSV_SEPARATOR);
        sb.append(escapeCsv(room.getDescription())).append(CSV_SEPARATOR);
        sb.append(String.format(Locale.US, "%.2f", room.getPricePerNight())).append(CSV_SEPARATOR);
        sb.append(room.getCapacity()).append(CSV_SEPARATOR);

        Booking booking = room.getBooking();
        if (booking != null) {
            sb.append(guestsToString(booking.getGuests())).append(CSV_SEPARATOR);
            sb.append(booking.getCheckinDate().format(DATE_FORMATTER)).append(CSV_SEPARATOR);
            sb.append(booking.getPlannedCheckoutDate().format(DATE_FORMATTER));
        } else {
            sb.append(CSV_SEPARATOR).append(CSV_SEPARATOR);
        }

        return sb.toString();
    }

    private String guestsToString(List<Guest> guests) {
        if (guests == null || guests.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < guests.size(); i++) {
            if (i > 0) {
                sb.append(";");
            }
            sb.append(guests.get(i).toString());
        }

        return sb.toString();
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

