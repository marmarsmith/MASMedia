package learn.capstone.controllers;

import learn.capstone.models.AppUser;
import learn.capstone.domain.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class AppUserController {

    private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping("/user")
    public List<AppUser> findAll(@AuthenticationPrincipal AppUser principal) {
        if (principal.hasAuthority("ADMIN")) {
        return service.findAll();
        }
        return null;
    }

    @GetMapping("/user/role")
    public List<String> findAllRoles(@AuthenticationPrincipal AppUser principal) {
        if (principal.hasAuthority("ADMIN")) {
            return service.findAllRoles();
        }
        return null;
    }

    @GetMapping("/user/{appUserId}")
    public ResponseEntity<Object> findByAppUserId(@PathVariable int appUserId,
                                                  @AuthenticationPrincipal AppUser principal) {
        if (!principal.hasAuthority("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        var user = service.findByAppUserId(appUserId);
        // always erase passwords before serialization
        user.setPassword("");
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user/username/{username}")
    public ResponseEntity<Object> findByUsername(@PathVariable String username,
                                                  @AuthenticationPrincipal AppUser principal) {
        var user = service.findByUsername(username);
        // always erase passwords before serialization
        user.setPassword("");
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/user/create")
    public ResponseEntity<Object> create(@RequestBody AppUser user) {

        var result = service.add(user);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/user/update")
    public ResponseEntity<Object> update(
            @RequestBody AppUser user,
            @AuthenticationPrincipal AppUser principal) {

        // can't update if not an admin
        if (!principal.hasAuthority("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // don't bother updating a user that doesn't exist
        var existing = service.findByAppUserId(user.getAppUserId());
        if (existing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // can't update another ADMIN
        if (existing.hasAuthority("ADMIN") && existing.getAppUserId() != principal.getAppUserId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        var result = service.update(user);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<Object> deleteById(
            @RequestBody AppUser user,
            @AuthenticationPrincipal AppUser principal) {

        // can't update if not an admin or self
        if (!principal.hasAuthority("ADMIN") || user.getAppUserId() != principal.getAppUserId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // don't bother deleting a user that doesn't exist
        var existing = service.findByAppUserId(user.getAppUserId());
        if (existing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // can't delete another ADMIN
        if (existing.hasAuthority("ADMIN") && existing.getAppUserId() != principal.getAppUserId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        var result = service.deleteById(user.getAppUserId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/user/privacy")
    public ResponseEntity<Object> changePrivacy(
            @RequestBody AppUser user,
            @AuthenticationPrincipal AppUser principal) {

        principal.setPrivateProfile(user.isPrivateProfile());

        var result = service.changePrivacy(principal);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/user/password")
    public ResponseEntity<Object> changePassword(
            @RequestBody HashMap<String, String> values,
            @AuthenticationPrincipal AppUser principal) {

        // can only update our own password, never someone else's
        principal.setPassword(values.get("password"));

        var result = service.changePassword(principal);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
