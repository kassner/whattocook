package se.kassner.whattocook;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Table(name = "recipe", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@EntityListeners(RecipeEntityListener.class)
public class Recipe
{
    @Id
    @SequenceGenerator(name = "recipe_id_seq", sequenceName = "recipe_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_id_seq")
    private long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @Column(name = "instructions")
    private String instructions;

    @ManyToMany
    @JoinTable(
        name = "recipe_ingredients",
        joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    )
    @OrderBy("name ASC")
    private List<Ingredient> ingredients;

    public Recipe()
    {
    }

    public Recipe(long id, @NonNull String name, String instructions, List<Ingredient> ingredients)
    {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    public long getId()
    {
        return id;
    }

    @NonNull
    public String getName()
    {
        return name;
    }

    public String getInstructions()
    {
        return instructions;
    }

    public List<Ingredient> getIngredients()
    {
        return ingredients;
    }

    public void setName(@NonNull String name)
    {
        this.name = name;
    }

    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }

    public void setIngredients(List<Ingredient> ingredients)
    {
        this.ingredients = ingredients;
    }
}
