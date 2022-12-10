package learn.capstone.data;

import learn.capstone.models.Genre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GenreRepositoryTest {

    @Autowired
    GenreRepository repository;

    @BeforeEach
    void state(){
        repository.setKnownGoodState();
    }

    @Test
    void shouldFindAll(){
        List<Genre> all = repository.findAll();
        assertEquals(6, all.size());
    }
    @Test
    void shouldFindComedy() {
        Genre comedy = repository.findById(3).orElse(null);
        assert comedy != null;
        assertEquals("Comedy", comedy.getName());
    }

    @Test
    void shouldAddGenre() {
        Genre input = new Genre();
        input.setName("Test");

        Genre actual = repository.save(input);
        assertNotNull(actual);
        assertEquals(7, actual.getGenreId());
    }

    @Test
    @Transactional
    void shouldUpdateComedy() {
        Genre comedy = repository.findById(3).orElse(null);
        comedy.setName("Test");
        Genre actual = repository.save(comedy);
        assertEquals("Test", actual.getName());
        actual = repository.findById(3).orElse(null);
        assertEquals("Test", actual.getName());
    }

    @Test
    void shouldDeleteTest() {
        repository.deleteById(6);
        List<Genre> actual = repository.findAll();
        assertEquals(5, actual.size());
    }

}
