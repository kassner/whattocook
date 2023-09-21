package se.kassner.whattocook;

import jakarta.persistence.PreRemove;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class RecipeEntityListener
{
    SessionRepository sessionRepository;

    public RecipeEntityListener(@Lazy SessionRepository sessionRepository)
    {
        this.sessionRepository = sessionRepository;
    }

    @PreRemove
    public void validateSessionBeforeDelete(Recipe recipe) throws RecipeAssignedToSessionException
    {
        Session session = sessionRepository.getLast();

        if (session == null) {
            return;
        }

        if (session.isTimeout()) {
            return;
        }

        if (session.getRecipeId() == null) {
            return;
        }

        if (session.getRecipeId() == recipe.getId()) {
            throw new RecipeAssignedToSessionException();
        }
    }
}
