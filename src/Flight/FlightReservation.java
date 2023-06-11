package Flight;

import java.util.*;

import FileHandler.UserHandler;
import Person.Passenger;

public class FlightReservation implements DisplayClass {
    //fields
    private Flight flight = new Flight();
    private int flightIndexInFlightList;

    //create a new User object
    private final UserHandler userHandler = new UserHandler();
    private final List<Passenger> passengerList = userHandler.createPassengerList();

    //method for booking the numOfTickets for said flight for the specific user
    public void bookFlight(String flightNumber, int numOfTickets, String passengerID) {
        boolean isFound = false;

        //search for the specified flight number in the flightList
        for (Flight f1 : flight.getFlightList()) {
            if (flightNumber.equalsIgnoreCase(f1.getFlightNumber())) {
                //search for the specified passenger ID in the passengerList
                for (Passenger passenger : passengerList) {
                    //check if the passengerID matches
                    if (passengerID.equals(passenger.getUserID())) {
                        isFound = true;

                        //reduce the available number of seats in the flight by the number of tickets being booked
                        f1.setNumOfSeats(f1.getNumOfSeats() - numOfTickets);

                        //check if passenger is already added to the flight's registered passenger list
                        if (!f1.isPassengerAdded(f1.getListOfRegisteredPassengerInTheFlight(), passenger)) {
                            f1.addNewPassengerToFlight(passenger);
                        }

                        //check if flight is already added to passenger's list of registered flights
                        if(isFlightAlreadyAddedToPassengerList(passenger.getFlightsRegisteredByPassenger(), f1)) {
                            //add the number of tickets to the already booked flight
                            addNumberOfTicketsToAlreadyBookedFlight(passenger, numOfTickets);

                            //check if the flight is also present in the flight list
                            if (flightIndex(flight.getFlightList(), flight) != -1) {
                                passenger.addExistingFlight(flightIndex(flight.getFlightList(), flight), numOfTickets);
                            }
                        } else {
                            //add the new flight to the passenger's list of registered flights
                            passenger.addNewFlight(f1);

                            //add the number of tickets for the new flight
                            addNumberOfTicketsForNewFlight(passenger, numOfTickets);
                        }
                        break;
                    }
                }
            }
        }

        //check if the passenger or flight was not found
        if (!isFound) {
            System.out.println("Invalid Flight Number...! No flight with the ID \"" + flightNumber + "\" was found...");
        } else {
            System.out.printf("\n %50s You've booked %d tickets for Flight \"%5s\"...", "", numOfTickets, flightNumber.toUpperCase());
            System.out.println();
        }
    }

    public void cancelFlight(String passengerID) {
        String flightNum = "";
        Scanner read = new Scanner(System.in);
        int index = 0, ticketsToBeReturned;
        boolean isFound = false;

        //search for the passenger with the specified passengerID in the passengerList
        for (Passenger passenger : passengerList) {
            if (passengerID.equals(passenger.getUserID())) {
                //check if the passenger has any registered flights
                if (passenger.getFlightsRegisteredByPassenger().size() != 0) {
                    //display the list of registered flights for the passenger
                    System.out.printf("%50s %s Here is the list of all the Flights registered by you %s", " ", "++++++++++++++", "++++++++++++++");
                    displayFlightsRegisteredByOneUser(passengerID);

                    //prompt the user to enter the flight number and number of tickets to cancel
                    System.out.print("Enter the Flight Number of the Flight you want to cancel : ");
                    flightNum = read.nextLine();
                    System.out.print("Enter the number of tickets to cancel : ");
                    int numOfTickets = read.nextInt();

                    Iterator<Flight> flightIterator = passenger.getFlightsRegisteredByPassenger().iterator();
                    while (flightIterator.hasNext()) {
                        Flight flight = flightIterator.next();
                        if (flightNum.equalsIgnoreCase(flight.getFlightNumber())) {
                            isFound = true;
                            int numOfTicketsForFlight = passenger.getNumOfTicketsBookedByPassenger().get(index);

                            //validate the number of tickets to be canceled
                            while (numOfTickets > numOfTicketsForFlight) {
                                System.out.print("ERROR!!! Number of tickets cannot be greater than " + numOfTicketsForFlight + " for this flight. Please enter the number of tickets again : ");
                                numOfTickets = read.nextInt();
                            }

                            //calculate the tickets to be returned and update the passenger and flight information
                            if (numOfTicketsForFlight == numOfTickets) {
                                ticketsToBeReturned = flight.getNumOfSeats() + numOfTicketsForFlight;
                                passenger.getNumOfTicketsBookedByPassenger().remove(index);
                                flightIterator.remove();
                            } else {
                                ticketsToBeReturned = numOfTickets + flight.getNumOfSeats();
                                passenger.getNumOfTicketsBookedByPassenger().set(index, (numOfTicketsForFlight - numOfTickets));
                            }
                            flight.setNumOfSeats(ticketsToBeReturned);
                            break;
                        }
                        index++;
                    }

                } else {
                    System.out.println("No Flight Has been Registered by you with ID \"\"" + flightNum.toUpperCase() +"\"\".....");
                }

                //display error message if the flight is not found
                if (!isFound) {
                    System.out.println("ERROR!!! Couldn't find Flight with ID \"" + flightNum.toUpperCase() + "\".....");
                }
            }
        }
    }

