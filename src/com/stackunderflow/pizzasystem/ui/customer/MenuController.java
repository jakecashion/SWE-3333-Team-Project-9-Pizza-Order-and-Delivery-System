package com.stackunderflow.pizzasystem.ui.customer;

import com.stackunderflow.pizzasystem.data.MenuDataManager;
import com.stackunderflow.pizzasystem.model.Cart;
import com.stackunderflow.pizzasystem.model.MenuItem;
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
import javafx.scene.layout.Priority; // ✅ Added for Spacer
import javafx.scene.layout.Region;   // ✅ Added for Spacer
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class MenuController {

    @FXML private HBox pizzaContainer;
    @FXML private HBox drinkContainer;
    @FXML private HBox sideContainer;
    
    @FXML private Label totalLabel;
    @FXML private Label countLabel;
    @FXML private Button checkoutButton;

    private static Cart currentCart = Cart.getInstance(); 

    @FXML
    public void initialize() {
        loadMenuData();
        updateLabels();
    }

    private void loadMenuData() {
        MenuDataManager dataManager = new MenuDataManager();
        List<MenuItem> items = dataManager.loadAllMenuItems();

        for (MenuItem item : items) {
            VBox card = createItemCard(item);
            
            String category = item.getCategory();
            
            if ("Pizza".equalsIgnoreCase(category)) {
                pizzaContainer.getChildren().add(card);
            } 
            else if ("Drink".equalsIgnoreCase(category)) {
                drinkContainer.getChildren().add(card);
            } 
            else {
                sideContainer.getChildren().add(card);
            }
        }
    }

    private VBox createItemCard(MenuItem item) {
        // 1. Image
        ImageView img = loadImage(item.getName());
        img.setFitHeight(140);
        img.setFitWidth(140);

        // 2. Name Label (FIXED HEIGHT for alignment)
        Label nameLbl = new Label(item.getName());
        nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");
        nameLbl.setWrapText(true);
        nameLbl.setAlignment(Pos.CENTER);
        nameLbl.setPrefHeight(45); // ✅ Forces 2 lines of space so names align
        nameLbl.setMinHeight(45);

        // 3. Price Label
        Label priceLbl = new Label("$" + String.format("%.2f", item.getBasePrice()));
        priceLbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        // ✅ 4. SPACER (Pushes button to bottom)
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // 5. Add Button
        Button addButton = new Button("ADD");
        String defaultStyle = "-fx-background-color: white; -fx-border-color: #ff6347; -fx-border-width: 2; -fx-text-fill: #ff6347; -fx-font-weight: bold; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: #ff6347; -fx-text-fill: white; -fx-font-weight: bold;";
        
        addButton.setStyle(defaultStyle);
        addButton.setPrefWidth(100);
        
        addButton.setOnMouseEntered(e -> addButton.setStyle(hoverStyle));
        addButton.setOnMouseExited(e -> addButton.setStyle(defaultStyle));

        addButton.setOnAction(e -> handleAddItem(item));

        // 6. Card Container
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));
        card.setPrefSize(200, 300); // Fixed size
        
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10;");
        
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        shadow.setRadius(5);
        shadow.setOffsetY(3);
        card.setEffect(shadow);

        // Add spacer before the button
        card.getChildren().addAll(img, nameLbl, priceLbl, spacer, addButton);
        return card;
    }

    private void handleAddItem(MenuItem item) {
        if ("Pizza".equalsIgnoreCase(item.getCategory())) {
            openCustomizationScreen(item);
            return; 
        }
        currentCart.addItem(item);
        updateLabels();
        
        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
        confirm.setTitle("Added");
        confirm.setHeaderText(null);
        confirm.setContentText(item.getName() + " added to cart!");
        confirm.show();
    }

    private void updateLabels() {
        double total = currentCart.calculateSubtotal();
        int count = currentCart.getItems().size();
        totalLabel.setText("Total: $" + String.format("%.2f", total));
        countLabel.setText("Items: " + count);
    }
    
    private ImageView loadImage(String name) {
        try {
            String filename = name.toLowerCase().replaceAll("\\s+", "") + ".png";
            String path = "pageImages/" + filename;
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView view = new ImageView(img);
            view.setPreserveRatio(true);
            return view;
        } catch (Exception e) {
            return new ImageView(); 
        }
    }
    
    @FXML
    private void handleCheckout() throws IOException {
         if (currentCart.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Your cart is empty!");
            alert.show();
            return;
        }
        Stage stage = (Stage) pizzaContainer.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("order-summary-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Checkout - Mom & Pop's Pizzeria");
        stage.setScene(scene);
    }

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) pizzaContainer.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homepage-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
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
            stage.showAndWait();
            updateLabels(); // Update cart labels after customization
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}