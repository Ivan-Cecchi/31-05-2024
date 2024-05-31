package event.manager.payloads;

import jakarta.validation.constraints.NotBlank;

public record LoginAuthDTO(
        @NotBlank
        String usernameOrEmail,
        @NotBlank
        String password) {
}
