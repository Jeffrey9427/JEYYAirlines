package FileHandler;

import Flight.Flight;
import Person.Passenger;
import Person.Admin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class UserHandler {
    //file paths for storing admin and passenger data
    private static String ADMIN_FILE_PATH = "admin.txt";
    private static String PASSENGER_FILE_PATH = "passenger.txt";
    private ArrayList<Admin> admins = new ArrayList<>();
    private ArrayList<Passenger> passengers = new ArrayList<>();

    //method for adding an admin to the text file
    public void registerAdmin(String name, String password, String email) {
        try (FileWriter writer = new FileWriter(ADMIN_FILE_PATH, true)) {
            //create new passenger object
            Admin admin = new Admin(name, password, email);

            //write admin details by getters method into the admin file
            writer.write(String.format("%s,%s,%s%n", admin.getName(), admin.getPassword(), admin.getEmail()));
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }
    }

    //method for signing in an admin
    public boolean signInAdmin(String username, String password) {
        try (Scanner reviewScanner = new Scanner(Path.of(ADMIN_FILE_PATH))) {
            while (reviewScanner.hasNextLine()) {
                String line = reviewScanner.nextLine();

                //split the line by ","
                String[] parts = line.split(",");

                //extract username and password
                String tempUsername = parts[0].trim();
                String tempPassword = parts[1].trim();

                //check if provided username and password match the stored admin credentials
                if (tempUsername.equals(username) && tempPassword.equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }
        return false;
    }

    //method for registering a passenger into the text file
    public void registerPassenger(String name, String email, String password, String address, String phoneNumber) {
        try (FileWriter writer = new FileWriter(PASSENGER_FILE_PATH, true)) {
            //create new passenger object and add it into passenger array
            Passenger aPassenger = new Passenger(name, email, password, address, phoneNumber);

            //write passenger details by getters method into the passenger file
            writer.write(String.format("%s,%s,%s,%s,%s,%s%n", aPassenger.getUserID(), aPassenger.getName(), aPassenger.getEmail(), aPassenger.getPassword(), aPassenger.getAddress(), aPassenger.getPhone()));
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }
    }

    //method for signing in a passenger
    public String signInPassenger(String username, String password) {
        String isFound = "0";
        try (Scanner reviewScanner = new Scanner(Path.of(PASSENGER_FILE_PATH))) {
            while (reviewScanner.hasNextLine()) {
                String line = reviewScanner.nextLine();

                //split the line by ","
                String[] parts = line.split(",");

                //extract userID, username and password
                String tempUserID = parts[0].trim();
                String tempUsername = parts[1].trim();
                String tempPassword = parts[3].trim();

                //check if provided email and password match the stored passenger credentials.
                if (tempUsername.equals(username) && tempPassword.equals(password)) {
                    isFound = "1-" + tempUserID;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }
        return isFound;
    }

    //method for checking if an email already exists
    public boolean emailExist(String email, String role) {
        String filePath = role.equals("admin") ? ADMIN_FILE_PATH : PASSENGER_FILE_PATH;
        try (Scanner scanner = new Scanner(Path.of(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                //split the line by "," and extract email
                String tempEmail = line.split(",")[2].trim();

                //check if provided email matches any stored email in the specified file
                if (tempEmail.equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }
        return false;
    }

    //method for creating a passengerArray containing passengers from the passenger text file
    public ArrayList<Passenger> createPassengerList() {
        try (BufferedReader reviewScanner = new BufferedReader(new FileReader(PASSENGER_FILE_PATH))) {
            String line;
            while ((line = reviewScanner.readLine()) != null) {
                //split the line by ","
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    //extract userID, username, email, password, address, and phoneNumber
                    String tempUserId = parts[0].trim();
                    String tempName = parts[1].trim();
                    String tempEmail = parts[2].trim();
                    String tempPassword = parts[3].trim();
                    String tempAddress = parts[4].trim();
                    String tempPhoneNumber = parts[5].trim();

                    //create a new passenger object and add it into the passenger array
                    Passenger aPassenger = new Passenger(tempUserId, tempName, tempEmail, tempPassword, tempAddress, tempPhoneNumber);
                    passengers.add(aPassenger);
                } else {
                    System.out.println("Invalid line format in the passenger text file: " + line);
                }
            }
            return passengers;
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }
        return null;
    }

    //method for creating an adminArray containing admins from the admin text file
    public ArrayList<Admin> createAdminList() {
        try (Scanner reviewScanner = new Scanner(Path.of(ADMIN_FILE_PATH))) {
            while (reviewScanner.hasNextLine()) {
                String line = reviewScanner.nextLine();

                //split the line by ","
                String[] parts = line.split(",");

                //extract username, password and email
                String tempName = parts[0].trim();
                String tempPassword = parts[1].trim();
                String tempEmail = parts[2].trim();

                //create a new admin object and add it into the admin array
                Admin anAdmin = new Admin(tempName, tempPassword, tempEmail);
                admins.add(anAdmin);
            }
            return admins;
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }
        return null;
    }

    public void updatePassengerFile() {
        //delete the contents of the passenger file
        try (FileWriter deleteWriter = new FileWriter(PASSENGER_FILE_PATH)) {
            deleteWriter.write("");
        } catch (IOException e) {
            System.out.println("Error deleting file contents");
            return;
        }

        //update the file with the rewritten string format
        try (FileWriter writer = new FileWriter(PASSENGER_FILE_PATH, true)) {
            for (Passenger passenger : createPassengerList()) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s%n", passenger.getUserID(), passenger.getName(), passenger.getEmail(), passenger.getPassword(), passenger.getAddress(), passenger.getPhone()));
            }
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }
    }

    public ArrayList<Admin> getAdminsList() {
        return admins;
    }
}