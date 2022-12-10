package learn.capstone.data;

import learn.capstone.models.AppUser;
import learn.capstone.models.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserRepositoryTest {

    @Autowired
    AppUserRepository repository;

    @BeforeEach
    void state(){
        repository.setKnownGoodState();
    }

    @Test
    void shouldFindUser() {
        AppUser user = repository.findById(1).orElse(null);
        assert user != null;
        assertEquals("user", user.getUsername());
    }

    @Test
    void shouldFindAll() {
        List<AppUser> actual = repository.findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void shouldAddUser() {
        AppUser user = new AppUser();
        user.setUsername("test");
        user.setPassword("$2a$10$z8mwVv2mOjkWkFuzxYUFcO6SH1FaEftCw4M2Ltv6/5x7nigwEJKIO");

        AppUser out = repository.save(user);
        assertNotNull(out);
        assertTrue(out.getAppUserId() > 0);
    }

    @Test
    @Transactional
    void shouldUpdateUser() {
        AppUser input = repository.findById(1).orElse(null);
        assert input != null;
        input.setUsername("test");
        repository.save(input);
        AppUser actual = repository.findById(1).orElse(null);
        assertEquals("test", actual.getUsername());
    }

    // DELETE USER NOT IMPLEMENTED! -stretch goal
//    @Test
//    @Transactional
//    void shouldDeleteUser() {
//        repository.deleteById(2);
//        assertEquals(1, repository.findAll().size());
//    }
}