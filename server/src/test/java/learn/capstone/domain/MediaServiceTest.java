package learn.capstone.domain;


import learn.capstone.data.GenreRepository;
import learn.capstone.data.MediaRepository;
import learn.capstone.data.ReviewRepository;
import learn.capstone.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MediaServiceTest {
    @Autowired
    MediaService service;

    @Autowired
    ReviewService reviewService;

    @Autowired
    GenreService genreService;

    @MockBean
    MediaRepository repository;

    @MockBean
    ReviewRepository reviewRepository;

    @MockBean
    GenreRepository genreRepository;

    @Test
    void shouldFindTheLastHourOfGann(){
        Media expected = makeMedia();
        when(repository.findById(1)).thenReturn(Optional.ofNullable(expected));
        Media actual = service.findById(1);
        assertEquals(expected, actual);
        assertEquals(5, actual.getMean_stars());
        assertEquals("The Last Hour of Gann", actual.getTitle());
    }//shouldFindTheLastHourOfGann

    @Test
    void shouldFindByIdAndReturnAverageRatingIfThereAreReviewsForTheMedia(){
        Media expected = makeMediaWithEverything();
        when(repository.findById(1)).thenReturn(Optional.ofNullable(expected));

        List<Media> mediaList = new ArrayList<>();
        when(repository.findAll()).thenReturn(mediaList);
        mediaList.add(expected);

        List<Review> expectedReviewList = makeReviews();
        when(reviewRepository.findAll()).thenReturn((expectedReviewList));

        Media actual = service.findById(1);
        assertEquals(expected, actual);
        assertEquals(3, actual.getMean_stars());
    }//shouldFindByIdAndReturnAverageRatingIfThereAreReviewsForTheMedia

    @Test
    void shouldFindAll(){
        List<Media> mediaList = new ArrayList<>();
        mediaList.add(makeMedia());

        when(repository.findAll()).thenReturn(mediaList);
        assertEquals(1, mediaList.size());
    }//shouldFindAll

//    @Test
//    void shouldFindMediaByGenre(){
//        Media media = makeMediaWithGenres();
//        Media mediaTwo = makeMediaTwoWithGenres();
//        List<Media> mediaList = new ArrayList<>();
//        mediaList.add(media);
//        mediaList.add(mediaTwo);
//
//        when(genreRepository.findByGenreIdIn(any())).thenReturn(makeMediaWithGenres().getGenres());
//
//        List<Media> medias = service.findByGenre(makeMediaWithGenres().getGenres());
//        assertTrue(medias.get(0).getTitle().equalsIgnoreCase(media.getTitle()));
//        assertTrue(medias.get(1).getTitle().equalsIgnoreCase(mediaTwo.getTitle()));
//    }//shouldFindMediaByGenre

//    @Test
//    void shouldFindReviewsByMedia(){
//        List<Media> media = List.of(makeMediaWithEverything());
//        when(repository.findAll()).thenReturn(media);
//        List<Review> expectedReviewList = makeReviews();
//        when(reviewRepository.findAll()).thenReturn((expectedReviewList));
//        assertEquals(2, service.findReviewsByMedia(media.get(0)).size());
//    }//shouldFindReviewsByMedia

    @Test
    void shouldAdd(){
        List<Media> mediaList = new ArrayList<>();
        mediaList.add(makeMediaWithEverything());

        when(repository.findAll()).thenReturn(mediaList);

        Result<Media> result = service.add(makeMedia());
        System.out.println(service.findAll().size());
        assertTrue(result.isSuccess());
        assertEquals(service.findAll().size(), 1);
    }//shouldAdd

    @Test
    void shouldNotAddDuplicateMedia(){
        List<Media> mediaList = new ArrayList<>();
        System.out.println(repository.findAll().size());
        mediaList.add(makeMedia());

        when(repository.findAll()).thenReturn(mediaList);

        Result<Media> result = service.add(makeMedia());
        Result<Media> resultTwo = service.add(makeMedia());
        assertTrue(resultTwo.getMessages().contains("This matches something already in the system."));
    }//shouldAdd

    @Test
    void shouldNotAddNullMedia(){
        Media media = null;

        Result<Media> result = service.add(media);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("cannot be null"));
    }//shouldNotAddNullMedia

    @Test
    void shouldRequireTitleOfMedia(){
        Media media = makeMedia();
        media.setTitle(null);

        Result<Media> result = service.add(media);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("Title is required"));
    }//shouldRequireTitleOfMedia

    @Test
    void shouldNotHaveBlankTitleOfMedia(){
        Media media = makeMedia();
        media.setTitle(" ");

        Result<Media> result = service.add(media);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("Title is required"));
    }//shouldNotHaveBlankTitleOfMedia

    @Test
    void shouldNotUpdateNonExistentMedia() {
        Media expected = makeMedia();
        when(repository.findById(1)).thenReturn(Optional.ofNullable(expected));
        Media actual = service.findById(999);
        Result<Media> result = service.update(actual);
        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getType());
    }//shouldNotUpdateNonExistentMedia

    @Test
    void shouldUpdateMedia(){
        Media expected = makeMedia();
        when(repository.findById(1)).thenReturn(Optional.ofNullable(expected));
        Media actual = service.findById(1);
        actual.setTitle("Testing Update");
        assertNotNull(service.update(actual));
        assertEquals(repository.findById(1).get().getTitle(), "Testing Update");
    }//shouldUpdateMedia

