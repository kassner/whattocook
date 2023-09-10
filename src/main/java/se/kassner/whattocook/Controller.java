package se.kassner.whattocook;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class Controller
{
    private final RecipeRepository recipeRepository;

    public Controller(RecipeRepository recipeRepository)
    {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping(value = "/recipe", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> getRecipe()
    {
        // @TODO id should come from session
        long id = (long)(Math.random() * 3) + 1;

        Optional<Recipe> recipe = recipeRepository.findById(id);
        return ResponseEntity.of(recipe);
    }
}
