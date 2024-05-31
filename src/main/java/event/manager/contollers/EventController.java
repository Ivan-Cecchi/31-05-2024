package event.manager.contollers;

import event.manager.exceptions.BadRequestException;
import event.manager.entities.Event;
import event.manager.entities.User;
import event.manager.payloads.EventDTO;
import event.manager.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public Event createEvent(@RequestBody @Validated EventDTO eventDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new BadRequestException("Invalid data", result.getAllErrors());
        }
        return eventService.createEvent(eventDTO);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public Page<Event> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "date") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventService.getEvents(pageable);
    }


    @GetMapping("/{id}")
    public Event getEvent(@PathVariable("id") UUID id) {
        return eventService.getEvent(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public Event updateEvent(@PathVariable UUID id, @RequestBody @Validated EventDTO eventDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new BadRequestException("invalid data", result.getAllErrors());
        }
        return eventService.updateEvent(id, eventDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public void deleteEvent(@PathVariable("id") UUID id) {
        eventService.deleteEvent(id);
    }

    @GetMapping("/me")
    public Page<Event> getMyEvents(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "date") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventService.getEventsByUser(user.getId(), pageable);
    }

    @PutMapping("/{id}/users/me")
    public Event addUserToEvent(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        return eventService.addUserToEvent(id, user.getId());
    }

    @PutMapping("{id}/users/{userId}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public Event addUserToEvent(@PathVariable UUID id, @PathVariable UUID userId) {
        return eventService.addUserToEvent(id, userId);
    }

    @DeleteMapping("{id}/users/{userId}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public Event removeUserFromEvent(@PathVariable UUID id, @PathVariable UUID userId) {
        return eventService.removeUserFromEvent(id, userId);
    }
}
