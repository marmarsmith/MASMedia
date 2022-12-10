package learn.capstone.security;

import learn.capstone.data.AppUserJdbcTemplateRepository;
import learn.capstone.data.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserService implements UserDetailsService {

    private final AppUserJdbcTemplateRepository userRepository;

    public SecurityUserService(AppUserJdbcTemplateRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = userRepository.findByUsername(username);

        if (user == null || !user.isEnabled()) {
            throw new UsernameNotFoundException("username " + username + " not found.");
        }

        return user;
    }
}
