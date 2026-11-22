package flowershop;

public class Succulent extends Plant {
    public Succulent(String name, double price, String imagePath, String description) {
        super(name, price, imagePath, description);
    }

    @Override
    public String getCategory() { return "Succulent"; }
}
