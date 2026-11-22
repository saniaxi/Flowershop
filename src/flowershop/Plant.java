package flowershop;

public abstract class Plant implements Purchasable {

    private String name;
    private double price;
    private String imagePath;
    private String description;

    private static int totalPlantsPurchased = 0;

    public Plant(String name, double price, String imagePath, String description) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.description = description;
    }

    public abstract String getCategory();

    public String getName()        { return name; }
    public String getImagePath()   { return imagePath; }
    public String getDescription() { return description; }

    public static int getTotalPlantsPurchased() { return totalPlantsPurchased; }
    public static void incrementPurchased()     { totalPlantsPurchased++; }
    public static void resetPurchased()         { totalPlantsPurchased = 0; }

    @Override
    public double getPrice() { return price; }

    @Override
    public void purchase() {
        totalPlantsPurchased++;
        System.out.println(name + " purchased for Rs. " + price);
    }

    @Override
    public String getDetails() {
        return name + "\n" + getCategory() + "\nRs. " + String.format("%.0f", price) + "\n" + description;
    }

    @Override
    public String toString() {
        return name + "  —  Rs. " + String.format("%.0f", price);
    }
}
