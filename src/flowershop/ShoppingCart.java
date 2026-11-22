package flowershop;

import java.util.ArrayList;

public class ShoppingCart {

    private ArrayList<Plant> items;
    private static int totalOrdersPlaced = 0;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public void addItem(Plant plant) {
        items.add(plant);
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public ArrayList<Plant> getItems() { return items; }

    public double getTotal() {
        double total = 0;
        for (Plant p : items) total += p.getPrice();
        return total;
    }

    public int getCount() { return items.size(); }

    public void clear() { items.clear(); }

    public static void incrementOrders()      { totalOrdersPlaced++; }
    public static int getTotalOrdersPlaced()  { return totalOrdersPlaced; }
}
