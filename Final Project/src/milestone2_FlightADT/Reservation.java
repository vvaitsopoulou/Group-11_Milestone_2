//Code by: I.Chatzivasileiadou
package milestone2_FlightADT;

import java.time.LocalDate;

public class Reservation {
    private static int idCounter = 0; // Auto-incremented reservation ID
    private int id;            // Unique reservation ID
    private Flight flight;     // Associated flight
    private LocalDate bookingDate; // Date of the reservation
    private double finalPrice; // Final price after dynamic pricing adjustments

    // Constructor
    public Reservation(Flight flight, LocalDate bookingDate, double finalPrice) {
        this.id = ++idCounter;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.finalPrice = finalPrice;
    }

    // GETTER METHODS
    //Returns the unique reservation id 
    public int getId() {
        return id;
    }

    //Returns the flight associated to the reservation 
    public Flight getFlight() {
        return flight;
    }

    //Returns the booking date of the rervation 
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    //Returns the final price of the reservation 
    public double getFinalPrice() {
        return finalPrice;
    }

    // Overrides the toString method to provide a string representation of the reservation 
    @Override
    public String toString() {
        return "Reservation{" +
               "ID=" + id +
               ", FlightID=" + flight.getCode() +
               ", BookingDate=" + bookingDate +
               ", FinalPrice=" + finalPrice +
               '}';
    }
}
