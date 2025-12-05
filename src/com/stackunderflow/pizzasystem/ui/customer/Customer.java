package com.stackunderflow.pizzasystem.ui.customer;

public class Customer {

    private int customerId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;


    public Customer(int customerId, String username, String firstName,
                    String lastName, String phoneNumber, String address) {

        this.customerId = customerId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }



    // Getters
    public int getCustomerId() { return customerId; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phoneNumber; }
    public String getAddress() { return address; }

    // Setters
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setUsername(String username) { this.username = username; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address) { this.address = address; }
}
