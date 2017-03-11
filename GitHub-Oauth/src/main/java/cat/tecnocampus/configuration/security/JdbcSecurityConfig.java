package cat.tecnocampus.configuration.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Profile("jdbc_auth")
public class JdbcSecurityConfig extends BaseSecurityConfig {

	private static final String USERS_QUERY = "select username,password, enabled from users where username = ?";
	
	private static final String AUTHORITIES_QUERY = "select username, role from user_roles where username = ?";
	
	
    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passEncoder() {
        return new BCryptPasswordEncoder();
    }
   
     //Configure authentication manager
     @Override
     public void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.jdbcAuthentication()
                 .dataSource(dataSource)
                 .usersByUsernameQuery(USERS_QUERY)
                 .authoritiesByUsernameQuery(AUTHORITIES_QUERY)
                 .passwordEncoder(passEncoder());
     }
}
