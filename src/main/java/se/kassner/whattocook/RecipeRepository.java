package se.kassner.whattocook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>
{
    @Query("SELECT r FROM Recipe r ORDER BY RANDOM() LIMIT 1")
    public Recipe findOneRandom();

    @Query(
        value = "SELECT r.* FROM recipe r WHERE r.id NOT IN (SELECT ri.recipe_id FROM recipe_ingredients ri WHERE ri.ingredient_id IN (:ingredient_ids)) ORDER BY RANDOM() LIMIT 1",
        nativeQuery = true
    )
    public Recipe findOneRandomWithoutIngredients(@Param("ingredient_ids") List<Long> excludedIngredientIds);

    @Query("SELECT r FROM Recipe r ORDER BY r.name ASC")
    public List<Recipe> findAllForListing();
}
