package softwareone.c482;

import softwareone.c482.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for part.fxml.
 */
public class PartController extends Node implements javafx.fxml.Initializable {
    @FXML
    private RadioButton inHouseRadio;

    @FXML
    private RadioButton outsourcedRadio;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField stockTextField;

    @FXML
    private TextField costPerUnitTextField;

    @FXML
    private TextField maxStockTextField;

    @FXML
    private TextField minStockTextField;

    @FXML
    private TextField machineOrCompanyTextField;

    @FXML
    private Label machineOrCompanyLabel;

    @FXML
    private Label warningLabel;

    private Stage myStage;

    /**
     * The event called when the in-house radio button is selected. Sets correct label text.
     */
    @FXML
    private void onInHouseRadioSelected() {
        outsourcedRadio.setSelected(false);
        setLabelText(false);
    }

    /**
     * The event called when the outsourced radio button is selected. Sets correct label text.
     */
    @FXML
    private void onOutsourcedRadioSelected() {
        inHouseRadio.setSelected(false);
        setLabelText(true);
    }

    /**
     * The event called when the save button is selected. Checks to see if all fields are valid and then saves the part.
     * @throws IOException
     */
    @FXML
    private void onSaveButtonClicked() throws IOException {
        if (allFieldsValid()) {
            if (idTextField.getText().contains("Auto Assign")) {
                saveNewPart();
            } else {
                saveExistingPart();
            }

            onCancelButtonClicked();
        }
    }

    /**
     * The event called when the cancel button is clicked. Loads the inventorymanagementsystem.fxml stage and unloads part.fxml.
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
     * The event called when the controller is first loaded into memory. Sets up default form controls such as if we're modifying or adding new.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserData userData = (UserData)(this.getUserData());

        myStage = userData.getStageReference();

        if (userData.getIsModifying()) {
            setupModifyPart(userData);
        } else {
            setupAddPart();
        }
    }

    /**
     * Performs initialization logic related to if the form is loaded to add a part.
     */
    private void setupAddPart() {
        setLabelText(false);

        inHouseRadio.setSelected(true);
        inHouseRadio.requestFocus();
    }

    /**
     * Performs initialization logic related to if the form is loaded to modify a part.
     */
    private void setupModifyPart(UserData userData) {
        try {
            InHouse part = (InHouse)(Inventory.getAllParts().get(userData.getPartOrProductIndex()));
            loadInHousePart(part);
        } catch (ClassCastException ex) {
            Outsourced part = (Outsourced)(Inventory.getAllParts().get(userData.getPartOrProductIndex()));
            loadOutsourcedPart(part);
        }
    }

    /**
     * Flips the label text between 'Machine ID' or 'Company Name'
     */
    private void setLabelText(boolean isOutsourced) {
        machineOrCompanyLabel.setText(!isOutsourced ? "Machine ID" : "Company Name");
    }

    /**
     * Reads the properties of an in-house part and readies the stage for editing.
     */
    private void loadInHousePart(InHouse inHousePart) {
        setLabelText(false);
        inHouseRadio.setSelected(true);
        inHouseRadio.requestFocus();

        idTextField.setText(String.valueOf(inHousePart.getId()));
        nameTextField.setText(inHousePart.getName());
        stockTextField.setText(String.valueOf(inHousePart.getStock()));
        costPerUnitTextField.setText(String.valueOf(inHousePart.getPrice()));
        maxStockTextField.setText(String.valueOf(inHousePart.getMax()));
        minStockTextField.setText(String.valueOf(inHousePart.getMin()));
        machineOrCompanyTextField.setText(String.valueOf(inHousePart.getMachineId()));
    }

    /**
     * Reads the properties of an outsourced part and readies the stage for editing.
     */
    private void loadOutsourcedPart(Outsourced outsourcedPart) {
        setLabelText(true);
        outsourcedRadio.setSelected(true);
        outsourcedRadio.requestFocus();

        idTextField.setText(String.valueOf(outsourcedPart.getId()));
        nameTextField.setText(outsourcedPart.getName());
        stockTextField.setText(String.valueOf(outsourcedPart.getStock()));
        costPerUnitTextField.setText(String.valueOf(outsourcedPart.getPrice()));
        maxStockTextField.setText(String.valueOf(outsourcedPart.getMax()));
        minStockTextField.setText(String.valueOf(outsourcedPart.getMin()));
        machineOrCompanyTextField.setText(outsourcedPart.getCompanyName());
    }

