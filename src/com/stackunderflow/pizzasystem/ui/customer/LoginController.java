package com.stackunderflow.pizzasystem.ui.customer;

import com.stackunderflow.pizzasystem.data.CustomerDataManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;
import java.io.IOException;



public class LoginController {
    int count = 4;
    //login stuff
    @FXML
    private TextField enterPhonenumber;
    @FXML
    private PasswordField enterPassword;

    // errors
    @FXML
    private Label errorMessagePhonenumber;
    @FXML
    private Label errorMessagePasswordCount;
    @FXML
    private Label errorMessagePhonenumberExist;
    @FXML
    private Label errorMessagePassword;
    @FXML
    private ImageView errorTooManyAttempts;

    //buttons
    @FXML
    private Button submitLogin;
    @FXML
    private Button visitCreatAccountPage;
    @FXML
    private Button visitHomePage;
    @FXML
    private Button visitPrevousPage;


    @FXML
    protected void onvisitSignupPage(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader((getClass().getResource("signUP-view.fxml")));
            Parent signUproot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene singUpScene = new Scene(signUproot);
            stage.setScene(singUpScene);
            stage.setTitle("Mom & Pop's Pizzaria");
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }


    }
    @FXML
    protected void visitHomepage(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader((getClass().getResource("homepage-view.fxml")));
            Parent homepageroot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene homepageScene = new Scene(homepageroot);
            stage.setScene(homepageScene);
            stage.setTitle("Mom & Pop's Pizzaria");
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }


    }

    protected boolean checkPassword(String password){
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

    public boolean checkPhoneNumber(String phonenum){
        int counter = 0;
        for (char c : phonenum.toCharArray()) {
           if (Character.isDigit(c)) {
                counter++;
            }



        }
        return counter == 10;
    }
    @FXML
    protected void submitLoginPress(ActionEvent event) {
        // 1. Reset error messages
        errorMessagePhonenumber.setVisible(false);
        errorMessagePhonenumberExist.setVisible(false);
        errorMessagePassword.setVisible(false);
        visitCreatAccountPage.setVisible(false);

        String inputId = enterPhonenumber.getText();
        String inputPass = enterPassword.getText();

        // 2. Basic Input Validation (Is it empty?)
        if (inputId.isBlank() || inputPass.isBlank()) {
            errorMessagePhonenumber.setText("Please enter phone/user and password.");
            errorMessagePhonenumber.setVisible(true);
            return;
        }

        // 3. Call the Database to Verify
        CustomerDataManager dataManager = new CustomerDataManager();
        String result = dataManager.validateLogin(inputId, inputPass);

        if (result.equals("Login Success")) {
            System.out.println("✅ Login Successful! Redirecting...");
            // Proceed to Homepage
            visitHomepage(event);
        } else if (result.contains("User not found")) {
            // User doesn't exist
            errorMessagePhonenumberExist.setVisible(true);
            visitCreatAccountPage.setVisible(true);
        } else if (result.contains("Invalid password")) {
            // Wrong password logic
            count--;
            errorMessagePassword.setVisible(true);
            errorMessagePasswordCount.setVisible(true);
            errorMessagePasswordCount.setText("Remaining attempts: " + count);

            if (count <= 0) {
                errorTooManyAttempts.setVisible(true);
                errorTooManyAttempts.toFront();
                submitLogin.setDisable(true); // Stop them from clicking again
            }
        } else {
            // Database error
            System.err.println(result);
        }
    }

}