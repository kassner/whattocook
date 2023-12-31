package se.kassner.whattocook;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.Set;

@Entity
@Table(name = "ingredient")
public class Ingredient
{
    @Id
    @SequenceGenerator(name = "ingredient_id_seq", sequenceName = "ingredient_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_id_seq")
    private long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "ingredients")
    private Set<Recipe> recipes;

    public Ingredient()
    {
    }

    public Ingredient(long id, @NonNull String name)
    {
        this.id = id;
        this.name = name;
    }

    public Ingredient(@NonNull String name)
    {
        this.name = name;
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
}
