package uga.menik.cs4370.models;

public class Item {

    /**
     * Unique identifier for the item.
     */
    private final String itemId;

    /**
     * Name of the item.
     */
    private final String name;

    /**
     * Item category.
     */
    private final String category;

    /**
     * Item size.
     */
    private final String size;

    /**
     * Item price
     */
    private final double price;

    /**
     * Recipe id the item uses
     */
    private final String rec_id;

    /**
     * Review count for an item
     */
    private final int reviewCount;

    /**
     * Profile image path
     */
    private final String path;

    /**
     * Constructs an Item with specified details.
     *
     * @param itemId        the unique identifier of the item
     * @param name          the item name
     * @param category      the item category
     * @param size          the item size
     * @param price         the item price
     * @param rec_id        the item recipe
     * @param reviewCount   number of reviews for the item
     */
    public Item(String itemId, String name, String category, String size, double price, String rec_id, int reviewCount) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.size = size;
        this.price = price;
        this.rec_id = rec_id;
        this.reviewCount = reviewCount;
        this.path = "/items/" + name + ".jpg";
    } // Item constructor

    /**
     * Contructs an empty item
     * 
     * @param itemId the unique identifier of the item 
     */
    public Item(String itemId) {
        this.itemId = itemId;
        this.name = "NULL";
        this.category = "NULL";
        this.size = "NULL";
        this.price = 0;
        this.rec_id = "NULL";
        this.reviewCount = 0;
        this.path = "/item/" + name + ".jpg";
    } // NULL Item Constructor

    /**
     * Returns the item's unique id.
     *
     * @return the item's id
     */
    public String getItemId() {
        return itemId;
    } // getItemId

    /**
     * Returns the item's name.
     *
     * @return the item's name
     */
    public String getItemName() {
        return name;
    } // getItemName

    /**
     * Returns the item's category.
     *
     * @return the item's category
     */
    public String getItemCategory() {
        return category;
    } // getItemCategory

    /**
     * Returns the item's size.
     *
     * @return the item's size
     */
    public String getItemSize() {
        return size;
    } // getItemSize

    /**
     * Returns the item's unique id.
     *
     * @return the item's id
     */
    public double getItemPrice() {
        return price;
    } // getItemPrice

    /**
     * Returns the item's unique id.
     *
     * @return the item's id
     */
    public String getItemRecId() {
        return rec_id;
    } // getItemRecId

    /**
     * Returns the item's review count.
     *
     * @return the item's review count
     */
    public int getRevCount() {
        return reviewCount;
    } // getRevCount

    /**
     * Returns the path of the item's profile picture.
     *
     * @return the item's pfp path
     */
    public String getItemPath() {
        return path;
    } // getItemRecId
} // Item
