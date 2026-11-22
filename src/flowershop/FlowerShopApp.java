package flowershop;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.ArrayList;

public class FlowerShopApp extends Application {

private ShoppingCart cart = new ShoppingCart();
private ArrayList<Plant> allPlants = new ArrayList<>();
private Stage primaryStage;
private double lastTotal = 0;

private Label cartCountLabel = new Label("🛒  Cart: 0 items");
private Label cartTotalLabel = new Label("Total: Rs. 0");
private String currentCategory = "Indoor Plant";

@Override
public void start(Stage stage) {
this.primaryStage = stage;
loadPlants();
showWelcomeScreen();
stage.setTitle("🌸 Bloom & Petal — Flower Shop");
stage.setResizable(false);
stage.show();
}

// ═══════════════════════════════════════════════════════════════
//  SCREEN 0 — WELCOME
// ═══════════════════════════════════════════════════════════════
private void showWelcomeScreen() {
StackPane root = new StackPane();

// background image
ImageView bg = loadBg("images/cherryleaves.jpg", 750, 600);
root.getChildren().add(bg);

// dark pink overlay for readability
Region overlay = new Region();
overlay.setPrefSize(750, 600);
overlay.setStyle("-fx-background-color: rgba(100, 10, 50, 0.45);");
root.getChildren().add(overlay);

// center content
VBox center = new VBox(22);
center.setAlignment(Pos.CENTER);
center.setPadding(new Insets(0, 0, 80, 0)); // shift up a bit from bottom

Label emoji = new Label("🌸");
emoji.setStyle("-fx-font-size:58px;");

Label welcome = new Label("Welcome to");
welcome.setStyle(
"-fx-font-size:22px; -fx-text-fill:#ffe0ec; -fx-font-weight:normal;"
);

Label shopName = new Label("Bloom & Petal");
shopName.setStyle(
"-fx-font-size:42px; -fx-font-weight:bold; -fx-text-fill:white;" +
"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 8, 0, 0, 2);"
);

Label tagline = new Label("Your little corner of flowers & greenery 🌿");
tagline.setStyle("-fx-font-size:14px; -fx-text-fill:#f8bbd0;");

Button continueBtn = new Button("Enter Shop  →");
continueBtn.setStyle(
"-fx-background-color:#c2185b; -fx-text-fill:white;" +
"-fx-font-size:16px; -fx-font-weight:bold;" +
"-fx-padding:12 40; -fx-background-radius:30; -fx-cursor:hand;"
);
continueBtn.setOnMouseEntered(e -> continueBtn.setStyle(
"-fx-background-color:#880e4f; -fx-text-fill:white;" +
"-fx-font-size:16px; -fx-font-weight:bold;" +
"-fx-padding:12 40; -fx-background-radius:30; -fx-cursor:hand;"
));
continueBtn.setOnMouseExited(e -> continueBtn.setStyle(
"-fx-background-color:#c2185b; -fx-text-fill:white;" +
"-fx-font-size:16px; -fx-font-weight:bold;" +
"-fx-padding:12 40; -fx-background-radius:30; -fx-cursor:hand;"
));
continueBtn.setOnAction(e -> showShopScreen());

center.getChildren().addAll(emoji, welcome, shopName, tagline, continueBtn);
StackPane.setAlignment(center, Pos.CENTER);
root.getChildren().add(center);

// flower image — bottom right
ImageView flowerImg = loadImage("images/Minecraft-Lively-Flower-Display-Concept-PNG.png", 220, 220);
StackPane.setAlignment(flowerImg, Pos.BOTTOM_RIGHT);
StackPane.setMargin(flowerImg, new Insets(0, 10, 0, 0));
root.getChildren().add(flowerImg);

primaryStage.setScene(new Scene(root, 750, 600));
}

// ═══════════════════════════════════════════════════════════════
//  SCREEN 1 — SHOP
// ═══════════════════════════════════════════════════════════════
private void showShopScreen() {

StackPane root = new StackPane();
ImageView bg = loadBg("images/background.jpg", 750, 600);
root.getChildren().add(bg);

VBox content = new VBox(0);
content.setAlignment(Pos.TOP_CENTER);

HBox titleBar = new HBox();
titleBar.setPadding(new Insets(14, 20, 14, 20));
titleBar.setAlignment(Pos.CENTER_LEFT);
titleBar.setStyle("-fx-background-color: rgba(120,30,70,0.82);");

Label title = new Label("🌸  Bloom & Petal");
title.setStyle("-fx-font-size:22px; -fx-font-weight:bold; -fx-text-fill:white;");

Region spacer = new Region();
HBox.setHgrow(spacer, Priority.ALWAYS);

Button historyBtn = styledBtn("📜 Order History", "#c2185b", "#880e4f");
historyBtn.setOnAction(e -> showHistoryPopup());

titleBar.getChildren().addAll(title, spacer, historyBtn);

HBox tabs = new HBox(6);
tabs.setPadding(new Insets(10, 20, 6, 20));
tabs.setAlignment(Pos.CENTER);
tabs.setStyle("-fx-background-color: rgba(255,220,230,0.55);");

Button indoorTab    = tabBtn("🌿 Indoor");
Button outdoorTab   = tabBtn("🌹 Outdoor");
Button succulentTab = tabBtn("🌵 Succulent");
tabs.getChildren().addAll(indoorTab, outdoorTab, succulentTab);

HBox cardsRow = new HBox(18);
cardsRow.setPadding(new Insets(18, 20, 10, 20));
cardsRow.setAlignment(Pos.CENTER);
cardsRow.setMinHeight(340);

Label infoLabel = new Label("← Select a plant to see details");
infoLabel.setWrapText(true);
infoLabel.setMaxWidth(700);
infoLabel.setAlignment(Pos.CENTER);
infoLabel.setStyle(
"-fx-font-size:13px; -fx-text-fill:#4a0030; -fx-padding:10 20;" +
"-fx-background-color:rgba(255,210,230,0.72);" +
"-fx-background-radius:10;"
);
HBox infoBox = new HBox(infoLabel);
infoBox.setAlignment(Pos.CENTER);
infoBox.setPadding(new Insets(0,20,0,20));

HBox cartBar = buildCartBar();

content.getChildren().addAll(titleBar, tabs, cardsRow, infoBox, cartBar);
root.getChildren().add(content);

Runnable refreshCards = () -> {
cardsRow.getChildren().clear();
for (Plant p : allPlants) {
    if (p.getCategory().equals(currentCategory)) {
        VBox card = buildPlantCard(p, infoLabel);
        cardsRow.getChildren().add(card);
    }
}
};

indoorTab.setOnAction(e   -> { currentCategory = "Indoor Plant";  refreshCards.run(); });
outdoorTab.setOnAction(e  -> { currentCategory = "Outdoor Plant"; refreshCards.run(); });
succulentTab.setOnAction(e -> { currentCategory = "Succulent";    refreshCards.run(); });

refreshCards.run();
primaryStage.setScene(new Scene(root, 750, 600));
}
private VBox buildPlantCard(Plant plant, Label infoLabel) {
VBox card = new VBox(8);
card.setAlignment(Pos.TOP_CENTER);
card.setPrefWidth(200);
card.setPadding(new Insets(12));
card.setStyle("-fx-background-color:rgba(255,240,248,0.88);" +"-fx-background-radius:14;" +"-fx-border-color:#e8a0b8;" +"-fx-border-radius:14;" +"-fx-border-width:1.5;");

ImageView iv = loadImage(plant.getImagePath(), 170, 150);
Label name  = new Label(plant.getName());
name.setStyle("-fx-font-size:14px; -fx-font-weight:bold; -fx-text-fill:#7b0045;");
Label price = new Label("Rs. " + String.format("%.0f", plant.getPrice()));
price.setStyle("-fx-font-size:13px; -fx-text-fill:#c2185b;");
Button addBtn = styledBtn("Add to Cart 🛒", "#e91e8c", "#ad1457");

card.setOnMouseClicked(e -> infoLabel.setText(plant.getDetails()));

addBtn.setOnAction(e -> {
try {
    plant.purchase();
    cart.addItem(plant);
    refreshCartBar();
    infoLabel.setText("✔ " + plant.getName() + " added to cart!");
    infoLabel.setStyle(
        "-fx-font-size:13px; -fx-text-fill:#1a6b2a; -fx-padding:10 20;" +
        "-fx-background-color:rgba(200,255,215,0.75);" +
        "-fx-background-radius:10;"
    );
} catch (Exception ex) {
    infoLabel.setText("⚠ Error: " + ex.getMessage());
}
});

card.getChildren().addAll(iv, name, price, addBtn);
return card;
}

// ═══════════════════════════════════════════════════════════════
//  SCREEN 2 — CART
// ═══════════════════════════════════════════════════════════════
private void showCartScreen() {

StackPane root = new StackPane();
ImageView bg = loadBg("images/background.jpg", 750, 600);
root.getChildren().add(bg);

VBox content = new VBox(0);
content.setAlignment(Pos.TOP_CENTER);

HBox titleBar = new HBox();
titleBar.setPadding(new Insets(14, 20, 14, 20));
titleBar.setAlignment(Pos.CENTER_LEFT);
titleBar.setStyle("-fx-background-color: rgba(120,30,70,0.82);");

Button backBtn = styledBtn("← Back", "#c2185b", "#880e4f");
backBtn.setOnAction(e -> showShopScreen());

Label title = new Label("🛒  My Cart");
title.setStyle("-fx-font-size:22px; -fx-font-weight:bold; -fx-text-fill:white; -fx-padding:0 0 0 16;");
titleBar.getChildren().addAll(backBtn, title);

VBox itemsList = new VBox(10);
itemsList.setPadding(new Insets(20, 30, 10, 30));
itemsList.setAlignment(Pos.TOP_LEFT);

ScrollPane scrollPane = new ScrollPane(itemsList);
scrollPane.setFitToWidth(true);
scrollPane.setPrefHeight(360);
scrollPane.setStyle("-fx-background:transparent; -fx-background-color:transparent;");

Label msgLabel = new Label("");
msgLabel.setStyle("-fx-font-size:13px; -fx-text-fill:#880e4f; -fx-padding:0 30;");

HBox bottomBar = new HBox(16);
bottomBar.setPadding(new Insets(14, 24, 14, 24));
bottomBar.setAlignment(Pos.CENTER_RIGHT);
bottomBar.setStyle("-fx-background-color: rgba(173,20,87,0.88);");

Label totalLbl = new Label("Total: Rs. " + String.format("%.0f", cart.getTotal()));
totalLbl.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:white;");

Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
Button checkoutBtn = styledBtn("Checkout ✔", "#880e4f", "#560027");
bottomBar.getChildren().addAll(sp, totalLbl, checkoutBtn);

ArrayList<Plant> items = cart.getItems();
if (items.isEmpty()) {
Label empty = new Label("Your cart is empty.");
empty.setStyle("-fx-font-size:14px; -fx-text-fill:#fff; -fx-background-color:rgba(120,30,70,0.6); -fx-padding:10 20; -fx-background-radius:8;");
itemsList.getChildren().add(empty);
}
for (int i = 0; i < items.size(); i++) {
Plant p = items.get(i);
final int idx = i;

HBox row = new HBox(12);
row.setAlignment(Pos.CENTER_LEFT);
row.setPadding(new Insets(10, 16, 10, 16));
row.setStyle(
    "-fx-background-color:rgba(255,235,245,0.88);" +
    "-fx-background-radius:10;" +
    "-fx-border-color:#f48fb1;" +
    "-fx-border-radius:10;"
);

ImageView iv = loadImage(p.getImagePath(), 60, 55);
VBox info = new VBox(3);
Label nm  = new Label(p.getName());
nm.setStyle("-fx-font-size:14px; -fx-font-weight:bold; -fx-text-fill:#7b0045;");
Label cat = new Label(p.getCategory());
cat.setStyle("-fx-font-size:11px; -fx-text-fill:#888;");
Label pr  = new Label("Rs. " + String.format("%.0f", p.getPrice()));
pr.setStyle("-fx-font-size:13px; -fx-text-fill:#c2185b;");
info.getChildren().addAll(nm, cat, pr);

Region rowSp = new Region(); HBox.setHgrow(rowSp, Priority.ALWAYS);

Button removeBtn = styledBtn("✕ Remove", "#f06292", "#c2185b");
removeBtn.setOnAction(ev -> {
    cart.removeItem(idx);
    totalLbl.setText("Total: Rs. " + String.format("%.0f", cart.getTotal()));
    refreshCartBar();
    showCartScreen();
});

row.getChildren().addAll(iv, info, rowSp, removeBtn);
itemsList.getChildren().add(row);
}

checkoutBtn.setOnAction(e -> {
try {
    if (cart.getCount() == 0) throw new Exception("Cart is empty!");
    lastTotal = cart.getTotal();
    FileManager.saveOrder(cart);
    ShoppingCart.incrementOrders();
    cart.clear();
    refreshCartBar();
    showThankYouScreen();
} catch (Exception ex) {
    msgLabel.setText("⚠ " + ex.getMessage());
}
});

content.getChildren().addAll(titleBar, scrollPane, msgLabel, bottomBar);
root.getChildren().add(content);
primaryStage.setScene(new Scene(root, 750, 600));
}

// ═══════════════════════════════════════════════════════════════
//  SCREEN 3 — THANK YOU
// ═══════════════════════════════════════════════════════════════
private void showThankYouScreen() {

StackPane root = new StackPane();
root.setStyle("-fx-background-color: #880e4f;");

VBox content = new VBox(24);
content.setAlignment(Pos.CENTER);
content.setPadding(new Insets(60));

Label icon = new Label("🌸");
icon.setStyle("-fx-font-size:64px;");

Label thanks = new Label("Thank You!");
thanks.setStyle("-fx-font-size:38px; -fx-font-weight:bold; -fx-text-fill:white;");

Label sub = new Label("Your order has been placed successfully.");
sub.setStyle("-fx-font-size:16px; -fx-text-fill:#f8bbd0;");

Label totalPaid = new Label("Amount Paid: Rs. " + String.format("%.0f", lastTotal));
totalPaid.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-text-fill:#ffcdd2;");

Label orders = new Label("Total orders placed this session: " + ShoppingCart.getTotalOrdersPlaced());
orders.setStyle("-fx-font-size:13px; -fx-text-fill:#f48fb1;");

Label saved = new Label("✔ Your order has been saved to orders.txt");
saved.setStyle("-fx-font-size:13px; -fx-text-fill:#ce93d8;");

Button shopAgain = styledBtn("🌸 Shop Again", "#c2185b", "#ad1457");
shopAgain.setStyle(shopAgain.getStyle() + "-fx-font-size:16px; -fx-padding:12 32;");
shopAgain.setOnAction(e -> showShopScreen());

Button histBtn = styledBtn("📜 View History", "#6a1040", "#4a0030");
histBtn.setStyle(histBtn.getStyle() + "-fx-font-size:14px; -fx-padding:10 24;");
histBtn.setOnAction(e -> showHistoryPopup());

HBox btnRow = new HBox(16, shopAgain, histBtn);
btnRow.setAlignment(Pos.CENTER);

content.getChildren().addAll(icon, thanks, sub, totalPaid, orders, saved, btnRow);
root.getChildren().add(content);
primaryStage.setScene(new Scene(root, 750, 600));
}
//  ORDER HISTORY POPUP
private void showHistoryPopup() {
Stage popup = new Stage();
popup.setTitle("📜 Order History");
VBox box = new VBox(10);
box.setPadding(new Insets(20));
box.setStyle("-fx-background-color:#fce4ec;");
Label title = new Label("Order History");
title.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-text-fill:#880e4f;");
TextArea area = new TextArea();
area.setEditable(false);
area.setPrefSize(440, 340);
area.setStyle("-fx-font-size:12px;");
try {
ArrayList<String> lines = FileManager.loadOrders();
StringBuilder sb = new StringBuilder();
for (String l : lines) sb.append(l).append("\n");
area.setText(sb.toString());
} catch (Exception ex) {
area.setText("Could not load order history: " + ex.getMessage());
}
box.getChildren().addAll(title, area);
popup.setScene(new Scene(box, 480, 400));
popup.show();
}
//  CART BAR
private HBox buildCartBar() {
HBox bar = new HBox(16);
bar.setPadding(new Insets(12, 24, 12, 24));
bar.setAlignment(Pos.CENTER_RIGHT);
bar.setStyle("-fx-background-color: rgba(173,20,87,0.88);");
cartCountLabel.setStyle("-fx-font-size:14px; -fx-text-fill:white; -fx-font-weight:bold;");
cartTotalLabel.setStyle("-fx-font-size:14px; -fx-text-fill:#ffcdd2; -fx-font-weight:bold;");
Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
Button viewCartBtn = styledBtn("View Cart →", "#880e4f", "#560027");
viewCartBtn.setOnAction(e -> showCartScreen());
bar.getChildren().addAll(cartCountLabel, sp, cartTotalLabel, viewCartBtn);
return bar;
}
private void refreshCartBar() {
cartCountLabel.setText("🛒  Cart: " + cart.getCount() + " item" + (cart.getCount() == 1 ? "" : "s"));
cartTotalLabel.setText("Total: Rs. " + String.format("%.0f", cart.getTotal()));
}
//  HELPERS
private Button styledBtn(String text, String bg, String hover) {
Button btn = new Button(text);
btn.setStyle("-fx-background-color:" + bg + "; -fx-text-fill:white;" +"-fx-font-size:12px; -fx-padding:7 16;" +"-fx-background-radius:8; -fx-cursor:hand;");
btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color:" + hover + "; -fx-text-fill:white;" +"-fx-font-size:12px; -fx-padding:7 16;" +"-fx-background-radius:8; -fx-cursor:hand;"));
btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color:" + bg + "; -fx-text-fill:white;" +"-fx-font-size:12px; -fx-padding:7 16;" +"-fx-background-radius:8; -fx-cursor:hand;"));
return btn;
}
private Button tabBtn(String text) {
Button btn = new Button(text);
btn.setStyle("-fx-background-color:#f8bbd0; -fx-text-fill:#880e4f;" +"-fx-font-size:13px; -fx-font-weight:bold; -fx-padding:7 22;" +"-fx-background-radius:20; -fx-cursor:hand;");
btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color:#c2185b; -fx-t-fill:white;" +"-fx-font-size:13px; -fx-font-weight:bold; -fx-padding:7 22;" +"-fx-background-radius:20; -fx-cursor:hand;"));
btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color:#f8bbd0; -fx-text-fill:#880e4f;" +"-fx-font-size:13px; -fx-font-weight:bold; -fx-padding:7 22;" +"-fx-background-radius:20; -fx-cursor:hand;"));
return btn;
}
private ImageView loadImage(String path, double w, double h) {
try {
Image img = new Image(getClass().getResourceAsStream("/" + path));
ImageView iv = new ImageView(img);
iv.setFitWidth(w);
iv.setFitHeight(h);
iv.setPreserveRatio(true);
return iv;
} catch (Exception e) {
ImageView iv = new ImageView();
iv.setFitWidth(w);
iv.setFitHeight(h);
return iv;
}}
private ImageView loadBg(String path, double w, double h) {
try {
Image img = new Image(getClass().getResourceAsStream("/" + path));
ImageView iv = new ImageView(img);
iv.setFitWidth(w);
iv.setFitHeight(h);
iv.setPreserveRatio(false);
return iv;
} catch (Exception e) {
return new ImageView();}}
//  PLANT DATA
private void loadPlants() {
// Indoor
allPlants.add(new IndoorPlant("Peace Lily",        850,  "images/lily.png",              "Graceful blooms, loves shade and humidity."));
allPlants.add(new IndoorPlant("Enchanted Flower",  650,  "images/Enchantedflower.png",   "Bright and cheerful. Perfect for beginners."));
allPlants.add(new IndoorPlant("Rose Bush",         400,  "images/rosebush.png",          "Delicate pink rosette succulent. Windowsill favorite."));
allPlants.add(new IndoorPlant("Fantasy Flower",    850,  "images/FantasyFlower.png",     "Loves shade and humidity."));
// Outdoor
allPlants.add(new OutdoorPlant("Sakura",           1200, "images/sakura.jpg",            "Classic beauty. Needs full sun and regular watering."));
allPlants.add(new OutdoorPlant("Pink Rose",        650,  "images/pinkrose.jpg",          "Bright and cheerful. Perfect for beginners."));
allPlants.add(new OutdoorPlant("Jasmine",          750,  "images/jasmine.jpg",           "Fragrant white flowers. Grows beautifully on fences."));
allPlants.add(new OutdoorPlant("Sunflower",        500,  "images/sunflower.jpg",         "Bright and cheerful. Grows tall in sunny gardens."));
// Succulents
allPlants.add(new Succulent("Bloomy",              450,  "images/bloomy.jpg",            "Soft pastel blooms. Drought-resistant and charming."));
allPlants.add(new Succulent("Clover Plant",        350,  "images/clover.jpg",            "Lucky four-leaf variety. Easy to grow indoors."));
allPlants.add(new Succulent("Fern Leaves",         600,  "images/leaves.jpg",            "Lush green fern. Loves moisture and indirect light."));
allPlants.add(new Succulent("Tulip Purple",        450,  "images/tulippurple.jpg",       "Loves moisture and indirect light."));
}
public static void main(String[] args) {
launch(args);
}
}