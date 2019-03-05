package hr.fer.opp.project.controllers.security;

import hr.fer.opp.project.services.RequestDeniedException;
import hr.fer.opp.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;
@Service
public class AppUserDetailsService implements UserDetailsService {

    @Value("${thegenerics.admin.password}")
    private String adminPasswordHash;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("admin")) {
            return new User(username, adminPasswordHash, authorities());
        }

        hr.fer.opp.project.entities.User user = userService.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException("User with username " + username + " does not exist!")
        );
        if(user.isEmailVerified()) {
            return new User(user.getUsername(), user.getPassword(), authorities());
        } else {
            throw new RequestDeniedException("Mail address is not verified!");
        }
    }

    private List<GrantedAuthority> authorities() {
        return commaSeparatedStringToAuthorityList("ROLE_USER");
    }
}
