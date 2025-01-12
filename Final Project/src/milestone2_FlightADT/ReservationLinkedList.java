//Code by: I.Chatzivasileiadou
package milestone2_FlightADT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import java.util.ArrayList;

public class ReservationLinkedList implements ReservationInterface{
    
    private ResNode head;
	private ResNode tail;
    int numberOfItems;
    private FlightNetwork flightNetwork;

    public ReservationLinkedList() {
        head = tail = null;
		numberOfItems = 0;
    }

    public void createEmptyReservationLinkedList(){
        ReservationLinkedList reservations = new ReservationLinkedList();
    }
    
    public boolean checkIfEmpty() { 
        return (numberOfItems == 0);
    }

    public List<Flight> checkAvailableFlights(String originAirport, String destinationAirport, LocalDate date) {
        List<Flight> availableFlights = new ArrayList<Flight>();
        availableFlights = flightNetwork.checkAvailableFlightsonDate(originAirport, destinationAirport, date);
        return availableFlights;
    }

    private boolean addReservation(int index, int flightId, LocalDate date, LocalTime time) throws FlightDateBeforeBookingDate,ListIndexOutOfBoundsException,FlightOverCapacityException{
        Flight flight = flightNetwork.getSpecificFlightById(flightId, date, time);
        LocalDate bookingDate = LocalDate.now();
        int dateDifference = date.compareTo(bookingDate);

        if (dateDifference < 0) {

            throw new FlightDateBeforeBookingDate("The requested date preceds the current (booking) date.");

        } else if(dateDifference <= 2) {

            if (flight.getAvailableSeats()<Flight.TOTAL_SEATS) {

                double finalPrice = flightNetwork.findFinalPrice(flight, date);
                Reservation newReservation = new Reservation(flight, bookingDate, finalPrice);
    
                if (index >= 1 && index <= numberOfItems+1) {
                    if ( index == 1 ) {
                        ResNode newNode = new ResNode(newReservation, head);
                        head = newNode;
    
                        if (tail==null) {
                            tail = head;
                        }
                    } else if ( index==numberOfItems+1 ) {
                        ResNode newNode = new ResNode(newReservation);
                        tail.setNext(newNode);
                        tail = newNode;
                    } else {
                        ResNode prev = find(index-1);
                        ResNode newNode = new ResNode(newReservation, prev.getNext());
                        prev.setNext(newNode);
                    }
                    numberOfItems++;
                    flight.reserveSeat();
                    return true;
                } else {
                    throw new ListIndexOutOfBoundsException("List index out of bounds exception on addReservation.");
                }

            } else {
                return false;
            }

        } else {
            if((flight.getAvailableSeats() - (Flight.TOTAL_SEATS*5)/100)<Flight.TOTAL_SEATS){
                double finalPrice = flightNetwork.findFinalPrice(flight, date);
                Reservation newReservation = new Reservation(flight, bookingDate, finalPrice);

                if (index >= 1 && index <= numberOfItems+1) {
                    if ( index == 1 ) {
                        ResNode newNode = new ResNode(newReservation, head);
                        head = newNode;

                        if (tail==null) {
                            tail = head;
                        }
                    } else if ( index==numberOfItems+1 ) {
                        ResNode newNode = new ResNode(newReservation);
                        tail.setNext(newNode);
                        tail = newNode;
                    } else {
                        ResNode prev = find(index-1);
                        ResNode newNode = new ResNode(newReservation, prev.getNext());
                        prev.setNext(newNode);
                    }
                    numberOfItems++;
                    flight.reserveSeat();
                    return true;
                } else {
                    throw new ListIndexOutOfBoundsException("List index out of bounds exception on addReservation.");
                }
            } else {
                return false;
            }
        }
	}

    public void makeReservation(int flightId, LocalDate date, LocalTime time) throws FlightDateBeforeBookingDate, FlightOverCapacityException {
        this.addReservation(numberOfItems+1, flightId, date, time);
    }

    private ResNode find(int index) {
		ResNode curr = head;
		for (int skip = 1; skip < index; skip++)
			curr = curr.getNext();
		return curr;
	}

    public boolean cancelReservation(int reservationId) {
        Reservation reservation = getReservation(reservationId);
        Flight flight = reservation.getFlight();

        if (reservationId >= 1 && reservationId <= numberOfItems){
            if (reservationId == 1) {
                head = head.getNext();
				if (head == null) {
                    tail = null;
                }	
            } else {
                ResNode prev = find(reservationId-1);
                ResNode curr = prev.getNext();
                prev.setNext(curr.getNext());

                if (reservationId == numberOfItems) {
                    tail = prev;
                }
            }

            numberOfItems--;
            flight.cancelReservation();
            return true;
        } else {
            return false;
        }
    }

    public Reservation getReservation(int reservationId) {
        ResNode curr = head;

        while(curr != null){
            if (reservationId == curr.getReservationItem().getId()) {
                return curr.getReservationItem();
            } 
                    
            curr = curr.getNext();
                
        }
        return null; 
    }

    public void emptyReservationLinkedList() {
        head = tail = null;
		numberOfItems = 0;
    }

    public int determineReservationNumber() {
        return numberOfItems;
    }
}