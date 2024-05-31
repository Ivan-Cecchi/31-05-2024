package event.manager.contollers;

import event.manager.entities.User;
import event.manager.entities.UserRole;
import event.manager.exceptions.BadRequestException;
import event.manager.payloads.ChangePasswordDTO;
import event.manager.payloads.Role;
import event.manager.payloads.UserDTO;
import event.manager.services.UserService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "username") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return userService.getUsers(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User getUser(@PathVariable("id") UUID id) {
        return userService.getUser(id);
    }

    @GetMapping("/me")
    public User getMe(@AuthenticationPrincipal User user) {
        return user;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User updateUser(@PathVariable("id") UUID id, @RequestBody @Validated UserDTO user, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return userService.updateUser(id, user);
    }

    @PutMapping("/me")
    public User updateMe(@AuthenticationPrincipal User user, @RequestBody @Validated UserDTO userDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return userService.updateUser(user.getId(), userDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public void deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
    }

    @DeleteMapping("/me")
    public void deleteMe(@AuthenticationPrincipal User user) {
        userService.deleteUser(user.getId());
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User updateUserPassword(@PathVariable("id") UUID id, @RequestBody @Validated ChangePasswordDTO userPassword, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return userService.updatePassword(id, userPassword);
    }

    @PutMapping("/me/password")
    public User updateMePassword(@AuthenticationPrincipal User user, @RequestBody @Validated ChangePasswordDTO userPassword, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return userService.updatePassword(user.getId(), userPassword);
    }

    @PutMapping("/{id}/avatar")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User updateUserAvatar(@PathVariable("id") UUID id, @RequestParam("avatar") MultipartFile file) throws IOException {
        return userService.updateAvatar(id, file);
    }

    @PutMapping("/me/avatar")
    public User updateMeAvatar(@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) throws IOException {
        return userService.updateAvatar(user.getId(), file);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User updateUserRole(@PathVariable("id") UUID id, @RequestBody @Validated Role role, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return userService.updateRole(id, UserRole.valueOf(role.role()));
    }

    @PutMapping("/me/role")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User updateMeRole(@AuthenticationPrincipal User user, @RequestBody @Validated String roleString, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        UserRole role = UserRole.valueOf(roleString);
        return userService.updateRole(user.getId(), role);
    }
}
