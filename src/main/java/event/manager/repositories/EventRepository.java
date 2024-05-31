package event.manager.repositories;

import event.manager.entities.Event;
import event.manager.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Page<Event> findAllByUsers(User user, Pageable pageable);
}
