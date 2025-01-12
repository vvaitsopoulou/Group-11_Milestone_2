// Code by: A. Bajgora and B.Vinca
package milestone2_FlightADT;

public class Airport {
    private final int code; // Unique code identifying the airport
    private final String name; // Name of the airport
    private final String location; // Location of the airport

    //Constructor
    public Airport(int code, String name, String location) {
        this.code = code;
        this.name = name;
        this.location = location;
    }

    //GETTER METHODS
    // Returns the unique code of the airport
    public int getCode() {
        return code;
    }

    // Returns the name of the airport
    public String getName() {
        return name;
    }

    // Returns the location of the airport
    public String getLocation() {
        return location;
    }

    // Overrides the toString method to provide a string representation of the Airport object
    @Override
    public String toString() {
        return "Airport{" +
               "Code=" + code +
               ", Name='" + name + '\'' +
               ", Location='" + location + '\'' +
               '}';
    }
}
