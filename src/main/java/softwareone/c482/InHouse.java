package softwareone.c482;


/**
 * Tracks the parts that are made in-house.
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * Initializes the in-house part with all the information it needs.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
    }

    /**
     * Sets the machine ID of the in-house part.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Gets the machine ID of the part.
     */
    public int getMachineId () {return this.machineId;}
}
