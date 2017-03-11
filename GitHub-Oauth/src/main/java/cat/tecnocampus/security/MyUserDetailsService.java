package cat.tecnocampus.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by roure on 27/10/2016.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserSecurityRepository userSecurityRepository;

    
    public MyUserDetailsService(UserSecurityRepository userSecurityRepository) {
		this.userSecurityRepository = userSecurityRepository;
	}


	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
    	UserSecurity user = null;
        
    	try {
    		user = userSecurityRepository.findOne(username);
    	} catch(IncorrectResultSizeDataAccessException e) {
    		throw new UsernameNotFoundException("User " + username + " not found");
    	}

        Set<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                                                                  .map(SimpleGrantedAuthority::new)
                                                                  .collect(Collectors.toSet());

        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
