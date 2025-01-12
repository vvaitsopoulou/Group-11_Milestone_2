//Code by: I.Chatzivasileiadou
package milestone2_FlightADT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationInterface {
    public void createEmptyReservationLinkedList();
    //Creates an empty Reservation LinkedList
    // Preconditions: None
    // Postconditions: An empty Reservation LinkedList is created

    public boolean checkIfEmpty();
    // Determines whether the Reservation LinkedList is empty
    // Preconditions: None
    // Postconditions: Returns Boolean - true if the LinkedList is empty,otherwise false

    public List<Flight> checkAvailableFlights(String originAirport, String destinationAirport, LocalDate date);
    //Retrieves a list of Flight objects based on their origin & destination airports, and departure date
    // Preconditions: date parameter is not null, origin and destination correspond to existing Airport objects
    // Postconditions: Returns all Flight objects whose origin, destination, and date values match the ones provided, 
    //where the number of reserved seats is less than the maximum capacity

    public void makeReservation(int flightId, LocalDate date, LocalTime time) throws FlightDateBeforeBookingDate,FlightOverCapacityException;
    //Appends a Reservation object at the end of the Reservation LinkedList
    // Preconditions: date and time parameters are not null, flightId corresponds to the Flight object the reservation is made for
    // Postconditions: Inserts Reservation Object into the LinkedList, creating a unique reservationId by incrementing, increments the number of
    //reserved seats in the Flight object that corresponds to the flightId

    public boolean cancelReservation(int reservationId);
    //Removes a Reservation object from the Reservation LinkedList based on its Id
    // Preconditions: reservationId is a unique integer that is positive by convention and corresponds to an existing Reservation object
    // Postconditions: Removes the Reservation this id corresponds to from the Reservations LinkedList, decrements the number of reserved 
    //seats in the Flight object the reservation was made for

    public Reservation getReservation(int reservationId);
    //Retrieves a Reservation object from the Reservation LinkedList based on its Id
    // Preconditions: reservationId is a unique integer that is positive by convention and corresponds to an existing Reservation object
    // Postconditions: Returns the Reservation object corresponding to the reservationId if it exists in the LinkedList, otherwise return null

    public void emptyReservationLinkedList();
    //Removes all objects from the Reservation LinkedList
    // Preconditions: LinkedList is created and not empty
    // Postconditions: Removes all objects from the LinkedList

    public int determineReservationNumber();
    //Retrieves the number of Reservation objects in the Reservation LinkedList
    // Preconditions: LinkedList is created and not empty
    // Postconditions: Returns an integer number that is equivalent to the number of reservations on the LinkedList

}