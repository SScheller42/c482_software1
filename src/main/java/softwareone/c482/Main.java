package softwareone.c482;

import softwareone.c482.InventoryManagementSystemController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @author Gerald Seth Scheller Student ID: #001448388
 * Javadoc location is c482.javadoc(folder called javadoc)
 * FUTURE ENHANCEMENT will be able to display the Company Name and Machine ID on main screen
 */

public class Main extends Application {


    /**Creates the stage and loads initial scene*/
    @Override
    public void start(Stage stage) throws IOException {

        // Load the inventory with some parts and products
        seedPartsData();
        seedProductData();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("inventorymanagementsystem.fxml"));
        InventoryManagementSystemController controller = new InventoryManagementSystemController();
        controller.setUserData(new UserData(stage));
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main entry point for the program.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Accesses static class Inventory and adds Part objects to internal memory.
     */
    private void seedPartsData() {


        InHouse brakes = new InHouse(1, "Brakes", 30.00, 10, 1, 20);
        brakes.setMachineId(111);
        Inventory.addPart(brakes);

        Outsourced wheel = new Outsourced(2, "Wheel", 11.00, 16, 1, 20);
        wheel.setCompanyName("Super Bikes");
        Inventory.addPart(wheel);

        InHouse seat = new InHouse(3, "Seat", 15.00, 10, 1, 20);
        seat.setMachineId(112);
        Inventory.addPart(seat);
    }

    /**
     * Accesses static class Inventory and adds Product to internal memory.
     */
    private void seedProductData() {
        Product giantBike = new Product(1000, "Giant Bike", 299.99, 5, 0, 10);
        giantBike.addAssociatedPart(Inventory.lookupPart(1));
        giantBike.addAssociatedPart(Inventory.lookupPart(2));
        giantBike.addAssociatedPart(Inventory.lookupPart(3));

        Inventory.addProduct(giantBike);

        Product tricycle = new Product(1001, "Tricycle", 99.99, 3, 0, 10);
        tricycle.addAssociatedPart(Inventory.lookupPart(1));
        tricycle.addAssociatedPart(Inventory.lookupPart(2));
        tricycle.addAssociatedPart(Inventory.lookupPart(3));

        Inventory.addProduct(tricycle);
    }
}