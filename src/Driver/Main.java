package Driver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import FileHandler.ConsoleColor;
import Person.Passenger;
import Person.Admin;
import FileHandler.UserHandler;
import Flight.Flight;
import Flight.FlightReservation;

public class Main {
    private static String FLIGHT_FILE_PATH = "flightSchedule.txt";
    private static ConsoleColor cc = new ConsoleColor();
    public static void main(String[] args) {
        //create an instance of Admin, Passenger, UserHandler, FlightReservation, Flight class
        Admin admin = new Admin();
        Passenger passenger = new Passenger();
        UserHandler userHandler = new UserHandler();
        List<Passenger> passengers = userHandler.createPassengerList();
        FlightReservation bookingAndReserving = new FlightReservation();
        Flight f1 = new Flight();
        try (BufferedReader reader = new BufferedReader(new FileReader(FLIGHT_FILE_PATH))) {
            //check if the file is empty
            if (reader.readLine() == null) {
                //if the file is empty, create 20 new random flights and write them into the file
                f1.flightSchedulerWriter();
                f1.flightSchedulerReader();
            } else {
                f1.flightSchedulerReader();
            }
        } catch (IOException e) {
            System.out.println("File cannot be accessed");
        }

        Scanner input = new Scanner(System.in);

        System.out.println(cc.GREY_BACKGROUND + "+++++++++++++++++++++++++++++++++++++" + cc.RESET);
        System.out.println(cc.CYAN_BACKGROUND_BRIGHT + cc.BLACK_BOLD + "       Welcome to JEYY AirLines      " + cc.RESET);
        System.out.println(cc.GREY_BACKGROUND + "+++++++++++++++++++++++++++++++++++++" + cc.RESET);
        displayMainMenu();
        int desiredOption = input.nextInt();
        while (desiredOption < 0 || desiredOption > 4) {
            System.out.print("Error! Please enter a value between 0 - 4. Enter the value again: ");
            desiredOption = input.nextInt();
        }

        do {
            Scanner scanner = new Scanner(System.in);

            //login as an admin
            if (desiredOption == 1) {
                System.out.print("\nEnter the UserName to login to the Management System:   ");
                String username = scanner.nextLine();
                System.out.print("Enter the password to login to the Management System:   ");
                String password = scanner.nextLine();
                System.out.println();

                if (userHandler.signInAdmin(username, password)) {
                    int desiredChoice;
                    System.out.println("Logged In Successfully as " + username);
                    do {
                        System.out.println();
                        System.out.println(cc.CYAN_BACKGROUND + cc.BLACK_BOLD + "---------------->        Admin Menu      <----------------" + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 1 to add new passenger                              " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 2 to update passenger's data                        " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 3 to search a passenger                             " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 4 to delete a passenger                             " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 5 to display all passengers                         " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 6 to display all flights registered by a passenger  " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 7 to display all registered passengers in a flight  " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 8 to add a new flight                               " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 9 to delete a flight                                " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 10 to register another admin                        " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.RED_BOLD + "Enter 0 to go back to the Main Menu/Logout                " + cc.RESET);
                        System.out.print("Enter the desired choice: ");
                        desiredChoice = input.nextInt();
                        if (desiredChoice == 1) {
                            //add new passenger to the system
                            admin.addNewPassenger();
                        } else if (desiredChoice == 2) {
                            //first display passengers data and enter the passenger id to be modified
                            admin.displayPassengersData();
                            System.out.print("Enter the PassengerID to update its data: ");
                            String passengerID = scanner.nextLine();
                            if (passengers.size() > 0) {
                                admin.updatePassengerData(passengerID);
                            } else {
                                System.out.printf("%-50sNo Passenger with the ID %s Found...!!!\n", " ", passengerID);
                            }
                        } else if (desiredChoice == 3) {
                            //first display passengers data and enter the passenger id to be searched
                            admin.displayPassengersData();
                            System.out.print("Enter the passengerID to search: ");
                            String passengerID = scanner.nextLine();
                            System.out.println();
                            admin.searchPassenger(passengerID);
                        } else if (desiredChoice == 4) {
                            //display passengers data
                            admin.displayPassengersData();
                            //enter the passenger id to be deleted
                            System.out.print("Enter the passengerID to delete its data: ");
                            String passengerID = scanner.nextLine();
                            if (passengers.size() > 0) {
                                admin.deletePassenger(passengerID);
                            } else {
                                System.out.printf("%-50sNo Passenger with the ID %s Found...!!!\n", " ", passengerID);
                            }
                        } else if (desiredChoice == 5) {
                            //display all passengers information
                            admin.displayPassengersData();
                        } else if (desiredChoice == 6) {
                            //display all passengers information
                            admin.displayPassengersData();
                            System.out.print("\n\nEnter the ID of the user to display all flights registered by that user: ");
                            String passengerID = scanner.nextLine();

                            //display flights registered by one user based on the input ID
                            bookingAndReserving.displayFlightsRegisteredByOneUser(passengerID);
                        } else if (desiredChoice == 7) {
                            System.out.print("Do you want to display Passengers of all flights or a specific flight ('Y/y' for displaying all flights and 'N/n' to look for a" +
                                    " specific flight) :  ");
                            char choice = scanner.nextLine().charAt(0);
                            if ('y' == choice || 'Y' == choice) {
                                //if choice is Y, display registered passengers on all flights
                                bookingAndReserving.displayRegisteredPassengersForAllFlights();
                            } else if ('n' == choice || 'N' == choice) {
                                //if choice is N, display flight schedule
                                f1.displayFlightSchedule();
                                System.out.print("Enter the Flight Number to display the list of passengers registered in that flight: ");
                                String flightNum = scanner.nextLine();

                                //display registered passengers for a specified flight number
                                bookingAndReserving.displayRegisteredPassengersForASpecificFlight(flightNum);
                            } else {
                                System.out.println("Invalid Choice...No Response...!");
                            }

                        } else if (desiredChoice == 8) {
                            //add another flight into the flight schedule
                            f1.addFlight();
                        } else if (desiredChoice == 9) {
                            //display all flight schedule
                            f1.displayFlightSchedule();
                            System.out.print("Enter the Flight Number to delete the flight : ");
                            String flightNum = scanner.nextLine();

                            //delete a specified flight based on its number
                            f1.deleteFlight(flightNum);
                        } else if (desiredChoice == 10) {
                            //prompt for entering new admin details
                            System.out.print("\nEnter the UserName to Register :    ");
                            String usernameNewAdmin = scanner.nextLine();
                            System.out.print("Enter the Password to Register :     ");
                            String passwordNewAdmin = scanner.nextLine();
                            System.out.print("Enter the Email to Register :     ");
                            String emailNewAdmin = scanner.nextLine();

                            //check if email already exist, if does, reenter email
                            while (userHandler.emailExist(emailNewAdmin, "admin")) {
                                System.out.println();
                                System.out.print("Admin with the same email already exist. Enter new username: ");
                                usernameNewAdmin = scanner.nextLine();
                                System.out.print("Enter the Password Again:   ");
                                passwordNewAdmin = scanner.nextLine();
                                System.out.print("Enter the Email again:   ");
                                emailNewAdmin = scanner.nextLine();
                            }

                            //register admin into the system
                            userHandler.registerAdmin(usernameNewAdmin, passwordNewAdmin, emailNewAdmin);
                        } else if (desiredChoice == 0) {
                            System.out.println("Thank for Using JEYY Airlines Ticketing System!");
                            System.out.println();
                        } else {
                            System.out.println("Invalid Choice...Looks like you're Robot...Entering values randomly...You've Have to login again...");
                            desiredChoice = 0;
                        }
                    } while (desiredChoice != 0);
                } else {
                    System.out.println("ERROR! Unable to login cannot find admin with the entered credentials");
                }
            }

            //login as passenger
            else if (desiredOption == 2) {
                System.out.print("\nEnter the username to login to the Management System:   ");
                String username = scanner.nextLine();
                System.out.print("Enter the password to login to the Management System:   ");
                String password = scanner.nextLine();
                System.out.println();
                //save the true/false condition and passenger id
                String[] result = userHandler.signInPassenger(username, password).split("-");

                //if true, continue to the display passenger menu
                if (Integer.parseInt(result[0])== 1) {
                    int desiredChoice;
                    System.out.println("Logged In Successfully as " + username);
                    do {
                        System.out.println();
                        System.out.println(cc.CYAN_BACKGROUND + cc.BLACK_BOLD + "---------->     Passenger Menu    <-----------" + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 1 to Book a flight                      " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 2 to update your Data                   " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 3 to delete your account                " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 4 to Display Flight Schedule            " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 5 to Cancel a Flight                    " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Enter 6 to Display all flights registered     " + cc.RESET);
                        System.out.println(cc.GREY_BACKGROUND + cc.RED_BOLD + "Enter 0 to Go back to the Main Menu/Logout    " + cc.RESET);
                        System.out.print("Enter the desired choice: ");
                        desiredChoice = input.nextInt();
                        if (desiredChoice == 1) {
                            //display flight schedule
                            f1.displayFlightSchedule();

                            //prompt for the desired flight number and number of tickets
                            System.out.print("\nEnter the desired flight number to book :\t ");
                            String flightToBeBooked = scanner.nextLine();
                            System.out.print("Enter the Number of tickets for " + flightToBeBooked + " flight :   ");
                            int numOfTickets = input.nextInt();

                            while (numOfTickets > 10) {
                                System.out.print("ERROR!! You can't book more than 10 tickets at a time for single flight....Enter number of tickets again : ");
                                numOfTickets = input.nextInt();
                            }

                            //booking flights based on the flight number, number of tickets, and the passenger ID
                            bookingAndReserving.bookFlight(flightToBeBooked, numOfTickets, result[1]);
                        } else if (desiredChoice == 2) {
                            //update the passenger information
                            passenger.updatePassengerData(result[1]);
                        } else if (desiredChoice == 3) {
                            //delete the account
                            System.out.print("Are you sure to delete your account...It's an irreversible action...Enter Y/y to confirm...");
                            char confirmationChar = scanner.nextLine().charAt(0);
                            if (confirmationChar == 'Y' || confirmationChar == 'y') {
                                admin.deletePassenger(result[1]);
                                System.out.printf("User %s's account deleted Successfully...!!!", username);
                                desiredChoice = 0;
                            } else {
                                System.out.println("Action has been cancelled...");
                            }
                        } else if (desiredChoice == 4) {
                            //display all flight schedule and guideline/measurement instructions
                            f1.displayFlightSchedule();
                            f1.displayMeasurementInstructions();
                        } else if (desiredChoice == 5) {
                            //cancel a booked flight based on the passenger ID
                            bookingAndReserving.cancelFlight(result[1]);
                        } else if (desiredChoice == 6) {
                            //display all flights registered by the user
                            bookingAndReserving.displayFlightsRegisteredByOneUser(result[1]);
                        } else {
                            if (desiredChoice != 0) {
                                System.out.println("Invalid Choice...Looks like you're Robot...Entering values randomly...You've Have to login again...");
                            }
                            desiredChoice = 0;
                        }
                    } while (desiredChoice != 0);
                } else {
                    System.out.printf("\n%20sERROR!!! Unable to login Cannot find user with the entered credentials.... Try Creating New Credentials or get yourself register by pressing 4....\n", "");
                }
            } else if (desiredOption == 3) {
                //add another passenger into the system
                admin.addNewPassenger();
            } else if (desiredOption == 4) {
                //display user manual that explains how to use the program and its features
                displayManualInstructions();
            }

            displayMainMenu();
            desiredOption = scanner.nextInt();
            while (desiredOption < 0 || desiredOption > 8) {
                System.out.print("Error! Please enter a value between 0 - 4. Enter the value again: ");
                desiredOption = scanner.nextInt();
            }

        } while (desiredOption != 0);
    }

