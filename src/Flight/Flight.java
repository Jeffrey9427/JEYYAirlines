package Flight;

import Person.Passenger;
import FileHandler.RandomGenerator;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Flight extends FlightDistance {
    //fields
    private String flightSchedule;
    private String flightNumber;
    private int numOfSeats;
    private String departureCity;
    private String arrivalCity;
    private String flightTime;
    private String gate;
    private double distanceInMiles;
    private double distanceInKm;
    private List<Passenger> listOfRegisteredPassengerInTheFlight;      //create a list of passengers in the flight
    private int passengerIndex;

    //initialize nextFlightDay and flightList list
    private static final List<Flight> flightList = new ArrayList<>();    //create a list of flights
    private static int nextFlightDay=0;

    //file paths for storing flight data and temporary file for storing flight schedule
    private String FLIGHT_FILE_PATH = "flightSchedule.txt";
    private String TEMP_FILE_PATH = "myTempFile.txt";

    //constructor for Flight class
    public Flight() {
        this.flightSchedule = null;
        this.flightNumber = null;
        this.numOfSeats = 0;
        this.departureCity = null;
        this.arrivalCity = null;
        this.gate = null;
    }

    //create new random flight from the specified arguments
    public Flight(String flightSchedule, String flightNumber, int numOfSeats, String[][] chosenDestinations, String[] distanceBetweenCities, String gate) {
        this.flightSchedule = flightSchedule;
        this.flightNumber = flightNumber;
        this.numOfSeats = numOfSeats;
        this.departureCity = chosenDestinations[0][0];
        this.arrivalCity = chosenDestinations[1][0];
        this.distanceInMiles = Double.parseDouble(distanceBetweenCities[0]);
        this.distanceInKm = Double.parseDouble(distanceBetweenCities[1]);
        this.flightTime = calculateFlightTime(distanceInMiles);
        this.gate = gate;

        //initialize empty list of registered passengers
        this.listOfRegisteredPassengerInTheFlight = new ArrayList<>();
    }

    //method for creating 20 new random flight schedule from the specified arguments and write them into the flightSchedule.txt file
    public void flightSchedulerWriter() {
        try (FileWriter writer = new FileWriter(FLIGHT_FILE_PATH, true)) {
            int numOfFlights = 20;
            RandomGenerator random = new RandomGenerator();

            for (int i=0; i<numOfFlights; i++) {
                //create destination and arrival city along with their latitudes and longitudes
                String[][] chosenDestinations = random.randomDestinationsGenerator();

                //calculate distance between destination and arrival city in miles and km
                String[] distanceBetweenCities = calculateDistance(
                        Double.parseDouble(chosenDestinations[0][1]),
                        Double.parseDouble(chosenDestinations[0][2]),
                        Double.parseDouble(chosenDestinations[1][1]),
                        Double.parseDouble(chosenDestinations[1][2])
                );

                //create new random flights and time
                String flightSchedule = createNewFlightsAndTime();

                //generate random number for the flight numbers starting with two alphabets followed by numbers
                String flightNumber = random.randomFlightNumberGenerator(2, 1).toUpperCase();

                //generate random number of seats for the flights
                int numOfSeats = random.randomNumOfSeats();

                //generate random number for the gate starting with one alphabet followed by numbers
                String gate = random.randomFlightNumberGenerator(1, 30);

                //create a new flight object based on above parameters
                Flight aFlight = new Flight(flightSchedule, flightNumber, numOfSeats, chosenDestinations, distanceBetweenCities, gate.toUpperCase());

                writer.write(String.format(
                        "%s/%s/%s/%s:%s:%s:%s:%s:%s/%s:%s/%s%n",
                        aFlight.getFlightSchedule(),
                        aFlight.getFlightNumber(),
                        aFlight.getNumOfSeats(),
                        aFlight.getDepartureCity(),
                        chosenDestinations[0][1],
                        chosenDestinations[0][2],
                        aFlight.getArrivalCity(),
                        chosenDestinations[1][1],
                        chosenDestinations[1][2],
                        distanceBetweenCities[0],
                        distanceBetweenCities[1],
                        aFlight.getGate().toUpperCase())
                );
            }
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }
    }

    //method for reading flightScheduler.txt file and add those data into the flightList
    public void flightSchedulerReader() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FLIGHT_FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                //split the line by "/"
                String[] flightData = line.split("/");

                //extract flight schedule, flight number, and number of seats
                String flightSchedule = flightData[0];
                String flightNumber = flightData[1];
                int numOfSeats = Integer.parseInt(flightData[2]);

                //create a 2-dimensional array for chosen destinations
                String[][] chosenDestinations = new String[2][3];

                //extract departure city information
                chosenDestinations[0][0] = flightData[3].split(":")[0];  //departureCity
                chosenDestinations[0][1] = flightData[3].split(":")[1];  //departureCityLatitude
                chosenDestinations[0][2] = flightData[3].split(":")[2];  //departureCityLongitude

                //extract arrival city information
                chosenDestinations[1][0] = flightData[3].split(":")[3];  //arrivalCity
                chosenDestinations[1][1] = flightData[3].split(":")[4];  //arrivalCityLatitude
                chosenDestinations[1][2] = flightData[3].split(":")[5];  //arrivalCityLongitude

                //create an array for distance between cities
                String[] distanceBetweenCities = new String[2];

                //extract distance between cities information
                distanceBetweenCities[0] = flightData[4].split(":")[0];
                distanceBetweenCities[1] = flightData[4].split(":")[1];

                //extract gate information
                String gate = flightData[5];

                //create a new Flight object and add it to the flightList
                flightList.add(new Flight(flightSchedule, flightNumber, numOfSeats, chosenDestinations, distanceBetweenCities, gate));
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("File cannot be accessed");
        }
    }

    //method for calculating distance between two cities by using their latitudes and longitudes
    @Override
    public String[] calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        double theta = startLongitude - endLongitude;

        //calculate distance using the Haversine formula
        double distance = Math.sin(degreeToRadian(startLatitude)) * Math.sin(degreeToRadian(endLatitude)) +
                Math.cos(degreeToRadian(startLongitude)) * Math.cos(degreeToRadian(endLongitude)) * Math.cos(degreeToRadian(theta));

        //convert the distance to degrees and multiply by conversion factors
        distance = Math.acos(distance);
        distance = radianToDegree(distance) * 60 * 1.1515;

        //create an array to store the calculated distances
        String[] distanceString = new String[2];

        //format and store the distances in the array
        //the first index represents the distance in miles
        //the second index represents the distance in kilometers
        distanceString[0] = String.format("%.2f", distance);
        distanceString[1] = String.format("%.2f", distance * 1.609344);

        return distanceString;
    }

    //method for converting degree to radian
    public double degreeToRadian(double deg) { return (deg * Math.PI / 180.0); }

    //method for converting radian to degree
    public double radianToDegree(double radian) { return (radian * 180.0 / Math.PI); }

    //method for creating new random flight schedule
    public static String createNewFlightsAndTime() {
        //get the current date and time
        Calendar c = Calendar.getInstance();

        //incrementing nextFlightDay, so that next scheduled flight would be in the future, not in the present
        nextFlightDay += Math.random() * 7;

        //add the nextFlightDay to the current date to get the future date, hour
        c.add(Calendar.DATE, nextFlightDay);
        c.add(Calendar.HOUR, nextFlightDay);

        //set the minutes based on a random value within a specific range
        c.set(Calendar.MINUTE, ((c.get(Calendar.MINUTE) * 3) - (int) (Math.random() * 45)));

        //convert the calendar object to a date object and to a LocalDateTime object
        Date myDateObj = c.getTime();
        LocalDateTime date = Instant.ofEpochMilli(myDateObj.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        //round the LocalDateTime object to the nearest hour and quarter
        date = getNearestHourQuarter(date);

        //format the LocalDateTime object to a specific date-time string format
        return date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm a"));
    }

    //method for formatting flight schedule, so that the minutes would be to the nearest quarter
    public static LocalDateTime getNearestHourQuarter(LocalDateTime datetime) {
        int minutes = datetime.getMinute();
        int mod = minutes % 15;
        LocalDateTime newDatetime;

        //if the modulus is negative, subtract it from the datetime to round down to the nearest quarter
        if (mod < 0) {
            newDatetime = datetime.minusMinutes(mod);
        } else {
            //otherwise, add the difference to 15 to round up to the nearest quarter
            newDatetime = datetime.plusMinutes(15 - mod);
        }

        //truncate the datetime to remove any seconds or milliseconds
        newDatetime = newDatetime.truncatedTo(ChronoUnit.MINUTES);

        return newDatetime;
    }

    //method for calculating flight time between cities
    public String calculateFlightTime(double distanceBetweenCities) {
        double groundSpeed = 450;

        //calculate the flight time in hours
        double flightTime = distanceBetweenCities / groundSpeed;

        int hours = (int) flightTime;   //extract the integer part as hours
        int minutes = (int) ((flightTime - hours) * 60);   //calculate the minutes

        //format the hours and minutes into the "HH:MM" format
        return String.format("%02d:%02d", hours, minutes);
    }

    //method for creating arrival time by adding flight time to flight departure time
    public String calculateArrivalTime() {
        //convert the String of flightSchedule to LocalDateTime and add the arrivalTime to it
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm a");
        LocalDateTime departureDateTime = LocalDateTime.parse(flightSchedule, formatter);

        //get the Flight Time, plane was in the air
        String[] flightTime = getFlightTime().split(":");
        int hours = Integer.parseInt(flightTime[0]);
        int minutes = Integer.parseInt(flightTime[1]);

        //format the dateTime and arrivalTime
        LocalDateTime arrivalTime = departureDateTime.plusHours(hours).plusMinutes(minutes);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EE, dd-MM-yyyy HH:mm a");
        return arrivalTime.format(formatter1);
    }

    //method for checking if the specified passenger is already registered in the Flight's array list
    public boolean isPassengerAdded(List<Passenger> passengerList, Passenger passenger) {
        boolean isAdded = false;

        //iterate over passengerList to search the specified passenger
        for (Passenger passenger1 : passengerList) {
            if (passenger1.getUserID().equals(passenger.getUserID())) {
                isAdded = true;
                //store the index of the matching passenger
                passengerIndex = passengerList.indexOf(passenger1);
                break;
            }
        }

        return isAdded;
    }

    //method for registering new customer in this flight
    public void addNewPassengerToFlight(Passenger passenger) {
        this.listOfRegisteredPassengerInTheFlight.add(passenger);
    }

    //method for adding numOfTickets to existing customer's tickets for this flight
    public void addTicketsToExistingPassenger(Passenger passenger, int numOfTickets) {
        passenger.addExistingFlight(passengerIndex, numOfTickets);
    }

    //method for adding a flight
    public void addFlight() {
        boolean added = false;
        System.out.printf("\n\n\n%60s ++++++++++++++ Welcome to the Flight Registration Portal ++++++++++++++", "");
        Scanner read = new Scanner(System.in);

        //prompt the user to enter flight details
        System.out.print("\nEnter the date of flight (Day Month Year) :\t");
        String flightDateNew = read.nextLine();

        //get the day of the week for the provided date
        String flightDayNew = getDayOfWeek(flightDateNew);

        System.out.print("Enter the time of flight (HH:MM AM/PM) :\t");
        String flightTimeNew = read.nextLine();

        //create a string representing the flight schedule
        String flightScheduleNew = String.format("%s, %s, %s", flightDayNew, flightDateNew, flightTimeNew);
        System.out.print("Enter the flight number (XX-000) :\t");
        String flightNumberNew = read.nextLine();
        System.out.print("Enter the number of seats available in the flight :\t");
        int flightNumOfSeatsNew = read.nextInt();
        System.out.println();

        System.out.println("Flight Destinations available for JEYY Airlines");
        try (BufferedReader reader = new BufferedReader(new FileReader("destination.txt"))) {
            String line;
            int index = 1;

            //read and display the available destinations from the destinations.txt file
            while ((line = reader.readLine()) != null) {
                //split the line by ","
                String[] parts = line.split(",");

                //extract the destination city
                String destination = parts[0].trim();
                System.out.printf("%d. %s%n", index, destination);
                index++;
            }
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }

        //prompt the user to enter the departure city
        System.out.println();

        //create a 2d array to store chosen destinations
        String[][] chosenDestinationsNew = new String[2][3];

        Scanner scanner = new Scanner(System.in);

        boolean validDepartureCity = false, validArrivalCity = false;
        String flightDepartureCityNew = "", flightArrivalCityNew = "";

        //check if departure city entered is available in JEYY Airlines
        while (!validDepartureCity) {
            //prompt the user to enter the departure city
            System.out.print("Enter the departure city :\t");
            flightDepartureCityNew = scanner.nextLine();
            try (BufferedReader reader = new BufferedReader(new FileReader("destination.txt"))) {
                String line;

                //search for the chosen departure city and store its details
                while ((line = reader.readLine()) != null) {
                    //split the line by ","
                    String[] parts = line.split(",");

                    //extract the departure city information
                    if (parts[0].trim().equalsIgnoreCase(flightDepartureCityNew)) {
                        chosenDestinationsNew[0][0] = parts[0].trim();  //departureCity
                        chosenDestinationsNew[0][1] = parts[1].trim();  //departureCityLatitude
                        chosenDestinationsNew[0][2] = parts[2].trim();  //departureCityLongitude
                        validDepartureCity = true;  //set the condition to exit the loop when the departure city is found in the txt file
                        break;
                    }
                }

                //print failure message to find the departure city when not available
                if (!validDepartureCity) {
                    System.out.println("Departure city not found. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("File could not be accessed");
            }
        }

        //check if arrival city entered is available in JEYY Airlines
        while (!validArrivalCity) {
            //prompt the user to enter the arrival city
            System.out.print("Enter the arrival city :\t");
            flightArrivalCityNew = scanner.nextLine();
            try (BufferedReader reader = new BufferedReader(new FileReader("destination.txt"))) {
                String line;

                //search for the chosen arrival city and store its details
                while ((line = reader.readLine()) != null) {
                    //split the line by ","
                    String[] parts = line.split(",");

                    //extract the arrival city information
                    if (parts[0].trim().equalsIgnoreCase(flightArrivalCityNew)) {
                        chosenDestinationsNew[1][0] = parts[0].trim();  //departureCity
                        chosenDestinationsNew[1][1] = parts[1].trim();  //departureCityLatitude
                        chosenDestinationsNew[1][2] = parts[2].trim();  //departureCityLongitude
                        validArrivalCity = true;  //set the condition to exit the loop when the arrival city is found in the txt file
                        break;
                    }
                }

                //print failure message to find the arrival city when not available
                if (!validArrivalCity) {
                    System.out.println("Arrival city not found. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("File could not be accessed");
            }
        }

        //calculate the distance between the departure and arrival cities
        String[] distanceBetweenCitiesNew = calculateDistance(
                Double.parseDouble(chosenDestinationsNew[0][1]),
                Double.parseDouble(chosenDestinationsNew[0][2]),
                Double.parseDouble(chosenDestinationsNew[1][1]),
                Double.parseDouble(chosenDestinationsNew[1][2])
        );

        System.out.print("Enter the gate of flight (X-0) :\t");
        String gateNew = scanner.nextLine();

        try (FileWriter writer = new FileWriter(FLIGHT_FILE_PATH, true)) {
            //create a new flight object with the provided details
            Flight aFlight = new Flight(flightScheduleNew, flightNumberNew, flightNumOfSeatsNew, chosenDestinationsNew, distanceBetweenCitiesNew, gateNew);

            //write the flight details to a file
            writer.write(String.format(
                    "%s/%s/%s/%s:%s:%s:%s:%s:%s/%s:%s/%s%n",
                    aFlight.getFlightSchedule(),
                    aFlight.getFlightNumber(),
                    aFlight.getNumOfSeats(),
                    aFlight.getDepartureCity(),
                    chosenDestinationsNew[0][1],
                    chosenDestinationsNew[0][2],
                    aFlight.getArrivalCity(),
                    chosenDestinationsNew[1][1],
                    chosenDestinationsNew[1][2],
                    distanceBetweenCitiesNew[0],
                    distanceBetweenCitiesNew[1],
                    aFlight.getGate().toUpperCase())
            );

            added = true;
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }

        //display success or failure message based on whether the flight was added
        if (added) {
            System.out.printf("\n %50s Flight \"%s\" successfully added", "", flightNumberNew.toUpperCase());
        } else {
            System.out.println("Error! Flight cannot be added");
        }
        System.out.println();
    }

    //method for deleting a flight
    public void deleteFlight(String flightNumber) {
        boolean isFound = false;
        Flight flightFound = null;

        //iterate over the flightList to find the flight with the specified flight number
        for (Flight flight : flightList) {
            if (flight.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                isFound = true;
                flightFound = flight;
                break;
            }
        }

        //if the specified flight found, remove it from the flightList
        if (isFound) {
            flightList.remove(flightFound);
            try {
                //create file objects for input and temporary files
                File inputFile = new File(FLIGHT_FILE_PATH);
                File tempFile = new File(TEMP_FILE_PATH);

                //create readers and writers for input and temporary files
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String currentLine;

                //read each line from the input file
                while((currentLine = reader.readLine()) != null) {
                    //split the line by "/" to access flight details
                    String[] parts = currentLine.split("/");
                    String trimmedLine = parts[1].trim();

                    //skip the line if the flight number matches the one to be deleted
                    if (trimmedLine.equals(flightNumber)) continue;

                    //write the line to the temporary file
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
                writer.close();
                reader.close();

                //rename the temporary file to replace the input file
                boolean successful = tempFile.renameTo(inputFile);

                //display success or failure message
                if (successful) {
                    System.out.println("Line with flight number " + flightNumber + " deleted successfully.");
                } else {
                    System.out.println("Failed to delete the line with flight number " + flightNumber);
                }

            } catch (IOException e) {
                System.out.println("File cannot be accessed");
            }
        } else {
            System.out.println("Flight with the given number not found");
        }

        displayFlightSchedule();
    }

    //method for getting day of week based on date input
    public static String getDayOfWeek(String date) {
        try {
            //create a date format object to parse the input date string
            DateFormat format = new SimpleDateFormat("dd MMMM yyyy");

            //parse the date string into a Date object
            Date parsedDate = format.parse(date);

            //create a Calendar instance and set the parsed date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);

            //retrieve the day of the week from the Calendar object
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            //convert dayOfWeek to the corresponding day name
            String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            return dayNames[dayOfWeek - 1];
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    //method for displaying flight schedules
    public void displayFlightSchedule() {
        System.out.println();
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-------------------------+\n");
        System.out.printf("| Num  | FLIGHT SCHEDULE\t\t\t\t\t\t   | FLIGHT NO | Available Seats  | \tFROM ====>>       | \t====>> TO\t\t   | \t    ARRIVAL TIME       | FLIGHT TIME |  GATE  |   DISTANCE (MILES/KMS)  |%n");
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-------------------------+\n");

        int i = 0;
        for (Flight flight : flightList) {
            System.out.println(flight.toString(i));
            System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+-------------------------+\n");
            i++;
        }
    }

    @Override
    public String toString(int index) {
        return String.format("| %-5d| %-41s | %-9s | \t\t%-9s | %-21s | %-22s | %-10s  |   %-6sHrs |  %-4s  |   %-8s / %-11s|", index+1, flightSchedule, flightNumber, numOfSeats, departureCity, arrivalCity, calculateArrivalTime(), flightTime, gate, distanceInMiles, distanceInKm);
    }

    //setters and getters
    public void setNumOfSeats(int numOfSeats) { this.numOfSeats = numOfSeats; }

    public String getFlightSchedule() { return flightSchedule; }

    public String getFlightNumber() { return flightNumber; }

    public int getNumOfSeats() { return numOfSeats; }

    public String getDepartureCity() { return departureCity; }

    public String getArrivalCity() { return arrivalCity; }

    public String getFlightTime() { return flightTime; }

    public String getGate() { return gate; }

    public List<Flight> getFlightList() { return flightList; }

    public List<Passenger> getListOfRegisteredPassengerInTheFlight() { return listOfRegisteredPassengerInTheFlight; }
}
