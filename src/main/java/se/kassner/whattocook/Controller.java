package se.kassner.whattocook;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class Controller
{
    private final IngredientRepository ingredientRepository;
    private final SessionService sessionService;

    public Controller(IngredientRepository ingredientRepository, SessionService sessionService)
    {
        this.ingredientRepository = ingredientRepository;
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/session", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createSession()
    {
        sessionService.createIfNeeded();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/session/recipe", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> getRecipe()
    {
        Session session = sessionService.get();

        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        Recipe recipe = sessionService.getRecipe(session);
        return ResponseEntity.ok(recipe);
    }

    @DeleteMapping(value = "/session/ingredient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeIngredient(@PathVariable("id") Long id)
    {
        Session session = sessionService.get();

        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        Ingredient ingredient = ingredientRepository.getReferenceById(id);

        try {
            sessionService.removeIngredient(session, ingredient);
        } catch (Exception e) {
            JSONObject responseObj = new JSONObject();
            responseObj.accumulate("message", e.getMessage());

            return ResponseEntity.status(409).body(responseObj.toString());
        }

        return ResponseEntity.ok("true");
    }

    @GetMapping(value = "/session/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getHistory()
    {
        Session session = sessionService.get();

        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        JSONArray jsonResponse = new JSONArray(new ArrayList<JSONObject>());
        jsonResponse.putAll(sessionService.getEvents(session));

        return ResponseEntity.ok(jsonResponse.toString());
    }
}
