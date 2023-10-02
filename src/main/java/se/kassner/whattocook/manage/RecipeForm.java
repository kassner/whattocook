package se.kassner.whattocook.manage;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import se.kassner.whattocook.Ingredient;
import se.kassner.whattocook.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeForm
{
    private Long id;

    @NotBlank
    @Length(min = 2, max = 255)
    private String name;

    private String instructions;

    private List<String> ingredients = new ArrayList<>();

    public RecipeForm()
    {
    }

    public RecipeForm(Long id, String name, String instructions, List<String> ingredients)
    {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.setIngredients(ingredients);
    }

    public RecipeForm(Recipe recipe)
    {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.instructions = recipe.getInstructions();
        this.ingredients = recipe.getIngredients().stream().map(Ingredient::getName).toList();
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

    public List<String> getIngredients()
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

    public void setIngredients(List<String> ingredients)
    {
        this.ingredients = ingredients;
    }
}
