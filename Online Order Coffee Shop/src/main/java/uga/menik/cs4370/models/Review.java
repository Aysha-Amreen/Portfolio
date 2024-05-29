package uga.menik.cs4370.models;

public class Review {
    
        /**
     * Unique identifier for the review.
     */
    private final String reviewId;

    /**
     * Text content of the review.
     */
    private final String content;

    /**
     * Date when the review was created.
     */
    private final String reviewDate;

    /**
     * User who created the review.
     */
    private final User user;

    /**
    * Item being reviewed
    */
    private final Item item;

    /**
     * Constructs a BasicReview with specified details.
     *
     * @param reviewId   the unique identifier of the review
     * @param content    the text content of the review
     * @param reviewDate the creation date of the review
     * @param user       the user who created the review
     * @param item       the item being reviewed
     */
    public Review(String reviewId, String content, String reviewDate, User user, Item item) {
        this.reviewId = reviewId;
        this.content = content;
        this.reviewDate = reviewDate;
        this.user = user;
        this.item = item;
    } // BasicReview controller

    /**
     * Returns the review ID.
     *
     * @return the review ID
     */
    public String getReviewId() {
        return reviewId;
    } // getReviewId

    /**
     * Returns the content of the review.
     *
     * @return the content of the review
     */
    public String getContent() {
        return content;
    } // getContent

    /**
     * Returns the review creation date.
     *
     * @return the review creation date
     */
    public String getReviewDate() {
        return reviewDate;
    } // getPostDate

    /**
     * Returns the user who created the review.
     *
     * @return the user who created the review
     */
    public User getUser() {
        return user;
    } // getUser

    /**
     * Returns the item reviewed in the review.
     *
     * @return the item reviewed in the review
     */
    public Item getItem() {
        return item;
    } // getItem

} // Review
