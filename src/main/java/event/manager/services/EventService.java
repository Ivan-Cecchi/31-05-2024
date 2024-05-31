package event.manager.services;

import event.manager.exceptions.BadRequestException;
import event.manager.exceptions.RecordNotFoundException;
import event.manager.entities.Event;
import event.manager.entities.User;
import event.manager.payloads.EventDTO;
import event.manager.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventService {
    @Autowired
    private UserService userService;

    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.name());
        event.setDescription(eventDTO.description());
        event.setDate(eventDTO.date());
        event.setLocation(eventDTO.location());
        event.setAvailableTickets(eventDTO.availableTickets());
        return eventRepository.save(event);
    }

    public Event getEvent(UUID id) {
        return eventRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Event.class.getSimpleName(), id));
    }


    public Page<Event> getEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Event updateEvent(UUID id, EventDTO eventDTO) {
        Event event = this.getEvent(id);
        event.setName(eventDTO.name());
        event.setDescription(eventDTO.description());
        event.setDate(eventDTO.date());
        event.setLocation(eventDTO.location());
        event.setAvailableTickets(eventDTO.availableTickets());
        return eventRepository.save(event);
    }

    public void deleteEvent(UUID id) {
        eventRepository.deleteById(id);
    }


    public Event addUserToEvent(UUID eventId, UUID userId) {
        Event event = this.getEvent(eventId);
        User user = this.userService.getUser(userId);
        if (event.getUsers().contains(user)) {
            throw new BadRequestException("User already in event");
        }
        if (event.getAvailableTickets() == 0) {
            throw new BadRequestException("No tickets available");
        }
        event.setAvailableTickets(event.getAvailableTickets() - 1);
        event.getUsers().add(user);
        return eventRepository.save(event);
    }


    public Event removeUserFromEvent(UUID eventId, UUID userId) {
        Event event = this.getEvent(eventId);
        User user = this.userService.getUser(userId);
        if (!event.getUsers().contains(user)) {
            throw new BadRequestException("User not in event");
        }
        event.getUsers().remove(user);
        event.setAvailableTickets(event.getAvailableTickets() + 1);
        return eventRepository.save(event);
    }

    public Page<Event> getEventsByUser(UUID userId, Pageable pageable) {
        User user = this.userService.getUser(userId);
        return eventRepository.findAllByUsers(user, pageable);
    }
}
