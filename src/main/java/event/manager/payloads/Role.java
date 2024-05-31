package event.manager.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record Role(
        @NotBlank(message = "Role is required")
        @Pattern(regexp = "USER|EVENT_MANAGER", message = "Role must be either USER or EVENT_MANAGER")
        String role
) {
}
