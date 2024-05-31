package event.manager.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDTO(
        @NotBlank(message = "Password is required")
        @Size(min = 15, message = "Password must be at least 15 characters long")
        String oldPassword,
        @NotBlank(message = "Password is required")
        @Size(min = 15, message = "Password must be at least 15 characters long")
        String newPassword
) {
}
