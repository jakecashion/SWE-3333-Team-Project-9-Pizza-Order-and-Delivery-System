package com.stackunderflow.pizzasystem.ui.controllers;

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
        errorMessagePhonenumber.setVisible(false);
        errorMessagePhonenumberExist.setVisible(false);
        visitCreatAccountPage.setVisible(false);
        errorTooManyAttempts.setVisible(false);
        errorTooManyAttempts.toBack();


        if(enterPhonenumber.getText().isBlank() || enterPhonenumber.getLength() != 10 || !checkPhoneNumber((enterPhonenumber.getText()))){
            errorMessagePhonenumber.setVisible(true);
        } else if(checkPhoneNumber(enterPhonenumber.getText())){//if phone number is not in database
            errorMessagePhonenumberExist.setVisible(true);
            visitCreatAccountPage.setVisible(true);
        }
        if(checkPassword(enterPassword.getText())){
            count--;
            errorMessagePassword.setVisible(true);
            errorMessagePasswordCount.setVisible(true);
            errorMessagePasswordCount.setText("Remaining attempts: "+ count);
            

            if (count == 0){
                errorTooManyAttempts.setVisible(true);
                errorTooManyAttempts.toFront();

            }
        }
    }

}