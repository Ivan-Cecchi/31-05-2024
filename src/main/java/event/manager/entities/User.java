package event.manager.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"password", "enabled", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(value = AccessLevel.NONE)
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;
    private String avatarUrl;

    public User(String username, String password, String email, String firstName, String lastName, String avatarUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
