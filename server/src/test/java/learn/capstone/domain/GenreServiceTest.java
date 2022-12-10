package learn.capstone.domain;

import learn.capstone.data.GenreRepository;
import learn.capstone.models.Genre;
import learn.capstone.models.Media;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GenreServiceTest {

    @Autowired
    GenreService service;

    @MockBean
    GenreRepository repository;


    @Test
    void shouldFindFood(){
        Genre expected = makeGenre();
        when(repository.findById(1)).thenReturn(Optional.ofNullable(expected));
        Genre actual = service.findById(1);
        System.out.println(actual.getName());
        System.out.println(expected.getName());
        assertEquals(expected, actual);
    }

    @Test
    void shouldAdd(){
        Genre genre = new Genre();
        genre.setName("Testing");

        List<Genre> genreList = new ArrayList<>();
        genreList.add(makeGenre());

        when(repository.findAll()).thenReturn(genreList);

        Result<Genre> result = service.add(genre);

        System.out.println(service.findAll().size());
        System.out.println(service.findAll().get(1).getName());

        assertTrue(result.isSuccess());

        assertNotNull(result.getPayload());


    }


    @Test
    void shouldNotAddNullGenre(){
        Genre genre = null;

        Result<Genre> result = service.add(genre);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("Cannot be Null"));
    }

    @Test
    void shouldNotAddBlankGenreName(){
        Genre genre = new Genre();
        genre.setName("");
        Result<Genre> result = service.add(genre);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("Name is required"));

    }

    @Test
    void shouldNotAddWhenGenreIdIsGreaterThanZero(){
        Genre genre = new Genre();
        genre.setGenreId(1);
        genre.setName("Confidential");
        Result<Genre> result = service.add(genre);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("cannot be set for"));

    }

    @Test
    void shouldNotAddDuplicateGenre(){

        Genre genre = new Genre();
        genre.setName("Food");

        when(repository.findAll()).thenReturn(List.of(
                makeGenre()
        ));
        Result<Genre> result = service.add(genre);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).contains("Food is already a genre."));
    }

    @Test
    void shouldNotUpdateNonPositiveId(){
        Genre genre = new Genre();
        genre.setName("Section One");


        Result result = service.update(genre);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertEquals("Genre Id must be set for update operation", result.getMessages().get(0));

    }

    @Test
    void shouldNotUpdateNonExistentId(){
        Genre genre = new Genre();
        genre.setGenreId(97);
        genre.setName("Test Update");


        Result result = service.update(genre);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().get(0).equals("Genre Id: 97, not found"));
    }

    @Test
    void shouldUpdate(){
        Genre expected = makeGenre();
        //need to add to repo
        when(repository.findById(1)).thenReturn(Optional.ofNullable(expected));
        Genre actual = service.findById(1);
        actual.setName("Testing Update");
        assertNotNull(service.update(actual));
        assertEquals(repository.findById(1).get().getName(), "Testing Update");
    }

    @Test
    void shouldNotDeleteNonExistentId(){
        Result result = service.deleteById(90999);

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());

    }

    @Test
    void shouldDelete(){
        List<Genre> genreList = new ArrayList<>();
        genreList.add(makeGenre());

        when(repository.findAll()).thenReturn(genreList);

        service.deleteById(1);

        assertEquals(service.findById(1), null);
    }


    Genre makeGenre() {
        Genre genre = new Genre();
        genre.setGenreId(1);
        genre.setName("Food");
        return genre;
    }
}
