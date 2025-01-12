// Code by: A. Bajgora B.Vinca and V.Vaitsopoulou
package milestone2_FlightADT;

import java.time.LocalTime;
import java.util.Map;
import java.util.HashMap;


public class Flight {
    private int code;		// Unique flight code for each route
    private Map<Airport, Airport[]> fromTo;
    public static final int TOTAL_SEATS = 300;		// Total number of seats 
    public int reservedSeats;		// Number of reserved seats
    private double baseSalesPrice;		// Base price for the flight
    private LocalTime depTime;		// Departure time
    private double businessStatistic;
//    private double duration;      // Flight duration in hours //remnant of an alternative Implementation: if we decided to implement base sales price as a y = ax + b (where a = fuel factor b = standard such as crew salaries)
    								//then we would use this; now we don't need it
    
    // Constructor // with parametric overloading
    public Flight(int code, Airport origin, Airport destination, double baseSalesPrice, double businessStatistic,
                  LocalTime depTime) throws IllegalArgumentException {
    	if(code <= 0) {
    		throw new IllegalArgumentException("The flight code may only be a positive integer");
    	}
    	
        this.code = code;
        
        if(origin == null || destination == null) {
        	throw new IllegalArgumentException("A direct flight may not be instantiated while origin or destination airport are null");
        }
        
        fromTo = new HashMap<>();
        Airport[] towards = new Airport[2]; 
        towards[0] = null;
        towards[1] = destination; // we use 0-based index
        fromTo.put(origin, towards);

// COMMENT: We also considered the approach with three different attributes instead of the fromTo HashMap: Airport origin; Airport layover; Airport destination;
//			The advantage of this approach would be slightly less overhead as it is more straightforward.
//    		However, the HashMap is better for scalability and future use cases to dynamically manage 
//          more complex relationships between origins and destinations.        

        if (baseSalesPrice <= 0) {
        	throw new IllegalArgumentException("The base sales price must be a positive number");
        }
        this.baseSalesPrice = baseSalesPrice;
        
        if(0 > businessStatistic || businessStatistic > 1) {
        	throw new IllegalArgumentException("The businessStatistic must be a number between 0 and 1");
        }
        this.businessStatistic = businessStatistic;
        if(depTime == null) {
        	throw new IllegalArgumentException("Invalid departure time");
        }
        this.depTime = depTime;
        this.reservedSeats = 0; // 5% of seats initially reserved will be reflected in the availability that shows up when requesting to book       
    }
    
 // Constructor // with parametric overloading
    public Flight(int code, Airport origin, Airport layover, Airport destination, double baseSalesPrice, double businessStatistic,
            LocalTime depTime) { 
    	if(code <= 0) {
    		throw new IllegalArgumentException("The flight code may only be a positive integer");
    	}
    	this.code = code; 
    	
    	if(origin == null || layover == null || destination == null) {
        	throw new IllegalArgumentException("An indirect flight may not be instantiated while origin, layover or destination airports are null");
        }
    	fromTo = new HashMap<>();
        Airport[] towards = new Airport[2]; 
        towards[0] = layover;
        towards[1] = destination; // we use 0-based index
        fromTo.put(origin, towards);
        
        if (baseSalesPrice <= 0) {
        	throw new IllegalArgumentException("The base sales price must be a positive number");
        }
        this.baseSalesPrice = baseSalesPrice;
        
        if(0 > businessStatistic || businessStatistic > 1) {
        	throw new IllegalArgumentException("The businessStatistic must be a number between 0 and 1");
        }
        this.businessStatistic = businessStatistic;
        if(depTime == null) {
        	throw new IllegalArgumentException("Invalid departure time");
        }
        this.depTime = depTime;
        this.reservedSeats = 0;// 5% of seats initially reserved will be reflected in the availability that shows up when requesting to book           
}


    // Getters
    public int getCode() {
        return code;
    }

    public Airport getOrigin() throws IllegalStateException {
    		
    	if (fromTo == null || fromTo.isEmpty()) {
    		throw new IllegalStateException("fromTo map is null or empty");
    	}    	    
    	return fromTo.keySet().iterator().next(); //code taken from https://stackoverflow.com/questions/26230225/hashmap-getting-first-key-value

    	
    }

    public Airport getLayover() throws IllegalStateException{
    	
    	if (fromTo == null || fromTo.isEmpty()) {
    		throw new IllegalStateException("fromTo map is null or empty");
    	}
    	
    	Airport[] towards = fromTo.get(getOrigin());
    	if(towards[0] != null) {
    		return towards[0];
    	}
        return null;
    }
    
    public Airport getDestination() throws IllegalStateException{
    	
    	if (fromTo == null || fromTo.isEmpty()) {
    		throw new IllegalStateException("fromTo map is null or empty");
    	}
    	
    	Airport[] towards = fromTo.get(getOrigin());
    	if(towards == null || towards[1] == null) {
    		throw new IllegalStateException("destination Airport has not been initialised");
    	}
        return towards[1];
    }

    public int getReservedSeats() {
        return reservedSeats;
    }

    public double getBaseSalesPrice() {
        return baseSalesPrice;
    }
    
    public double getBusinessStatistic() {
        return businessStatistic;
    }

    public LocalTime getDepTime() {
        return depTime;
    }

    public int getAvailableSeats() {
        return TOTAL_SEATS - reservedSeats;
    }

    // Methods for seat reservations 
    public boolean reserveSeat() throws FlightOverCapacityException{
    	if(reservedSeats >= TOTAL_SEATS) {
    		throw new FlightOverCapacityException("Flight over capacity");
    	}

            reservedSeats++;
            return true;

    }
    //adt
    public boolean cancelReservation() throws IllegalStateException {
    	if(reservedSeats <= 0) {
    		throw new IllegalStateException("Illegal number of reserved seats in this flight");
    	}
            reservedSeats--;
            return true;

    }

    @Override
    public String toString(){
        return "Flight{" +
               "Code=" + code +
               ", Origin=" + getOrigin().getName() +
               ", Layover=" + ((getLayover() == null)? "none": getLayover().getName()) +
               ", Destination=" + getDestination().getName() +
               ", TOTAL_SEATS=" + TOTAL_SEATS +
               ", ReservedSeats=" + reservedSeats +
               ", AvailableSeats=" + getAvailableSeats() +
               ", BaseSalesPrice=" + baseSalesPrice +
               ", BusinessStatistic=" + businessStatistic +
               ", DepTime=" + depTime +
               '}';
    }
}
