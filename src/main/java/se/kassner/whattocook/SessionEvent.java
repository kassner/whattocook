package se.kassner.whattocook;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "session_event")
public class SessionEvent
{
    @Id
    @SequenceGenerator(name = "session_event_id_seq", sequenceName = "session_event_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_event_id_seq")
    private long id;

    @NonNull
    @ManyToOne(targetEntity = Session.class)
    private Session session;

    @NonNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "payload")
    private String payload;

    public SessionEvent()
    {
    }

    public SessionEvent(@NonNull Session session, Type eventType)
    {
        this.session = session;
        this.createdAt = LocalDateTime.now(ZoneOffset.UTC);
        this.eventType = eventType.dbType;
    }

    public SessionEvent(@NonNull Session session, Type eventType, String payload)
    {
        this.session = session;
        this.createdAt = LocalDateTime.now(ZoneOffset.UTC);
        this.eventType = eventType.dbType;
        this.payload = payload;
    }

    public long getId()
    {
        return id;
    }

    @NonNull
    public Session getSession()
    {
        return session;
    }

    @NonNull
    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public Type getEventType()
    {
        return Type.from(eventType);
    }

    public String getPayload()
    {
        return payload;
    }

    public enum Type
    {
        TIMEOUT_SET("timeout.set"),
        RECIPE_ASSIGN("recipe.assign");

        public final String dbType;

        Type(String dbType)
        {
            this.dbType = dbType;
        }

        public static Type from(String code)
        {
            for (Type e : Type.values()) {
                if (e.dbType.equals(code)) {
                    return e;
                }
            }

            return null;
        }
    }
}
