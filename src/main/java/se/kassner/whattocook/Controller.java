package se.kassner.whattocook;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller
{
    private final SessionService sessionService;

    public Controller(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/session", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createSession()
    {
        sessionService.createIfNeeded();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/recipe", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> getRecipe()
    {
        Session session = sessionService.get();

        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        Recipe recipe = sessionService.getRecipe(session);
        return ResponseEntity.ok(recipe);
    }
}
