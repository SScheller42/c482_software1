package softwareone.c482;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Tracks product information.
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Initializes the Product with all necessary information.
      */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets the ID for the product.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the name for the product.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the price for the product.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the current level of stock for the product.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Sets the minimum level of stock for the product.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets the maximum level of stock for the product.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Gets the ID for the product.
     */
    public int getId() {return this.id;}

    /**
     * Gets the name for the product.
     */
    public String getName() {return this.name;}

    /**
     * Gets the price for the product.
     */
    public double getPrice() {return this.price;}

    /**
     * Gets the current level of stock for the product.
     */
    public int getStock() {return this.stock;}

    /**
     * Gets the minimum level of stock for the product.
     */
    public int getMin() {return this.min;}

    /**
     * Gets the maximum level of stock for the product.
     */
    public int getMax() {return this.max;}

    /**
     * Adds a new part to the associated part list for the product.
     */
    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }

    /**
     * Deletes a part from the product.
     */
    public void deleteAssociatedPart(Part selectedAssociatedPart) {
        this.associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Gets all associated parts with a product.
     */
    public ObservableList<Part> getAllAssociatedParts() {return this.associatedParts;}
}
