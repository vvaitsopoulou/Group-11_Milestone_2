// Code by: V.Vaitsopoulou
package milestone2_FlightADT;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class FlightNetwork implements FlightNetworkInterface {
	
	public static String[] weekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	private Map<String, ArrayList<Flight>> weeklyFlights; // the regular flights for each day of the week will be stored here
	private Map<LocalDate, ArrayList<Flight>> allFlights; // will be populated for every day in a depth of 6 months based on data drawn from weeklyFlights
	private AirportsADT airportsADT;
	
	public Map<String, ArrayList<Flight>> getWeeklyFlights() {
		return weeklyFlights;
	}

	public Map<LocalDate, ArrayList<Flight>> getAllFlights() {
		return allFlights;
	}

	public AirportsADT getAirportsADT() {
		return airportsADT;
	}

	public FlightNetwork(AirportsADT airportsADT) throws IllegalArgumentException{
		if(airportsADT == null) {
			throw new IllegalArgumentException("AirportsADT cannot be null");
		}
		if(airportsADT.getAirports().isEmpty()) {
			throw new IllegalArgumentException("AirportsADT cannot be empty");
		}
		this.airportsADT = airportsADT;
		weeklyFlights = null;
		allFlights = null;
	}

	/* Creates an empty WeeklyFlights HashMap 
	   Preconditions: None 
	   Postconditions: An empty Weekly Flights HashMap is created
	*/
	public void createEmptyWeeklyFlightsHashMap(){
		weeklyFlights = new HashMap<String, ArrayList<Flight>>();
	}		
	
	/* Creates an empty AllFlights HashMap
	   Preconditions: None
	   Postconditions: An empty All Flights HashMap is created
	*/
	public void createEmptyAllFlightsHashMap() {
		allFlights = new HashMap<LocalDate, ArrayList<Flight>>();
	}
	
	/* Populates the AllFlights HashMap from the current date up to six months in the future by mapping each date to a copy of the ArrayList of weekly flights which corresponds to that day of the week
	   Preconditions: weeklyFlights HashMap has been created, allFlights HashMap has been created.
	   PostConditions: the AllFlights HashMap is populated with keys representing calendar dates, each mapping to an ArrayList of flights conducted on that date.
	   Each Arraylist is sorted such that all direct flights are placed before layover flights
	 */
	public void populateAllFlightsHashMap() throws IllegalStateException {
		
		if(weeklyFlights == null) {
			throw new IllegalStateException("weeklyFlights map has not been created");
		}
		
		if(allFlights == null) {
			throw new IllegalStateException("allFlights map has not been created");
		}
		LocalDate currDate = LocalDate.now();
		LocalDate endDate = currDate.plusMonths(6);
		
		for (LocalDate date = currDate; date.isBefore(endDate); date = date.plusDays(1)) // code adapted from here https://stackoverflow.com/questions/4534924/how-to-iterate-through-range-of-dates-in-java
		{
			DayOfWeek currDay = date.getDayOfWeek(); //code adapted from here https://www.tutorialspoint.com/java-program-to-get-day-of-week-as-string
		    String weekName = currDay.name();
		    
		    //capitalize only first letter to match the way we have written the days in weekdays array
		    String weekDay = weekName.substring(0, 1).toUpperCase() + weekName.substring(1).toLowerCase();
		    
		    if(weeklyFlights.get(weekDay) == null) { //ie there are no flights that day == no arraylist mapped to that key in weekly flights
		    	continue;
		    }
		    allFlights.put(date, new ArrayList<>(weeklyFlights.get(weekDay))); //We copy it here so that changes in allFlights dont affect weeklyFlights(which it would if they pointed to the same reference)
		}
		for (LocalDate key : allFlights.keySet())
			allFlights.get(key).sort((f1, f2) -> Boolean.compare(f1.getLayover() != null, f2.getLayover() != null));
		//code adapted from here: https://stackoverflow.com/questions/12542185/sort-a-java-collection-object-based-on-one-field-in-it

	}

	
	/*  Determines if the HashMap weeklyFlights is empty
	  	Preconditions: The HashMap is created
	  	Postconditions: Returns Boolean - true if the HashMap is empty, otherwise false
	*/
	public boolean checkIfWeeklyFlightsHashMapEmpty() throws IllegalStateException{
		if (weeklyFlights == null) {
	        throw new IllegalStateException("weeklyFlights HashMap has not been created.");
	    }
		return weeklyFlights.isEmpty();
	}
	
	/* Determines if the allFlights HashMap is empty.
	   Preconditions: The allFlights HashMap is created.
	   Postconditions: Returns Boolean - true if the allFlights HashMap is
	   empty, otherwise false
	*/
	public boolean checkIfAllFlightsHashMapEmpty() throws IllegalStateException {
		if (allFlights == null) {
	        throw new IllegalStateException("allFlights HashMap has not been created.");
	    }
		return allFlights.isEmpty();
	}
	
	
	/*Adds the Flight object to the WeeklyFlights Hashmap
	  Preconditions: Time parameter is not null, weekDay parameter is not
	  null and corresponds to one of the seven unique keys for each day of the
	  week, origin and destination are existing Airport objects, the number of
	  total seats is by assumption the same for all flights with maximum capacity arbitrarily set at 300, on flight creation all seats are considered
	  available except for a 5% which based on the pricing algorithm assumptions will be protected seats, business statistic is a double number
	  such that 0 < business statistic < 1
	  Postconditions: creates and adds the Flight object as a weekly flight
	  within the weeklyFlights HashMap to the end of the ArrayList which
	  corresponds to the weekDay provided. The layover airport points to null.
	*/

	public void addRegularDirectFlight(int flightId, Airport origin, Airport destination, double baseSalesPrice, double businessStatistic, String weekDay, LocalTime depTime) throws IllegalArgumentException {
		
		if(!airportsADT.getAirports().contains(origin) || !airportsADT.getAirports().contains(destination)) {
			throw new IllegalArgumentException("origin and destination airports must be existing airports in the airport adt");
		}
		Flight regularFlight = new Flight(flightId, origin, destination, baseSalesPrice, businessStatistic, depTime); //exceptions in the constructor
		
		if(weekDay == null || !Arrays.asList(weekdays).contains(weekDay)) {
			throw new IllegalArgumentException("The weekday provided is not valid");
		} else if(!weeklyFlights.containsKey(weekDay)) { //if no flights have been created for that day before create and add the key along with a new ArrayList
			weeklyFlights.put(weekDay, new ArrayList<Flight>()); 
		}
		(weeklyFlights.get(weekDay)).add(regularFlight);
	}
	
	//COMMENT: it is a business assumption that inserting additional unique regular flight routes is not something that happens fairly often during the operation of the 
	//flight network and after the initial airline network has been created. It will only happen en masse during the creation of the system. There was an initial thought 
	//to call repopulate allFlights hashmap method every time a new regular direct flight is added to the weeklyFlights Hashmap to
	//simulate a real life scenario when a new route is added to the model between two previously unconnected airports after the system has been initialised, and the
	// flights occuring from the current day forward should be updated. However,
	//because in our model the addition of flights both at the beginning and in this scenario happen with the same method, modifying the above
	//method would add computational overhead at the start, thus the thought was discarded. In the current model this operation mainly serves the purpose of
	//'refreshing' the alllfights hashmap every few months based on our model, so that flights conducted on past dates are no longer stored
	
	/* Removes all flights in the current allFlights HashMap and populates it by mapping each date from the current date up to six months in the future to a copy of the ArrayList of weekly flights which corresponds to that day of the week
	   Preconditions: weeklyFlights HashMap has been created, allFlights HashMap has been created and not empty.
	   PostConditions: the AllFlights HashMap is cleared then populated with keys representing calendar dates from the current date up to six months in the future, each mapping to an ArrayList of flights conducted on that date 
	*/
	public void repopulateAllFlightsHashMap() throws IllegalStateException{
		if(allFlights == null) {
			throw new IllegalStateException("allFlights map has not been created");
		}
		removeAllFlights();
		populateAllFlightsHashMap();
	}
	

	/*Adds the Flight object to the WeeklyFlights Hashmap
      Preconditions: Time parameter is not null, weekDay parameter is not
      null and corresponds to one of the seven unique keys for each day of the
      week, origin layover and destination are existing Airport objects, the number of total seats is by assumption the same for all flights with maximum
	  capacity arbitrarily set at 300, on flight creation all seats are considered
	  available except for a 5% which based on the pricing algorithm assumptions will be protected seats, business statistic is a double number
	  such that 0 < business statistic < 1
	  Postconditions: adds the Flight object as a weekly flight within the
	  weeklyFlights HashMap to the end of the ArrayList which corresponds to
	  the weekDay provided. 
	 */
	public void addRegularLayoverFlight( int flightId, Airport origin, Airport layover, Airport destination, double baseSalesPrice, double businessStatistic,  String weekDay, LocalTime depTime) throws IllegalArgumentException {
		
		if(!airportsADT.getAirports().contains(origin) || !airportsADT.getAirports().contains(destination) || !airportsADT.getAirports().contains(layover)) {
			throw new IllegalArgumentException("origin and destination airports must be existing airports in the airport adt");
		}
		
		Flight regularFlight = new Flight(flightId, origin, layover, destination, baseSalesPrice, businessStatistic, depTime);
		
		if(weekDay == null || !Arrays.asList(weekdays).contains(weekDay)) {
			throw new IllegalArgumentException("The weekday provided is not valid");
		} else if(!weeklyFlights.containsKey(weekDay)) {
			weeklyFlights.put(weekDay, new ArrayList<Flight>());
		}
		(weeklyFlights.get(weekDay)).add(regularFlight);
	}
		
		
		

	/* Adds a direct Flight object to the AllFlights HashMap
	   Preconditions: date and time parameters are not null, origin and destination are existing Airport objects, 
	   the number of total seats is by assumption the same for all flights with maximum capacity arbitrarily set at
	   300, on flight creation all seats are considered available with the exception
	   of a 5% which based on the pricing algorithm assumptions will be protected seats, business statistic is a double number
       such that 0 < business statistic < 1
	   Postconditions:Adds the Flight object to the All Flights HashMap as
	   a singular flight on the Arraylist corresponding to the specific date, sorting the List such that the new direct flight is placed before all layover flights. The
	   layover airport points to null.
	*/
	public void	addSpecificDirectFlight( int flightId, Airport origin, Airport destination, double baseSalesPrice, double businessStatistic, LocalDate depDate, LocalTime depTime) throws IllegalArgumentException, IllegalStateException {
		
		if(!airportsADT.getAirports().contains(origin) || !airportsADT.getAirports().contains(destination)) {
			throw new IllegalArgumentException("origin and destination airports must be existing airports in the airport adt");
		}
		
		Flight specificFlight = new Flight(flightId, origin, destination, baseSalesPrice, businessStatistic, depTime);
		
		if(depDate == null) {
			throw new IllegalArgumentException("The date provided is not valid");
		}
		if(allFlights.isEmpty() || allFlights == null) {
			throw new IllegalStateException("allFlights hashmap has not been created or initialized");
		}
		(allFlights.get(depDate)).add(specificFlight);
		(allFlights.get(depDate)).sort((f1, f2) -> Boolean.compare(f1.getLayover() != null, f2.getLayover() != null)); //since the flight is direct we need to make sure it will be sorted before the layover flights in the arraylist corresponding to that date
	}
	
	/* Adds a layover Flight object to the AllFlights HashMap
	   Preconditions: date and time parameters are not null, origin layover and destination are existing Airport objects, 
	   the number of total seats is by assumption the same for all flights with maximum capacity arbitrarily set at
	   300, on flight creation all seats are considered available with the exception
	   of a 5% which based on the pricing algorithm assumptions will be protected seats, business statistic is a double number
       such that 0 < business statistic < 1
	   Postconditions:Adds the Flight object to the All Flights HashMap as
	   a singular flight on the Arraylist corresponding to the specific date, at the end of the list. 
	*/
	public void	addSpecificLayoverFlight( int flightId, Airport origin, Airport layover, Airport destination, double baseSalesPrice, double businessStatistic, LocalDate depDate, LocalTime depTime) throws IllegalArgumentException, IllegalStateException {
		
		if(!airportsADT.getAirports().contains(origin) || !airportsADT.getAirports().contains(destination) || !airportsADT.getAirports().contains(layover)) {
			throw new IllegalArgumentException("origin and destination airports must be existing airports in the airport adt");
		}
		
		Flight specificFlight = new Flight(flightId, origin, layover, destination, baseSalesPrice, businessStatistic, depTime);
		
		if(depDate == null) {
			throw new IllegalArgumentException("The date provided is not valid");
		}
		if(allFlights.isEmpty() || allFlights == null) {
			throw new IllegalStateException("allFlights hashmap has not been created or initialized");
		}
		(allFlights.get(depDate)).add(specificFlight);
	}

	//COMMENT: with the current implementation we consider that if the company wants to 
	//cancel a flight permanently then they should first perform this operation and then repopulate the allflights hashmap
	
	/*Removes a Flight object from the WeeklyFlights HashMap
	  Preconditions: flightId is a unique for each route integer that is positive
	  by convention and corresponds to an existing flight object, weekDay parameter is not null and corresponds to one of the seven HashMap unique
	  keys for each day of the week, time is not null and corresponds to the
	  existing flight object satisfying the rest of the parameters
	  Postconditions: removes the Flight object connecting the specified locations on the specified weekday and time from the Weekly Flights ArrayList
	  which corresponds to the weekDay key entirely. Returns true if successful,
	  false otherwise.
	*/
	public boolean cancelFlightPermanently(int flightId, String weekDay, LocalTime time) throws IllegalArgumentException {
		
		Flight requestedFlight = getWeeklyFlightById(flightId, weekDay, time);
		if(requestedFlight == null) {
			throw new IllegalArgumentException("No such flight was found");
		}
		return (weeklyFlights.get(weekDay)).remove(requestedFlight);				
	}

	
	/* Retrieves a flight object from the weeklyFlights HashMap based on its'
		flight Id time and day of operation
		// Preconditions: flightId is a unique for each route integer that is positive
		by convention and corresponds to an existing flight object 
		weekDay parameter is not null and corresponds to one of the seven HashMap unique
		keys for each day of the week time is not null and corresponds to the
		existing flight object satisfying the rest of the parameters		 
		// Postconditions: Returns the Flight with the specific flight Id for
		the specified date and time if it exists in the HashMap, otherwise return null
	*/	

	public Flight getWeeklyFlightById(int flightId, String weekDay, LocalTime time) throws IllegalArgumentException, IllegalStateException {
		
		if(weeklyFlights == null) {
			throw new IllegalStateException("weeklyFlights has not been initialised");
		}
		if (flightId <= 0) {
			throw new IllegalArgumentException("invalid flightId argument");
		}
		
		if(time == null) {
			throw new IllegalArgumentException("invalid time argument");
		}
		
		if(weekDay == null | !weeklyFlights.containsKey(weekDay)) {
			throw new IllegalArgumentException("The weekday provided is not valid");
		}
		
		
		Flight requestedFlight = null;
		for (Flight f: weeklyFlights.get(weekDay)) {
			if(f.getCode() == flightId && f.getDepTime().equals(time)) {
				requestedFlight = f;
			}				
		}
		return requestedFlight;
		
	}

	
	
	/*Removes a Flight object from the AllFlights HashMap based on its’
	  flightId, date and time
	  Preconditions: flightId is a unique for each route integer that is positive
	  by convention and corresponds to an existing Flight object, date and time
	  are not null and correspond to an existing Flight object connecting origin
	  and destination as defined by the id
	  Postconditions: Removes the flight between origin and destination for
	  the specified date and time from the All Flights HashMap. Returns true
	  if successful, false otherwise.
	*/
	public boolean cancelSpecificFlight(int flightId, LocalDate date, LocalTime time) throws IllegalArgumentException{			

		Flight requestedFlight = getSpecificFlightById(flightId, date, time);
		
		if(requestedFlight == null) {
			throw new IllegalArgumentException("no such flight was found");
		}
		return (allFlights.get(date)).remove(requestedFlight);				
	}
	
	 /* Retrieves a flight object from the allFlights HashMap based on its’
		flight Id time and date of operation
		Preconditions: flightId is a unique for each route integer that is positive
		by convention and corresponds to an existing flight object 
		Date parameter is not null and corresponds to one of the HashMap unique
		keys time is not null and corresponds to the
		existing flight object satisfying the rest of the parameters		 
		Postconditions: Returns the Flight with the specific flight Id for
		the specified date and time if it exists in the HashMap, otherwise return null
	  */

	public Flight getSpecificFlightById(int flightId, LocalDate date, LocalTime time) throws IllegalArgumentException {
		
		if(allFlights == null) {
			throw new IllegalStateException("allFlights has not been initialised");
		}
		
		if (flightId <= 0) {
			throw new IllegalArgumentException("invalid flightId argument");
		}
		
		if(time == null) {
			throw new IllegalArgumentException("invalid time argument");
		}
		
		if(date == null | !allFlights.containsKey(date)) {
			throw new IllegalArgumentException("The date provided is not valid");
		}
		
		Flight requestedFlight = null;
		for (Flight f: allFlights.get(date)) {
			if(f.getCode() == flightId && f.getDepTime().equals(time)) { //it is a business assumption that we may not have two flights for the exact same route(same code) departing at the exact same time
				requestedFlight = f;
			}				
		}
		return requestedFlight;
		
	}
	
	
	
	/*
	  Removes all Flight objects from the AllFlights HashMap
	  Preconditions: The HashMap is created and not empty
	  Postconditions: Removes all Flight objects from the HashMap
	*/
	public void removeAllFlights(){
		if(!checkIfAllFlightsHashMapEmpty()) { //this operation already ensures the hashmap exists and is not null
			allFlights.clear();
		}
	}


	/*
	  Removes all flights from the WeeklyFlights Hashmap
	  Preconditions: The Hashmap is created and not empty
	  Postconditions: Removes all weekly Flight objects from the Hashmap
	*/
	public void removeAllWeeklyFlights(){
		if(!checkIfWeeklyFlightsHashMapEmpty()) { //this operation already ensures the hashmap exists and is not null
			weeklyFlights.clear();
		}
	}
	

	/*Retrieves a List of flight objects from the AllFlights HashMap based on their
	  origin & destination airports, and departure date
	  Preconditions: origin and destination correspond to existing Airport
	  objects, date is not null and corresponds to at least one existing Flight object connecting origin and destination
	  Postconditions: Returns the Flights List between origin and destination for
	  the specified date if they exist in the HashMap (if no flights satisfy these conditions the list is empty)
	*/
	public List<Flight> findFlightsOnSpecificDate(String originAirportLocation, String destinationAirportLocation, LocalDate date) throws IllegalArgumentException{
		if(date == null) {
			throw new IllegalArgumentException("The date provided is not valid");
		}
		
		Airport originAirport = airportsADT.findAirportByLocation(originAirportLocation);
		Airport destinationAirport = airportsADT.findAirportByLocation(destinationAirportLocation);
		
		if(originAirport == null || destinationAirport == null) {
			throw new IllegalArgumentException("invalid airport location parameter");
		}
		
		if(!airportsADT.getAirports().contains(originAirport) || !airportsADT.getAirports().contains(destinationAirport)) {
			throw new IllegalArgumentException("origin and destination airports must be existing airports in the airport adt");
		}
		
		if(allFlights == null) {
			throw new IllegalStateException("allFlights has not been initialised");
		}
		
		List<Flight> dailyFlights = new ArrayList<>();
		dailyFlights = allFlights.get(date);
		
		if(dailyFlights == null || dailyFlights.isEmpty()) {
			throw new IllegalArgumentException("no flights are conducted on that date");
		}
		
		List<Flight> flightsFound = new ArrayList<>();
		
		for(Flight flight : dailyFlights) {
			if(flight.getOrigin() == originAirport && flight.getDestination() == destinationAirport) {
				flightsFound.add(flight);
			}
		}
		//not a good idea to sort in a search - sorting transferred to populate hashmap method
		//flightsFound.sort((f1, f2) -> Boolean.compare(f1.getLayover() != null, f2.getLayover() != null)); //code adapted from here: https://stackoverflow.com/questions/12542185/sort-a-java-collection-object-based-on-one-field-in-it
		//and here: https://stackoverflow.com/questions/28002342/sort-an-arraylist-by-primitive-boolean-type
		
		return flightsFound;
		
	}
	
	/*
	Returns the number of flights in the AllFlights HashMap
	Preconditions: HashMap is created and not empty
	Postconditions: Returns an integer number that is equivalent to the
	number of Flights in the HashMap
    */
	public int determineTotalFlightsNumber(){
		
		if(allFlights == null) {
			throw new IllegalStateException("allFlights has not been initialised");
		}
		
		int totalFlights = 0;
		Set<LocalDate> dateSet = allFlights.keySet();
					
		if(!checkIfAllFlightsHashMapEmpty()) { 			
			for(LocalDate dateKey : dateSet) {
				List<Flight> dailyFlightsList = allFlights.get(dateKey);
				if(dailyFlightsList != null) { // safety measure
					totalFlights += dailyFlightsList.size();
				}					
			}
		}
		return totalFlights;
	}
	
	
	/*
	Checks if there is an available Flight object in the AllFlights HashMap,
	based on the given parameters
	Preconditions: date parameter is not null, origin and destination correspond to existing Airport objects
	Postconditions: Returns all Flight objects whose origin, destination,
	and date values match the ones provided, where the number of reserved
	seats is less than the maximum capacity
	*/
	public List<Flight> checkAvailableFlightsonDate(String originAirportLocation, String destinationAirportLocation, LocalDate date) throws IllegalArgumentException{
		
		List<Flight> allFlights = findFlightsOnSpecificDate(originAirportLocation, destinationAirportLocation, date); //this takes care of checking all preconditions
		List<Flight> availableFlights = new ArrayList<Flight>();
		
		for (Flight f: allFlights) {
			if(f.getAvailableSeats() > 0) {
				availableFlights.add(f);
			}
		}
		
		if(availableFlights.isEmpty()) {
			System.out.println("No available flights found");	
		}
		return availableFlights; 
	}

	
	/*Calculates the final ticket price of an available flight
	  Preconditions: The Flight object is not null and corresponds to an existing flight, which has available seats
	  Postconditions: returns the final ticket price which is a double.
	*/
	public double findFinalPrice(Flight flight, LocalDate depDate) throws IllegalArgumentException { // practically this may be called in tandem with checkAvailableFlightsonDate for (Flight flight: availableFlights)
		if(flight == null) {
			throw new IllegalArgumentException("invalid flight object");
		}
		
		if(depDate == null) {
			throw new IllegalArgumentException("invalid departure date");
		}
		
		 double pBase = flight.getBaseSalesPrice();
		 int freeSeats = flight.getAvailableSeats();
		 int totalSeats = Flight.TOTAL_SEATS;
		 double occupancyFactor;
		 double occupancyFactorMax = 1.2;
		 double occupancyFactorMin = 0.3;
		 double timeFactor;
		 double timeFactorMax = 1.3;
		 double timeFactorMin = 0.4;
		 int daysUntilFlight;
		 double protectedSeatsFactor;
		 double holidaysWeight;
		 double finalPrice;
		 
		 occupancyFactor = occupancyFactorMax * flight.getBusinessStatistic() + occupancyFactorMin*(1 - flight.getBusinessStatistic());
		 timeFactor = timeFactorMin * flight.getBusinessStatistic() + timeFactorMax*(1 - flight.getBusinessStatistic());
		 daysUntilFlight = (int) ChronoUnit.DAYS.between(LocalDate.now(), depDate);  //code from: https://www.baeldung.com/java-date-difference
		 
		 if(daysUntilFlight <= 2 && freeSeats <= (Flight.TOTAL_SEATS*5)/100) {
			 protectedSeatsFactor = 1.1;
		 }else {
			 protectedSeatsFactor = 1;
		 }
		 
		 if(depDate.isAfter(LocalDate.of( depDate.getYear() , 12 , 21 )) && depDate.isBefore(LocalDate.of( depDate.getYear() + 1 , 1 , 6 ))) {
			 holidaysWeight = 1.1;
		 }else {
			 holidaysWeight = 1;
		 }
		 finalPrice = pBase * (1 + (((double)(totalSeats - freeSeats)/totalSeats)* occupancyFactor) + Math.max(0.1, 1 - (timeFactor * daysUntilFlight / 100)) * protectedSeatsFactor * holidaysWeight);
		 return finalPrice;
	}
}
