package com.stackunderflow.pizzasystem.ui.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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


public class homepageController {
    //buttons
    @FXML
    private Button visitAccount;
    @FXML
    private Button visitCustomPizza;
    @FXML
    private Button visitMenuPage;
    @FXML
    private Button vistDrinkPage;
    @FXML
    private  Button visitSidePage;
    @FXML
    private Button visitSpecialsPage;
    @FXML
    private Button vistitOrderPage;
    @FXML
    private Button vistCartpage;
    @FXML
    private Button vistHomepage;


    @FXML
    protected void visitLoginPage(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader((getClass().getResource("Login-view.fxml")));
            Parent loginroot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene loginScene = new Scene(loginroot);
            stage.setScene(loginScene);
            stage.setTitle("Mom & Pop's Pizzaria");
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }


    }

}
