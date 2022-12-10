package learn.capstone.data;

import learn.capstone.models.Media;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MediaRepositoryTest {

    @Autowired
    MediaRepository repository;

    @BeforeEach
    void state(){
        repository.setKnownGoodState();
    }

    @Test
    @Transactional
    void shouldFindHarryPotter() {
        Media harryPotter = repository.findById(1).orElse(null);
        assert harryPotter != null;
        assertEquals(harryPotter.getGenres().size(), 1);
        assertEquals("Harry Potter", harryPotter.getTitle());
    }//shouldFindHarryPotter

    @Test
    void shouldFindAll(){
        List<Media> actual = repository.findAll();
        assert actual != null;
        assertEquals(2, actual.size());
    }//shouldFindAll

    @Test
    void shouldAddMedia(){
        Media input = new Media();
        input.setTitle("Sunshine");
        input.setType("BOOK");
        input.setYear(2003);
        input.setLink("https://en.wikipedia.org/wiki/Sunshine_(novel)");
        input.setMean_stars(5.0);

        Media sunshine = repository.save(input);
        assertNotNull(sunshine);
        assertTrue(sunshine.getMediaId() > 0);
    }//shouldAddMedia

    @Test
    @Transactional
    void shouldUpdate(){
        Media mediaToUpdate = repository.findById(1).orElse(null);
        mediaToUpdate.setType("BOOK");
        Media actual = repository.save(mediaToUpdate);
        actual = repository.findById(1).orElse(null);
        assertEquals("BOOK", actual.getType());
    }//shouldUpdate

    @Test
    @Transactional
    void shouldDeleteMedia(){
        Media input = new Media();
        input.setTitle("The Last Hour of Gann");
        input.setType("BOOK");
        input.setYear(2018);
        input.setMean_stars(5.0);

        Media theLastHourOfGann = repository.save(input);
        repository.deleteById(1);
        //System.out.println(repository.findById(1));
    }//shouldDeleteMedia

}//end