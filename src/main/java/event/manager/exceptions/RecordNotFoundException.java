package event.manager.exceptions;

import java.util.UUID;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String type, UUID id) {
        super(type + " not found with id " + id);
    }

    public RecordNotFoundException(String type, String notFoundWith) {
        super(type + " not found with " + notFoundWith);
    }
}
