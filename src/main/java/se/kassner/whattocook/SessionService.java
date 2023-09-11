package se.kassner.whattocook;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class SessionService
{
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final SessionRepository sessionRepository;
    private final SessionEventRepository sessionEventRepository;
    private final EntityManager entityManager;

    public SessionService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, SessionRepository sessionRepository, SessionEventRepository sessionEventRepository, EntityManager entityManager)
    {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.sessionRepository = sessionRepository;
        this.sessionEventRepository = sessionEventRepository;
        this.entityManager = entityManager;
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

        return sessionRepository.findById(session.getId()).orElse(null);
    }

    private void refresh(Session session)
    {
        entityManager.refresh(session);
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

    @Transactional(rollbackOn = Exception.class)
    public void removeIngredient(Session session, Ingredient ingredient) throws Exception
    {
        // remove ingredient
        JSONObject ingredientObj = new JSONObject();
        ingredientObj.accumulate("id", ingredient.getId());
        ingredientObj.accumulate("name", ingredient.getName());

        JSONObject payload = new JSONObject();
        payload.accumulate("ingredient", ingredientObj);

        SessionEvent ingredientEvent = new SessionEvent(session, SessionEvent.Type.INGREDIENT_EXCLUDE, payload.toString());
        sessionEventRepository.save(ingredientEvent);

        // reload session
        this.refresh(session);

        // assign new recipe
        Recipe recipe = recipeRepository.findOneRandomWithoutIngredients(session.getExcludedIngredientIds());

        if (recipe == null) {
            // @TODO no more recipes available. save event?
            throw new Exception("No more recipes available.");
        }

        JSONObject recipePayload = new JSONObject();
        recipePayload.accumulate("recipe_id", recipe.getId());
        SessionEvent recipeEvent = new SessionEvent(session, SessionEvent.Type.RECIPE_ASSIGN, recipePayload.toString());
        sessionEventRepository.saveAndFlush(recipeEvent);
    }

    public List<JSONObject> getEvents(Session session)
    {
        return sessionEventRepository
            .findAllForHistory(session.getId())
            .stream()
            .map(this::convertEventToHistoryEntry)
            .filter(Objects::nonNull)
            .toList();
    }

    private JSONObject convertEventToHistoryEntry(SessionEvent event)
    {
        String description = "";
        String type = "";

        switch (event.getEventType()) {
            case TIMEOUT_SET -> {
                return null;
            }
            case RECIPE_ASSIGN -> {
                JSONObject payload = new JSONObject(event.getPayload());
                Recipe recipe = recipeRepository.getReferenceById(payload.getLong("recipe_id"));
                type = "Recipe assigned";
                description = recipe.getName();
            }
            case INGREDIENT_EXCLUDE -> {
                JSONObject payload = new JSONObject(event.getPayload());
                Ingredient ingredient = ingredientRepository.getReferenceById(payload.getJSONObject("ingredient").getLong("id"));
                type = "Ingredient excluded";
                description = ingredient.getName();
            }
        }

        return new JSONObject()
            .accumulate("type", type)
            .accumulate("description", description)
            .accumulate("date", event.getCreatedAt().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
        ;
    }
}
