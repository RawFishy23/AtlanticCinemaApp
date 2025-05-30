import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

public class AppView {
    public static void getCheckoutScene(AppController controller)
    {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Checkout");

        VBox finalizeLayout = new VBox(10);
        finalizeLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label finalizeLabel = new Label("Finalize Order");
        finalizeLabel.setStyle("-fx-font-size: 18;");

        Label totalPriceLabel = new Label("Total Price: $" + controller.order.getTotalPrice());
        Button completeButton = new Button("Complete Purchase");
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> dialog.close());

        completeButton.setOnAction(e -> {
            if (controller.order.getTotalPrice() <= controller.customer.getMoney()) {
                controller.customer.updateMoney((int) controller.order.getTotalPrice());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Purchase Complete");
                alert.setHeaderText(null);
                alert.setContentText("Order finalized! Thank you for your purchase.");
                alert.showAndWait();

                controller.finishedOrders.add(controller.order);

                controller.order = new AppModel.Order();
                
                controller.updateHomeMenu();
                dialog.close();
                
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insufficient Funds");
                alert.setHeaderText(null);
                alert.setContentText("You do not have enough money to complete this purchase.");
                alert.showAndWait();
            }
        });

        finalizeLayout.getChildren().addAll(finalizeLabel, totalPriceLabel, completeButton, backButton);

