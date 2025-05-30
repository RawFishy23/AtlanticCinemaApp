package src;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class AppController {

    public Map<AppModel.Genre, List<AppModel.Movie>> movies;
    public List<AppModel.Studios> studios;
    public AppModel.Customer customer;
    public AppModel.Order order;
    public VBox mainMenu;
    public Stage mainStage;
    public AppModel.SpecialPrice specialPriceToday;

    public AppModel.Studios selectedBuyStudios;

    private double baseTicketPrice = 15;
    public List<AppModel.Movie> movieList = new ArrayList<>();
    public List<AppModel.Studios> selectedStudios = new ArrayList<>();
    public ListView<String> movieListView = new ListView<>();
    public ListView<String> payableItemListView = new ListView<>();

    public List<AppModel.Order> finishedOrders = new ArrayList<>();
    public boolean isWeekend;

    public AppController(Stage primaryStage) {
        this.mainStage = primaryStage;
        this.movies = initializeMovies();

        Random random = new Random();
        AppModel.SpecialPrice[] values = {
            AppModel.SpecialPrice.WEEKDAY,
            AppModel.SpecialPrice.WEEKENDS,
        };
        this.specialPriceToday = values[random.nextInt(values.length)];

        this.isWeekend = this.specialPriceToday == AppModel.SpecialPrice.WEEKENDS ? true : false;
        this.studios = initializeStudios(movies, this.specialPriceToday);
        this.customer = new AppModel.Customer();
        this.order = new AppModel.Order();
    }

    public void start() {
        updateHomeMenu();
    }

    public Button viewMoviesButton;
    public Button buyTicketsButton;
    public Button buySnacksButton;
    public Button viewCartButton;
    public Button finalizeOrderButton;

    public void updateHomeMenu() {
        mainStage.setScene(AppView.getMainMenuScene(this));
        mainStage.setTitle("Atlantic Cinema");
        mainStage.show();
    }

    public void showMoviesByGenre() {
        AppView.getShowMoviesScene(this);
    }

    public void buyMovieTicket() {
        AppView.getBuyTicketScene(this);
    }

    public void buySelectedMovieTicket() {
        if (selectedBuyStudios == null) {
            showAlert(Alert.AlertType.ERROR, "Select a movie first");
        } else {
            AppView.getBuySelectedTicketScene(this);
        }
    }

    public void buySnacks() {
        AppView.getBuySnacksScene(this);
    }

    public void viewCart() {
        AppView.getCartScene(this);
    }

    public void finalizeOrder() {
        AppView.getCheckoutScene(this);
    }

    public void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }

    private Map<AppModel.Genre, List<AppModel.Movie>> initializeMovies() {
        Map<AppModel.Genre, List<AppModel.Movie>> movies = new HashMap<>();
        movies.put(AppModel.Genre.ACTION, Arrays.asList(
            new AppModel.Movie("Mad Max: Fury Road", AppModel.Genre.ACTION),
            new AppModel.Movie("John Wick", AppModel.Genre.ACTION)));
        movies.put(AppModel.Genre.COMEDY, Arrays.asList(
            new AppModel.Movie("Superbad", AppModel.Genre.COMEDY),
            new AppModel.Movie("The Hangover", AppModel.Genre.COMEDY)));
        movies.put(AppModel.Genre.SCIFI, Arrays.asList(
            new AppModel.Movie("Inception", AppModel.Genre.SCIFI),
            new AppModel.Movie("Interstellar", AppModel.Genre.SCIFI)));
        movies.put(AppModel.Genre.HORROR, Arrays.asList(
            new AppModel.Movie("The Conjuring", AppModel.Genre.HORROR),
            new AppModel.Movie("Insidious", AppModel.Genre.HORROR)));
        movies.put(AppModel.Genre.BIOPICS, Arrays.asList(
            new AppModel.Movie("The Social Network", AppModel.Genre.BIOPICS),
            new AppModel.Movie("Bohemian Rhapsody", AppModel.Genre.BIOPICS)));
        return movies;
    }

    private List<AppModel.Studios> initializeStudios(Map<AppModel.Genre, List<AppModel.Movie>> movies, AppModel.SpecialPrice dayPrice) {
        List<AppModel.Studios> studios = new ArrayList<>();
        Random random = new Random();

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        List<AppModel.Movie> allMovies = new ArrayList<>();
        for (List<AppModel.Movie> genreMovies : movies.values()) {
            allMovies.addAll(genreMovies);
        }
        int index = 0;
        for (AppModel.Movie movie : allMovies) {
            int randomNumber = numbers.get(index);
            double defaultTicketPrice = baseTicketPrice;

            String specialPriceName = "Normal";

            if (randomNumber <= 2) {
                AppModel.SpecialPrice studioMultiplier = AppModel.SpecialPrice.IMAX;
                defaultTicketPrice = baseTicketPrice * studioMultiplier.getMultiplier();
                
                specialPriceName = "IMAX";
            } else if (randomNumber <= 5) {
                AppModel.SpecialPrice studioMultiplier = AppModel.SpecialPrice.THREE_D;
                defaultTicketPrice = baseTicketPrice * studioMultiplier.getMultiplier();

                specialPriceName = "3D";
            } else {

            }

            if (dayPrice.equals(AppModel.SpecialPrice.WEEKDAY)) {
                specialPriceName = specialPriceName + " - Weekday";
            } else {
                specialPriceName = specialPriceName + " - Weekend";
            }
            double weekendMultiplier = dayPrice.getMultiplier();
            double price = defaultTicketPrice * weekendMultiplier;

            studios.add(new AppModel.Studios("Studio " + (randomNumber), movie, price, specialPriceName));

            index++;
        }
        return studios;
    }
}
