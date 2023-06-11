package Person;

import FileHandler.UserHandler;
import Flight.Flight;

import java.util.*;

public class Admin extends Person {
    //create a new user object
    private UserHandler userHandler = new UserHandler();

    //create a new passenger arrayList
    private final ArrayList<Passenger> passengerList = userHandler.createPassengerList();

    //create a new list containing flights of one user
    private List<Flight> flightsRegisteredByPassenger;

    //constructor for Admin class
    public Admin() {
        super();
    }

    public Admin(String name, String password, String email) {
        super(name, password, email);
    }

    //method for taking input for the new customer and adding it into the text file
    public void addNewPassenger() {
        System.out.printf("\n\n\n%60s ++++++++++++++ Welcome to the Customer Registration Portal ++++++++++++++", "");
        Scanner read = new Scanner(System.in);

        //prompt the user to enter passenger details
        System.out.print("\nEnter your name :\t");
        String username = read.nextLine();
        System.out.print("Enter your email address :\t");
        String email = read.nextLine();

        //check if email is already registered, and if it does, the user will be asked to enter new email
        while (userHandler.emailExist(email, "passenger")) {
            System.out.println("ERROR!!! User with the same email already exists... Use new email or login using the previous credentials....");
            System.out.print("Enter your email address :\t");
            email = read.nextLine();
        }

        System.out.print("Enter your Password :\t");
        String password = read.nextLine();
        System.out.print("Enter your address :\t");
        String address = read.nextLine();
        System.out.print("Enter your Phone number :\t");
        String phoneNumber = read.nextLine();
        userHandler.registerPassenger(username, email, password, address, phoneNumber);
    }

    //method for updating a passenger information
    public void updatePassengerData(String userID) {
        boolean isFound = false;
        Scanner read = new Scanner(System.in);

        //iterating over passenger list to search the corresponding userID
        for (Passenger passenger : passengerList) {
            //if it matches the corresponding passenger, it will ask the user for inputting new data
            if (userID.equals(passenger.getUserID())) {
                isFound = true;
                System.out.print("\nEnter the new name of the Passenger:\t");

                //update the name, email, address and phone after being modified
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

        //if found, update the passenger file with the change
        if (!isFound) {
            System.out.printf("No Passenger with the userID %s found\n ", userID);
        } else {
            userHandler.updatePassengerFile();
        }
    }

    //method for searching with the given ID and display the customers' data if found
    public void searchPassenger(String userID) {
        Passenger passengerWithTheID = null;

        //iterating over the passengerList to find the corresponding userID passenger
        for (Passenger passenger : passengerList) {
            if (userID.equals(passenger.getUserID())) {
                //assign/store the matched passenger in the passengerWithTheID
                passengerWithTheID = passenger;
                break;
            }
        }

        //if found, the program will display the corresponding passenger's data
        if (passengerWithTheID != null) {
            System.out.printf("%-50sPassenger Found...!!!Here is the Full Record...!!!\n\n\n", " ");
            displayHeader();
            System.out.println(passengerWithTheID.toString(1));
            System.out.printf("%10s+------------+------------+----------------------------------+-----------------------------+-------------------------------------+-------------------------+\n", "");
        } else {
            System.out.printf("No passenger with the ID %s found\n", userID);
        }
    }

    //method for deleting passenger with the given ID
    public void deletePassenger(String userID) {
        Passenger passengerToDelete = null;

        //iterating over the passengerList to find the corresponding userID passenger
        //if found, the passenger will be saved into passengerToDelete
        for (Passenger passenger : passengerList) {
            if (userID.equals(passenger.getUserID())) {
                passengerToDelete = passenger;
                break;
            }
        }

        //if found, the passenger will be removed from the passengerList
        if (passengerToDelete != null) {
            passengerList.remove(passengerToDelete);

            //update the passenger file
            userHandler.updatePassengerFile();

            //display the corresponding passenger's data
            System.out.printf("Printing all passenger's data after deleting passenger with the ID %s\n", userID);
            displayPassengersData();
        } else {
            System.out.printf("No customer with the ID %s found\n", userID);
        }
    }

    //method for displaying all the passengers data
    public void displayPassengersData() {
        displayHeader();
        for (int i=0; i<passengerList.size(); i++) {
            System.out.println(passengerList.get(i).toString(i));
            System.out.printf("%10s+------------+------------+----------------------------------+-----------------------------+-------------------------------------+-------------------------+\n", "");
        }
    }

    //method for displaying header
    public void displayHeader(){
        System.out.println();
        System.out.printf("%10s+------------+------------+----------------------------------+-----------------------------+-------------------------------------+-------------------------+\n", "");
        System.out.printf("%10s| SerialNum  |   UserID   | Passenger Name                   | EmailID\t\t\t         | Home Address\t\t\t\t\t       | Phone Number\t\t     |%n", "");
        System.out.printf("%10s+------------+------------+----------------------------------+-----------------------------+-------------------------------------+-------------------------+\n", "");
    }

    public List<Flight> getFlightsRegisteredByPassenger() {
        return flightsRegisteredByPassenger;
    }
}