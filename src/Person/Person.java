package Person;

import FileHandler.RandomGenerator;

public class Person {
    //fields
    private String userID;
    private String name;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;

    //constructor for Person Class
    public Person() {
        this.userID = null;
        this.name = null;
        this.email = null;
        this.password = null;
        this.address = null;
        this.phoneNumber = null;
    }

    //constructor made for Admin Class
    public Person(String name, String password, String email) {
        this.userID = null;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = null;
        this.phoneNumber = null;
    }

    //constructor made for Passenger Class
    public Person(String name, String email, String password, String address, String phoneNumber) {
        //object of RandomGenerator is being used to assign unique userID to the newly created customer
        RandomGenerator random = new RandomGenerator();
        random.randomIDGenerator();
        this.userID = random.getRandomNumber();
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    //constructor made if userID already exists
    public Person(String userID, String name, String email, String password, String address, String phoneNumber) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //getters
    public String getUserID() {
        return userID;
    }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phoneNumber;
    }

    public String toString(int index) {
        return String.format("%10s| %-10d | %-10s | %-32s | %-27s | %-35s | %-23s |", "", index+1, randomIDDisplay(userID), name, email, address, phoneNumber);
    }

    //display userID with space (ex: 123 456)
    public String randomIDDisplay(String userID) {
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i <= userID.length(); i++) {
            //add space when the length reaches 4
            if (i == 3) {
                newString.append(" ").append(userID.charAt(i));
            } else if (i < userID.length()) {
                newString.append(userID.charAt(i));
            }
        }
        return newString.toString();
    }
}
