package com.stackunderflow.pizzasystem.ui.customer;

import com.stackunderflow.pizzasystem.data.MenuDataManager;
import com.stackunderflow.pizzasystem.model.Cart;
import com.stackunderflow.pizzasystem.model.MenuItem;
import com.stackunderflow.pizzasystem.ui.customer.CustomPizzaController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MenuController {

    @FXML
    private VBox menuContainer;
    @FXML
    private Label totalLabel;
    @FXML
    private Label countLabel;

    // Shared Data (Singleton Pattern for the Cart is recommended here)
    private static Cart currentCart = new Cart(); 
    private final int MAX_PIZZAS = 5;

    @FXML
    public void initialize() {
        loadMenuData();
        updateLabels();
    }

    private void loadMenuData() {
        // 1. Get data from Backend (Task P1.3)
        MenuDataManager dataManager = new MenuDataManager();
        List<MenuItem> items = dataManager.loadAllMenuItems();

        // 2. Dynamically create a UI row for each item found in the DB
        for (MenuItem item : items) {
            HBox row = createItemRow(item);
            menuContainer.getChildren().add(row);
        }
    }

    private HBox createItemRow(MenuItem item) {
        // Image
        ImageView img = loadImage(item.getName());

        // Text Info
        VBox infoBox = new VBox(5);
        Label nameLbl = new Label(item.getName());
        nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label priceLbl = new Label("$" + String.format("%.2f", item.getBasePrice()));
        infoBox.getChildren().addAll(nameLbl, priceLbl);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        // Quantity Spinner (1-10)
        Spinner<Integer> qtySpinner = new Spinner<>(1, 10, 1);
        qtySpinner.setPrefWidth(60);

        // Add Button
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            handleAddItem(item, qtySpinner.getValue());
        });

        // Layout Container for Row
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10));
        row.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: white;");
        row.getChildren().addAll(img, infoBox, new Label("   "), qtySpinner, addButton);
        
        return row;
    }
    private void openCustomizationScreen(MenuItem baseItem) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("custom-pizza-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Pass the selected pizza to the new controller
            CustomPizzaController controller = fxmlLoader.getController();
            controller.setBasePizza(baseItem);

            Stage stage = new Stage();
            stage.setTitle("Customize: " + baseItem.getName());
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open customization screen.");
            alert.show();
        }
    }
    private void handleAddItem(MenuItem item, int quantity) {
        // 1. Check Pizza Constraint (Max 5)
        long currentPizzaCount = currentCart.getItems().stream()
                .filter(i -> "Pizza".equalsIgnoreCase(i.getCategory())).count();

        if ("Pizza".equalsIgnoreCase(item.getCategory()) && (currentPizzaCount + quantity > MAX_PIZZAS)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You can only order up to 5 pizzas total.");
            alert.show();
            return; // Stop everything if limit reached
        }

        // 2. TRAFFIC CONTROL: Is it a Pizza?
        if ("Pizza".equalsIgnoreCase(item.getCategory())) {
            // --- CASE A: PIZZA (Open Customization) ---
            openCustomizationScreen(item);
        } else {
            // --- CASE B: DRINK/SIDE (Add Directly) ---
            for(int i = 0; i < quantity; i++) {
                currentCart.addItem(item);
            }
            updateLabels();
            Alert confirm = new Alert(Alert.AlertType.INFORMATION);
            confirm.setTitle("Cart Update");
            confirm.setHeaderText(null); // Removes the header for a cleaner look
            confirm.setContentText(quantity + " x " + item.getName() + " added to your cart!");
            confirm.show();}
        }

    private void updateLabels() {
        double total = currentCart.calculateSubtotal();
        long pizzaCount = currentCart.getItems().stream()
                .filter(i -> "Pizza".equalsIgnoreCase(i.getCategory())).count();
        
        totalLabel.setText("Total: $" + String.format("%.2f", total));
        countLabel.setText("Pizzas Ordered: " + pizzaCount + " / " + MAX_PIZZAS);
    }

    private ImageView loadImage(String name) {
        try {
            // Tries to find "Cheese Pizza" as "cheesepizza.png" in your pageImages folder
            String filename = name.toLowerCase().replaceAll("\\s+", "") + ".png";
            String path = "pageImages/" + filename;
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView view = new ImageView(img);
            view.setFitHeight(60);
            view.setFitWidth(60);
            view.setPreserveRatio(true);
            return view;
        } catch (Exception e) {
            return new ImageView(); // Return empty image if not found
        }
    }

    @FXML
    private void handleBack() throws IOException {
        // Navigate back to Homepage
        Stage stage = (Stage) menuContainer.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homepage-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}