//    @Test
//    void shouldGetAverageRatingWhenUpdated(){
//        Media expected = makeMediaWithEverything();
//        List<Review> expectedReviewList = makeReviews();
//        when(repository.findById(1)).thenReturn(Optional.ofNullable(expected));
//        when(reviewRepository.findAll()).thenReturn((expectedReviewList));
//        Result<Media> result = service.update(expected);
//        assertEquals(3, expected.getMean_stars());
//    }//shouldGetAverageRatingWhenUpdated

    @Test
    void shouldNotDeleteNonExistentId(){
        Result result = service.deleteById(999);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }//shouldNotDeleteNonExistentId

    @Test
    void shouldDelete(){
        List<Media> mediaList = new ArrayList<>();
        mediaList.add(makeMedia());

        when(repository.findAll()).thenReturn(mediaList);

        service.deleteById(1);

        assertEquals(service.findById(1), null);
    }//shouldDelete

    @Test
    void shouldNotDeleteNonExistentMedia() {
        Media media = new Media();
        Result<Media> actual = service.deleteById(media.getMediaId());
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }//shouldNotDeleteNonExistentMedia

    //Helper methods
    Media makeMedia() {
        Media media = new Media();
        media.setTitle("The Last Hour of Gann");
        media.setType("BOOK");
        media.setYear(2018);
        media.setMean_stars(5.0);
        return media;
    }//makeMedia

    Media makeMediaWithGenres() {
        List<Genre> genreList = new ArrayList<>();
        Genre genre = new Genre();
        genre.setGenreId(1);
        genre.setName("Adventure");
        genreList.add(genre);

        Media media = new Media();
        media.setTitle("Bone");
        media.setType("BOOK");
        media.setYear(2010);
        media.setMean_stars(5.0);
        media.setGenres(genreList);

        genre.setMedia(List.of(media));

        return media;
    }//makeMedia

    Media makeMediaTwoWithGenres() {
        List<Genre> genreList = new ArrayList<>();
        Genre genre = new Genre();
        genre.setGenreId(1);
        genre.setName("Adventure");
        genreList.add(genre);

        Genre genreTwo = new Genre();
        genre.setGenreId(2);
        genre.setName("Religious");
        genreList.add(genreTwo);

        Media media = new Media();
        media.setTitle("The Bible");
        media.setType("BOOK");
        media.setYear(1);
        media.setMean_stars(5.0);
        media.setGenres(genreList);

        genre.setMedia(List.of(media));
        genreTwo.setMedia(List.of(media));

        return media;
    }//makeMediaTwoWithGenres

    Media makeMediaWithEverything() {
        List<Genre> genreList = new ArrayList<>();
        Genre genre = new Genre();
        genre.setGenreId(1);
        genre.setName("Adventure");
        genreList.add(genre);

        Genre genreTwo = new Genre();
        genre.setGenreId(2);
        genre.setName("Religious");
        genreList.add(genreTwo);

        Media media = new Media();
        media.setMediaId(1);
        media.setTitle("The Bible");
        media.setType("BOOK");
        media.setYear(1);
        media.setMean_stars(5);
        media.setGenres(genreList);

        genre.setMedia(List.of(media));

        AppUser user = new AppUser();
        user.setAppUserId(1);
        user.setUsername("username");
        user.setPassword("123");
        user.isEnabled();
        user.setPrivateProfile(false);

        AppUser userTwo = new AppUser();
        userTwo.setAppUserId(2);
        userTwo.setUsername("usernameOther");
        userTwo.setPassword("456");
        userTwo.isEnabled();
        userTwo.setPrivateProfile(false);

        Review reviewOne = new Review();
        reviewOne.setReviewId(1);
        reviewOne.setStars(5);
        reviewOne.setUser(user);
        reviewOne.setMedia(media);

        Review reviewTwo = new Review();
        reviewTwo.setReviewId(2);
        reviewTwo.setStars(1);
        reviewTwo.setUser(userTwo);
        reviewTwo.setMedia(media);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(reviewOne);
        reviewList.add(reviewTwo);

        reviewService.add(reviewOne);
        reviewService.add(reviewTwo);

        media.setReviews(reviewList);

        return media;
    }//makeMediaWithEverything

    List<Review> makeReviews(){
        List<Genre> genreList = new ArrayList<>();
        Genre genre = new Genre();
        genre.setGenreId(1);
        genre.setName("Adventure");
        genreList.add(genre);

        Genre genreTwo = new Genre();
        genre.setGenreId(2);
        genre.setName("Religious");
        genreList.add(genreTwo);

        Media media = new Media();
        media.setMediaId(1);
        media.setTitle("The Bible");
        media.setType("BOOK");
        media.setYear(1);
        media.setMean_stars(5);
        media.setGenres(genreList);

        genre.setMedia(List.of(media));

        AppUser user = new AppUser();
        user.setAppUserId(1);
        user.setUsername("username");
        user.setPassword("123");
        user.isEnabled();
        user.setPrivateProfile(false);

        AppUser userTwo = new AppUser();
        userTwo.setAppUserId(2);
        userTwo.setUsername("usernameOther");
        userTwo.setPassword("456");
        userTwo.isEnabled();
        userTwo.setPrivateProfile(false);

        Review reviewOne = new Review();
        reviewOne.setReviewId(1);
        reviewOne.setStars(5);
        reviewOne.setUser(user);
        reviewOne.setMedia(media);

        Review reviewTwo = new Review();
        reviewTwo.setReviewId(2);
        reviewTwo.setStars(1);
        reviewTwo.setUser(userTwo);
        reviewTwo.setMedia(media);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(reviewOne);
        reviewList.add(reviewTwo);

        return reviewList;
    }
}//end
