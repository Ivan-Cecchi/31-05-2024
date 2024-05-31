package event.manager;

import event.manager.entities.User;
import event.manager.entities.UserRole;
import event.manager.payloads.NewUserDTO;
import event.manager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserRunner implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Add an event manager if none exists
        if (userService.getEventManagersCount() == 0) {
            User user = userService.createUser(new NewUserDTO("mattia", "info@consitech.it", "f1ropT43$XbIPYGAJkEU", "Mattia", "Consiglio"));
            userService.updateRole(user.getId(), UserRole.EVENT_MANAGER);
        }
    }
}
