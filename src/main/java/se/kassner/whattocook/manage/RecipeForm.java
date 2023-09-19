package se.kassner.whattocook.manage;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import se.kassner.whattocook.Ingredient;
import se.kassner.whattocook.Recipe;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RecipeForm
{
    private Long id;

    @NotBlank
    @Length(min = 2, max = 255)
    private String name;

    private String instructions;

    private String[] ingredients = new String[]{""};

    public RecipeForm()
    {
    }

    public RecipeForm(Long id, String name, String instructions, String[] ingredients)
    {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.setIngredients(ingredients);

        if (this.ingredients.length == 0) {
            this.ingredients = new String[]{""};
        }
    }

    public RecipeForm(Recipe recipe)
    {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.instructions = recipe.getInstructions();
        this.ingredients = recipe.getIngredients().stream().map(Ingredient::getName).toArray(String[]::new);

        if (this.ingredients.length == 0) {
            this.ingredients = new String[]{""};
        }
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getInstructions()
    {
        return instructions;
    }

    public String[] getIngredients()
    {
        return ingredients;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }

    public void setIngredients(String[] ingredients)
    {
        this.ingredients = Arrays.stream(ingredients).map(String::trim).toArray(String[]::new);
    }
}
