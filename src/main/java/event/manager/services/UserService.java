package event.manager.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import event.manager.entities.User;
import event.manager.entities.UserRole;
import event.manager.exceptions.BadRequestException;
import event.manager.exceptions.RecordNotFoundException;
import event.manager.payloads.ChangePasswordDTO;
import event.manager.payloads.NewUserDTO;
import event.manager.payloads.UserDTO;
import event.manager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bCrypt;
    @Autowired
    private Cloudinary cloudinary;

    public User createUser(NewUserDTO userDTO) {
        String avatarUrl = "https://ui-avatars.com/api/?name=" + userDTO.firstName().charAt(0) + "+" + userDTO.lastName().charAt(0);
        User user = new User();
        //using a sort of building block pattern
        user.setUsername(userDTO.username());
        user.setPassword(bCrypt.encode(userDTO.password()));
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setAvatarUrl(avatarUrl);
        return userRepository.save(user);
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(User.class.getSimpleName(), id));
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new RecordNotFoundException(User.class.getSimpleName(), usernameOrEmail));
    }

    public User updateUser(UUID id, UserDTO userDTO) {
        User user = this.getUser(id);
        if (userRepository.existsByUsernameAndEmail(userDTO.username(), userDTO.email()) && !user.getUsername().equals(userDTO.username()) && !user.getEmail().equals(userDTO.email())) {
            throw new BadRequestException("Username and email already in use");
        } else if (userRepository.existsByUsername(userDTO.username()) && !user.getUsername().equals(userDTO.username())) {
            throw new BadRequestException("Username already in use");
        } else if (userRepository.existsByEmail(userDTO.email()) && !user.getEmail().equals(userDTO.email())) {
            throw new BadRequestException("Email already in use");
        }
        String avatarUrl = "https://ui-avatars.com/api/?name=" + userDTO.firstName().charAt(0) + "+" + userDTO.lastName().charAt(0);

        if (!user.getAvatarUrl().startsWith("https://ui-avatars.com/api/")) {
            avatarUrl = user.getAvatarUrl();
        }
        if (this.getEventManagersCount() == 1 && UserRole.valueOf(userDTO.role()).equals(UserRole.USER)) {
            throw new BadRequestException("Cannot remove last event manager");
        }
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setAvatarUrl(avatarUrl);
        user.setPassword(bCrypt.encode(userDTO.password()));
        user.setRole(UserRole.valueOf(userDTO.role()));
        return userRepository.save(user);

    }

    public User updateUser(UUID id, NewUserDTO userDTO) {
        User user = this.getUser(id);
        if (userRepository.existsByUsernameAndEmail(userDTO.username(), userDTO.email()) && !user.getUsername().equals(userDTO.username()) && !user.getEmail().equals(userDTO.email())) {
            throw new BadRequestException("Username and email already in use");
        } else if (userRepository.existsByUsername(userDTO.username()) && !user.getUsername().equals(userDTO.username())) {
            throw new BadRequestException("Username already in use");
        } else if (userRepository.existsByEmail(userDTO.email()) && !user.getEmail().equals(userDTO.email())) {
            throw new BadRequestException("Email already in use");
        }

        String avatarUrl = "https://ui-avatars.com/api/?name=" + userDTO.firstName().charAt(0) + "+" + userDTO.lastName().charAt(0);

        if (!user.getAvatarUrl().startsWith("https://ui-avatars.com/api/")) {
            avatarUrl = user.getAvatarUrl();
        }
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setAvatarUrl(avatarUrl);
        user.setPassword(bCrypt.encode(userDTO.password()));
        return userRepository.save(user);
    }


    public void deleteUser(UUID id) {
        User user = this.getUser(id);
        userRepository.delete(user);
    }

    public User updatePassword(UUID id, ChangePasswordDTO changePasswordDTO) {
        User user = this.getUser(id);
        if (changePasswordDTO.newPassword().equals(changePasswordDTO.oldPassword())) {
            throw new BadRequestException("New password cannot be the same as the old one");
        }
        if (!bCrypt.matches(changePasswordDTO.oldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }
        String password = bCrypt.encode(changePasswordDTO.newPassword());
        user.setPassword(password);
        return userRepository.save(user);
    }


    public User updateRole(UUID id, UserRole role) {
        User user = this.getUser(id);
        if (this.getEventManagersCount() == 1 && role.equals(UserRole.USER)) {
            throw new BadRequestException("Cannot remove last event manager");
        }
        user.setRole(role);
        return userRepository.save(user);
    }


    public User updateAvatar(UUID id, MultipartFile avatar) throws IOException {
        User user = this.getUser(id);
        String avatarUrl = (String) cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap()).get("url");
        user.setAvatarUrl(avatarUrl);
        return userRepository.save(user);
    }

    public boolean eventManagerExists() {
        return userRepository.existsByRole(UserRole.EVENT_MANAGER);
    }

    public int getEventManagersCount() {
        return userRepository.countByRole(UserRole.EVENT_MANAGER);
    }

}
