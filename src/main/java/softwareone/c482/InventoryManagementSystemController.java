package softwareone.c482;

import softwareone.c482.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for inventorymanagementsystem.fxml
 */
public class InventoryManagementSystemController extends Node implements javafx.fxml.Initializable {
    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn<Part, Integer> partIdTableColumn;

    @FXML
    private TableColumn<Part, String> partNameTableColumn;

    @FXML
    private TableColumn<Part, Integer> partInventoryLevelTableColumn;

    @FXML
    private TableColumn<Part, Double> partCPUTableColumn;

    @FXML
    private TextField partSearchTextField;

    @FXML
    private TextField productSearchTextField;

    @FXML
    private Label partFeedbackLabel;

    @FXML
    private Label productFeedbackLabel;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, Integer> productIdTableColumn;

    @FXML
    private TableColumn<Product, String> productNameTableColumn;

    @FXML
    private TableColumn<Product, Integer> productInventoryLevelTableColumn;

    @FXML
    private TableColumn<Product, Double> productCPUTableColumn;

    private Stage myStage = null;

    /**
     * The event called when the controller is first loaded into memory. Sets up default form controls.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserData userData = (UserData)(this.getUserData());
        myStage = userData.getStageReference();

        partTableView.setItems(Inventory.getAllParts());
        partIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCPUTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(Inventory.getAllProducts());
        productIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevelTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCPUTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * The event called when the add button for parts is called. Loads part.fxml and closes the current stage.
     */
    @FXML
    private void onPartAddButtonClicked() throws IOException {
        Stage partStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("part.fxml"));
        PartController controller = new PartController();
        controller.setUserData(new UserData(partStage));
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        partStage.setScene(scene);
        partStage.show();
        this.myStage.close();
    }

    /**
     * The event called when the modify button for parts is called. Loads part.fxml and determines what part should be modified, then closes the current stage.
     */
    @FXML
    private void onPartModifyButtonClicked() throws IOException {
        int selectedPartIndex = getPartIndexFromTableSelection();
        if (selectedPartIndex > -1) {
            Stage partStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("part.fxml"));
            PartController controller = new PartController();
            controller.setUserData(new UserData(true, true, selectedPartIndex, partStage));
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load());
            partStage.setScene(scene);
            partStage.show();
            this.myStage.close();
        }
    }

    /**
     * The event called when the delete button for parts is called. Loads delete dialog confirmation box.
     */
    @FXML
    private void onPartDeleteButtonClicked() {
        try {
            loadDeleteDialogBox(true);
        } catch (IOException exception) {
        }
    }

    /**
     * The event called when the delete button for products is called. Loads delete dialog confirmation box.
     */
    @FXML
    private void onProductDeleteButtonClicked() {
        try {
            loadDeleteDialogBox(false);
        } catch (IOException exception) {
        }
    }

    /**
     * The event called when the user releases a key in the part search text field. Checks if enter was pressed and if so attempts to either find a matching search or puts the part search back to its default view.
     */
    @FXML
    private void onPartTextFieldKeyReleased(KeyEvent key) {
        if (key.getCode() == KeyCode.ENTER) {
            if (partSearchTextField.getText().trim().equals("")) {
                partTableView.setItems(Inventory.getAllParts());
            } else {
                processPartSearch();
            }
        }
    }

    /**
     * The event called when the user releases a key in the product search text field. Checks if enter was pressed and if so attempts to either find a matching search or puts the product search back to its default view.
     */
    @FXML
    private void onProductTextFieldKeyReleased(KeyEvent key) {
        if (key.getCode() == KeyCode.ENTER) {
            if (productSearchTextField.getText().trim().equals("")) {
                productTableView.setItems(Inventory.getAllProducts());
            } else {
                processProductSearch();
            }
        }
    }

    /**
     * The event called when add products button is clicked. Loads product.fxml and closes the current stage.
     */
    @FXML
    private void onAddProductButtonClicked() throws IOException {
        Stage productStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("product.fxml"));
        ProductController controller = new ProductController();
        controller.setUserData(new UserData(productStage));
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        productStage.setScene(scene);
        productStage.show();
        this.myStage.close();
    }

    /**
     * The event called when the modify button for products is clicked. Determines what product is being modified, loads product.fxml, and passes the information to its controller while closing the current stage.
     */
    @FXML
    private void onProductModifyButtonClicked () throws IOException {
        int selectedProductIndex = getProductIndexFromTableSelection();

        if (selectedProductIndex > -1) {
            Stage productStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("product.fxml"));
            ProductController controller = new ProductController();
            controller.setUserData(new UserData(true, false, selectedProductIndex, productStage));
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load());
            productStage.setScene(scene);
            productStage.show();
            this.myStage.close();
        }
    }

    /**
     * The event called when the exit button is clicked. Closes out the entire application.
     */
    @FXML
    private void onExitButtonClicked() {
        this.myStage.close();
    }

    /**
     * Gets the index of a part from the Inventory internal list.
     */
    private int getPartIndexFromTableSelection() {
        try {
            Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
            return Inventory.getAllParts().indexOf(selectedPart);
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * Gets the index of a product from the Inventory internal list.
     */
    private int getProductIndexFromTableSelection() {
        try {
            Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
            return Inventory.getAllProducts().indexOf(selectedProduct);
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * RUNTIME ERROR: Attempts to parse the search box as an integer first, if an error arises assumes that the user is doing a string search and attempts to find results via that method instead.
     * Attempts to find matching parts based off of the user's search field criteria.
     */
    private void processPartSearch() {
        try {
            int partId = Integer.parseInt(partSearchTextField.getText());
            Part matchingPart = Inventory.lookupPart(partId);

            if (matchingPart != null) {
                partTableView.getSelectionModel().select(Inventory.getAllParts().indexOf(matchingPart), partIdTableColumn);
            }
        } catch (NumberFormatException ex) {
            ObservableList<Part> matchingParts = Inventory.lookupPart(partSearchTextField.getText());

            if (matchingParts.size() > 0)
                partTableView.setItems(matchingParts);
        }
    }

    /**
     * RUNTIME ERROR: Attempts to parse the search box as an integer first, if an error arises assumes that the user is doing a string search and attempts to find results via that method instead.
     * Attempts to find matching products based off of the user's search field criteria.
     */
    private void processProductSearch() {
        try {
            int productId = Integer.parseInt(productSearchTextField.getText());
            Product matchingProduct = Inventory.lookupProduct(productId);

            if (null != matchingProduct) {
                productTableView.getSelectionModel().select(Inventory.getAllProducts().indexOf(matchingProduct), productIdTableColumn);
            }
        } catch (NumberFormatException ex) {
            ObservableList<Product> matchingProducts = Inventory.lookupProduct(productSearchTextField.getText());

            if (matchingProducts.size() > 0)
                productTableView.setItems(matchingProducts);
        }
    }

    /**
     * The method responsible for deleting a part. Called from delete confirmation box controller. Notifies user of success or failure.
     */
    public void deletePart() {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (Inventory.deletePart(selectedPart)) {
            partFeedbackLabel.setText("Part deleted.");
        } else {
            partFeedbackLabel.setText("Couldn't delete part.");
        }
    }

    /**
     * The method responsible for deleting a product. Called from delete confirmation box controller. Notifies user of success or failure and if that failure is due to associated parts.
     */
    public void deleteProduct() {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct.getAllAssociatedParts().size() > 0) {
            productFeedbackLabel.setText("Product still has associated parts, cannot delete.");

            return;
        }

        if (Inventory.deleteProduct(selectedProduct)) {
            productFeedbackLabel.setText("Product deleted");
        } else {
            productFeedbackLabel.setText("Couldn't delete product.");
        }
    }

    /**
     * Cancels the part delete request. Called from delete confirmation box. Notifies user that request was not processed.
     */
    public void cancelPartDelete() {
        partFeedbackLabel.setText("Did not delete part.");
    }

    /**
     * Cancels the product delete request. Called from delete confirmation box. Notifies user that request was not processed.
     */
    public void cancelProductDelete() {
        productFeedbackLabel.setText("Did not delete product.");
    }

    /**
     * Loads delete confirmation box and passes along UserData information whether the request is a part, product, and what the desired index is to be deleted.
     */
    private void loadDeleteDialogBox(boolean isPart) throws IOException {
        if (productTableView.getSelectionModel().getSelectedItem() != null) {
            Stage deleteStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("deletedialogbox.fxml"));

            UserData userData = new UserData(true, isPart, -1, deleteStage);
            userData.setParent(this);

            DeleteDialogController controller = new DeleteDialogController();
            controller.setUserData(userData);
            fxmlLoader.setController(controller);

            if (isPart) {
                deleteStage.setTitle("Parts");
            } else {
                deleteStage.setTitle("Products");
            }

            Scene scene = new Scene(fxmlLoader.load());
            deleteStage.initModality(Modality.APPLICATION_MODAL);
            deleteStage.setScene(scene);
            deleteStage.show();
        }
    }
}
