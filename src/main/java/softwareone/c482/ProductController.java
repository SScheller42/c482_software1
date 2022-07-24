package softwareone.c482;

import softwareone.c482.*;
import javafx.collections.FXCollections;
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
 *
 * @author Gerald Seth Scheller Student ID: #001448388
 */

/**
 * The controller for the product.fxml view.
 */
public class ProductController extends Node implements javafx.fxml.Initializable {
    @FXML
    private TextField productIdTextField;

    @FXML
    private TextField productNameTextField;

    @FXML
    private TextField productStockTextField;

    @FXML
    private TextField productPriceTextField;

    @FXML
    private TextField productMaxTextField;

    @FXML
    private TextField productMinTextField;

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
    private TableView<Part> associatedPartTableView;

    @FXML
    private TableColumn<Part, Integer> associatedPartIdTableColumn;

    @FXML
    private TableColumn<Part, String> associatedPartNameTableColumn;

    @FXML
    private TableColumn<Part, Integer> associatedPartInventoryLevelTableColumn;

    @FXML
    private TableColumn<Part, Double> associatedPartCPUTableColumn;

    @FXML
    private TextField partSearchTextField;

    @FXML
    private Label warningLabel;

    private Stage myStage;

    /**
     * The event called when the add associated part button is clicked. Adds an associated part to a product.
     */
    @FXML
    private void onAddAssociatedPartButtonClicked() {
        Part selectedAssociatedPart = partTableView.getSelectionModel().getSelectedItem();

        if (null != selectedAssociatedPart) {
            ObservableList<Part> listCopy = associatedPartTableView.getItems();

            listCopy.add(selectedAssociatedPart);

            associatedPartTableView.setItems(listCopy);
        }
    }

    /**
     * The event called when the remove associated part button is clicked. Loads a delete confirmation dialog box.
     */
    @FXML
    private void onRemoveAssociatedPartButtonClicked() {
        try {
            loadAssociatedPartDeleteDialogBox();
        } catch (IOException ex) {
        }
    }

    /**
     * The event called when a user releases a key in the part search box. Runs the search logic to find matching search results.
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
     * The event called when a user clicks the save button. Saves the product if it passes all logic checks.
     * @throws IOException
     */
    @FXML
    private void onSaveButtonClicked() throws IOException {
        if (allFieldsValid()) {
            saveProduct(productIdTextField.getText().contains("Auto Assign"));

            onCancelButtonClicked();
        }
    }

