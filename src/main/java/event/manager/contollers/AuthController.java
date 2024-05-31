package event.manager.contollers;

import event.manager.entities.User;
import event.manager.exceptions.BadRequestException;
import event.manager.payloads.JWTDTO;
import event.manager.payloads.LoginAuthDTO;
import event.manager.payloads.NewUserDTO;
import event.manager.services.AuthService;
import event.manager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public JWTDTO login(@RequestBody @Validated LoginAuthDTO loginAuthDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return authService.login(loginAuthDTO);
    }


    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Validated NewUserDTO userDTO, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("Invalid data", validation.getAllErrors());
        }
        return userService.createUser(userDTO);
    }
}
