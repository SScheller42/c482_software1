package softwareone.c482;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;



/**
 * The controller for deletedialogbox.fxml.
 */
public class DeleteDialogController extends Node implements javafx.fxml.Initializable {
    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label warningLabel;

    private Stage myStage;

    /**
     * The event called when the controller is first loaded into memory. Sets the stage.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserData userData = (UserData)(this.getUserData());

        this.myStage = userData.getStageReference();

        if (userData.getIsPart()) {
            warningLabel.setText("Are you sure you want to delete this part?");
        } else {
            if (userData.getPartOrProductIndex() > -1) {
                warningLabel.setText("Are you sure you want to delete this associated part?");
            } else {
                warningLabel.setText("Are you sure you want to delete this product?");
            }
        }
    }

    /**
     * The event called when the confirm button is clicked and sends the request to the proper controller.
     */
    @FXML
    private void onConfirmButtonClicked() {
        UserData userData = (UserData)(this.getUserData());

        if (userData.getPartOrProductIndex() < 0) {
            InventoryManagementSystemController controller = (InventoryManagementSystemController)(userData.getParent());

            if (userData.getIsPart()) {
                controller.deletePart();
            } else {
                controller.deleteProduct();
            }
        } else {
            ProductController controller = (ProductController)(userData.getParent());
            controller.deleteAssociatedPart();
        }

        userData.getStageReference().close();
    }

    /**
     * The event called when the cancel button is clicked and sends the request to the proper controller.
     */
    @FXML
    private void onCancelButtonClicked() {
        UserData userData = (UserData)(this.getUserData());

        if (userData.getPartOrProductIndex() < 0) {
            InventoryManagementSystemController controller = (InventoryManagementSystemController)(userData.getParent());

            if (userData.getIsPart()) {
                controller.cancelPartDelete();
            } else {
                controller.cancelProductDelete();
            }
        } else {
            ProductController controller = (ProductController)(userData.getParent());
            controller.cancelDeleteAssociatedPartRequest();
        }

        if (userData.getIsPart()) {
            InventoryManagementSystemController controller = (InventoryManagementSystemController)(userData.getParent());
            controller.cancelPartDelete();
        }

        userData.getStageReference().close();
    }
}
