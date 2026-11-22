package flowershop;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
public class FileManager {
    private static final String ORDER_FILE = "orders.txt";
    public static void saveOrder(ShoppingCart cart) throws IOException {
        FileWriter fw = new FileWriter(ORDER_FILE, true);
        BufferedWriter bw = new BufferedWriter(fw);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        bw.write("===== ORDER =====");
        bw.newLine();
        bw.write("Date: " + dtf.format(LocalDateTime.now()));
        bw.newLine();

        for (Plant p : cart.getItems()) {
            bw.write("  - " + p.getName() + " (" + p.getCategory() + ") — Rs. " + String.format("%.0f", p.getPrice()));
            bw.newLine();
        }

        bw.write("Total: Rs. " + String.format("%.0f", cart.getTotal()));
        bw.newLine();
        bw.write("Total Orders So Far: " + ShoppingCart.getTotalOrdersPlaced());
        bw.newLine();
        bw.newLine();

        bw.close();
        fw.close();
    }

    public static ArrayList<String> loadOrders() throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(ORDER_FILE);

        if (!file.exists()) {
            lines.add("No order history found.");
            return lines;
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
        lines.add(line);
        }
        br.close();
        return lines;
    }
}
