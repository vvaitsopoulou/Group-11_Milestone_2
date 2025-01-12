// Code by: A. Bajgora and B.Vinca
package milestone2_FlightADT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AirportsADT {
    private List<Airport> airports;

    // Constructor
    public AirportsADT() {
        this.airports = new ArrayList<>();
    }

    // Add an airport
    public void addAirport(Airport airport) {
        airports.add(airport);
    }

    // Remove an airport by code
    public boolean removeAirport(int code) {
        return airports.removeIf(airport -> airport.getCode() == code);
    }

    // Get all airports
    public List<Airport> getAirports() {
        return airports;
    }

    // Find an airport by code
    public Airport findAirportByCode(int code) {
        return airports.stream()
                .filter(airport -> airport.getCode() == code)
                .findFirst()
                .orElse(null);
    }

    // Find an airport by location
    public Airport findAirportByLocation(String location) {
        return airports.stream()
                .filter(airport -> Objects.equals(airport.getLocation(), location))
                .findFirst()
                .orElse(null);
    }

    // Creates an empty Airport ArrayList
    public void createEmptyAirportList() {
        airports = new ArrayList<>();
    }

    // Determines whether the ArrayList is empty
    public boolean checkIfEmpty() {
        return airports.isEmpty();
    }

    // Determines if the Airport object exists in the ArrayList
    public boolean isAirportInList(int airportId) {
        return airports.stream().anyMatch(airport -> airport.getCode() == airportId);
    }

    // Removes an airport object from the ArrayList
    public boolean removeAirportById(int airportId) {
        return airports.removeIf(airport -> airport.getCode() == airportId);
    }

    // Removes all Airports from the ArrayList
    public void emptyArrayList() {
        airports.clear();
    }

    // Retrieves the Airport object corresponding to the airportId
    public Airport searchForAirportById(int airportId) {
        return airports.stream()
                .filter(airport -> airport.getCode() == airportId)
                .findFirst()
                .orElse(null);
    }

    // Returns the size of the Airport ArrayList
    public int determineAirportNumber() {
        return airports.size();
    }
}
