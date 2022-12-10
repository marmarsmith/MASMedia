package learn.capstone.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnWebApplication
public class SecurityConfig {

    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationConfiguration config) throws Exception {

        http.csrf().disable();
        http.cors();

        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET).permitAll()
//                .antMatchers(HttpMethod.POST).permitAll()
//                .antMatchers(HttpMethod.PUT).permitAll()
//                .antMatchers(HttpMethod.DELETE).permitAll()
//                .antMatchers(HttpMethod.GET,
//                        "/api/genre",
//                        "/api/genre/*",
//                        "/api/user/*",
//                        "/api/media",
//                        "/api/media/*").permitAll() //only path changed
                .antMatchers(HttpMethod.GET, "/api/profile/**", "api/review/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/profile/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/review/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/profile/**", "api/review/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/media/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/media/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/media/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/media/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/genre/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/genre/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/genre/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/genre/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/authenticate", "/expire_token", "/user/create").permitAll()
                .antMatchers(HttpMethod.POST, "/refresh_token").authenticated()
                .antMatchers(HttpMethod.PUT, "/user/password").authenticated()
                .antMatchers(HttpMethod.GET, "/user/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/user/update").hasAuthority("ADMIN")
                .and()
                .addFilter(new JwtRequestFilter(manager(config), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}