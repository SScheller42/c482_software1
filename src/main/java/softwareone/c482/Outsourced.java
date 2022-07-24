package softwareone.c482;

/**
 * Used to track outsourced part information.
 */
public class Outsourced extends Part {
    private String companyName;

    /**
     * Initializes the outsourced part with all needed information
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
    }

    /**
     * Sets the name of the company that the outsourced part comes from.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Gets the name of the company that the part is outsourced from.
     */
    public String getCompanyName() {
        return this.companyName;
    }
}
