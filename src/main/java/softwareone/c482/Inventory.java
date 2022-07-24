package softwareone.c482;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;



/**
 * Manages the inventory
 */
public class Inventory {
    /**All Parts in the inventory.*/
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**All Products in the inventory.*/
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a part to inventory.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a product to inventory.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Looks up a part stored in memory by its part id.
     */
    public static Part lookupPart(int partId) {
        for (Part part: allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }

        return null;
    }

    /**
     * Looks up a product stored in memory by its product id.
     */
    public static Product lookupProduct(int productId) {
        for (Product prod: allProducts) {
            if (prod.getId() == productId) {
                return prod;
            }
        }

        return null;
    }

    /**
     * Looks up a part stored in memory by its name.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> matchingParts = FXCollections.observableArrayList();

        for (Part part: allParts) {
            if (part.getName().trim().toLowerCase(Locale.ROOT).contains(partName.trim().toLowerCase(Locale.ROOT))) {
                matchingParts.add(part);
            }
        }

        return matchingParts;
    }

    /**
     * Looks up a product stored in memory by its name.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> matchingProducts = FXCollections.observableArrayList();

        for (Product prod: allProducts) {
            if (prod.getName().trim().toLowerCase(Locale.ROOT).contains(productName.trim().toLowerCase(Locale.ROOT))) {
                matchingProducts.add(prod);
            }
        }

        return matchingProducts;
    }

    /**
     * Updates a part stored in memory with a newer version of information.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.remove(index);
        allParts.add(index, selectedPart);
    }

    /**
     * Updates a product stored in memory with a newer version of information.
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.remove(index);
        allProducts.add(index, newProduct);
    }

    /**
     * Deletes a part from memory.
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes a product from memory.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (selectedProduct.getAllAssociatedParts().isEmpty()) {
            return allProducts.remove(selectedProduct);
        }

        return false;
    }

    /**
     * Gets all the parts currently stored in memory.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Gets all the products currently stored in memory.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
