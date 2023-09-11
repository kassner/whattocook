package se.kassner.whattocook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionEventRepository extends JpaRepository<SessionEvent, Long>
{
    public List<SessionEvent> findAllBySession(Session session);

    @Query(
        value = "SELECT e.* FROM session_event e WHERE e.session_id = :session_id AND e.event_type != 'timeout.set' ORDER BY e.created_at DESC",
        nativeQuery = true
    )
    public List<SessionEvent> findAllForHistory(@Param("session_id") Long sessionId);
}
