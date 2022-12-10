package learn.capstone.controllers;

import learn.capstone.models.AppUser;
import learn.capstone.security.JwtConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@ConditionalOnWebApplication
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtConverter converter;
    private final PasswordEncoder encoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtConverter converter,
                          PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
        this.encoder = encoder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> login(@RequestBody HashMap<String, String> credentials,
                                        HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(credentials.get("username"),
                        credentials.get("password"));

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);

            if (authentication.isAuthenticated()) {
                AppUser user = (AppUser) authentication.getPrincipal();
                String jwtToken = converter.getTokenFromUser(user);

                HashMap<String, Object> map = new HashMap<>();
                map.put("username", user.getUsername());
                map.put("authorities", user.getAuthorityNames());

                Cookie cookie = new Cookie("jwt", jwtToken);
                cookie.setHttpOnly(true);
                response.addCookie(cookie);

                return new ResponseEntity<>(map, HttpStatus.OK);
            }

        } catch (AuthenticationException ex) {
            System.out.println(ex);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/expire_token")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<Object> refresh(@AuthenticationPrincipal AppUser user,
                                          HttpServletResponse response) {
        String jwtToken = converter.getTokenFromUser(user);

        HashMap<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("authorities", user.getAuthorityNames());

        Cookie cookie = new Cookie("jwt", jwtToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/encode")
    public ResponseEntity encode(@RequestBody HashMap<String, String> map) {
        System.out.println();
        System.out.println("ENCODED: " + encoder.encode(map.get("value")));
        System.out.println();
        return new ResponseEntity(HttpStatus.OK);
    }
}
