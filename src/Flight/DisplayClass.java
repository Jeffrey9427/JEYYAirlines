package Flight;

import Person.Passenger;
import java.util.*;

public interface DisplayClass {
    public void displayRegisteredPassengersForAllFlights();

    public void displayRegisteredPassengersForASpecificFlight(String flightNum);

    public void displayHeaderForPassengers(Flight flight, List<Passenger> passenger);

    public void displayFlightsRegisteredByOneUser(String passengerID);
}
