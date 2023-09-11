package se.kassner.whattocook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>
{
    @Query("SELECT r FROM Recipe r ORDER BY RANDOM() LIMIT 1")
    public Recipe findOneRandom();
}
