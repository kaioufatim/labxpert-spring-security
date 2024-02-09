package yass.jouao.labx.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yass.jouao.labx.entities.UserLab;
import yass.jouao.labx.repositories.IUserLabRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserLabRepository dbUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<yass.jouao.labx.entities.UserLab> user = dbUserRepository.findByName(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new User(
                user.get().getName(),
                user.get().getPassword(),
                getGrantedAuthorities(String.valueOf(user.get().getUserRole()))
        );
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}
