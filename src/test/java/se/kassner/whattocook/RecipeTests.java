package se.kassner.whattocook;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RecipeTests
{
    @Autowired
    RecipeRepository recipeRepository;

    @Test
    public void retrieveRecipeShouldWork()
    {
        Recipe recipe = recipeRepository.getReferenceById(1L);
        Assertions.assertEquals("Tacos", recipe.getName());
    }
}
