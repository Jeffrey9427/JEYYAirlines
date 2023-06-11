package Person;

import FileHandler.UserHandler;
import Flight.Flight;

import java.util.*;

public class Passenger extends Person {
    private List<Integer> numOfTicketsBookedByPassenger;
    private List<Flight> flightsRegisteredByPassenger;

    //create a new user object
    private UserHandler userHandler = new UserHandler();

    //constructor for Passenger class
    public Passenger() {
        super();
    }

    public Passenger(String name, String email, String password, String address, String phoneNumber) {
        super(name, email, password, address, phoneNumber);
    }

    public Passenger(String userID, String name, String email, String password, String address, String phoneNumber) {
        super(userID, name, email, password, address, phoneNumber);

        //initialize empty list of registered flights and number of tickets booked
        this.flightsRegisteredByPassenger = new ArrayList<>();
        this.numOfTicketsBookedByPassenger = new ArrayList<>();
    }

    //method for updating a passenger's data based on his ID
    public void updatePassengerData(String userID) {
        boolean isFound = false;
        Scanner read = new Scanner(System.in);
        //iterating over passenger list to search the corresponding userID
        for (Passenger passenger : userHandler.createPassengerList()) {
            //if it matches the corresponding passenger, it will ask the user for inputting new data
            if (userID.equals(passenger.getUserID())) {
                isFound = true;

                //update the name, email, address and phone after being modified
                System.out.print("\nEnter the new name of the Passenger:\t");
                String name = read.nextLine();
                passenger.setName(name);
                System.out.print("Enter the new email address of Passenger " + name + ":\t");
                passenger.setEmail(read.nextLine());
                System.out.print("Enter the new address of Passenger " + name + ":\t");
                passenger.setAddress(read.nextLine());
                System.out.print("Enter the new Phone number of Passenger " + name + ":\t");
                passenger.setPhone(read.nextLine());
                break;
            }
        }

        //if passenger not found, display appropriate message
        if (!isFound) {
            System.out.printf("No Passenger with the userID %s found\n ", userID);
        } else {
            //update the passenger file with the changes
            userHandler.updatePassengerFile();
        }
    }

    //associate a new flight with the specified passenger
    public void addNewFlight(Flight flight) {
        this.flightsRegisteredByPassenger.add(flight);
    }

    //add numOfTickets to already booked flights
    public void addExistingFlight(int index, int numOfTickets) {
        //calculate the new number of tickets after adding the given number of tickets
        int newNumOfTickets = numOfTicketsBookedByPassenger.get(index) + numOfTickets;

        //update the number of tickets booked by the passenger at the specified index
        this.numOfTicketsBookedByPassenger.set(index, newNumOfTickets);
    }

    //getters
    public List<Flight> getFlightsRegisteredByPassenger() {
        return flightsRegisteredByPassenger;
    }

    public List<Integer> getNumOfTicketsBookedByPassenger() {
        return numOfTicketsBookedByPassenger;
    }
}