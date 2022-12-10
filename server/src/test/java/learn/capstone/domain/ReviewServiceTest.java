package learn.capstone.domain;

import learn.capstone.data.ReviewRepository;
import learn.capstone.models.AppUser;
import learn.capstone.models.Media;
import learn.capstone.models.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReviewServiceTest {
    @Autowired
    ReviewService service;

    @MockBean
    ReviewRepository repository;

    Media media = new Media(1, "Harry Potter", 2007, "BOOK", "https://en.wikipedia.org/wiki/Harry_Potter", 4.0);
    AppUser user = new AppUser();

    @Test
    void shouldFindHarryPotter() {
        Review expected = makeReview();

        when(repository.findById(1)).thenReturn(Optional.ofNullable(expected));

        Review actual = service.findById(1);
        assertEquals(expected, actual);
    }

//    @Test
//    void shouldFindUserReviews() {
//        List<Review> expected = new ArrayList<>();
//        Review review = makeReview();
//        review.setReviewId(1);
//
//        expected.add(review);
//
//        Review review2 = makeReview();
//        review2.setReviewId(2);
//
//        AppUser user2 = new AppUser();
//        user2.setUsername("user");
//        review2.setUser(user2);
//
//        Media media2 = new Media();
//        media2.setTitle("Test");
//        review2.setMedia(media2);
//
//        expected.add(review2);
//
//        when(repository.findAll()).thenReturn(expected);
//
//        List<Review> actual = service.findByUsername("user");
//
//        assertEquals(1, actual.size());
//        assertEquals("Test", actual.get(0).getMedia().getTitle());
//    }

    @Test
    void shouldFindReviewsByMediaId() {
        List<Review> expected = new ArrayList<>();
        Review review = makeReview();
        review.setReviewId(1);

        expected.add(review);

        Review review2 = makeReview();
        review2.setReviewId(2);

        AppUser user2 = new AppUser();
        user2.setUsername("user");
        review2.setUser(user2);

        Media media2 = new Media();
        media2.setMediaId(2);
        media2.setTitle("Test");
        review2.setMedia(media2);

        expected.add(review2);

        when(repository.findAll()).thenReturn(expected);

        List<Review> actual = service.findByMediaId(2);

        assertEquals(1, actual.size());
        assertEquals("Test", actual.get(0).getMedia().getTitle());
    }

    @Test
    void shouldAdd() {
        Review review = makeReview();
        List<Review> reviews = new ArrayList<>();
        reviews.add(review);

        when(repository.findAll()).thenReturn(reviews);

        Result<Review> result = service.add(review);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(1, service.findAll().size());
    }

    @Test
    void shouldNotAddNullReview() {
        Review review = null;
        Result<Review> result = service.add(review);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("null"));
    }

    @Test
    void shouldNotAddWhenReviewIdSet() {
        Review review = makeReview();
        review.setReviewId(1);
        Result<Review> result = service.add(review);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("should not be set"));
    }

    @Test
    void shouldNotAddNullMedia() {
        Review review = makeReview();
        review.setMedia(null);
        Result<Review> result = service.add(review);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("media attached"));
    }

    @Test
    void shouldNotAddNullUser() {
        Review review = makeReview();
        review.setUser(null);
        Result<Review> result = service.add(review);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("must have a user"));
    }

    @Test
    void shouldNotAddNonUniqueMediaUser() {
        Review review = makeReview();
        review.setReviewId(1);

        when(repository.findAll()).thenReturn(List.of(review));
        Review actual = makeReview();
        actual.setStars(3);

        Result<Review> result = service.add(actual);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("must be unique"));

    }

    @Test
    void shouldUpdate() {
        Review expected = makeReview();

        when(repository.findById(1)).thenReturn(Optional.ofNullable(expected));

        Review actual = service.findById(1);
        actual.setStars(3);
        assertNotNull(service.update(actual));
        assertEquals(3, actual.getStars());
    }

    @Test
    void shouldNotUpdateNonPositiveId() {
        Review review = makeReview();

        Result<Review> result = service.update(review);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("should be set"));
    }

    @Test
    void shouldNotUpdateNonExistentId() {
        Review review = makeReview();
        review.setReviewId(99);

        Result<Review> result = service.update(review);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("not found"));
    }

    @Test
    void shouldDelete() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(makeReview());

        when(repository.findAll()).thenReturn(reviews);

        service.deleteById(1);
        assertNull(service.findById(1));
    }

    @Test
    void shouldNotDeleteNonExistentId() {
        Result<Review> result = service.deleteById(99);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("not found"));
    }

    Review makeReview() {
        Review review = new Review();

        user.setAppUserId(2);
        user.setUsername("admin");
        user.setPassword("$2a$10$z8mwVv2mOjkWkFuzxYUFcO6SH1FaEftCw4M2Ltv6/5x7nigwEJKIO");
        review.setMedia(media);
        review.setUser(user);
        review.setStars(5);

        return review;
    }
}