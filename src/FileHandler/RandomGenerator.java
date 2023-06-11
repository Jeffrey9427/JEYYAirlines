package FileHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class RandomGenerator {
    //fields
    private String randomNumber;
    private String[][] destinations = getDestinations();

    //methods for generating random ID for the passengers
    public void randomIDGenerator() {
        Random random = new Random();

        //set a 6 digit bound for randomId
        String randomID = Integer.toString(random.nextInt(1000000));

        //create a 5-6 digit ID
        while (Integer.parseInt(randomID) < 100000) {
            randomID = Integer.toString(random.nextInt(1000000));
        }
        setRandomNumber(randomID);
    }

    //method for generating random departure and arrival cities from the destinations array
    public String[][] randomDestinationsGenerator() {
        Random random = new Random();

        //create random destination and arrival city
        int randomCity1 = random.nextInt(destinations.length);
        int randomCity2 = random.nextInt(destinations.length);

        //get departure city details
        String departureCity = destinations[randomCity1][0];
        String departureCityLatitude = destinations[randomCity1][1];
        String departureCityLongitude = destinations[randomCity1][2];

        //preventing the similarity between departure city and arrival city
        while (randomCity2 == randomCity1) {
            randomCity2 = random.nextInt(destinations.length);
        }

        //get arrival city details
        String arrivalCity = destinations[randomCity2][0];
        String arrivalCityLatitude = destinations[randomCity2][1];
        String arrivalCityLongitude = destinations[randomCity2][2];

        //create a new array containing all the data - departure and arrival city
        String[][] chosenDestinations = new String[2][3];
        chosenDestinations[0][0] = departureCity;
        chosenDestinations[0][1] = departureCityLatitude;
        chosenDestinations[0][2] = departureCityLongitude;
        chosenDestinations[1][0] = arrivalCity;
        chosenDestinations[1][1] = arrivalCityLatitude;
        chosenDestinations[1][2] = arrivalCityLongitude;

        return chosenDestinations;
    }

    //method for generating random number of seats for each flight
    public int randomNumOfSeats() {
        Random random = new Random();

        //set a 500 number seats bound
        int numOfSeats = random.nextInt(500);

        //ensure 100-500 number of seats for each flight
        while (numOfSeats < 100) {
            numOfSeats = random.nextInt(500);
        }
        return numOfSeats;
    }

    //method for generating a unique flight number
    public String randomFlightNumberGenerator(int letter, int divisible) {
        Random random = new Random();
        StringBuilder flightNumber = new StringBuilder();
        for (int i=0; i<letter; i++) {
            //generate a random number between 0 and 25 and add to 'a' to get a random lowercase letter
            flightNumber.append((char) (random.nextInt(26) + 'a'));
        }

        //append a random number of seats divided by the divisible value
        flightNumber.append("-").append(randomNumOfSeats() / divisible);
        return flightNumber.toString();
    }

    //setter
    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }

    //getter
    public String getRandomNumber(){
        return randomNumber;
    }

    //method for getting destinations array from the text file
    public String[][] getDestinations() {
        String[][] destinations = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("destination.txt"))) {
            //read the number of rows in the destination.txt file
            long rowCount = reader.lines().count();

            //reset the reader to read from the beginning of the file
            reader.close();
            BufferedReader newReader = new BufferedReader(new FileReader("destination.txt"));

            //read the data into the destination array
            destinations = new String[(int)rowCount][];
            String line;
            int row = 0;

            //add the data into the destination array
            while ((line = newReader.readLine()) != null) {
                String[] rowData = line.split(",");
                destinations[row] = rowData;
                row++;
            }
        } catch (IOException e) {
            System.out.println("File could not be accessed");
        }

        return destinations;
    }
}
