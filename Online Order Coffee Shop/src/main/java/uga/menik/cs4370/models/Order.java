
package uga.menik.cs4370.models;
import java.util.ArrayList;
import java.util.List;

public class Order {
    
    /**
     * Unique identifier for the order.
     */
    private final String orderId;

    /**
     * Text content of the date.
     */
    private final String date;

    /**
     * Unique identifier for the user.
     */
    private final String userId;

    /**
     * Items in the order.
     */
    private final List<Item> items;

    /**
     *  Quantities of items in the cart
     */
    private final List<Integer> quantities;

    /**
     *  Quantities of items in the order as String
     */
    private final List<String> quantitiesStr;

    /**
     * Constructs a BasicPost with specified details.
     *
     * @param orderId     the unique identifier of the order
     * @param date        the creation date of the order
     * @param userId      the unique identifier of the user
     * @param items       the items in the order
     */
    public Order(String orderId, String date, String userId, List<Item> items, List<Integer> quantities) {
        this.orderId = orderId;
        this.date = date;
        this.userId = userId;
        this.items = items;
        this.quantities = quantities;
        this.quantitiesStr = getQuantityStrings(quantities);
    } // Order constructor

    /**
     * Returns the order Id.
     *
     * @return the order Id
     */
    public String getOrderId() {
        return orderId;
    } // getOrderId

    /**
     * Returns the order creation date.
     *
     * @return the order creation date
     */
    public String getDate() {
        return date;
    } // getDate

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
     * Returns the quantities of items in the order for the constructor.
     *
     * @return the quantities
     */
    private List<String> getQuantityStrings(List<Integer> myQuantities) {
        List<String> qStrings = new ArrayList<>();
        for (Integer ints : myQuantities) {
            qStrings.add(String.valueOf(ints));
        } // for
        return qStrings;
    } // getQuantities

    /**
     * Returns the quantities of items in the order.
     *
     * @return the quantities
     */
    public List<String> getQS() {
        return quantitiesStr;
    } // getQS
} // order
