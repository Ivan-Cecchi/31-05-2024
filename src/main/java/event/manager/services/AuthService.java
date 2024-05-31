package event.manager.services;

import event.manager.entities.User;
import event.manager.exceptions.UnauthorizedException;
import event.manager.payloads.JWTDTO;
import event.manager.payloads.LoginAuthDTO;
import event.manager.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public JWTDTO login(LoginAuthDTO loginAuthDTO) {
        User user = userService.getUserByUsernameOrEmail(loginAuthDTO.usernameOrEmail());
        if (user == null || !passwordEncoder.matches(loginAuthDTO.password(), user.getPassword())) {
            throw new UnauthorizedException("Credentials not valid. Try login again");
        }
        return new JWTDTO(jwtTools.generateToken(user));
    }

}
