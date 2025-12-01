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
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class MenuController {

    @FXML private VBox menuContainer;
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
            HBox row = createItemRow(item);
            menuContainer.getChildren().add(row);
        }
    }

    private HBox createItemRow(MenuItem item) {
        // 1. Image
        ImageView img = loadImage(item.getName());

        // 2. Text Info
        VBox infoBox = new VBox(5);
        Label nameLbl = new Label(item.getName());
        nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label priceLbl = new Label("$" + String.format("%.2f", item.getBasePrice()));
        infoBox.getChildren().addAll(nameLbl, priceLbl);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        // 3. Quantity Spinner
        Spinner<Integer> qtySpinner = new Spinner<>(1, 10, 1);
        qtySpinner.setPrefWidth(60);

        // 4. Add Button
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> handleAddItem(item, qtySpinner.getValue()));

        // 5. Layout
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10));
        row.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: white;");
        row.getChildren().addAll(img, infoBox, new Label("   "), qtySpinner, addButton);
        
        return row;
    }

    private void handleAddItem(MenuItem item, int quantity) {
        // A. Handle Pizza (Open Customization P1.7)
        if ("Pizza".equalsIgnoreCase(item.getCategory())) {
            openCustomizationScreen(item);
            return; 
        }

        // B. Handle Drinks/Sides (Add Directly)
        for(int i = 0; i < quantity; i++) {
            currentCart.addItem(item);
        }
        updateLabels();
        
        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
        confirm.setTitle("Added");
        confirm.setHeaderText(null);
        confirm.setContentText(quantity + " x " + item.getName() + " added to cart!");
        confirm.show();
    }
    
    // Helper for P1.7
    private void openCustomizationScreen(MenuItem baseItem) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("custom-pizza-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            CustomPizzaController controller = fxmlLoader.getController();
            controller.setBasePizza(baseItem);

            Stage stage = new Stage();
            stage.setTitle("Customize: " + baseItem.getName());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper for P1.9
    @FXML
    private void handleCheckout() throws IOException {
        if (currentCart.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Your cart is empty!");
            alert.show();
            return;
        }
        
        Stage stage = (Stage) menuContainer.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("order-summary-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Checkout - Mom & Pop's Pizzeria");
        stage.setScene(scene);
    }

    private void updateLabels() {
        double total = currentCart.calculateSubtotal();
        int count = currentCart.getItems().size();
        
        totalLabel.setText("Total: $" + String.format("%.2f", total));
        countLabel.setText("Items Ordered: " + count);
    }

    private ImageView loadImage(String name) {
        try {
            String filename = name.toLowerCase().replaceAll("\\s+", "") + ".png";
            String path = "pageImages/" + filename;
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView view = new ImageView(img);
            view.setFitHeight(60); view.setFitWidth(60); view.setPreserveRatio(true);
            return view;
        } catch (Exception e) {
            return new ImageView(); 
        }
    }

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) menuContainer.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homepage-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}