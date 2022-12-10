package learn.capstone.data;

import learn.capstone.models.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserJdbcTemplateRepositoryTest {

    @Autowired
    AppUserJdbcTemplateRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        // set known good state
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldFindAll() {
        List<AppUser> actual = repository.findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void shouldPrintMap() {
        Map<Integer, AppUser> map = repository.getUserIds();
        map.forEach((key, value) -> System.out.println(key + ": " + value.getUsername()));
    }

    @Test
    void shouldFindAllRoles() {
        List<String> actual = repository.findAllRoles();
        assertEquals(2, actual.size());
    }

    @Test
    void shouldFindAdmin() {
        AppUser actual = repository.findByUsername("admin");
        assertNotNull(actual);
    }//shouldFindAdmin

    @Test
    void shouldNotFindNone() {
        AppUser actual = repository.findByUsername("none");
        assertNull(actual);
    }//shouldNotFindNone

    @Test
    void shouldFindById() {
        AppUser actual = repository.findByAppUserId(1);
        assertEquals("user", actual.getUsername());
    }

    @Test
    void shouldAdd() {
        AppUser expected = new AppUser();
        expected.setUsername("test");
        expected.setPassword("test");
        AppUser actual = repository.add(expected);
        assertEquals(expected, actual);
        assertEquals("test", repository.findByAppUserId(3).getUsername());
    }

    @Test
    void shouldUpdate() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        user.setUsername("test");
        boolean result = repository.update(user);
        assertTrue(result);
        assertEquals("test", repository.findByAppUserId(1).getUsername());
    }

//    @Test
//    void shouldDeleteById() {
//        boolean result = repository.deleteById(1);
//        assertTrue(result);
//        assertEquals(1, repository.findAll().size());
//    }

    @Test
    void shouldChangePrivacy() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        user.setUsername("test");
        user.setPrivateProfile(true);
        boolean result = repository.changePrivacy(user);
        assertTrue(result);
        assertTrue(repository.findByAppUserId(1).isPrivateProfile());
    }

    @Test
    void shouldChangePassword() {
        AppUser user = new AppUser();
        user.setAppUserId(1);
        user.setUsername("test");
        user.setPassword("testing");
        boolean result = repository.changePassword(user);
        assertTrue(result);
        assertEquals("testing", repository.findByAppUserId(1).getPassword());
    }
}