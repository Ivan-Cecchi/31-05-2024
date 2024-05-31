package event.manager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(value = AccessLevel.NONE)
    private UUID id;
    private String name;
    private String description;
    private String location;
    private LocalDate date;
    @Column(name = "available_tickets")
    private int availableTickets;
    @ManyToMany
    @JoinTable(
            name = "events_users",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    public Event(String name, String description, String location, LocalDate date, int availableTickets) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.availableTickets = availableTickets;
    }


}
