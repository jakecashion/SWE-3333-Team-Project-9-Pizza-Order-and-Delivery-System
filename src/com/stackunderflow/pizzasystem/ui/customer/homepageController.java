package com.stackunderflow.pizzasystem.ui.customer;


import com.stackunderflow.pizzasystem.data.MenuDataManager;
import com.stackunderflow.pizzasystem.model.MenuItem;
import java.util.List;
import javafx.fxml.FXML;
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
import java.util.Optional;


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
    @FXML
    void visitOrderPage(ActionEvent event) throws IOException {
        // 1. Create a Dialog to ask for Order Type (FR-008)
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Pickup", "Pickup", "Delivery");
        dialog.setTitle("Start Order");
        dialog.setHeaderText("How would you like to receive your order?");
        dialog.setContentText("Choose option:");

        // 2. Show the dialog and wait for an answer
        Optional<String> result = dialog.showAndWait();

        // 3. If they picked something, save it and go to the Menu
        if (result.isPresent()){
            String orderType = result.get();
            
            // TODO: In the future, save this 'orderType' to your Cart/Order model!
            System.out.println("Starting new order for: " + orderType); 
            
            // Now reuse your existing method to go to the menu
            visitMenuPage(event); 
        }
    }
        @FXML
    public void visitMenuPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Menu - Mom & Pop's Pizzeria");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void handleCustomPizza(ActionEvent event) {
        // 1. Fetch menu items to find the "Custom Pizza" object
        MenuDataManager dataManager = new MenuDataManager();
        List<MenuItem> items = dataManager.loadAllMenuItems();
        
        // 2. Find the specific item (must match the name in DatabaseSeeder)
        MenuItem customBase = null;
        for (MenuItem item : items) {
            if ("Custom Pizza".equalsIgnoreCase(item.getName())) {
                customBase = item;
                break;
            }
        }

        // 3. Open the customization window
        if (customBase != null) {
            openCustomizationScreen(customBase);
        } else {
            System.err.println("Error: 'Custom Pizza' item not found in database. Did you run the Seeder?");
        }
    }

    private void openCustomizationScreen(MenuItem baseItem) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("custom-pizza-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            
            CustomPizzaController controller = fxmlLoader.getController();
            controller.setBasePizza(baseItem);
            
            Stage stage = new Stage();
            stage.setTitle("Customize: " + baseItem.getName());
            stage.setScene(scene);
            stage.showAndWait(); // Wait for user to finish
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