    //method for adding number of tickets to a flight that has already booked by a passenger
    public void addNumberOfTicketsToAlreadyBookedFlight(Passenger passenger, int numOfTickets) {
        //add the new number of tickets to the current number of tickets booked by the passenger for the specific flight
        int newNumOfTickets = passenger.getNumOfTicketsBookedByPassenger().get(flightIndexInFlightList) + numOfTickets;

        //update the number of tickets booked by passenger for the specific flight
        passenger.getNumOfTicketsBookedByPassenger().set(flightIndexInFlightList, newNumOfTickets);
    }

    //method for adding number of tickets of a passenger for a new flight
    public void addNumberOfTicketsForNewFlight(Passenger passenger, int numOfTickets) {
        //add the number of tickets to the current passenger's ticket count
        passenger.getNumOfTicketsBookedByPassenger().add(numOfTickets);
    }

    //method for checking if flight is already added to a passenger
    public boolean isFlightAlreadyAddedToPassengerList(List<Flight> flightList, Flight flight) {
        boolean added = false;

        //iterate over flightList
        for (Flight flight1 : flightList) {
            //check if the flight number of current flight matches the flight number of the given flight
            if (flight1.getFlightNumber().equalsIgnoreCase(flight.getFlightNumber())) {
                //set the variable to the index of the matching flight in flightList
                this.flightIndexInFlightList = flightList.indexOf(flight1);
                added = true;
                break;
            }
        }

        return added;
    }

    //method for displaying flight status
    public String flightStatus(Flight flight) {
        boolean isFlightAvailable = false;
        for (Flight list : flight.getFlightList()) {
            if (list.getFlightNumber().equalsIgnoreCase(flight.getFlightNumber())) {
                isFlightAvailable = true;
                break;
            }
        }
        if (isFlightAvailable) {
            return "As Per Schedule";
        } else {
            return "   Cancelled   ";
        }
    }

    //method for displaying number of flights registered by single user
    public String toString(int serialNum, Flight flights, Passenger passenger) {
        return String.format("| %-5d| %-41s | %-9s | %-9d\t      | %-21s | %-22s | %-10s  |   %-6sHrs |  %-4s  | %-10s |", serialNum, flights.getFlightSchedule(), flights.getFlightNumber(), passenger.getNumOfTicketsBookedByPassenger().get(serialNum - 1), flights.getDepartureCity(), flights.getArrivalCity(), flights.calculateArrivalTime(), flights.getFlightTime(), flights.getGate(), flightStatus(flights));
    }

