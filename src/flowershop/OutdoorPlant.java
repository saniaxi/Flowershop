package flowershop;
public class OutdoorPlant extends Plant {
public OutdoorPlant(String name, double price, String imagePath, String description) {
super(name, price, imagePath, description);
}
@Override
public String getCategory() { return "Outdoor Plant"; }
}
