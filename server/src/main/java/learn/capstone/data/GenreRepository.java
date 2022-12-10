package learn.capstone.data;

import learn.capstone.models.Genre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    @Query(value = "CALL set_known_good_state();", nativeQuery = true)
    void setKnownGoodState();

    List<Genre> findByGenreIdIn(Collection<Integer> genreIds);
}
