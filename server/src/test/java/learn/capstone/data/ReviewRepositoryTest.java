package learn.capstone.data;

import learn.capstone.models.AppUser;
import learn.capstone.models.Media;
import learn.capstone.models.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository repository;


    Media media = new Media(1, "Harry Potter", 2007, "BOOK", "https://en.wikipedia.org/wiki/Harry_Potter", "https://en.wikipedia.org/wiki/Harry_Potter", 4.0);
    AppUser user = new AppUser();

    @BeforeEach
    void state(){
        repository.setKnownGoodState();
    }

    @Test
    void shouldFindHarryPotter() {
        Review harryPotter = repository.findById(1).orElse(null);
        assert harryPotter != null;
        assertEquals(4, harryPotter.getStars());
    }

    @Test
    void shouldFindAll() {
        List<Review> actual = repository.findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void shouldAddReview(){
        Review input = new Review();
        user.setAppUserId(2);
        user.setUsername("admin");
        user.setPassword("$2a$10$z8mwVv2mOjkWkFuzxYUFcO6SH1FaEftCw4M2Ltv6/5x7nigwEJKIO");
        input.setMedia(media);
        input.setUser(user);
        input.setStars(5);

        Review sunshine = repository.save(input);
        assertNotNull(sunshine);
        assertEquals(3, sunshine.getReviewId());
    }

    @Test
    @Transactional
    void shouldUpdateReview() {
        Review input = repository.findById(1).orElse(null);
        assert input != null;
        input.setStars(3);
        repository.save(input);
        Review actual = repository.findById(1).orElse(null);
        assertEquals(3, actual.getStars());
    }

    @Test
    @Transactional
    void shouldDeleteReview() {
        repository.deleteById(2);
        assertEquals(1, repository.findAll().size());
    }

}