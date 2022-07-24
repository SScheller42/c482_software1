package softwareone.c482;

import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * UserData is used to pass data as an object between forms and manage state across program execution.
 */
public class UserData {
    private boolean isModifying;
    private boolean isPart;
    private int partOrProductIndex;
    private Stage stageReference;
    private Node parent;

    /**
     * Initializes the user data class with default data. Contains mostly immutable data after creation.
     */
    public UserData() {
        this.isModifying = false;
        this.isPart = false;
        this.partOrProductIndex = -1;
        stageReference = null;
    }

    /**
     * Initializes the user data class with fully specified data.
     * @param isModifying True if modifying a part or product, false if not.
     * @param isPart True if modifying a part, false if for a product.
     * @param partOrProductIndex 0 or higher array index of the part or product we are going to modify.
     * @param stageReference Reference to the newly created stage so that it can perform any necessary operations on itself.
     */
    public UserData(boolean isModifying, boolean isPart, int partOrProductIndex, Stage stageReference) {
        this.isModifying = isModifying;
        this.isPart = isPart;
        this.partOrProductIndex = partOrProductIndex;
        this.stageReference = stageReference;
    }

    /**
     * Initializes the user data class with just a stage reference. Preferred use is for part or product addition actions.
     */
    public UserData(Stage stageReference) {
        this.isModifying = false;
        this.isPart = false;
        this.partOrProductIndex = 0;
        this.stageReference = stageReference;
    }

    /**
     * Accesses the isModifying property of the object.
     */
    public boolean getIsModifying() {return this.isModifying;}

    /**
     * Accesses the isPart property of the object.
     */
    public boolean getIsPart() {return this.isPart;}

    /**
     * Accesses the array index property for a part or product we may be modifying.
     */
    public int getPartOrProductIndex() {return this.partOrProductIndex;}

    /**
     * Accesses the stage property of the object.
     */
    public Stage getStageReference() {return this.stageReference;}

    /**
     * Accesses the controller reference property of the parent stage that created this object.
     */
    public Node getParent() {return this.parent;}

    /**
     * Sets the controller reference property of the parent stage that created this object.
     */
    public void setParent(Node newParent) {this.parent = newParent;}
}