    /**
     * The event called when a user clicks the cancel button. Loads the inventorymanagementsystem.fxml view and closes the product.fxml view.
     * @throws IOException
     */
    @FXML
    private void onCancelButtonClicked() throws IOException {
        Stage mainMenuStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("inventorymanagementsystem.fxml"));
        InventoryManagementSystemController controller = new InventoryManagementSystemController();
        controller.setUserData(new UserData(mainMenuStage));
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        mainMenuStage.setScene(scene);
        mainMenuStage.show();
        myStage.close();
    }

    /**
     * The event called when the controller is first loaded into memory. Sets up default form controls such as the tableview.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserData userData = (UserData)(this.getUserData());

        this.myStage = userData.getStageReference();

        partTableView.setItems(Inventory.getAllParts());
        partIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCPUTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        if (userData.getIsModifying()) {
            loadExistingProduct(userData.getPartOrProductIndex());

            associatedPartTableView.setItems(Inventory.getAllProducts().get(userData.getPartOrProductIndex()).getAllAssociatedParts());
        } else {
            associatedPartTableView.setItems(FXCollections.observableArrayList());
        }

        associatedPartIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryLevelTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartCPUTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Loads an existing product for modification.
     */
    private  void loadExistingProduct(int productListIndex) {
        Product existingProduct = Inventory.getAllProducts().get(productListIndex);

        productIdTextField.setText(String.valueOf(existingProduct.getId()));
        productNameTextField.setText(existingProduct.getName());
        productStockTextField.setText(String.valueOf(existingProduct.getStock()));
        productMinTextField.setText(String.valueOf(existingProduct.getMin()));
        productMaxTextField.setText(String.valueOf(existingProduct.getMax()));
        productPriceTextField.setText(String.valueOf(existingProduct.getPrice()));
    }

    /**
     * RUNTIME ERROR: Tries to pull from the internal product list however if it's null it will throw an out of bounds error in which case we just return 1.
     * Creates a new ID for a part or product. Gets the highest existing ID and adds 1 to it.
     */
    private int generateNewID() {
        try {
            return Inventory.getAllProducts().get(Inventory.getAllProducts().size() -1).getId() + 1;
        } catch (IndexOutOfBoundsException ex) {
            return 1;
        }
    }

    /**
     * RUNTIME ERROR: Attempts to parse strings and numbers to appropriate destination formats. Catches any errors to communicate to the user to input proper values. Also performs logic checks to make sure that stock stays between min/max, and min is less than max.
     * Performs logic as well as try-catch error checks to ensure all values are valid.
     */
    private boolean allFieldsValid() {
        boolean passedAllChecks = true;
        boolean allInventoryChecksPassed = true;
        int currentStock = 0, minStock = 0, maxStock = 0;

        warningLabel.setText("");

        if (productNameTextField.getText().trim().equals("")) {
            passedAllChecks = false;
            appendTextToWarningLabel("Name Field needs a value.");
        }

        try {
            currentStock = Integer.parseInt(productStockTextField.getText());
        } catch (NumberFormatException ex) {
            passedAllChecks = false;
            allInventoryChecksPassed = false;
            appendTextToWarningLabel("Inventory needs to have an integer value.");
        }

        try {
            Double.parseDouble(productPriceTextField.getText());
        } catch (NumberFormatException ex) {
            passedAllChecks = false;
            appendTextToWarningLabel("Price needs to have a double value.");
        } catch (NullPointerException ex) {
            passedAllChecks = false;
            appendTextToWarningLabel("Price cannot have a null value.");
        }

        try {
            maxStock = Integer.parseInt(productMaxTextField.getText());
        } catch (NumberFormatException ex) {
            passedAllChecks = false;
            allInventoryChecksPassed = false;
            appendTextToWarningLabel("Max needs to have an integer value.");
        }

        try {
            minStock = Integer.parseInt(productMinTextField.getText());
        } catch (NumberFormatException ex) {
            passedAllChecks = false;
            allInventoryChecksPassed = false;
            appendTextToWarningLabel("Min needs to have an integer value.");
        }

        if (allInventoryChecksPassed) {
            if (minStock >= maxStock) {
                appendTextToWarningLabel("Min stock value needs to be less than Max stock value.");
                passedAllChecks = false;
            }

            if (currentStock > maxStock || currentStock < minStock) {
                appendTextToWarningLabel("Inv has to be between or equal to max and min.");
                passedAllChecks = false;
            }
        }

        return passedAllChecks;
    }

    /**
     * Attempts to save the product currently being edited.
     */
    private void saveProduct(boolean isNew) {
        int partId = isNew ? generateNewID() : Integer.parseInt(productIdTextField.getText());
        Product newProduct = new Product(partId, productNameTextField.getText(), Double.parseDouble(productPriceTextField.getText()),
                Integer.parseInt(productStockTextField.getText()), Integer.parseInt(productMinTextField.getText()), Integer.parseInt(productMaxTextField.getText()));

        for (Part associatedPart : associatedPartTableView.getItems()) {
            newProduct.addAssociatedPart(associatedPart);
        }

        if (isNew) {
            Inventory.addProduct(newProduct);
        } else {
            UserData userData = (UserData)(this.getUserData());
            Inventory.updateProduct(userData.getPartOrProductIndex(), newProduct);
        }
    }

    /**
     * Adds a new line of text to the warning label.
     */
    private void appendTextToWarningLabel(String textToAdd) {
        warningLabel.setText(warningLabel.getText() + "\n" + textToAdd);
    }

    /**
     * RUNTIME ERROR: Attempts to parse the text field as an integer first, if an error occurs it switches logic to handling a string instead.
     * Attempts to find appropriate part search results based on user input.
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
     * Loads deletedialogbox.fxml to remove an associated part if the user confirms they want to follow through with the action.
     * @throws IOException
     */
    private void loadAssociatedPartDeleteDialogBox() throws IOException {
        if (associatedPartTableView.getSelectionModel().getSelectedItem() != null) {
            Stage deleteStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("deletedialogbox.fxml"));

            UserData userData = new UserData(true, false, 1, deleteStage);
            userData.setParent(this);

            DeleteDialogController controller = new DeleteDialogController();
            controller.setUserData(userData);
            fxmlLoader.setController(controller);

            deleteStage.setTitle("Associated Parts");

            Scene scene = new Scene(fxmlLoader.load());
            deleteStage.initModality(Modality.APPLICATION_MODAL);
            deleteStage.setScene(scene);
            deleteStage.show();
        }
    }

    /**
     * Called by delete confirmation box if user confirms that an associated part should be deleted. If a valid associated part is selected, removes it.
     */
    public void deleteAssociatedPart() {
        Part selectedAssociatedPart = associatedPartTableView.getSelectionModel().getSelectedItem();

        if (null != selectedAssociatedPart) {
            Product currentlyEditedProduct = Inventory.lookupProduct(Integer.parseInt(productIdTextField.getText()));

            currentlyEditedProduct.deleteAssociatedPart(selectedAssociatedPart);

            warningLabel.setText("Associated part removed.");
        }
    }

    /**
     * Called by delete confirmation box if user cancels the process to remove an associated part.
     */
    public void cancelDeleteAssociatedPartRequest() {
        warningLabel.setText("Did not delete associated part.");
    }
}
