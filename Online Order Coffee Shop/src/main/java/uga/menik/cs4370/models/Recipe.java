package uga.menik.cs4370.models;
import java.util.ArrayList;
import java.util.List;

public class Recipe {

    /**
     * Unique identifier for the recipe.
     */
    private final String recipeId;

    /**
     * Ingredient ids the recipe uses
     */
    private final List<Ingredient> ingredients;

    /**
     * Ingredient quantities the recipe uses
     */
    private final List<String> quantities;

    /**
     * Constructs an recipe with specified details.
     *
     * @param recipeId      the unique identifier of the recipe
     * @param ingredients   the recipe ingredients
     * @param quantities    the recipe ingredient quantities
     */
    public Recipe(String recipeId, List<Ingredient> ingredients, List<String> quantities) {
        this.recipeId = recipeId;
        this.ingredients = ingredients;
        this.quantities = quantities;
    } // recipe constructor


    /**
     * Constructs an empty recipe
     *
     * @param recipeId      the unique identifier of the recipe
     */
    public Recipe(String recipeId) {
        this.recipeId = recipeId;
        this.ingredients = new ArrayList<>();
        this.quantities = new ArrayList<>();
    } // recipe constructor

    /**
     * Returns the recipe's unique id.
     *
     * @return the recipe's id
     */
    public String getRecipeId() {
        return recipeId;
    } // getrecipeId

    /**
     * Returns the recipe's name.
     *
     * @return the recipe's name
     */
    public List<Ingredient> getRecipeIngredients() {
        return ingredients;
    } // getrecipeName

    /**
     * Returns the recipe's category.
     *
     * @return the recipe's category
     */
    public List<String> getRecipeQuantities() {
        return quantities;
    } // getrecipeCategory
} // Recipe
