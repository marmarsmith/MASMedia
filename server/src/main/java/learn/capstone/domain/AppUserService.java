package learn.capstone.domain;

import learn.capstone.data.AppUserJdbcTemplateRepository;
import learn.capstone.domain.Result;
import learn.capstone.domain.ResultType;
import learn.capstone.models.AppUser;
import learn.capstone.models.Review;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AppUserService {

    private final AppUserJdbcTemplateRepository repository;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserJdbcTemplateRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public List<AppUser> findAll() {
        return repository.findAll();
    }

    public List<String> findAllRoles() {
        return repository.findAllRoles();
    }

    public AppUser findByAppUserId(int id) {
        return repository.findByAppUserId(id);
    }

    public AppUser findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Map<Integer, AppUser> getUserIds() {
        return repository.getUserIds();
    }

    public Result<AppUser> add(AppUser user) {

        Result<AppUser> result = validateWithoutPassword(user);
        if (!result.isSuccess()) {
            return result;
        }

        result = validatePassword(user);
        if (!result.isSuccess()) {
            return result;
        }

        if (user.getAppUserId() != 0) {
            result.addMessage("user id cannot be set before user creation", ResultType.INVALID);
            return result;
        }

//        user.setAuthorityNames(List.of("USER"));

        user.setPassword(encoder.encode(user.getPassword()));

        user = repository.add(user);
        if (user == null) {
            result.addMessage("user not created.", ResultType.INVALID);
        }
        result.setPayload(user);

        return result;
    }

    public Result<AppUser> update(AppUser user) {
        Result<AppUser> result = validateWithoutPassword(user);
        if (!result.isSuccess()) {
            return result;
        }

        if (user.getAppUserId() <= 0) {
            result.addMessage("user id must be set for update", ResultType.INVALID);
            return result;
        }

        boolean success = repository.update(user);
        if (!success) {
            result.addMessage("user not updated", ResultType.INVALID);
        }

        return result;
    }

    public Result<AppUser> deleteById(int appUserId) {
        Result<AppUser> result = new Result<>();

        if (repository.findByAppUserId(appUserId) == null) {
            result.addMessage("AppUser ID %s was not found.", ResultType.NOT_FOUND, appUserId);
        } else {
            repository.deleteById(appUserId);
        }
        return result;
    }

    public Result<AppUser> changePrivacy(AppUser user) {
        Result<AppUser> result = new Result<>();

        if (user == null) { // redundant check, needed only for changePassword
            result.addMessage("user cannot be null", ResultType.INVALID);
            return result;
        }

        if (user.getAppUserId() <= 0) {
            result.addMessage("user id must be set to change privacy", ResultType.INVALID);
            return result;
        }

        boolean success = repository.changePrivacy(user);
        if (!success) {
            result.addMessage("Privacy not updated", ResultType.INVALID);
        }

        return result;
    }

    public Result<AppUser> changePassword(AppUser user) {
        Result<AppUser> result = validatePassword(user);
        if (!result.isSuccess()) {
            return result;
        }

        if (user.getAppUserId() <= 0) {
            result.addMessage("user id must be set to change password", ResultType.INVALID);
            return result;
        }

        user.setPassword(encoder.encode(user.getPassword()));
        boolean success = repository.changePassword(user);
        if (!success) {
            result.addMessage("password not updated", ResultType.INVALID);
        }

        return result;
    }

    private Result<AppUser> validateWithoutPassword(AppUser user) {
        var result = new Result<AppUser>();

        if (user == null) {
            result.addMessage("user cannot be null", ResultType.INVALID);
            return result;
        }

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            result.addMessage("username is required", ResultType.INVALID);
            return result;
        }

        var existing = repository.findByUsername(user.getUsername());
        if (existing != null && existing.getAppUserId() != user.getAppUserId()) {
            result.addMessage("username is already in use", ResultType.INVALID);
            return result;
        }

        return result;
    }

    private Result<AppUser> validatePassword(AppUser user) {

        var result = new Result<AppUser>();

        if (user == null) { // redundant check, needed only for changePassword
            result.addMessage("user cannot be null", ResultType.INVALID);
            return result;
        }

        if (user.getPassword() == null || user.getPassword().isBlank() || user.getPassword().length() < 8) {
            result.addMessage("password must be at least 8 characters", ResultType.INVALID);
            return result;
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : user.getPassword().toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        if (digits == 0 || letters == 0 || others == 0) {
            result.addMessage("password must contain a digit, a letter, and a non-digit/non-letter", ResultType.INVALID);
        }

        return result;
    }
}