    /**
     * Saves a new part into the persistent Inventory class memory.
     */
    private void saveNewPart() {
        if (inHouseRadio.isSelected()) {
            InHouse part = new InHouse(generateNewID(), nameTextField.getText(), Double.parseDouble(costPerUnitTextField.getText()), Integer.parseInt(stockTextField.getText()),
                    Integer.parseInt(minStockTextField.getText()), Integer.parseInt(maxStockTextField.getText()));
            part.setMachineId(Integer.parseInt(machineOrCompanyTextField.getText()));
            Inventory.addPart(part);
        }

        if (outsourcedRadio.isSelected()) {
            Outsourced part = new Outsourced(generateNewID(), nameTextField.getText(), Double.parseDouble(costPerUnitTextField.getText()), Integer.parseInt(stockTextField.getText()),
                    Integer.parseInt(minStockTextField.getText()), Integer.parseInt(maxStockTextField.getText()));
            part.setCompanyName(machineOrCompanyTextField.getText());
            Inventory.addPart(part);
        }
    }

    /**
     * Updates an existing part in the persistent Inventory class memory.
     */
    private  void saveExistingPart() {
        UserData userData = (UserData)(this.getUserData());
        if (inHouseRadio.isSelected()) {
            InHouse part = new InHouse(Integer.parseInt(idTextField.getText()), nameTextField.getText(), Double.parseDouble(costPerUnitTextField.getText()), Integer.parseInt(stockTextField.getText()),
                    Integer.parseInt(minStockTextField.getText()), Integer.parseInt(maxStockTextField.getText()));
            part.setMachineId(Integer.parseInt(machineOrCompanyTextField.getText()));
            Inventory.updatePart(userData.getPartOrProductIndex(), part);
        }

        if (outsourcedRadio.isSelected()) {
            Outsourced part = new Outsourced(Integer.parseInt(idTextField.getText()), nameTextField.getText(), Double.parseDouble(costPerUnitTextField.getText()), Integer.parseInt(stockTextField.getText()),
                    Integer.parseInt(minStockTextField.getText()), Integer.parseInt(maxStockTextField.getText()));
            part.setCompanyName(machineOrCompanyTextField.getText());
            Inventory.updatePart(userData.getPartOrProductIndex(), part);
        }
    }

    /**
     * RUNTIME ERROR: Tries to pull from the internal part list however if it's null it will throw an out of bounds error in which case we just return 1.
     * Creates a new ID for a part or product. Gets the highest existing ID and adds 1 to it.
     */
    private int generateNewID() {
        try {
            return Inventory.getAllParts().get(Inventory.getAllParts().size() -1).getId() + 1;
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

        if (nameTextField.getText().trim().equals("")) {
            passedAllChecks = false;
            appendTextToWarningLabel("Name Field requires a value.");
        }

        try {
            currentStock = Integer.parseInt(stockTextField.getText());
        } catch (NumberFormatException ex) {
            passedAllChecks = false;
            allInventoryChecksPassed = false;
            appendTextToWarningLabel("Inventory requires an integer value.");
        }

        try {
            Double.parseDouble(costPerUnitTextField.getText());
        } catch (NumberFormatException ex) {
            passedAllChecks = false;
            appendTextToWarningLabel("Price/Cost requires a double value.");
        } catch (NullPointerException ex) {
            passedAllChecks = false;
            appendTextToWarningLabel("Price/Cost cannot have a null value.");
        }

        try {
            maxStock = Integer.parseInt(maxStockTextField.getText());
        } catch (NumberFormatException ex) {
            passedAllChecks = false;
            allInventoryChecksPassed = false;
            appendTextToWarningLabel("Max requires an integer value.");
        }

        try {
            minStock = Integer.parseInt(minStockTextField.getText());
        } catch (NumberFormatException ex) {
            passedAllChecks = false;
            allInventoryChecksPassed = false;
            appendTextToWarningLabel("Min requires an integer value.");
        }

        if (inHouseRadio.isSelected()) {
            try {
                Integer.parseInt(machineOrCompanyTextField.getText());
            } catch (NumberFormatException ex) {
                passedAllChecks = false;
                appendTextToWarningLabel("Machine Id requires an integer value.");
            }
        }

        if (outsourcedRadio.isSelected()) {
            if (machineOrCompanyTextField.getText().trim().equals("")) {
                passedAllChecks = false;
                appendTextToWarningLabel("Company Name Field requires a value.");
            }
        }

        if (allInventoryChecksPassed) {
            if (minStock >= maxStock) {
                appendTextToWarningLabel("Min stock value must be less than Max stock value.");
                passedAllChecks = false;
            }

            if (currentStock > maxStock || currentStock < minStock) {
                appendTextToWarningLabel("Inv must be between or equal to max and min.");
                passedAllChecks = false;
            }
        }

        return passedAllChecks;
    }

    /**
     * Adds a new line of text to the warning label.
     */
    private void appendTextToWarningLabel(String textToAdd) {
        warningLabel.setText(warningLabel.getText() + "\n" + textToAdd);
    }
}