        dialog.setScene(new Scene(finalizeLayout));
        dialog.showAndWait();
    }

    public static void getBuySnacksScene(AppController controller)
    {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Buy Snacks");
    
        Label snackTypeLabel = new Label("Select Snack Type:");
        ToggleGroup snackTypeGroup = new ToggleGroup();
        VBox snackTypeRadioButtons = new VBox(5);
        RadioButton popcornButton = new RadioButton("Popcorn");
        popcornButton.setToggleGroup(snackTypeGroup);
        RadioButton sodaButton = new RadioButton("Soda");
        sodaButton.setToggleGroup(snackTypeGroup);
        snackTypeRadioButtons.getChildren().addAll(popcornButton, sodaButton);
    
        Label sizeFlavorLabel = new Label("Select Size / Flavor:");
        ToggleGroup sizeFlavorGroup = new ToggleGroup();
        VBox sizeFlavorRadioButtons = new VBox(5);
    

        HBox quantityRow = new HBox(10);
        Label quantityLabel = new Label("Enter Quantity:");
        TextField quantityField = new TextField();
        TextField totalField = new TextField("0");

        Button addToCartButton = new Button("Add to Cart");
        Button closeButton = new Button("Close");

        snackTypeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            sizeFlavorRadioButtons.getChildren().clear();
            quantityRow.setVisible(false);
            totalField.setVisible(false);
            totalField.setText("Total $0.0");
            addToCartButton.setVisible(false);
            quantityField.setText("0");
            if (newToggle == popcornButton) {
                for (AppModel.Flavor flavor : AppModel.Flavor.values()) {
                    RadioButton rb = new RadioButton(flavor.name());
                    rb.setToggleGroup(sizeFlavorGroup);
                    rb.setUserData(flavor);
                    sizeFlavorRadioButtons.getChildren().add(rb);
                }
            } else if (newToggle == sodaButton) {
                for (AppModel.Size size : AppModel.Size.values()) {
                    RadioButton rb = new RadioButton(size.name());
                    rb.setToggleGroup(sizeFlavorGroup);
                    rb.setUserData(size);
                    sizeFlavorRadioButtons.getChildren().add(rb);
                }
            }
        });

        sizeFlavorGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            quantityRow.setVisible(true);
            totalField.setVisible(true);
            addToCartButton.setVisible(true);
            quantityField.setText("0");
            totalField.setText("Total $0.0");
        });
    
        totalField.setVisible(false);
        quantityRow.setVisible(false);
        addToCartButton.setVisible(false);
        quantityField.setText("0");
        quantityField.setEditable(false);
        totalField.setEditable(false);
        
        Button quantityPlusButton = new Button("+");
        Button quantityMinusButton = new Button("-");


        quantityPlusButton.setOnAction(e -> {
            int value = Integer.parseInt(quantityField.getText());
            value = value + 1;

            quantityField.setText(String.valueOf(value));

            RadioButton selectedSnackType = (RadioButton) snackTypeGroup.getSelectedToggle();
            RadioButton selectedSizeFlavor = (RadioButton) sizeFlavorGroup.getSelectedToggle();

            double totalPrice = Double.parseDouble(quantityField.getText());
            if (selectedSnackType == popcornButton) {
                AppModel.Flavor flavor = (AppModel.Flavor) selectedSizeFlavor.getUserData();

                double basePrice = AppModel.Popcorn.BASE_PRICE;
                totalPrice = value * basePrice;

            } else if (selectedSnackType == sodaButton) {
                AppModel.Size size = (AppModel.Size) selectedSizeFlavor.getUserData();

                double basePrice = AppModel.Soda.REGULAR_PRICE;

                if (size.equals(AppModel.Size.JUMBO)) {
                    basePrice = AppModel.Soda.JUMBO_PRICE;
                }
                totalPrice = value * basePrice;
            }
            totalField.setText("Total $" + String.valueOf(totalPrice));
        });


        quantityMinusButton.setOnAction(e -> {
            int value = Integer.parseInt(quantityField.getText());
            value = value - 1;

            if (value <= 0) {
                quantityField.setText(String.valueOf(0));
            } else {
                quantityField.setText(String.valueOf(value));
            }

            RadioButton selectedSnackType = (RadioButton) snackTypeGroup.getSelectedToggle();
            RadioButton selectedSizeFlavor = (RadioButton) sizeFlavorGroup.getSelectedToggle();

            double totalPrice = Double.parseDouble(quantityField.getText());
            if (selectedSnackType == popcornButton) {
                AppModel.Flavor flavor = (AppModel.Flavor) selectedSizeFlavor.getUserData();

                double basePrice = AppModel.Popcorn.BASE_PRICE;
                totalPrice = value * basePrice;

            } else if (selectedSnackType == sodaButton) {
                AppModel.Size size = (AppModel.Size) selectedSizeFlavor.getUserData();

                double basePrice = AppModel.Soda.REGULAR_PRICE;

                if (size.equals(AppModel.Size.JUMBO)) {
                    basePrice = AppModel.Soda.JUMBO_PRICE;
                }
                totalPrice = value * basePrice;
            }
            totalField.setText("Total $" + String.valueOf(totalPrice));

        });

        quantityRow.getChildren().addAll(quantityMinusButton, quantityField, quantityPlusButton);
    
        addToCartButton.setOnAction(e -> {
            try {
                RadioButton selectedSnackType = (RadioButton) snackTypeGroup.getSelectedToggle();
                RadioButton selectedSizeFlavor = (RadioButton) sizeFlavorGroup.getSelectedToggle();
                int quantity = Integer.parseInt(quantityField.getText());
    
                if (selectedSnackType == null || selectedSizeFlavor == null || quantity <= 0) {
                    throw new IllegalArgumentException();
                }
    
                if (selectedSnackType == popcornButton) {
                    AppModel.Flavor flavor = (AppModel.Flavor) selectedSizeFlavor.getUserData();
                    controller.order.addItem(new AppModel.Popcorn(flavor, quantity));

                } else if (selectedSnackType == sodaButton) {
                    AppModel.Size size = (AppModel.Size) selectedSizeFlavor.getUserData();
                    controller.order.addItem(new AppModel.Soda(size, quantity));
                }

                controller.updateHomeMenu();
                dialog.close();
    
                controller.showAlert(Alert.AlertType.INFORMATION, "Snack added to cart!");
            } catch (Exception ex) {
                controller.showAlert(Alert.AlertType.ERROR, "Minimum 1 item to purchase!");
            }
        });
    
        closeButton.setOnAction(e -> {
            dialog.close();
        });

    
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
            snackTypeLabel, snackTypeRadioButtons,
            sizeFlavorLabel, sizeFlavorRadioButtons,
            quantityLabel, quantityRow,
            totalField,
            addToCartButton, closeButton
        );
    
        dialog.setScene(new Scene(layout));
        dialog.showAndWait();
    }
    public static void getCartScene(AppController controller)
    {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("View Cart");

        VBox cartLayout = new VBox(10);
        cartLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

    
        Label cartLabel = new Label("Cart");
        cartLabel.setStyle("-fx-font-size: 18;");

        TextArea cartTextArea = new TextArea(controller.order.toString());
        cartTextArea.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            dialog.close();
        });

        controller.payableItemListView = new ListView<>();
        controller.payableItemListView.getItems().clear();
        double total = 0;
        for (AppModel.PayableItem payableItem : controller.order.getPayableItems()) {
            total = total + payableItem.getPrice();
            controller.payableItemListView.getItems().add(payableItem.toString() + String.valueOf(payableItem.getPrice()));
        }

        cartLayout.getChildren().addAll(cartLabel, cartTextArea, backButton);

        dialog.setScene(new Scene(cartLayout));
        dialog.showAndWait();
    }
    public static void getShowMoviesScene(AppController controller)
    {

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Select Genre");

        VBox genreLayout = new VBox(10);
        genreLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label genreLabel = new Label("Select a Genre");
        genreLabel.setStyle("-fx-font-size: 18;");

        ToggleGroup genreGroup = new ToggleGroup();
        VBox genreRadioButtons = new VBox(5);
        for (AppModel.Genre genre : AppModel.Genre.values()) {
            RadioButton rb = new RadioButton(genre.name());
            rb.setToggleGroup(genreGroup);
            genreRadioButtons.getChildren().add(rb);
        }

        Button backButton = new Button("Apply");
        backButton.setOnAction(e -> {
            controller.updateHomeMenu();
            dialog.close();
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> {
            controller.selectedStudios.clear();
            controller.updateHomeMenu();
            dialog.close();
        });

        controller.movieListView = new ListView<>();

        genreGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                AppModel.Genre selectedGenre = AppModel.Genre.valueOf(((RadioButton) newToggle).getText());
                List<AppModel.Movie> genreMovies = controller.movies.get(selectedGenre);
                controller.movieListView.getItems().clear();
                controller.selectedStudios.clear();
                if (!selectedGenre.equals(AppModel.Genre.ALL)) {
                    for (AppModel.Movie movie : genreMovies) {
                        controller.movieListView.getItems().add(movie.toString());
                        for (AppModel.Studios studios : controller.studios) {
                            if (studios.getMovie().getMovieName() == movie.getMovieName()) {
                                controller.selectedStudios.add(studios);
                            }
                        }
                    }
                }

            }
        });

        genreLayout.getChildren().addAll(genreLabel, genreRadioButtons, backButton, resetButton);
        dialog.setScene(new Scene(genreLayout));
        dialog.showAndWait();

    }

    public static void getBuySelectedTicketScene(AppController controller)
    {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Buy Tickets");

        VBox ticketLayout = new VBox(10);
        ticketLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label ticketLabel = new Label(controller.selectedBuyStudios.toString());
        ticketLabel.setStyle("-fx-font-size: 18;");

        TextField quantityField = new TextField("1");
        quantityField.setEditable(false);
        Button quantityPlusButton = new Button("+");
        Button quantityMinusButton = new Button("-");

        HBox quantityRow = new HBox(10);
        quantityRow.setStyle("-fx-padding: 10; -fx-alignment: center;");

        quantityPlusButton.setOnAction(e -> {
            int value = Integer.parseInt(quantityField.getText());
            value = value + 1;

            quantityField.setText(String.valueOf(value));
        });

        quantityMinusButton.setOnAction(e -> {
            int value = Integer.parseInt(quantityField.getText());
            value = value - 1;

            if (value <= 1) {
                quantityField.setText(String.valueOf(1));
            } else {
                quantityField.setText(String.valueOf(value));
            }

        });

        quantityRow.getChildren().addAll(quantityMinusButton, quantityField, quantityPlusButton);

        Button addButton = new Button("Add to Cart");
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> dialog.close());

        addButton.setOnAction(e -> {
            AppModel.Studios selectedStudio = controller.selectedBuyStudios;
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity > 0) {
                for (int i = 0; i < quantity; i++) {
                    controller.order.addItem(selectedStudio);
                }
                controller.updateHomeMenu();
                dialog.close();
                controller.showAlert(Alert.AlertType.INFORMATION, "Added " + quantity + " ticket(s) to cart");
            }
        });
        
        ticketLayout.getChildren().addAll(ticketLabel, quantityRow, addButton, backButton);
        dialog.setScene(new Scene(ticketLayout));
        dialog.showAndWait();
    }

    public static void getBuyTicketScene(AppController controller)
    {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Buy Tickets");

        VBox ticketLayout = new VBox(10);
        ticketLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label ticketLabel = new Label("Select a Movie");
        ticketLabel.setStyle("-fx-font-size: 18;");

        ToggleGroup studiosGroup = new ToggleGroup();
        VBox studiosRadioButtons = new VBox(5);
        for (AppModel.Studios studio : controller.studios) {
            RadioButton rb = new RadioButton(studio.getMovie().getMovieName() + " - " + studio.getStudioName());
            rb.setToggleGroup(studiosGroup);
            rb.setUserData(studio);
            studiosRadioButtons.getChildren().add(rb);
        }

        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");

        Button addButton = new Button("Add to Cart");
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> dialog.close());

        addButton.setOnAction(e -> {
            RadioButton selectedRadioButton = (RadioButton) studiosGroup.getSelectedToggle();
            if (selectedRadioButton != null) {
                AppModel.Studios selectedStudio = (AppModel.Studios) selectedRadioButton.getUserData();
                int quantity = Integer.parseInt(quantityField.getText());
                if (quantity > 0) {
                    for (int i = 0; i < quantity; i++) {
                        controller.order.addItem(selectedStudio);
                    }
                    controller.updateHomeMenu();
                    dialog.close();
                    controller.showAlert(Alert.AlertType.INFORMATION, "Added " + quantity + " ticket(s) to cart");
                }
            }
        });

        ticketLayout.getChildren().addAll(ticketLabel, studiosRadioButtons, quantityField, addButton, backButton);
        dialog.setScene(new Scene(ticketLayout));
        dialog.showAndWait();
    }
    
    public static Scene getMainMenuScene(AppController controller)
    {
        controller.mainMenu = new VBox(10);
        controller.mainMenu.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label welcomeLabel = new Label("Atlantic Cinema");
        welcomeLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        controller.mainMenu.getChildren().add(welcomeLabel);

        HBox firstRow = new HBox(10);
        VBox mainLayout = new VBox(15);

        controller.viewMoviesButton = new Button("View Movies by Genre");
        controller.buyTicketsButton = new Button("Buy Tickets");
        controller.buySnacksButton = new Button("Buy Snacks");
        controller.viewCartButton = new Button("View Cart");
        controller.finalizeOrderButton = new Button("Finalize Order");

        double totalSpent = 0;
        for (AppModel.PayableItem payableItem : controller.order.getPayableItems()) {
            totalSpent = totalSpent + payableItem.getPrice();
        }
        double remainingMoney = controller.customer.getMoney();
        Label isWeekendLabel = new Label(controller.isWeekend ? "Today is a Weekend, Special Price Applies" : "Today is a Weekday, Special Price Applies");
        Label remainingLabel = new Label("Balance: $" + String.valueOf(remainingMoney));
        remainingLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        controller.mainMenu.getChildren().add(isWeekendLabel);

        TableView<AppModel.Studios> studiosTableView = new TableView<>();

        TableColumn<AppModel.Studios, String> nameColumn = new TableColumn<>("Studio");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("studioName"));

        TableColumn<AppModel.Studios, String> movieColumn = new TableColumn<>("Movie");
        movieColumn.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().getMovie().getMovieName()));

        TableColumn<AppModel.Studios, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().getMovie().getMovieGenre().name()));

        TableColumn<AppModel.Studios, String> specialPriceColumn = new TableColumn<>("Price");
        specialPriceColumn.setCellValueFactory(cellData -> 
        new SimpleStringProperty("$"+String.valueOf(cellData.getValue().getPrice())));

        TableColumn<AppModel.Studios, String> noteColumn = new TableColumn<>("Special Price");
        noteColumn.setCellValueFactory(cellData -> 
        new SimpleStringProperty(String.valueOf(cellData.getValue().getSpecialPriceName())));
        
        ObservableList<AppModel.Studios> studios = FXCollections.observableArrayList();
        
        if (controller.selectedStudios.size() > 0) {
            for (AppModel.Studios studiosItem : controller.selectedStudios) {
                studios.add(studiosItem);
            }
        } else {
            for (AppModel.Studios studiosItem : controller.studios) {
                studios.add(studiosItem);
            }
        }
        studiosTableView.getColumns().addAll(nameColumn, movieColumn, genreColumn, specialPriceColumn, noteColumn);
        studiosTableView.setItems(studios);

        studiosTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        studiosTableView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<AppModel.Studios>() {
                @Override
                public void changed(ObservableValue<? extends AppModel.Studios> observable, AppModel.Studios oldValue, AppModel.Studios newValue) {
                    controller.selectedBuyStudios = newValue;
                    if (controller.selectedBuyStudios != null) {
                        System.out.println("Selected Studio: " + controller.selectedBuyStudios.getStudioName());
                    }
                }
            }
        );

        firstRow.getChildren().addAll(
            controller.viewMoviesButton, 
            controller.buyTicketsButton, 
            controller.buySnacksButton, 
            controller.viewCartButton, 
            controller.finalizeOrderButton            
        );
        mainLayout.getChildren().addAll(studiosTableView);
        controller.mainMenu.getChildren().addAll(firstRow, mainLayout);
        controller.mainMenu.getChildren().add(remainingLabel);

        controller.viewMoviesButton.setOnAction(e -> controller.showMoviesByGenre());
        controller.buyTicketsButton.setOnAction(e -> controller.buySelectedMovieTicket());
        controller.buySnacksButton.setOnAction(e -> controller.buySnacks());
        controller.viewCartButton.setOnAction(e -> controller.viewCart());
        controller.finalizeOrderButton.setOnAction(e -> controller.finalizeOrder());

        return new Scene(controller.mainMenu, 800, 500);
    }
}