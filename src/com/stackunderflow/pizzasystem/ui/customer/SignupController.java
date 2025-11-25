package com.stackunderflow.pizzasystem.ui.customer;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable{
    //Sign up Fields
    @FXML
    private TextField enterName;
    @FXML
    private PasswordField enterPassword;
    @FXML
    private TextField enterPhonenumber;
    @FXML
    private TextField enterStreetAddress;
    @FXML
    private TextField enterCity;
    @FXML
    private ChoiceBox<String> stateSelector;
    private String selectedState;

    //error labels
    @FXML
    private Label errorPasswordCreationp1;
    @FXML
    private Label errorPasswordCreationp2;
    @FXML
    private Label errorPasswordCreationp3;
    @FXML
    private Label errorPasswordCreationp4;
    @FXML
    private Label errorPasswordCreationp5;
    @FXML
    private Label errorMessageName;
    @FXML
    private Label errorMessagePhonenumberTaken;
    @FXML
    private Label errorMessagePhonenumberInvalid;
    @FXML
    private Label errorMessageStreetAddress;
    @FXML
    private Label errorMessageCity;
    @FXML
    private Label errorMessageState;

    //Buttons
    @FXML
    private Button submitAccountCreation;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // List of 50 US states (alphabetical)
        ObservableList<String> states = FXCollections.observableArrayList(
                "Select a State","Alabama", "Alaska", "Arizona", "Arkansas", "California",
                "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
                "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa",
                "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
                "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri",
                "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey",
                "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio",
                "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
                "South Dakota", "Tennessee", "Texas", "Utah", "Vermont",
                "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"
        );

        stateSelector.setItems(states);
        stateSelector.setValue("Select a State");

        stateSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {selectedState = (newValue);});
    }

    protected boolean checkIncorrectPassword(String password){
        if (password == null || password.isEmpty()) {
            return true;
        }


        if (password.length() < 8) {
            return true;
        }

        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;


        String specialCharacters = "!@#$%&*_?";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (specialCharacters.indexOf(c) != -1) {
                hasSpecialChar = true;
            }


            if (hasUppercase && hasDigit && hasSpecialChar) {
                return false; // Password is valid
            }
        }


        return true;
    }

    @FXML
    protected void onsumbitAccountCreation(ActionEvent event){
        errorMessageName.setVisible(false);
        errorPasswordCreationp1.setVisible(false);
        errorPasswordCreationp2.setVisible(false);
        errorPasswordCreationp3.setVisible(false);
        errorPasswordCreationp4.setVisible(false);
        errorPasswordCreationp5.setVisible(false);
        errorMessageCity.setVisible(false);
        errorMessageStreetAddress.setVisible(false);
        errorMessagePhonenumberInvalid.setVisible(false);
        errorMessageState.setVisible(false);


        if(enterName.getText().isBlank()){
            errorMessageName.setVisible(true);
        }
        if(enterPassword.getText().isBlank() || checkIncorrectPassword(enterPassword.getText())){
            errorPasswordCreationp1.setVisible(true);
            errorPasswordCreationp2.setVisible(true);
            errorPasswordCreationp3.setVisible(true);
            errorPasswordCreationp4.setVisible(true);
            errorPasswordCreationp5.setVisible(true);
        }
        if(enterCity.getText().isBlank()){
            errorMessageCity.setVisible(true);
        }
        if(enterStreetAddress.getText().isBlank()){
            errorMessageStreetAddress.setVisible(true);
        }
        if(enterPhonenumber.getText().isBlank()){
            errorMessagePhonenumberInvalid.setVisible(true);
        }
        if(selectedState.equals("Select a State")){
            errorMessageState.setVisible(true);
        }


    }
}
