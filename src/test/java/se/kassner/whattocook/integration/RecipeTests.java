package se.kassner.whattocook.integration;

import jakarta.persistence.EntityManager;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Lazy;
import se.kassner.whattocook.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;

@DataJpaTest
public class RecipeTests
{
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    SessionEventRepository sessionEventRepository;

    @Autowired
    @Lazy
    EntityManager entityManager;

    @Test
    public void retrieveRecipeShouldWork()
    {
        Recipe recipe = recipeRepository.getReferenceById(1L);
        Assertions.assertEquals("Tacos", recipe.getName());
    }

    @Test
    public void deleteRecipeShouldFailIfAssignedToSession()
    {
        this.createSession();

        // assert
        List<String> namesBefore = recipeRepository.findAll().stream().map(Recipe::getName).toList();
        assertThat(namesBefore, hasItems("Tacos", "Lasagna"));

        // delete recipe
        Assertions.assertThrows(RecipeAssignedToSessionException.class, () -> {
            recipeRepository.deleteById(1L);
        });

        // assert
        List<String> namesAfter = recipeRepository.findAll().stream().map(Recipe::getName).toList();
        assertThat(namesAfter, hasItems("Tacos", "Lasagna"));
    }

    @Test
    public void deleteRecipeShouldWorkIfNotAssignedToSession()
    {
        this.createSession();

        // assert
        List<String> namesBefore = recipeRepository.findAll().stream().map(Recipe::getName).toList();
        assertThat(namesBefore, hasItems("Tacos", "Lasagna"));

        // delete recipe
        recipeRepository.deleteById(2L);

        // assert
        Assertions.assertEquals(1, recipeRepository.count());
        List<String> namesAfter = recipeRepository.findAll().stream().map(Recipe::getName).toList();
        assertThat(namesAfter, hasItems("Tacos"));
        assertThat(namesAfter, not(hasItems("Lasagna")));
    }

    private void createSession()
    {
        // create session
        Session session = new Session(LocalDateTime.now(ZoneOffset.UTC));
        sessionRepository.save(session);
        sessionRepository.flush();

        // set timeout
        LocalDateTime timeout = LocalDateTime.now(ZoneOffset.UTC).plusHours(1);
        JSONObject timeoutPayload = new JSONObject();
        timeoutPayload.accumulate("timeout", timeout.toString());
        SessionEvent timeoutEvent = new SessionEvent(session, SessionEvent.Type.TIMEOUT_SET, timeoutPayload.toString());
        sessionEventRepository.save(timeoutEvent);

        // assign recipe
        JSONObject payload = new JSONObject();
        payload.accumulate("recipe_id", 1L);
        SessionEvent recipeAssignEvent = new SessionEvent(session, SessionEvent.Type.RECIPE_ASSIGN, payload.toString());
        sessionEventRepository.save(recipeAssignEvent);
        sessionEventRepository.flush();

        entityManager.refresh(session);
    }
}
