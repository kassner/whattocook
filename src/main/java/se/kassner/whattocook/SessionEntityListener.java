package se.kassner.whattocook;

import jakarta.persistence.PostLoad;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionEntityListener
{
    RecipeRepository recipeRepository;
    SessionEventRepository sessionEventRepository;

    public SessionEntityListener(@Lazy RecipeRepository recipeRepository, @Lazy SessionEventRepository sessionEventRepository)
    {
        this.recipeRepository = recipeRepository;
        this.sessionEventRepository = sessionEventRepository;
    }

    @PostLoad
    public void applyEventsOnLoad(Session session)
    {
        List<SessionEvent> events = sessionEventRepository.findAllBySession(session);

        for (SessionEvent event : events) {
            session.applyEvent(event);
        }
    }
}
