package se.kassner.whattocook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>
{
    public Ingredient getOneByName(String name);

    @Query("SELECT i.name FROM Ingredient i ORDER BY i.name ASC")
    public List<String> findAllNames();
}
