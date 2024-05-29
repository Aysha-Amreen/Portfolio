package uga.menik.cs4370.models;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    
    /**
     * Unique user id of the user who's cart we are viewing.
     */
    private final String userId;

    /**
     * Items in the cart.
     */
    private final List<Item> items;

    /**
     *  Quantities of items in the cart
     */
    private final List<Integer> quantities;

    /**
     *  Quantities of items in the cart as String
     */
    private final List<String> quantitiesStr;

    /**
     * Constructs a Cart with specified details.
     *
     * @param userId     the unique identifier of the user
     * @param items      the items in the cart
     */
    public Cart(String userId, List<Item> items, List<Integer> quantities) {
        this.userId = userId;
        this.items = items;
        this.quantities = quantities;
        this.quantitiesStr = getQuantityStrings(quantities);
    } // Cart constructor

    /**
     * Returns the user Id of the user that made the order.
     *
     * @return the user Id
     */
    public String getUserId() {
        return userId;
    } // getUserId

    /**
     * Returns the items in the order.
     *
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    } // getItems

    /**
     * Returns the quantities of items in the order.
     *
     * @return the quantities
     */
    public List<Integer> getQuantities() {
        return quantities;
    } // getQuantities

    /**
     * Returns the quantities of items in the cart for the constructor.
     *
     * @return the quantities
     */
    private List<String> getQuantityStrings(List<Integer> myQuantities) {
        List<String> qStrings = new ArrayList<>();
        for (Integer ints : myQuantities) {
            qStrings.add(String.valueOf(ints));
        } // for
        return qStrings;
    } // getQuantityStrings

    /**
     * Returns the quantities of items in the cart.
     *
     * @return the quantities
     */
    public List<String> getQS() {
        return quantitiesStr;
    } // getQS
} // Cart
