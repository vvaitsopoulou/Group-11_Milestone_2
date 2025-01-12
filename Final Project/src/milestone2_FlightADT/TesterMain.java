//Code by:V.Vaitsopoulou and I.Chatzivasileiadou
package milestone2_FlightADT;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class TesterMain {
    private static Random r = new Random();
    private static final int AIRPORTS_NUMBER = 50;
    private static int N = 100000; //representing the number of weekly flights we would like to generate

    public static void main(String[] args) {

        AirportsADT myAirports = new AirportsADT();
        populateCodeNames((ArrayList<Airport>) myAirports.getAirports());
        FlightNetwork network = new FlightNetwork(myAirports);

        network.createEmptyWeeklyFlightsHashMap();

        Airport a1,a2;
        //generate 50 regular direct random flights
        for (int i = 0 ; i < N / 2 ; i++) {
            int []randomIndexes = generate2RandomInd();
            a1 = myAirports.getAirports().get(randomIndexes[0]);
            a2 = myAirports.getAirports().get(randomIndexes[1]);

            network.addRegularDirectFlight(
                    Integer.parseInt(a1.getCode() + "" + a2.getCode()),
                    a1,
                    a2,
                    r.nextInt(10,1000),
                    round2Digits(r.nextFloat()*0.99 + 0.01),
                    generateRandomDay(FlightNetwork.weekdays),
                    LocalTime.of(
                            r.nextInt(0,24),
                            r.nextInt(0,60),
                            r.nextInt(0,60)
                    )
            );

        }

        //generate 50 regular layover random flights
        Airport a3;
        for (int i = 0 ; i < N / 2 ; i++) {
            int []randomIndexes = generate3RandomInd();
            a1 = myAirports.getAirports().get(randomIndexes[0]);
            a2 = myAirports.getAirports().get(randomIndexes[1]);
            a3 = myAirports.getAirports().get(randomIndexes[2]);

            network.addRegularLayoverFlight(
                    Integer.parseInt(a1.getCode() + "" + a2.getCode()+ "" + a3.getCode()),
                    a1,
                    a2,
                    a3,
                    r.nextInt(10,1000),
                    round2Digits(r.nextFloat()*0.99 + 0.01),
                    generateRandomDay(FlightNetwork.weekdays),
                    LocalTime.of(
                            r.nextInt(0,24),
                            r.nextInt(0,60),
                            r.nextInt(0,60)
                    )
            );

        }
        network.createEmptyAllFlightsHashMap();
        network.populateAllFlightsHashMap();

        List<Flight> flightsFound = null;
        final int searches = 100; //instead of performing one test for each number of flights we perform 100 and calculate average time
        long totalTime = 0;
        float avgTime;
        for (int i = 0 ; i < searches ; i++) {
            long start = System.currentTimeMillis();
            try {
                flightsFound = network.checkAvailableFlightsonDate("Thessaloniki", "Budapest", LocalDate.of(2025, 3, 4)); //locations and dates picked randomly for testing

                long end = System.currentTimeMillis();
                totalTime += (end - start);
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        avgTime = (float)totalTime/searches;
        System.out.println("Average search Time in milli seconds: " + avgTime);
        
        if(flightsFound != null) {
        	for (Flight flight: flightsFound)
            {double finalPrice = network.findFinalPrice(flight, LocalDate.of(2025, 3, 4));
                System.out.println(flight.toString() + "final price: " + round2Digits(finalPrice));
            }
        }
        

    }

    private static int[] generate2RandomInd() { //generate two random indexes to pick airports from the list
        int MaxIndex = AIRPORTS_NUMBER;
        int index1 = r.nextInt(0,MaxIndex);
        int index2 = r.nextInt(0,MaxIndex);
        while (index2 == index1) //check if by chance the indexes are equal and keep generating random values for index 2 until they are different
            index2 = r.nextInt(0,MaxIndex);

        int [] indexes = {index1,index2};
        return indexes;
    }

    private static int[] generate3RandomInd() {
        int MaxIndex = AIRPORTS_NUMBER;
        int index1 = r.nextInt(0,MaxIndex);
        int index2 = r.nextInt(0,MaxIndex);
        while (index2 == index1)
            index2 = r.nextInt(0,MaxIndex);
        int index3 = r.nextInt(0,MaxIndex);
        while (index3 == index1 || index3 == index2)
            index3 = r.nextInt(0,MaxIndex);

        int [] indexes = {index1,index2, index3};
        return indexes;
    }

    private static void populateCodeNames(ArrayList<Airport> airports) {
        try {
            File f = new File("airportCodes.txt"); //the file we have stored the airport codes into 
            FileReader fr = new FileReader(f); 
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {

                String [] splittedLine = line.split(","); //the format of our text file has three strings separated by commas which will correspond to each airport attribute
                airports.add(new Airport(
                        Integer.parseInt(splittedLine[0]),
                        splittedLine[1],
                        splittedLine[2]));
                line = br.readLine();

            }

            fr.close();
            br.close();
        }
        catch (IOException e) {
            //asdad
        }
    }

    public static String generateRandomDay(String [] days) {
        return days[r.nextInt(0,7)];
    }

    //Round to 2 digits so that the double prices don't look unappealing
    public static double round2Digits(double num) {
        return ((int)(num * 100))/100.0;
    }

    public static void printAllWeeklyFlights(FlightNetwork network){
        for(String day : FlightNetwork.weekdays) {
            System.out.println(day);
            List<Flight> flightsList = network.getWeeklyFlights().get(day);
            if(flightsList != null && !flightsList.isEmpty())
	            for(Flight f : flightsList){
	                System.out.println(f.toString());
	            }
        }
    }

    public static void printAllFlights(FlightNetwork network){

        Set<LocalDate> sortedSet = new TreeSet<>(network.getAllFlights().keySet()); //we do this because we want the dates to appear in order

        for(LocalDate date : sortedSet) {
            System.out.println(date);
            List<Flight> flightsList = network.getAllFlights().get(date);
            for(Flight f : flightsList){
                System.out.println(f.toString());
            }
        }
    }


}

