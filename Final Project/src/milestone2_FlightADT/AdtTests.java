// Code by: A. Bajgora and B.Vinca
package milestone2_FlightADT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AdtTests {
    public static void main(String[] args) {
        // To start we initiate the Airport ADT 
        AirportsADT airportsADT = new AirportsADT();

        // Then we populate the Airport ADT with enough data to run the neccecary test cases        
        airportsADT.addAirport(new Airport(101, "LHR", "London"));
        airportsADT.addAirport(new Airport(102, "CDG", "Paris"));
        airportsADT.addAirport(new Airport(103, "ROM", "Rome"));

        // Next we initiate the FlightNetwork and create an empty weekly flights hashmap
        FlightNetwork flightNetwork = new FlightNetwork(airportsADT);
        flightNetwork.createEmptyWeeklyFlightsHashMap();

        // Next we add some flights
        flightNetwork.addRegularDirectFlight(
            301, airportsADT.findAirportByLocation("London"), airportsADT.findAirportByLocation("Paris"),
            150.0, 0.9, "Tuesday", LocalTime.of(9, 15)
        );
        flightNetwork.addRegularDirectFlight(
            302, airportsADT.findAirportByLocation("Paris"), airportsADT.findAirportByLocation("Rome"),
            200.0, 0.8, "Wednesday", LocalTime.of(14, 45)
        );

        // Next we initiate and populate the all flight hashmap
        flightNetwork.createEmptyAllFlightsHashMap();
        flightNetwork.populateAllFlightsHashMap();

        //From this point we divide test cases into edge test cases and "regual" test cases
        // --- Edge Case Tests ---
        System.out.println("\n--- Edge Case Tests ---");

        // Edge Case 1: Invalid flight parameters
          /* When trying to add a flight to the flight network we would expect the code to check the validity of the given
           flights' parameters (only allowing a positive flight id, not allowing the origin and destination airport to be set to null)
           In this specific case we try to add a flight with a negative flight ID to the flight network 
           The code then catches this input error, and throws the appropriate exception 
          */
        System.out.println("\nAdding flights with invalid parameters...");
        try {
            flightNetwork.addRegularDirectFlight(
                -1, airportsADT.findAirportByLocation("London"), airportsADT.findAirportByLocation("Paris"),
                150.0, 0.9, "Tuesday", LocalTime.of(9, 15) // Negative flight ID
            );
        } catch (IllegalArgumentException e) {
            System.err.println("Expected Error: " + e.getMessage());
        }

        // Edge Case 2: Search for flights on invalid dates
          /*
           * To ensure that the search operation of the flight network is running smoothly we intended to search for a flight that we knew 
           * did not exist (more particularly the flight date is an invalid one)
           * Our code catches this error and returns an appropriate response to the user, informing them that the flight that they 
           * searched for, for that specific date, is not in the flight network
          */
        System.out.println("\nSearching for flights on invalid dates...");
        try {
            flightNetwork.checkAvailableFlightsonDate("London", "Paris", LocalDate.of(2025, 2, 30)); // Invalid date
        } catch (Exception e) {
            System.err.println("Expected Error: " + e.getMessage());
        }

        // Edge Case 3: Case sensitivity in airport codes
          /*
           * Another test case for user input error that we wanted to test was that of case sensitivity in airport codes, 
           * more specifically if our code would catch inconsistencies in the airport code  
          */
        System.out.println("\nSearching for flights with case-insensitive airport codes...");
        try {
            List<Flight> flights = flightNetwork.checkAvailableFlightsonDate("lhr", "cdg", LocalDate.of(2025, 3, 4)); // Lowercase codes
            System.out.println("Flights found: " + flights);
        } catch (IllegalArgumentException e) {
            System.err.println("Expected Error: " + e.getMessage());
        }

        // --- Regular Tests ---
        System.out.println("\n\nRegular case tests");
        // --- Experiment: Search for Existing and Nonexistent Flights ---
        System.out.println("\n--- Experiment: Search for Existing and Nonexistent Flights ---");

        // Search for a direct flight between two airports that exists
        // Since one the main functionality of our system is that of searching for specific flights it makes sense
        // that we would conduct a test to see if our system can do exactly that
        System.out.println("\nSearching for a direct flight from London to Paris on 2025-03-04...");
        try {
            List<Flight> directFlights = flightNetwork.checkAvailableFlightsonDate("London", "Paris", LocalDate.of(2025, 3, 4));
            if (!directFlights.isEmpty()) {
                for (Flight flight : directFlights) {
                    double price = flightNetwork.findFinalPrice(flight, LocalDate.of(2025, 3, 4));
                    System.out.println(flight.toString() + " Final Price: $" + price);
                }
            } else {
                System.out.println("No direct flights found for the given route and date.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Test Case : Search for a nonexistent flight
        
        //Another test we saw fit to consuct was to search for a flight that we knew wasn't available 
        //In this case we're searching for a flight from London to Rome on 04.03.2025, which according to the data that we've
        //input should return an exception message
        System.out.println("\nSearching for a nonexistent flight from London to Rome on 2025-03-04...");
        try {
            List<Flight> nonexistentFlights = flightNetwork.checkAvailableFlightsonDate("London", "Rome", LocalDate.of(2025, 3, 4));
            if (nonexistentFlights.isEmpty()) {
                System.out.println("No flights found for the given route and date (as expected).");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Expected Error: " + e.getMessage());
        }


           // --- Next Experiment: Make and Cancel a Reservation ---
           System.out.println("\n--- Experiment: Make and Cancel a Reservation ---");
           try {
               // Retrieve the flight object directly
               Flight flight = flightNetwork.checkAvailableFlightsonDate("London", "Paris", LocalDate.of(2025, 3, 4)).get(0);
   
               // Reserve a seat
               System.out.println("\nMaking a reservation for Flight 301...");
               boolean reserved = flight.reserveSeat();
               if (reserved) {
                   System.out.println("Reservation made successfully!");
               } else {
                   System.out.println("Reservation failed.");
               }
   
               // Print seat details after reservation
               System.out.println("Reserved Seats: " + flight.getReservedSeats());
               System.out.println("Available Seats: " + flight.getAvailableSeats());
   
               // Cancel the reservation
               System.out.println("\nCanceling the reservation for Flight 301...");
               boolean canceled = flight.cancelReservation();
               if (canceled) {
                   System.out.println("Reservation canceled successfully!");
               } else {
                   System.out.println("Cancellation failed.");
               }
   
               // Print seat details after cancellation
               System.out.println("Reserved Seats: " + flight.getReservedSeats());
               System.out.println("Available Seats: " + flight.getAvailableSeats());
           } catch (Exception e) {
               System.err.println("Error during reservation operation: " + e.getMessage());
           }
        

          // --- Experiment: Attempt to Book a Fully Booked Flight ---
          System.out.println("\n--- Experiment: Attempt to Book a Fully Booked Flight ---");
          try {
              // To achieve this we 1st retrieve the flight object directly
              Flight flight = flightNetwork.checkAvailableFlightsonDate("London", "Paris", LocalDate.of(2025, 3, 4)).get(0);
  
              // Then we manually reserve all available seats on said flight through a for loop 
              for (int i = 0; i < Flight.TOTAL_SEATS; i++) {
                  flight.reserveSeat();
              }
              System.out.println("Flight 301 is now fully booked.");
  
              // Finally we attempt to reserve one more seat
              System.out.println("\nAttempting to book one more seat on a fully booked flight...");
              flight.reserveSeat(); // This should throw a FlightOverCapacityException
          } catch (FlightOverCapacityException e) {
              System.err.println("Expected Error: " + e.getMessage());
          } catch (Exception e) {
              System.err.println("Unexpected Error: " + e.getMessage());
          }   

          
        // --- Test Case: Comparing Flight Prices Based on Departure Time ---
        // Through this test we wanted to prove that the dynamic pricing algorithm works as expected, meaning that
        // the further the flight date is from today's date the price should change respectively  
        //(ie. earlier booking => lower price)
        System.out.println("\n--- Experiment: Comparing Flight Prices Based on Departure Time ---");

        // First we set a date for 1 week from now and one for 2 months from now
        LocalDate oneWeekFromNow = LocalDate.now().plusWeeks(1);
        LocalDate twoMonthsFromNow = LocalDate.now().plusMonths(2);
        
        // Then we ensure that the dates exist in allFlights map before adding specific flights
        ensureDateInAllFlightsMap(flightNetwork, oneWeekFromNow);
        ensureDateInAllFlightsMap(flightNetwork, twoMonthsFromNow);

        // Then we add a flight departing in one week
        flightNetwork.addSpecificDirectFlight(
            303, airportsADT.findAirportByLocation("London"), airportsADT.findAirportByLocation("Paris"),
            150.0, 0.9, oneWeekFromNow, LocalTime.of(10, 30)
        );
        // As well as a flight departing in two months
        flightNetwork.addSpecificDirectFlight(
            304, airportsADT.findAirportByLocation("London"), airportsADT.findAirportByLocation("Paris"),
            150.0, 0.9, twoMonthsFromNow, LocalTime.of(11, 45)
        );

        // Then we compare prices for the two flights
        try {
            List<Flight> flightsOneWeek = flightNetwork.checkAvailableFlightsonDate("London", "Paris", oneWeekFromNow);
            List<Flight> flightsTwoMonths = flightNetwork.checkAvailableFlightsonDate("London", "Paris", twoMonthsFromNow);

            if (!flightsOneWeek.isEmpty() && !flightsTwoMonths.isEmpty()) {
                Flight flightOneWeek = flightsOneWeek.get(0);
                Flight flightTwoMonths = flightsTwoMonths.get(0);

                double priceOneWeek = flightNetwork.findFinalPrice(flightOneWeek, oneWeekFromNow);
                double priceTwoMonths = flightNetwork.findFinalPrice(flightTwoMonths, twoMonthsFromNow);

                System.out.println("\nFlight 303 (One Week From Now):");
                System.out.println(flightOneWeek.toString() + " Price: $" + priceOneWeek);

                System.out.println("\nFlight 304 (Two Months From Now):");
                System.out.println(flightTwoMonths.toString() + " Price: $" + priceTwoMonths);
            } else {
                System.out.println("Could not find flights for comparison.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // A basic function of all ADTs is removing data from said ADTs, so it made sense
        //for us to conduct tests confirming the functionality of these operations for all of our ADTs
        
        // --- Experiment: Removing Airports ---
        System.out.println("\n--- Experiment: Removing Airports ---");
        System.out.println("\nBefore removal:");
        System.out.println("Airports: " + airportsADT.getAirports());

        System.out.println("\nRemoving 'CDG' (Paris)...");
        Airport airportToRemove = airportsADT.findAirportByCode(102);
        if (airportToRemove != null) {
            airportsADT.getAirports().remove(airportToRemove);
            System.out.println("Successfully removed 'CDG'.");
        } else {
            System.out.println("Airport 'CDG' not found.");
        }

        System.out.println("\nAfter removal:");
        System.out.println("Airports: " + airportsADT.getAirports());


        // --- Experiment: Removing Flights ---
        System.out.println("\n--- Experiment: Removing Flights from FlightNetwork ---");

        // Remove a specific flight on a specific date
        System.out.println("\nRemoving a specific flight on a specific date...");
        try {
            flightNetwork.cancelSpecificFlight(301, LocalDate.of(2025, 3, 4), LocalTime.of(9, 15));
            System.out.println("Successfully removed flight 301 on 2025-03-04.");
        } catch (Exception e) {
            System.err.println("Error removing specific flight: " + e.getMessage());
        }

        // Remove a flight permanently from the weekly schedule
        System.out.println("\nRemoving a flight permanently from the weekly schedule...");
        try {
            flightNetwork.cancelFlightPermanently(302, "Wednesday", LocalTime.of(14, 45));
            System.out.println("Successfully removed flight 302 from the weekly schedule.");
        } catch (Exception e) {
            System.err.println("Error removing flight permanently: " + e.getMessage());
        }

        // Remove all flights from the allFlights map
        System.out.println("\nRemoving all flights from allFlights...");
        try {
            flightNetwork.removeAllFlights();
            System.out.println("All flights removed successfully from allFlights.");
        } catch (Exception e) {
            System.err.println("Error removing all flights: " + e.getMessage());
        }

        // Remove all weekly flights
        System.out.println("\nRemoving all weekly flights...");
        try {
            flightNetwork.removeAllWeeklyFlights();
            System.out.println("All weekly flights removed successfully.");
        } catch (Exception e) {
            System.err.println("Error removing all weekly flights: " + e.getMessage());
        }

        // Final state of FlightNetwork
        System.out.println("\n--- Final State of FlightNetwork ---");
        System.out.println("Weekly Flights: " + flightNetwork.getWeeklyFlights());
        System.out.println("All Flights: " + flightNetwork.getAllFlights());
    }

    private static void ensureDateInAllFlightsMap(FlightNetwork flightNetwork, LocalDate date) {
        if (!flightNetwork.getAllFlights().containsKey(date)) {
            flightNetwork.getAllFlights().put(date, new ArrayList<>());
        }
    }
}
