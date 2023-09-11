package se.kassner.whattocook;

import jakarta.persistence.*;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@EntityListeners(SessionEntityListener.class)
@Table(name = "session")
public class Session
{
    @Id
    @SequenceGenerator(name = "session_id_seq", sequenceName = "session_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_id_seq")
    private long id;

    @NonNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Transient
    private Long recipeId;

    @Transient
    private LocalDateTime timeoutAt;

    public Session()
    {
    }

    public Session(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public Session(long id, @NonNull LocalDateTime createdAt)
    {
        this.id = id;
        this.createdAt = createdAt;
    }

    public long getId()
    {
        return id;
    }

    @NonNull
    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public boolean isTimeout()
    {
        return LocalDateTime.now().isAfter(timeoutAt);
    }

    public void applyEvent(SessionEvent event)
    {
        if (event.getEventType() == SessionEvent.Type.RECIPE_ASSIGN && event.getPayload() != null) {
            JSONObject js = new JSONObject(event.getPayload());
            this.recipeId = js.getLong("recipe_id");
        }

        if (event.getEventType() == SessionEvent.Type.TIMEOUT_SET && event.getPayload() != null) {
            JSONObject js = new JSONObject(event.getPayload());
            this.timeoutAt = LocalDateTime.parse(js.getString("timeout"));
        }
    }

    public Long getRecipeId()
    {
        return recipeId;
    }
}
