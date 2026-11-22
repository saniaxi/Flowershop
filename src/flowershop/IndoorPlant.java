package flowershop;
public class IndoorPlant extends Plant {
public IndoorPlant(String name, double price, String imagePath, String description) {
super(name, price, imagePath, description);
}
@Override
public String getCategory() { return "Indoor Plant"; }
}
