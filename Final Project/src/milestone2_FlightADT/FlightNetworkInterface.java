// Code by: V.Vaitsopoulou
package milestone2_FlightADT;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public interface FlightNetworkInterface {

		/* 
		   Creates an empty WeeklyFlights HashMap 
		   Preconditions: None 
		   Postconditions: An empty Weekly Flights HashMap is created
		*/
		public void createEmptyWeeklyFlightsHashMap();		
		
		/* 
		   Creates an empty AllFlights HashMap
		   Preconditions: None
		   Postconditions: An empty All Flights HashMap is created
		*/
		public void createEmptyAllFlightsHashMap();
		
		/* 
		   Populates the AllFlights HashMap from the current date up to six months in the future by mapping each date to a copy of the ArrayList of weekly flights which corresponds to that day of the week
		   Preconditions: weeklyFlights HashMap has been created, allFlights HashMap has been created.
		   PostConditions: the AllFlights HashMap is populated with keys representing calendar dates, each mapping to an ArrayList of flights conducted on that date.
		   Each Arraylist is sorted such that all direct flights are placed before layover flights
		 */
		public void populateAllFlightsHashMap() throws IllegalStateException;
		
		/*  
		    Determines if the HashMap weeklyFlights is empty
		  	Preconditions: The HashMap is created
		  	Postconditions: Returns Boolean - true if the HashMap is empty, otherwise false
		*/
		public boolean checkIfWeeklyFlightsHashMapEmpty() throws IllegalStateException;
		
		/* 
		   Determines if the allFlights HashMap is empty.
		   Preconditions: The allFlights HashMap is created.
		   Postconditions: Returns Boolean - true if the allFlights HashMap is
		   empty, otherwise false
		*/
		public boolean checkIfAllFlightsHashMapEmpty() throws IllegalStateException;
		
		
		/*
		  Adds the Flight object to the WeeklyFlights Hashmap
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

		public void addRegularDirectFlight(int flightId, Airport origin, Airport destination, double baseSalesPrice, double businessStatistic, String weekDay, LocalTime depTime) throws IllegalArgumentException;
		
		//COMMENT: it is a business assumption that inserting additional unique regular flight routes is not something that happens fairly often during the operation of the 
		//flight network and after the initial airline network has been created. It will only happen en masse during the creation of the system. There was an initial thought 
		//to call repopulate allFlights hashmap method every time a new regular direct flight is added to the weeklyFlights Hashmap to
		//simulate a real life scenario when a new route is added to the model between two previously unconnected airports after the system has been initialised, and the
		// flights occuring from the current day forward should be updated. However,
		//because in our model the addition of flights both at the beginning and in this scenario happen with the same method, modifying the above
		//method would add computational overhead at the start, thus the thought was discarded. In the current model this operation mainly serves the purpose of
		//'refreshing' the alllfights hashmap every few months based on our model, so that flights conducted on past dates are no longer stored
		
		/* 
		   Removes all flights in the current allFlights HashMap and populates it by mapping each date from the current date up to six months in the future to a copy of the ArrayList of weekly flights which corresponds to that day of the week
		   Preconditions: weeklyFlights HashMap has been created, allFlights HashMap has been created and not empty.
		   PostConditions: the AllFlights HashMap is cleared then populated with keys representing calendar dates from the current date up to six months in the future, each mapping to an ArrayList of flights conducted on that date 
		*/
		public void repopulateAllFlightsHashMap() throws IllegalStateException;
		

		/*
		  Adds the Flight object to the WeeklyFlights Hashmap
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
		public void addRegularLayoverFlight( int flightId, Airport origin, Airport layover, Airport destination, double baseSalesPrice, double businessStatistic,  String weekDay, LocalTime depTime) throws IllegalArgumentException;
			
		/* 
		   Adds a direct Flight object to the AllFlights HashMap
		   Preconditions: date and time parameters are not null, origin and destination are existing Airport objects, 
		   the number of total seats is by assumption the same for all flights with maximum capacity arbitrarily set at
		   300, on flight creation all seats are considered available with the exception
		   of a 5% which based on the pricing algorithm assumptions will be protected seats, business statistic is a double number
	       such that 0 < business statistic < 1
		   Postconditions:Adds the Flight object to the All Flights HashMap as
		   a singular flight on the Arraylist corresponding to the specific date, sorting the List such that the new direct flight is placed before all layover flights. The
		   layover airport points to null.
		*/
		public void	addSpecificDirectFlight( int flightId, Airport origin, Airport destination, double baseSalesPrice, double businessStatistic, LocalDate depDate, LocalTime depTime) throws IllegalArgumentException, IllegalStateException;
		
		/* 
		   Adds a layover Flight object to the AllFlights HashMap
		   Preconditions: date and time parameters are not null, origin layover and destination are existing Airport objects, 
		   the number of total seats is by assumption the same for all flights with maximum capacity arbitrarily set at
		   300, on flight creation all seats are considered available with the exception
		   of a 5% which based on the pricing algorithm assumptions will be protected seats, business statistic is a double number
	       such that 0 < business statistic < 1
		   Postconditions:Adds the Flight object to the All Flights HashMap as
		   a singular flight on the Arraylist corresponding to the specific date, at the end of the list. 
		*/
		public void	addSpecificLayoverFlight( int flightId, Airport origin, Airport layover, Airport destination, double baseSalesPrice, double businessStatistic, LocalDate depDate, LocalTime depTime) throws IllegalArgumentException, IllegalStateException;

		//COMMENT: with the current implementation we consider that if the company wants to 
		//cancel a flight permanently then they should first perform this operation and then repopulate the allflights hashmap
		
		/*
		  Removes a Flight object from the WeeklyFlights HashMap
		  Preconditions: flightId is a unique for each route integer that is positive
		  by convention and corresponds to an existing flight object, weekDay parameter is not null and corresponds to one of the seven HashMap unique
		  keys for each day of the week, time is not null and corresponds to the
		  existing flight object satisfying the rest of the parameters
		  Postconditions: removes the Flight object connecting the specified locations on the specified weekday and time from the Weekly Flights ArrayList
		  which corresponds to the weekDay key entirely. Returns true if successful,
		  false otherwise.
		*/
		public boolean cancelFlightPermanently(int flightId, String weekDay, LocalTime time) throws IllegalArgumentException;

		
		/*  Retrieves a flight object from the weeklyFlights HashMap based on its'
			flight Id time and day of operation
			Preconditions: flightId is a unique for each route integer that is positive
			by convention and corresponds to an existing flight object 
			weekDay parameter is not null and corresponds to one of the seven HashMap unique
			keys for each day of the week time is not null and corresponds to the
			existing flight object satisfying the rest of the parameters		 
			Postconditions: Returns the Flight with the specific flight Id for
			the specified date and time if it exists in the HashMap, otherwise return null
		*/	

		public Flight getWeeklyFlightById(int flightId, String weekDay, LocalTime time) throws IllegalArgumentException, IllegalStateException;

		
		
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
		public boolean cancelSpecificFlight(int flightId, LocalDate date, LocalTime time) throws IllegalArgumentException;
		
		 /* 
		    Retrieves a flight object from the allFlights HashMap based on its’
			flight Id time and date of operation
			Preconditions: flightId is a unique for each route integer that is positive
			by convention and corresponds to an existing flight object 
			Date parameter is not null and corresponds to one of the HashMap unique
			keys time is not null and corresponds to the
			existing flight object satisfying the rest of the parameters		 
			Postconditions: Returns the Flight with the specific flight Id for
			the specified date and time if it exists in the HashMap, otherwise return null
		  */

		public Flight getSpecificFlightById(int flightId, LocalDate date, LocalTime time) throws IllegalArgumentException;
		
		
		
		/*
		  Removes all Flight objects from the AllFlights HashMap
		  Preconditions: The HashMap is created and not empty
		  Postconditions: Removes all Flight objects from the HashMap
		*/
		public void removeAllFlights();


		/*
		  Removes all flights from the WeeklyFlights Hashmap
		  Preconditions: The Hashmap is created and not empty
		  Postconditions: Removes all weekly Flight objects from the Hashmap
		*/
		public void removeAllWeeklyFlights();
		

		/*
		  Retrieves a List of flight objects from the AllFlights HashMap based on their
		  origin & destination airports, and departure date
		  Preconditions: origin and destination correspond to existing Airport
		  objects, date is not null and corresponds to at least one existing Flight object connecting origin and destination
		  Postconditions: Returns the Flights List between origin and destination for
		  the specified date if they exist in the HashMap (if no flights satisfy these conditions the list is empty)
		*/
		public List<Flight> findFlightsOnSpecificDate(String originAirportLocation, String destinationAirportLocation, LocalDate date) throws IllegalArgumentException;
		
		/*
		Returns the number of flights in the AllFlights HashMap
		Preconditions: HashMap is created and not empty
		Postconditions: Returns an integer number that is equivalent to the
		number of Flights in the HashMap
	    */
		public int determineTotalFlightsNumber();
		
		
		/*
		Checks if there is an available Flight object in the AllFlights HashMap,
		based on the given parameters
		Preconditions: date parameter is not null, origin and destination correspond to existing Airport objects
		Postconditions: Returns all Flight objects whose origin, destination,
		and date values match the ones provided, where the number of reserved
		seats is less than the maximum capacity
		*/
		public List<Flight> checkAvailableFlightsonDate(String originAirportLocation, String destinationAirportLocation, LocalDate date) throws IllegalArgumentException;

		
		/*
		  Calculates the final ticket price of an available flight
		  Preconditions: The Flight object is not null and corresponds to an existing flight, which has available seats
		  Postconditions: returns the final ticket price which is a double.
		*/
		public double findFinalPrice(Flight flight, LocalDate depDate) throws IllegalArgumentException;

}
