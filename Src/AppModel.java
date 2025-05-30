package src;

import java.util.ArrayList;
import java.util.List;

public class AppModel {
    public static enum Genre { ALL, ACTION, COMEDY, SCIFI, HORROR, BIOPICS }
    public static enum Flavor { BUTTER, CARAMEL }
    public static enum Size { REGULAR, JUMBO }
    public static enum SpecialPrice {
        WEEKENDS(1.2), WEEKDAY(1.0), IMAX(1.5), THREE_D(1.3);

        private final double multiplier;
        SpecialPrice(double multiplier) { this.multiplier = multiplier; }
        public double getMultiplier() { return multiplier; }
    }

    public static class Customer {
        private int money;
    
        // Constructor to initialize the budget
        public Customer() {
            this.money = 200; // Starting budget is $200
        }
    
        // Method to get the current budget
        public int getMoney() {
            return money;
        }
    
        // Method to update the budget after a purchase
        public void updateMoney(int amount) {
            if (amount <= money) {
                money -= amount;
            } else {
                System.out.println("Insufficient funds for this transaction.");
            }
        }
    
        // Optional: Method to add money to the budget if needed
        public void addMoney(int amount) {
            this.money += amount;
        }
    }
    public static class Movie {
        private String movieName;
        private AppModel.Genre movieGenre;
    
        // Constructor to initialize movie name and genre
        public Movie(String movieName, AppModel.Genre movieGenre) {
            this.movieName = movieName;
            this.movieGenre = movieGenre;
        }
    
        // Get movie name
        public String getMovieName() {
            return movieName;
        }
    
        // Get movie genre
        public AppModel.Genre getMovieGenre() {
            return movieGenre;
        }
    }

public static class Order {
    private List<PayableItem> payableItems;

    // Constructor
    public Order() {
        this.payableItems = new ArrayList<>();
    }

    // Method to add an item to the order
    public void addItem(PayableItem item) {
        payableItems.add(item);
    }

    // Method to calculate the total price of the order
    public List<PayableItem> getPayableItems() {
        return payableItems;
    }
    public double getTotalPrice() {
        double total = 0.0;
        for (PayableItem item : payableItems) {
            total += item.getPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder orderDetails = new StringBuilder("Order Details:\n");
        for (PayableItem item : payableItems) {
            orderDetails.append(item.toString()).append("\n");
        }
        orderDetails.append("Total Price: $").append(getTotalPrice());
        return orderDetails.toString();
    }
}

public static interface PayableItem {
    double getPrice();
}

// Base class for food and beverage menu items
abstract static class FoodBeverageMenu implements PayableItem {
    protected int quantity;

    public FoodBeverageMenu(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

// Popcorn class, extending FoodBeverageMenu and implementing PayableItem
static class Popcorn extends FoodBeverageMenu {
    private AppModel.Flavor flavor;
    public static final double BASE_PRICE = 5.0; // Base price per unit

    public Popcorn(AppModel.Flavor flavor, int quantity) {
        super(quantity);
        this.flavor = flavor;
    }

    @Override
    public double getPrice() {
        return BASE_PRICE * quantity;
    }

    @Override
    public String toString() {
        return "Popcorn (" + flavor + ") x" + quantity + ": $" + getPrice();
    }
}

// Soda class, extending FoodBeverageMenu and implementing PayableItem
static class Soda extends FoodBeverageMenu {
    private AppModel.Size size;
    public static final double REGULAR_PRICE = 3.0;
    public static final double JUMBO_PRICE = 4.5;

    public Soda(AppModel.Size size, int quantity) {
        super(quantity);
        this.size = size;
    }

    @Override
    public double getPrice() {
        return (size == AppModel.Size.JUMBO ? JUMBO_PRICE : REGULAR_PRICE) * quantity;
    }

    @Override
    public String toString() {
        return "Soda (" + size + ") x" + quantity + ": $" + getPrice();
    }
}

public static class Studios implements PayableItem {
    private String specialPriceName;
    private String studioName;
    private Movie movie;
    private double defaultPrice;
    private double basePrice;
    private List<AppModel.SpecialPrice> specialPrices;

    // Constructor to initialize studio with name, movie, and base price
    public Studios(String studioName, Movie movie, double basePrice) {
        this.studioName = studioName;
        this.movie = movie;
        this.defaultPrice = 15;
        this.basePrice = basePrice != 0 ? basePrice : 15;
        this.specialPrices = new ArrayList<>();
    }

    public Studios(String studioName, Movie movie, double basePrice, String specialPriceName) {
        this.studioName = studioName;
        this.movie = movie;
        this.defaultPrice = 15;
        this.basePrice = basePrice != 0 ? basePrice : 15;
        this.specialPrices = new ArrayList<>();
        this.specialPriceName = specialPriceName;
    }

    // Method to add a special price
    public void addSpecialPrice(AppModel.SpecialPrice specialPrice) {
        this.specialPrices.add(specialPrice);
    }

    // Implement getPrice method from PayableItem interface
    @Override
    public double getPrice() {
        double price = basePrice;
        for (AppModel.SpecialPrice specialPrice : specialPrices) {
            price *= specialPrice.getMultiplier();
        }
        return price;
    }

    // Get
    public double getDefaultPrice() {
        return defaultPrice;
    }

    public String getSpecialPriceName() {
        return specialPriceName;
    }

    public double getBasePrice() {
        return basePrice;
    }
    public String getStudioName() {
        return studioName;
    }

    public Movie getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "You have selected: " + movie.getMovieName() +
               ", Genre: " + movie.getMovieGenre() +
               ", Price: $" + getPrice();
    }
}

}
