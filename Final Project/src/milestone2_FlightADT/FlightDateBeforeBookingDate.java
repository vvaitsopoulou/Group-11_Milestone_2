//Code by: I.Chatzivasileiadou
package milestone2_FlightADT;

@SuppressWarnings("serial")
public class FlightDateBeforeBookingDate extends Exception {
    
    public FlightDateBeforeBookingDate (String s) {
        super(s);
    }
}