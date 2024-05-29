package uga.menik.cs4370.models;

import java.util.List;

/**
 * Represents an item in its expanded form within the cofee shop site.
 * An ExpandedItem includes reviews.
 */
public class ExpandedItem extends Item {
    private final List<Review> reviews;
    private final List<Ingredient> ingredients;

    /**
     * Constructs an Item with specified details.
     *
     * @param itemId        the unique identifier of the item
     * @param name          the item name
     * @param category      the item category
     * @param size          the item size
     * @param price         the item price
     * @param rec_id        the item recipe
     */
    public ExpandedItem(String itemId, String name, String category, String size, double price, String rec_id, int reviewCount, List<Review> reviews, List<Ingredient> ingredients) {
        super(itemId, name, category, size, price, rec_id, reviewCount);
        this.reviews = reviews;
        this.ingredients = ingredients;
    } // ExpandedItem constructor


    /**
     * Returns an unmodifiable view of the review list, to prevent external modifications.
     *
     * @return an unmodifiable list of reviews
     */
    public List<Review> getReviews() {
        return List.copyOf(reviews);
    } // getReviews

    /**
     * Returns an unmodifiable view of the review list, to prevent external modifications.
     *
     * @return an unmodifiable list of reviews
     */
    public List<Ingredient> getIngredients() {
        return List.copyOf(ingredients);
    } // getIngredients
}
