/* 
Create a simulated stock trading platform
with features like market data,
buying/selling stocks, and tracking portfolio
performance.
*/

import java.util.*;

class Stock {
    String symbol;
    double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
}

class Holding {
    String symbol;
    int quantity;
    double avgPrice;

    public Holding(String symbol, int quantity, double avgPrice) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.avgPrice = avgPrice;
    }
}

public class StockTradingSimulator {
    static Map<String, Stock> market = new HashMap<>();
    static Map<String, Holding> portfolio = new HashMap<>();
    static double cash = 10000.0;
    static Random rand = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initMarket();

        while (true) {
            updateMarketPrices();
            System.out.println("\n=== STOCK TRADING PLATFORM ===");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> displayMarket();
                case 2 -> buyStock(scanner);
                case 3 -> sellStock(scanner);
                case 4 -> viewPortfolio();
                case 5 -> {
                    System.out.println("Thank you for using the trading simulator!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // Initialize some stocks
    public static void initMarket() {
        market.put("AAPL", new Stock("AAPL", 150));
        market.put("GOOG", new Stock("GOOG", 2800));
        market.put("TSLA", new Stock("TSLA", 700));
        market.put("AMZN", new Stock("AMZN", 3300));
        market.put("NFLX", new Stock("NFLX", 500));
    }

    // Simulate price changes
    public static void updateMarketPrices() {
        for (Stock stock : market.values()) {
            double change = (rand.nextDouble() - 0.5) * 10; // +/- 5
            stock.price = Math.max(1, stock.price + change);
        }
    }

    public static void displayMarket() {
        System.out.println("\n--- Market Prices ---");
        for (Stock stock : market.values()) {
            System.out.printf("%s: $%.2f\n", stock.symbol, stock.price);
        }
    }

    public static void buyStock(Scanner scanner) {
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.nextLine().toUpperCase();

        if (!market.containsKey(symbol)) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.print("Enter quantity to buy: ");
        int qty = scanner.nextInt();
        scanner.nextLine();

        double cost = qty * market.get(symbol).price;

        if (cost > cash) {
            System.out.println("Insufficient cash.");
        } else {
            cash -= cost;
            Holding h = portfolio.getOrDefault(symbol, new Holding(symbol, 0, 0));
            h.avgPrice = ((h.avgPrice * h.quantity) + cost) / (h.quantity + qty);
            h.quantity += qty;
            portfolio.put(symbol, h);
            System.out.printf("Bought %d shares of %s for $%.2f\n", qty, symbol, cost);
        }
    }

    public static void sellStock(Scanner scanner) {
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.nextLine().toUpperCase();

        if (!portfolio.containsKey(symbol)) {
            System.out.println("You don't own this stock.");
            return;
        }

        Holding h = portfolio.get(symbol);
        System.out.print("Enter quantity to sell (you own " + h.quantity + "): ");
        int qty = scanner.nextInt();
        scanner.nextLine();

        if (qty > h.quantity) {
            System.out.println("Not enough shares.");
        } else {
            double revenue = qty * market.get(symbol).price;
            cash += revenue;
            h.quantity -= qty;
            if (h.quantity == 0) portfolio.remove(symbol);
            System.out.printf("Sold %d shares of %s for $%.2f\n", qty, symbol, revenue);
        }
    }

    public static void viewPortfolio() {
        System.out.println("\n--- Portfolio ---");
        System.out.printf("Cash: $%.2f\n", cash);
        double totalValue = cash;

        for (Holding h : portfolio.values()) {
            double currentPrice = market.get(h.symbol).price;
            double value = currentPrice * h.quantity;
            totalValue += value;
            System.out.printf("%s: %d shares | Avg Buy: $%.2f | Current: $%.2f | Value: $%.2f\n",
                    h.symbol, h.quantity, h.avgPrice, currentPrice, value);
        }

        System.out.printf("Total Portfolio Value: $%.2f\n", totalValue);
    }
}
