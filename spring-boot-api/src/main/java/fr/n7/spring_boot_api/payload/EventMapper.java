package fr.n7.spring_boot_api.payload;

import fr.n7.spring_boot_api.payload.EventDTO;
import fr.n7.spring_boot_api.model.Event;

public class EventMapper {
    public static EventDTO toResponse(Event event) {
        return new EventDTO(
            event.getId(),
            event.getName(),
            event.getDate(),
            event.getEventType(),
            event.getDescription(),
            event.getLikedBy().size()
        );
    }
}
