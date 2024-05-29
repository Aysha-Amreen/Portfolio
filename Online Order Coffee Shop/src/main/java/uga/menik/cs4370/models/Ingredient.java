package uga.menik.cs4370.models;

public class Ingredient {
    
     /**
     * Unique identifier for the ingredient.
     */
    private final String ingId;

    /**
     * Ingredient name
     */
    private final String ingName;

    /**
     * Ingredient weight
     */
    private final int weight;

    /**
     * Measurement type weight uses
     */
    private final String measurement;

    /**
     * Ingredient price
     */
    private final double price;

    /**
     * Constructs an ingredient with specified details.
     *
     * @param ingId         the unique identifier of the ingredient
     * @param ingName       the ingredient name
     * @param weight        the recipe category
     * @param meas          the measurement unit
     * @param price         the price of the ingredient
     */
    public Ingredient(String ingId, String ingName, int weight, String meas, double price) {
        this.ingId = ingId;
        this.ingName = ingName;
        this.weight = weight;
        this.measurement = meas;
        this.price = price;
    } // ingredient constructor

    /**
     * Returns the ingredient's unique id.
     *
     * @return the ingredient's id
     */
    public String getIngId() {
        return ingId;
    } // getIngId

    /**
     * Returns the ingredient's name.
     *
     * @return the ingredient's name
     */
    public String getIngName() {
        return ingName;
    } // getIngName

    /**
     * Returns the ingredient's weight.
     *
     * @return the ingredient's weight
     */
    public int getIngWeight() {
        return weight;
    } // getIngWeight

    /**
     * Returns the ingredient's measurement.
     *
     * @return the ingredient's measurement unit
     */
    public String getIngMeas() {
        return measurement;
    } // getIngMeas

    /**
     * Returns the ingredient's price.
     *
     * @return the ingredient's price
     */
    public double getIngPrice() {
        return price;
    } // getIngPrice

} // Ingredient