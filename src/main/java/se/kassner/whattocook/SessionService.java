package se.kassner.whattocook;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class SessionService
{
    private final RecipeRepository recipeRepository;
    private final SessionRepository sessionRepository;
    private final SessionEventRepository sessionEventRepository;

    public SessionService(RecipeRepository recipeRepository, SessionRepository sessionRepository, SessionEventRepository sessionEventRepository)
    {
        this.recipeRepository = recipeRepository;
        this.sessionRepository = sessionRepository;
        this.sessionEventRepository = sessionEventRepository;
    }

    public Session get()
    {
        Session session = sessionRepository.getLast();

        if (session == null) {
            return null;
        }

        if (session.isTimeout()) {
            return null;
        }

        return sessionRepository.getReferenceById(session.getId());
    }

    public void createIfNeeded()
    {
        Session session = this.get();

        if (session == null) {
            this.create();
        }
    }

    private void create()
    {
        Session session = new Session(LocalDateTime.now(ZoneOffset.UTC));
        sessionRepository.save(session);

        // set timeout
        LocalDateTime timeout = LocalDateTime.now(ZoneOffset.UTC).plusHours(4);
        JSONObject timeoutPayload = new JSONObject();
        timeoutPayload.accumulate("timeout", timeout.toString());
        SessionEvent timeoutEvent = new SessionEvent(session, SessionEvent.Type.TIMEOUT_SET, timeoutPayload.toString());
        sessionEventRepository.save(timeoutEvent);

        // assign recipe
        Recipe recipe = recipeRepository.findOneRandom();
        JSONObject payload = new JSONObject();
        payload.accumulate("recipe_id", recipe.getId());
        SessionEvent recipeAssignEvent = new SessionEvent(session, SessionEvent.Type.RECIPE_ASSIGN, payload.toString());

        sessionEventRepository.save(recipeAssignEvent);
        sessionEventRepository.flush();
    }

    public Recipe getRecipe(Session session)
    {
        Long recipeId = session.getRecipeId();

        if (recipeId == null) {
            return null;
        }

        return recipeRepository.findById(recipeId).orElse(null);
    }
}