    //method for displaying the main menu of the system
    public static void displayMainMenu() {
        System.out.println();
        System.out.println(cc.CYAN_BACKGROUND + cc.BLACK_BOLD + "---------->    Main Menu   <-----------" + cc.RESET);
        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Press 1 to Login as admin.             " + cc.RESET);
        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Press 2 to Login as passenger          " + cc.RESET);
        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Press 3 to Register as passenger       " + cc.RESET);
        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Press 4 to Display manual instructions " + cc.RESET);
        System.out.println(cc.GREY_BACKGROUND + cc.RED_BOLD + "Hello! Press 0 to exit                 " + cc.RESET);
        System.out.print("Enter the desired option: ");
    }

    //methods for displaying user manual that explains how to use the program and its features
    public static void displayManualInstructions() {
        Scanner read = new Scanner(System.in);
        System.out.println();
        System.out.println();
        System.out.println(cc.GREY_BACKGROUND + "++++++++++++++++++++++++++++++++++++++++++++++++" + cc.RESET);
        System.out.println(cc.CYAN_BACKGROUND_BRIGHT + cc.BLACK_BOLD + "       Welcome to JEYY AirLines User Manual     " + cc.RESET);
        System.out.println(cc.GREY_BACKGROUND + "++++++++++++++++++++++++++++++++++++++++++++++++" + cc.RESET);
        System.out.println();
        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Press 1 to to display Admin Manual.             " + cc.RESET);
        System.out.println(cc.GREY_BACKGROUND + cc.BLACK_BOLD + "Press 2 to display User Manual.                 " + cc.RESET);
        System.out.print("Enter the desired option :    ");
        int choice = read.nextInt();
        while (choice < 1 || choice > 2) {
            System.out.print("ERROR!!! Invalid entry...Please enter a value either 1 or 2....Enter again....");
            choice = read.nextInt();
        }
        if (choice == 1) {
            System.out.println("\n\n(1) Admin have the access to all users data...Admin can delete, update, add and can perform search for any customer...\n");
            System.out.println("(2) In order to access the admin module, you need to press 1 to login as an admin when the main menu gets displayed... \n");
            System.out.println("(3) Once you've logged in, 2nd layer menu will be displayed on the screen...From here on, you can select from variety of options...\n");
            System.out.println("(4) Pressing \"1\" will add a new Passenger, provide the program with required details to add the passenger...\n");
            System.out.println("(5) Pressing \"2\" will let you update any passengers data given the user ID provided to program...\n");
            System.out.println("(6) Pressing \"3\" will search for any passenger, given the admin(you) provides the ID from the table printing above....  \n");
            System.out.println("(7) Pressing \"4\" will let you delete any passenger given its ID provided...\n");
            System.out.println("(8) Pressing \"5\" will let you display all registered passenger...\n");
            System.out.println("(9) Pressing \"6\" will let you display flights registered by a specific passenger...\n");
            System.out.println("(10) Pressing \"7\" will let you display all registered passengers...After selecting, program will ask, if you want to display passengers for all flights(Y/y) or a specific flight(N/n)\n");
            System.out.println("(11) Pressing \"8\" will let you add a new flight into the system, provide the program with required details to add the flight...\n");
            System.out.println("(11) Pressing \"9\" will let you delete any flight given its flight number provided...\n");
            System.out.println("(12) Pressing \"10\" if you want to register another admin to the system. Provide the required details i.e., name, email, id....\n");
            System.out.println("(13) Pressing \"0\" will make you logged out of the program...You can login again any time you want during the program execution....\n");
        } else {
            System.out.println("\n\n(1) Local user has the access to its data only...He/She won't be able to change/update other users data...\n");
            System.out.println("(2) In order to access local users benefits, you've to get yourself register by pressing 4, when the main menu gets displayed...\n");
            System.out.println("(3) Provide the details asked by the program to add you to the users list...Once you've registered yourself, press \"3\" to login as a passenger...\n");
            System.out.println("(4) Once you've logged in, 3rd layer menu will be displayed...From here on, you embarked on the journey to fly with us...\n");
            System.out.println("(5) Pressing \"1\" will display available/scheduled list of flights...To get yourself booked for a flight, enter the flight number and number of tickets for the flight...Max num of tickets at a time is 10 ...\n");
            System.out.println("(7) Pressing \"2\" will let you update your own data...You won't be able to update other's data... \n");
            System.out.println("(8) Pressing \"3\" will delete your account... \n");
            System.out.println("(9) Pressing \"4\" will display randomly designed flight schedule for this runtime...\n");
            System.out.println("(10) Pressing \"5\" will let you cancel any flight registered by you...\n");
            System.out.println("(11) Pressing \"6\" will display all flights registered by you...\n");
            System.out.println("(12) Pressing \"0\" will make you logout of the program...You can login back at anytime with your credentials...for this particular run-time... \n");
        }
    }
}