    //method for displaying flights registered by one user based on his id
    @Override
    public void displayFlightsRegisteredByOneUser(String passengerID) {
        System.out.println();
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
        System.out.printf("| Num  | FLIGHT SCHEDULE\t\t\t\t\t\t   | FLIGHT NO | Booked Tickets   | \tFROM ====>>       |   \t====>> TO\t\t   | \t    ARRIVAL TIME       | FLIGHT TIME |  GATE  |  FLIGHT STATUS  |%n");
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");

        //iterate over the passengerList
        for (Passenger passenger : passengerList) {
            //get the list of registered flights for the current passenger
            List<Flight> flights = passenger.getFlightsRegisteredByPassenger();

            //check if the flights list is not null and not empty
            if (flights != null && !flights.isEmpty()) {
                int size = flights.size();

                //if the passengerID matches, display the flights
                if (passengerID.equals(passenger.getUserID())) {
                    for (int i = 0; i < size; i++) {
                        System.out.println(toString((i + 1), flights.get(i), passenger));
                        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-----------------+\n");
                    }
                }
            }
        }
    }

    //method for displaying list of passengers based on flight(s)
    public String toString(int serialNum, Passenger passenger, int index) {
        return String.format("%10s| %-10d | %-10s | %-32s | %-27s | %-35s | %-23s |       %-7s  |", "", (serialNum + 1), passenger.randomIDDisplay(passenger.getUserID()), passenger.getName(),
                 passenger.getEmail(), passenger.getAddress(), passenger.getPhone(), passenger.getNumOfTicketsBookedByPassenger().get(index));
    }

    @Override
    //method for displaying the header for passengers based on the flight
    public void displayHeaderForPassengers(Flight flight, List<Passenger> passenger) {
        System.out.printf("\n%65s Displaying Registered Customers for Flight No. \"%-6s\" %s \n\n", "+++++++++++++", flight.getFlightNumber(), "+++++++++++++");
        System.out.printf("%10s+------------+------------+----------------------------------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
        System.out.printf("%10s| SerialNum  |   UserID   | Passenger Names                  | EmailID\t\t\t         | Home Address\t\t\t\t\t       | Phone Number\t\t     | Booked Tickets |%n", "");
        System.out.printf("%10s+------------+------------+----------------------------------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
        int size = flight.getListOfRegisteredPassengerInTheFlight().size();
        for (int i = 0; i < size; i++) {
            System.out.println(toString(i, passenger.get(i), flightIndex(passenger.get(i).getFlightsRegisteredByPassenger(), flight)));
            System.out.printf("%10s+------------+------------+----------------------------------+-----------------------------+-------------------------------------+-------------------------+----------------+\n", "");
        }
    }

    @Override
    //method for displaying registered passengers for all the flights
    public void displayRegisteredPassengersForAllFlights() {
        System.out.println();

        //iterate over the flightList
        for (Flight flight : flight.getFlightList()) {
            //get the list of registered passengers for the current flight
            List<Passenger> passenger = flight.getListOfRegisteredPassengerInTheFlight();
            int size = flight.getListOfRegisteredPassengerInTheFlight().size();

            //check if the passenger in the list is empty
            if (size != 0) {
                displayHeaderForPassengers(flight, passenger);
            }
        }
    }

    //method for finding the index of corresponding flight in the flightList
    public int flightIndex(List<Flight> flightList, Flight flight) {
        int i = -1;

        //iterate over the flightList to search the specified flight
        for (Flight flight1 : flightList) {
            //if a match is found, update the index variable to the index of matching flight in flightList
            if (flight1.equals(flight)) {
                i = flightList.indexOf(flight1);
            }
        }
        return i;
    }

    @Override
    //method for displaying registered passengers on a specific flight based on the given flight number
    public void displayRegisteredPassengersForASpecificFlight(String flightNum) {
        System.out.println();
        //iterate over the flightList
        for (Flight flight : flight.getFlightList()) {
            //get the list of registered passengers for the current flight
            List<Passenger> passenger = flight.getListOfRegisteredPassengerInTheFlight();

            //check if the flight number matches the specified flightNum
            if (flight.getFlightNumber().equalsIgnoreCase(flightNum)) {
                displayHeaderForPassengers(flight, passenger);
            }
        }
    }
}
