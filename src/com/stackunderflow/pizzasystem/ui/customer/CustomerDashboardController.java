package com.stackunderflow.pizzasystem.ui.customer;

import com.stackunderflow.pizzasystem.data.CustomerDataManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class CustomerDashboardController {

    @FXML private Label usernameLabel;
    @FXML private Label nameLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    @FXML private javafx.scene.image.ImageView profileIcon;

    private final CustomerDataManager dataManager = new CustomerDataManager();
    private String currentUserPhone;




    public void loadCustomer(String phone) {
        this.currentUserPhone = phone;


        var customer = dataManager.getCustomerByPhone(phone);

        usernameLabel.setText(customer.getUsername());
        nameLabel.setText(customer.getFirstName() + " " + customer.getLastName());
        phoneLabel.setText(customer.getPhone());
        addressLabel.setText(customer.getAddress());

        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        phoneField.setText(customer.getPhone());
        addressField.setText(customer.getAddress());
    }

    @FXML
    private void handleSave() {
        String newFirst = firstNameField.getText();
        String newLast = lastNameField.getText();
        String newPhone = phoneField.getText();
        String newAddress = addressField.getText();

        dataManager.updateCustomer(Integer.parseInt(currentUserPhone), newFirst, newLast, newPhone, newAddress);

        loadCustomer(newPhone);

        Alert a = new Alert(Alert.AlertType.INFORMATION, "Account updated successfully!");
        a.show();
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
