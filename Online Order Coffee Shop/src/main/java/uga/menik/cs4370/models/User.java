package uga.menik.cs4370.models;
import java.util.Random;

/**
 * Represents a user of the micro blogging platform.
 */
public class User {

    /**
     * Unique identifier for the user.
     */
    private final String userId;

    /**
     * Path of the profile image file for the user.
     */
    private final String profileImagePath;

    /**
     * Constructs a User with specified details.
     *
     * @param userId           the unique identifier of the user
     * @param profileImagePath the path of the profile image file for the user
     */
    public User(String userId, String profileImagePath) {
        this.userId = userId;
        this.profileImagePath = profileImagePath;
    } // User constructor

    /**
     * Constructs a User with specified details.
     *
     * @param userId           the unique identifier of the user
     * @param profileImagePath the path of the profile image file for the user
     */
    public User(String userId) {
        this(userId, getAvatarPath(userId));
    } // User constructor

    /**
     * Given a userId generate a valid avatar path.
     */
    private static String getAvatarPath(String userId) {
        Random random = new Random();
        // Generate a random number between 1 and 20
        int randomNumber = 1 + random.nextInt(20);
        String avatarFileName = String.format("avatar_%d.png", randomNumber);
        System.out.println("/avatars/" + avatarFileName);
        return "/avatars/" + avatarFileName;
    } // getAvatarPath

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    } // getUserId

    /**
     * Returns the path of the profile image file for the user.
     *
     * @return the profile image path
     */
    public String getProfileImagePath() {
        return profileImagePath;
    } // getProfileImagePath
} // User
